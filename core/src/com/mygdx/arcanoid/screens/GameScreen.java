package com.mygdx.arcanoid.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.arcanoid.MyGdxGame;
import com.mygdx.arcanoid.entities.Ball;
import com.mygdx.arcanoid.entities.Brick;
import com.mygdx.arcanoid.entities.Obstacle;
import com.mygdx.arcanoid.entities.Paddle;
import com.mygdx.arcanoid.levels.LevelData;
import com.mygdx.arcanoid.levels.LevelFactory;
import com.mygdx.arcanoid.physics.BoundsFactory;
import com.mygdx.arcanoid.physics.GameContactListener;

import static com.mygdx.arcanoid.physics.Box2DConstants.MAX_FRAME_TIME;
import static com.mygdx.arcanoid.physics.Box2DConstants.PIXELS_PER_METER;
import static com.mygdx.arcanoid.physics.Box2DConstants.POSITION_ITERATIONS;
import static com.mygdx.arcanoid.physics.Box2DConstants.TIME_STEP;
import static com.mygdx.arcanoid.physics.Box2DConstants.VELOCITY_ITERATIONS;
import static com.mygdx.arcanoid.physics.Box2DConstants.WORLD_HEIGHT;
import static com.mygdx.arcanoid.physics.Box2DConstants.WORLD_WIDTH;

public class GameScreen implements Screen {

    private static final boolean DEBUG_PHYSICS = true;
    private static final int MAX_LEVEL = 4;
    private static final float PADDLE_Y_PX = 40f;
    private static final float PADDLE_KEYBOARD_SPEED_PX_PER_SEC = 400f;
    private static final float RESPAWN_ANGLE_SPREAD_DEG = 15f;

    private final MyGdxGame game;

    private int currentLevel;
    private int livesRemaining;

    private World world;
    private GameContactListener contactListener;
    private OrthographicCamera camera;
    private Viewport viewport;
    private Box2DDebugRenderer debugRenderer;
    private boolean showDebug = DEBUG_PHYSICS;

    private SpriteBatch batch;
    private Paddle paddle;
    private Array<Ball> balls;
    private Array<Brick> bricks;
    private Obstacle obstacle;

    private float physicsAccumulator = 0f;
    private float paddleTargetXPx;
    private final Vector3 touchPoint = new Vector3();

    private Stage hudStage;
    private Label livesLabel;
    private Label levelLabel;

    private boolean pendingLevelAdvance = false;
    private boolean pendingGameOver = false;

    public GameScreen(MyGdxGame game, int startLevel) {
        this.game = game;
        this.currentLevel = startLevel;
    }

    @Override
    public void show() {
        world = new World(new Vector2(0, 0), true);
        contactListener = new GameContactListener();
        world.setContactListener(contactListener);
        BoundsFactory.createBounds(world);

        camera = new OrthographicCamera();
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        camera.position.set(WORLD_WIDTH / 2f, WORLD_HEIGHT / 2f, 0);
        camera.update();

        debugRenderer = new Box2DDebugRenderer();
        batch = game.batch;

        paddle = Paddle.create(world, WORLD_WIDTH / 2f, PADDLE_Y_PX, Paddle.WIDTH_PX, Paddle.HEIGHT_PX);
        paddleTargetXPx = WORLD_WIDTH / 2f;

        bricks = new Array<>();
        balls = new Array<>();
        obstacle = null;

        livesRemaining = game.settings.getLivesRemaining();

        buildHud();
        loadLevel(currentLevel);
    }

    private void buildHud() {
        hudStage = new Stage(new ScreenViewport());
        Table root = new Table();
        root.setFillParent(true);
        root.top();
        hudStage.addActor(root);

        levelLabel = new Label("", game.assets.getSkin());
        livesLabel = new Label("", game.assets.getSkin());

        root.add(levelLabel).pad(10f).left().expandX();
        root.add(livesLabel).pad(10f).right();
    }

    private void updateHudText() {
        levelLabel.setText("Уровень " + currentLevel);
        livesLabel.setText("Жизни: " + livesRemaining);
    }

    private void loadLevel(int levelNumber) {
        for (Brick brick : bricks) {
            world.destroyBody(brick.getBody());
        }
        for (Ball ball : balls) {
            world.destroyBody(ball.getBody());
        }
        if (obstacle != null) {
            world.destroyBody(obstacle.getBody());
        }

        paddle.moveTo(WORLD_WIDTH / 2f);
        paddleTargetXPx = WORLD_WIDTH / 2f;

        float ballSpawnX = WORLD_WIDTH / 2f;
        float ballSpawnY = ballSpawnYPx();

        LevelData data = LevelFactory.buildLevel(world, levelNumber, ballSpawnX, ballSpawnY);
        bricks = data.bricks;
        balls = data.balls;
        obstacle = data.obstacle;

        physicsAccumulator = 0f;
        currentLevel = levelNumber;
        game.settings.setCurrentLevel(levelNumber);
        updateHudText();
    }

