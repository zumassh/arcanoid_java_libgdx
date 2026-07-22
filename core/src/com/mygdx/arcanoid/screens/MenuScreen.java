package com.mygdx.arcanoid.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.mygdx.arcanoid.MyGdxGame;

public class MenuScreen extends AbstractMenuScreen {

    public MenuScreen(MyGdxGame game) {
        super(game);
    }

    @Override
    protected void buildUI(Table root) {
        root.setBackground(new TextureRegionDrawable(new TextureRegion(game.assets.getMenuBackgroundTexture())));

        Label title = new Label("АРКАНОИД", game.assets.getSkin());
        title.setFontScale(2f);

        TextButton playButton = new TextButton("Играть", game.assets.getSkin());
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.settings.resetProgress();
                game.setScreen(new GameScreen(game, 1));
            }
        });

        TextButton settingsButton = new TextButton("Настройки", game.assets.getSkin());
        settingsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new SettingsScreen(game));
            }
        });

        TextButton exitButton = new TextButton("Выход", game.assets.getSkin());
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        root.add(title).padBottom(40f).row();
        root.add(playButton).width(240f).height(60f).padBottom(16f).row();
        root.add(settingsButton).width(240f).height(60f).padBottom(16f).row();
        root.add(exitButton).width(240f).height(60f).row();
    }
}
