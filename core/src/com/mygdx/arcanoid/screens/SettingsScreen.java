package com.mygdx.arcanoid.screens;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.arcanoid.MyGdxGame;

public class SettingsScreen extends AbstractMenuScreen {

    private static final int[] LIVES_OPTIONS = {3, 5, 10};

    public SettingsScreen(MyGdxGame game) {
        super(game);
    }

    @Override
    protected void buildUI(Table root) {
        Label title = new Label("Количество жизней", game.assets.getSkin());

        Table optionsRow = new Table();
        ButtonGroup<TextButton> group = new ButtonGroup<>();
        group.setMinCheckCount(1);
        group.setMaxCheckCount(1);

        int currentLives = game.settings.getLivesSetting();
        for (int lives : LIVES_OPTIONS) {
            TextButton button = new TextButton(String.valueOf(lives), game.assets.getSkin());
            button.setChecked(lives == currentLives);
            button.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    if (button.isChecked()) {
                        game.settings.setLivesSetting(lives);
                    }
                }
            });
            group.add(button);
            optionsRow.add(button).width(100f).height(60f).pad(8f);
        }

        TextButton backButton = new TextButton("Назад", game.assets.getSkin());
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MenuScreen(game));
            }
        });

        root.add(title).padBottom(30f).row();
        root.add(optionsRow).padBottom(30f).row();
        root.add(backButton).width(240f).height(60f);
    }
}