    private float ballSpawnYPx() {
        return PADDLE_Y_PX + Paddle.HEIGHT_PX / 2f + Ball.RADIUS_PX + 4f;
    }

    @Override
    public void render(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.F1)) {
            showDebug = !showDebug;
        }
        if (DEBUG_PHYSICS && Gdx.input.isKeyJustPressed(Input.Keys.F2)) {
            pendingLevelAdvance = true;
        }

        handleInput();
        stepPhysics(delta);

        if (pendingGameOver) {
            pendingGameOver = false;
            game.setScreen(new LoseScreen(game));
            return;
        }
        if (pendingLevelAdvance) {
            pendingLevelAdvance = false;
            advanceLevel();
            return;
        }

        draw(delta);
    }

    private void handleInput() {
        if (Gdx.input.isTouched()) {
            touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            viewport.unproject(touchPoint);
            paddleTargetXPx = touchPoint.x;
        } else {
            float step = PADDLE_KEYBOARD_SPEED_PX_PER_SEC * Gdx.graphics.getDeltaTime();
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                paddleTargetXPx -= step;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                paddleTargetXPx += step;
            }
        }
        paddle.moveTo(paddleTargetXPx);
    }

    private void stepPhysics(float delta) {
        physicsAccumulator += Math.min(delta, MAX_FRAME_TIME);
        while (physicsAccumulator >= TIME_STEP) {
            if (obstacle != null) {
                obstacle.update(TIME_STEP);
            }
            world.step(TIME_STEP, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
            for (Ball ball : balls) {
                ball.renormalizeSpeed();
            }
            processContactResults();
            physicsAccumulator -= TIME_STEP;

            if (pendingLevelAdvance || pendingGameOver) {
                break;
            }
        }
    }

    private void processContactResults() {
        if (contactListener.anyBallContact) {
            game.assets.playBounce();
        }

        for (Brick brick : contactListener.bricksToBreak) {
            bricks.removeValue(brick, true);
            world.destroyBody(brick.getBody());
        }

        for (Ball ball : contactListener.ballsLostBottom) {
            if (!balls.removeValue(ball, true)) {
                continue;
            }
            world.destroyBody(ball.getBody());
            livesRemaining--;
            game.settings.setLivesRemaining(livesRemaining);
            if (livesRemaining <= 0) {
                pendingGameOver = true;
            } else {
                Ball respawned = Ball.create(world, paddle.getXPx(), ballSpawnYPx(), ball.getColor());
                respawned.launch(90f + MathUtils.random(-RESPAWN_ANGLE_SPREAD_DEG, RESPAWN_ANGLE_SPREAD_DEG));
                balls.add(respawned);
            }
        }

        if (contactListener.levelComplete) {
            pendingLevelAdvance = true;
        }

        updateHudText();
        contactListener.reset();
    }

    private void advanceLevel() {
        for (Ball ball : balls) {
            world.destroyBody(ball.getBody());
        }
        balls.clear();

        int nextLevel = currentLevel + 1;
        if (nextLevel > MAX_LEVEL) {
            game.setScreen(new WinScreen(game));
        } else {
            loadLevel(nextLevel);
        }
    }

    private void draw(float delta) {
        ScreenUtils.clear(0.1f, 0.1f, 0.15f, 1f);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        for (Brick brick : bricks) {
            drawCentered(brick.getXPx(), brick.getYPx(), brick.getWidthPx(), brick.getHeightPx(), game.assets.getBrickTexture(brick.getColor()));
        }
        drawCentered(paddle.getXPx(), paddle.getYPx(), paddle.getWidthPx(), Paddle.HEIGHT_PX, game.assets.getPaddleTexture());
        if (obstacle != null) {
            drawCentered(obstacle.getXPx(), obstacle.getYPx(), obstacle.getWidthPx(), obstacle.getHeightPx(), game.assets.getObstacleTexture());
        }
        for (Ball ball : balls) {
            drawCentered(ball.getXPx(), ball.getYPx(), Ball.RADIUS_PX * 2f, Ball.RADIUS_PX * 2f, game.assets.getBallTexture(ball.getColor()));
        }
        batch.end();

        hudStage.act(delta);
        hudStage.draw();

        if (showDebug) {
            debugRenderer.render(world, camera.combined.cpy().scl(PIXELS_PER_METER));
        }
    }

    private void drawCentered(float centerXPx, float centerYPx, float widthPx, float heightPx, Texture texture) {
        batch.draw(texture, centerXPx - widthPx / 2f, centerYPx - heightPx / 2f, widthPx, heightPx);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        hudStage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        world.dispose();
        debugRenderer.dispose();
        hudStage.dispose();
    }
}
