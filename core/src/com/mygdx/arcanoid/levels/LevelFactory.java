package com.mygdx.arcanoid.levels;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.arcanoid.entities.Ball;
import com.mygdx.arcanoid.entities.BallColor;
import com.mygdx.arcanoid.entities.Brick;
import com.mygdx.arcanoid.entities.BrickColor;
import com.mygdx.arcanoid.entities.Obstacle;

import static com.mygdx.arcanoid.physics.Box2DConstants.WORLD_HEIGHT;
import static com.mygdx.arcanoid.physics.Box2DConstants.WORLD_WIDTH;

public final class LevelFactory {

    public static final int GRID_COLS = 7;
    public static final int GRID_ROWS = 4;
    public static final float BRICK_WIDTH_PX = 96f;
    public static final float BRICK_HEIGHT_PX = 32f;
    public static final float BRICK_GUTTER_PX = 8f;
    public static final float GRID_TOP_MARGIN_PX = 24f;

    private static final int MAX_METAL_BRICKS = 5;

    private static final float OBSTACLE_WIDTH_PX = 60f;
    private static final float OBSTACLE_HEIGHT_PX = 20f;
    private static final float OBSTACLE_SPEED_PX_PER_SEC = 80f;
    private static final float OBSTACLE_MIN_X_PX = WORLD_WIDTH * 0.3f;
    private static final float OBSTACLE_MAX_X_PX = WORLD_WIDTH * 0.7f;
    private static final float OBSTACLE_Y_PX = WORLD_HEIGHT * 0.4f;

    private LevelFactory() {
    }

    public static LevelData buildLevel(World world, int levelNumber, float ballSpawnXPx, float ballSpawnYPx) {
        switch (levelNumber) {
            case 1:
                return buildLevel1(world, ballSpawnXPx, ballSpawnYPx);
            case 2:
                return buildLevel2(world, ballSpawnXPx, ballSpawnYPx);
            case 3:
                return buildLevel3(world, ballSpawnXPx, ballSpawnYPx);
            case 4:
                return buildLevel4(world, ballSpawnXPx, ballSpawnYPx);
            default:
                throw new IllegalArgumentException("Unknown level: " + levelNumber);
        }
    }

    private static LevelData buildLevel1(World world, float ballSpawnXPx, float ballSpawnYPx) {
        Array<Brick> bricks = buildGrid(world, (col, row) -> BrickColor.RED);
        Array<Ball> balls = new Array<>();
        balls.add(spawnBall(world, ballSpawnXPx, ballSpawnYPx, BallColor.RED, 90f));
        return new LevelData(bricks, balls, null);
    }

    private static LevelData buildLevel2(World world, float ballSpawnXPx, float ballSpawnYPx) {
        Array<Brick> bricks = buildGrid(world, (col, row) -> randomRedOrGreen());
        Array<Ball> balls = spawnTwoBalls(world, ballSpawnXPx, ballSpawnYPx);
        return new LevelData(bricks, balls, null);
    }

    private static LevelData buildLevel3(World world, float ballSpawnXPx, float ballSpawnYPx) {
        Array<Brick> bricks = buildGridWithMetal(world);
        Array<Ball> balls = spawnTwoBalls(world, ballSpawnXPx, ballSpawnYPx);
        return new LevelData(bricks, balls, null);
    }

    private static LevelData buildLevel4(World world, float ballSpawnXPx, float ballSpawnYPx) {
        Array<Brick> bricks = buildGridWithMetal(world);
        Array<Ball> balls = spawnTwoBalls(world, ballSpawnXPx, ballSpawnYPx);
        Obstacle obstacle = Obstacle.create(world, WORLD_WIDTH / 2f, OBSTACLE_Y_PX,
            OBSTACLE_WIDTH_PX, OBSTACLE_HEIGHT_PX, OBSTACLE_SPEED_PX_PER_SEC, OBSTACLE_MIN_X_PX, OBSTACLE_MAX_X_PX);
        return new LevelData(bricks, balls, obstacle);
    }

    private static BrickColor randomRedOrGreen() {
        return MathUtils.randomBoolean() ? BrickColor.RED : BrickColor.GREEN;
    }

    private static Array<Brick> buildGridWithMetal(World world) {
        BrickColor[][] layout = new BrickColor[GRID_ROWS][GRID_COLS];
        for (int row = 0; row < GRID_ROWS; row++) {
            for (int col = 0; col < GRID_COLS; col++) {
                layout[row][col] = randomRedOrGreen();
            }
        }

        Array<Integer> slots = new Array<>();
        for (int i = 0; i < GRID_ROWS * GRID_COLS; i++) {
            slots.add(i);
        }
        slots.shuffle();

        int metalCount = MathUtils.random(1, MAX_METAL_BRICKS);
        for (int i = 0; i < metalCount; i++) {
            int slot = slots.get(i);
            layout[slot / GRID_COLS][slot % GRID_COLS] = BrickColor.METAL;
        }

        return buildGrid(world, (col, row) -> layout[row][col]);
    }

    private static Array<Ball> spawnTwoBalls(World world, float ballSpawnXPx, float ballSpawnYPx) {
        Array<Ball> balls = new Array<>();
        balls.add(spawnBall(world, ballSpawnXPx - 20f, ballSpawnYPx, BallColor.RED, 75f));
        balls.add(spawnBall(world, ballSpawnXPx + 20f, ballSpawnYPx, BallColor.GREEN, 105f));
        return balls;
    }

    private interface ColorPicker {
        BrickColor colorFor(int col, int row);
    }

    private static Array<Brick> buildGrid(World world, ColorPicker colorPicker) {
        Array<Brick> bricks = new Array<>();
        float gridWidth = GRID_COLS * BRICK_WIDTH_PX + (GRID_COLS - 1) * BRICK_GUTTER_PX;
        float startX = (WORLD_WIDTH - gridWidth) / 2f + BRICK_WIDTH_PX / 2f;
        float startY = WORLD_HEIGHT - GRID_TOP_MARGIN_PX - BRICK_HEIGHT_PX / 2f;

        for (int row = 0; row < GRID_ROWS; row++) {
            for (int col = 0; col < GRID_COLS; col++) {
                float x = startX + col * (BRICK_WIDTH_PX + BRICK_GUTTER_PX);
                float y = startY - row * (BRICK_HEIGHT_PX + BRICK_GUTTER_PX);
                BrickColor color = colorPicker.colorFor(col, row);
                bricks.add(Brick.create(world, x, y, BRICK_WIDTH_PX, BRICK_HEIGHT_PX, color));
            }
        }
        return bricks;
    }

    private static Ball spawnBall(World world, float xPx, float yPx, BallColor color, float launchAngleDeg) {
        Ball ball = Ball.create(world, xPx, yPx, color);
        ball.launch(launchAngleDeg);
        return ball;
    }
}
