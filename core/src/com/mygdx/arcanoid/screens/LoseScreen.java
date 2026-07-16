package com.mygdx.arcanoid.screens;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.arcanoid.MyGdxGame;

public class LoseScreen extends AbstractMenuScreen {

    public LoseScreen(MyGdxGame game) {
        super(game);
    }

    @Override
    protected void buildUI(Table root) {
        game.assets.playLose();

        Label label = new Label("Игра окончена", game.assets.getSkin());

        TextButton retryButton = new TextButton("Попробовать снова", game.assets.getSkin());
        retryButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.settings.resetProgress();
                game.setScreen(new GameScreen(game, 1));
            }
        });

        TextButton menuButton = new TextButton("В меню", game.assets.getSkin());
        menuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MenuScreen(game));
            }
        });

        root.add(label).padBottom(30f).row();
        root.add(retryButton).width(240f).height(60f).padBottom(16f).row();
        root.add(menuButton).width(240f).height(60f);
    }
}
