package com.mygdx.arcanoid.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Array;
import com.mygdx.arcanoid.entities.BallColor;
import com.mygdx.arcanoid.entities.BrickColor;


public class Assets {

    private final Array<Texture> ownedTextures = new Array<>();

    private final Skin skin;

    private final Texture ballRedTexture;
    private final Texture ballGreenTexture;
    private final Texture brickRedTexture;
    private final Texture brickGreenTexture;
    private final Texture brickMetalTexture;
    private final Texture paddleTexture;
    private final Texture obstacleTexture;

    private final Music backgroundMusic;
    private final Sound bounceSound;
    private final Sound winSound;
    private final Sound loseSound;

    public Assets() {
        ballRedTexture = createCircleTexture(16, Color.RED);
        ballGreenTexture = createCircleTexture(16, Color.GREEN);
        brickRedTexture = createRectTexture(96, 32, Color.FIREBRICK);
        brickGreenTexture = createRectTexture(96, 32, Color.FOREST);
        brickMetalTexture = createRectTexture(96, 32, Color.GRAY);
        paddleTexture = createRectTexture(120, 20, Color.SKY);
        obstacleTexture = createRectTexture(60, 20, Color.ORANGE);

        skin = buildSkin();

        backgroundMusic = loadMusicIfPresent("audio/music.ogg");
        if (backgroundMusic != null) {
            backgroundMusic.setLooping(true);
        }
        bounceSound = loadSoundIfPresent("audio/bounce.ogg");
        winSound = loadSoundIfPresent("audio/win.ogg");
        loseSound = loadSoundIfPresent("audio/lose.ogg");
    }

    private Music loadMusicIfPresent(String path) {
        FileHandle file = Gdx.files.internal(path);
        return file.exists() ? Gdx.audio.newMusic(file) : null;
    }

    private Sound loadSoundIfPresent(String path) {
        FileHandle file = Gdx.files.internal(path);
        return file.exists() ? Gdx.audio.newSound(file) : null;
    }

    private Skin buildSkin() {
        Skin skin = new Skin();

        Texture white = createRectTexture(4, 4, Color.WHITE);
        skin.add("white", new TextureRegion(white));

        BitmapFont font = createFont(24);
        skin.add("default-font", font);

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;
        labelStyle.fontColor = Color.WHITE;
        skin.add("default", labelStyle);

        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = font;
        buttonStyle.up = skin.newDrawable("white", new Color(0.25f, 0.25f, 0.32f, 1f));
        buttonStyle.down = skin.newDrawable("white", new Color(0.42f, 0.42f, 0.55f, 1f));
        buttonStyle.over = skin.newDrawable("white", new Color(0.32f, 0.32f, 0.42f, 1f));
        buttonStyle.checked = skin.newDrawable("white", new Color(0.5f, 0.5f, 0.65f, 1f));
        buttonStyle.fontColor = Color.WHITE;
        skin.add("default", buttonStyle);

        return skin;
    }

    private BitmapFont createFont(int sizePx) {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("roboto.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = sizePx;
        parameter.color = Color.WHITE;
        parameter.characters = FreeTypeFontGenerator.DEFAULT_CHARS + cyrillicCharacters();
        BitmapFont font = generator.generateFont(parameter);
        generator.dispose();
        return font;
    }

    private String cyrillicCharacters() {
        StringBuilder builder = new StringBuilder();
        for (char c = 0x0400; c <= 0x04FF; c++) {
            builder.append(c);
        }
        return builder.toString();
    }

    private Texture createRectTexture(int width, int height, Color color) {
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        pixmap.setColor(color);
        pixmap.fill();
        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        ownedTextures.add(texture);
        return texture;
    }

    private Texture createCircleTexture(int diameter, Color color) {
        Pixmap pixmap = new Pixmap(diameter, diameter, Pixmap.Format.RGBA8888);
        pixmap.setColor(color);
        pixmap.fillCircle(diameter / 2, diameter / 2, diameter / 2);
        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        ownedTextures.add(texture);
        return texture;
    }

    public Skin getSkin() {
        return skin;
    }

    public Texture getBallTexture(BallColor color) {
        switch (color) {
            case RED:
                return ballRedTexture;
            case GREEN:
                return ballGreenTexture;
            default:
                throw new IllegalArgumentException("Unknown ball color: " + color);
        }
    }

    public Texture getBrickTexture(BrickColor color) {
        switch (color) {
            case RED:
                return brickRedTexture;
            case GREEN:
                return brickGreenTexture;
            case METAL:
                return brickMetalTexture;
            default:
                throw new IllegalArgumentException("Unknown brick color: " + color);
        }
    }

    public Texture getPaddleTexture() {
        return paddleTexture;
    }

    public Texture getObstacleTexture() {
        return obstacleTexture;
    }

    public void playBounce() {
        if (bounceSound != null) {
            bounceSound.play();
        }
    }

    public void playWin() {
        if (winSound != null) {
            winSound.play();
        }
    }

    public void playLose() {
        if (loseSound != null) {
            loseSound.play();
        }
    }

    public void startMusic() {
        if (backgroundMusic != null && !backgroundMusic.isPlaying()) {
            backgroundMusic.play();
        }
    }

    public void dispose() {
        for (Texture texture : ownedTextures) {
            texture.dispose();
        }
        skin.dispose();
        if (backgroundMusic != null) {
            backgroundMusic.dispose();
        }
        if (bounceSound != null) {
            bounceSound.dispose();
        }
        if (winSound != null) {
            winSound.dispose();
        }
        if (loseSound != null) {
            loseSound.dispose();
        }
    }
}
