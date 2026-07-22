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
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.math.MathUtils;
import com.mygdx.arcanoid.objects.BallColor;
import com.mygdx.arcanoid.objects.BrickColor;


public class Assets {

    public static final float MENU_MUSIC_VOLUME = 0.8f;
    public static final float GAME_MUSIC_VOLUME = 0.3f;

    private final Array<Texture> ownedTextures = new Array<>();

    private float currentBaseMusicVolume = MENU_MUSIC_VOLUME;
    private float userVolumeMultiplier = 1f;

    private final Skin skin;

    private final Texture ballRedTexture;
    private final Texture ballGreenTexture;
    private final Texture brickRedTexture;
    private final Texture brickGreenTexture;
    private final Texture brickMetalTexture;
    private final Texture modificatorRedTexture;
    private final Texture modificatorGreenTexture;
    private final Texture paddleTexture;
    private final Texture obstacleTexture;
    private final Texture menuBackgroundTexture;
    private final Texture level1BackgroundTexture;
    private final Texture level2BackgroundTexture;
    private final Texture level3BackgroundTexture;
    private final Texture level4BackgroundTexture;

    private final Music backgroundMusic;
    private final Sound bounceSound;
    private final Sound winSound;
    private final Sound loseSound;
    private final Sound modifierSound;

    public Assets() {
        ballRedTexture = loadTexture("textures/ball_red.png");
        ballGreenTexture = loadTexture("textures/ball_green.png");
        brickRedTexture = loadTexture("textures/brick_red.png");
        brickGreenTexture = loadTexture("textures/brick_green.png");
        brickMetalTexture = loadTexture("textures/brick_metal.png");
        modificatorRedTexture = loadTexture("textures/modificator_red.png");
        modificatorGreenTexture = loadTexture("textures/modificator_green.png");
        paddleTexture = loadTexture("textures/paddle.png");
        obstacleTexture = loadTexture("textures/obstacle.png");
        menuBackgroundTexture = loadTexture("textures/menu_background.png");
        level1BackgroundTexture = loadTexture("textures/level_1_background.png");
        level2BackgroundTexture = loadTexture("textures/level_2_background.png");
        level3BackgroundTexture = loadTexture("textures/level_3_background.png");
        level4BackgroundTexture = loadTexture("textures/level_4_background.png");

        skin = buildSkin();

        backgroundMusic = loadMusicIfPresent("music/back_sound.mp3");
        if (backgroundMusic != null) {
            backgroundMusic.setLooping(true);
            applyMusicVolume();
        }
        bounceSound = loadSoundIfPresent("music/bounce_sound.wav");
        winSound = loadSoundIfPresent("music/win_sound.wav");
        loseSound = loadSoundIfPresent("music/defeat_sound.wav");
        modifierSound = loadSoundIfPresent("music/modif_sound.wav");
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

        Slider.SliderStyle sliderStyle = new Slider.SliderStyle();
        sliderStyle.background = skin.newDrawable("white", new Color(0.25f, 0.25f, 0.32f, 1f));
        sliderStyle.background.setMinHeight(8f);
        sliderStyle.knob = skin.newDrawable("white", new Color(0.7f, 0.7f, 0.85f, 1f));
        sliderStyle.knob.setMinWidth(20f);
        sliderStyle.knob.setMinHeight(20f);
        skin.add("default-horizontal", sliderStyle);

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

    private Texture loadTexture(String path) {
        Texture texture = new Texture(Gdx.files.internal(path));
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        ownedTextures.add(texture);
        return texture;
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

    public Texture getModifierTexture(BrickColor color) {
        switch (color) {
            case RED:
                return modificatorRedTexture;
            case GREEN:
                return modificatorGreenTexture;
            default:
                throw new IllegalArgumentException("Unknown modifier color: " + color);
        }
    }

    public Texture getPaddleTexture() {
        return paddleTexture;
    }

    public Texture getObstacleTexture() {
        return obstacleTexture;
    }

    public Texture getMenuBackgroundTexture() {
        return menuBackgroundTexture;
    }

    public Texture getLevelBackgroundTexture(int levelNumber) {
        switch (levelNumber) {
            case 1:
                return level1BackgroundTexture;
            case 2:
                return level2BackgroundTexture;
            case 3:
                return level3BackgroundTexture;
            case 4:
                return level4BackgroundTexture;
            default:
                throw new IllegalArgumentException("Unknown level: " + levelNumber);
        }
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

    public void playModifier() {
        if (modifierSound != null) {
            modifierSound.play();
        }
    }

    public void startMusic() {
        if (backgroundMusic != null && !backgroundMusic.isPlaying()) {
            backgroundMusic.play();
        }
    }

    public void setMusicVolume(float baseVolume) {
        currentBaseMusicVolume = baseVolume;
        applyMusicVolume();
    }

    public void setUserVolumeMultiplier(float multiplier) {
        userVolumeMultiplier = MathUtils.clamp(multiplier, 0f, 1f);
        applyMusicVolume();
    }

    public float getUserVolumeMultiplier() {
        return userVolumeMultiplier;
    }

    private void applyMusicVolume() {
        if (backgroundMusic != null) {
            backgroundMusic.setVolume(currentBaseMusicVolume * userVolumeMultiplier);
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
