package com.mygdx.arcanoid;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.arcanoid.assets.Assets;
import com.mygdx.arcanoid.persistence.GameSettings;
import com.mygdx.arcanoid.screens.MenuScreen;

public class MyGdxGame extends Game {

    public SpriteBatch batch;
    public Assets assets;
    public GameSettings settings;

    @Override
    public void create() {
        batch = new SpriteBatch();
        assets = new Assets();
        settings = new GameSettings();
        assets.startMusic();
        setScreen(new MenuScreen(this));
    }

    @Override
    public void setScreen(Screen screen) {
        Screen old = getScreen();
        super.setScreen(screen);
        if (old != null) {
            old.dispose();
        }
    }

    @Override
    public void dispose() {
        if (getScreen() != null) {
            getScreen().dispose();
        }
        batch.dispose();
        assets.dispose();
    }
}
