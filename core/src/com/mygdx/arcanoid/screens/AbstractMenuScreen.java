package com.mygdx.arcanoid.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.arcanoid.MyGdxGame;

public abstract class AbstractMenuScreen implements Screen {

    protected final MyGdxGame game;
    protected Stage stage;

    protected AbstractMenuScreen(MyGdxGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport(), game.batch);
        Table root = new Table();
        root.setFillParent(true);
        stage.addActor(root);
        buildUI(root);
        Gdx.input.setInputProcessor(stage);
    }

    protected abstract void buildUI(Table root);

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.08f, 0.08f, 0.1f, 1f);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
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
        stage.dispose();
    }
}
