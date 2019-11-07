package com.mygdx.arborium.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.mygdx.arborium.game.Arborium;

// Base game class

public abstract class GameScreen implements Screen {

    protected Arborium game;

    protected OrthographicCamera camera;

    protected Stage stage;
    protected Table UITable;

    protected SpriteBatch spriteBatch;

    public GameScreen(Arborium game) {
        this.game = game;

        stage = new Stage(new ExtendViewport(800, 480));
        UITable = new Table();
        UITable.setFillParent(true);
        stage.addActor(UITable);

        spriteBatch = new SpriteBatch();
        spriteBatch.enableBlending();
        camera = new OrthographicCamera();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        clearScreen();
        camera.update();
        spriteBatch.setProjectionMatrix(camera.combined);
    }

    @Override
    public void resize(int width, int height) {
        float aspectRatio = (float)width / (float)height;

        camera.setToOrtho(false, 2f * aspectRatio, 2f);
        camera.update();
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
        spriteBatch.dispose();
        stage.dispose();
    }

    protected void clearScreen() {
        Gdx.gl.glClearColor(0, 0.5f, 0.5f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    public Arborium getGameInstance() {
        return game;
    }

    public OrthographicCamera getCamera() {
        return camera;
    }
}
