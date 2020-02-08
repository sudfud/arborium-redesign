package com.mygdx.arborium.screen;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.arborium.game.Arborium;

// Base game class

public abstract class GameScreen implements Screen {

    final int WORLD_WIDTH = 100;
    final int WORLD_HEIGHT = 100;

    protected Arborium game;

    protected OrthographicCamera camera;

    private Viewport viewport;
    public Stage stage;
    protected Table UITable;

    protected SpriteBatch spriteBatch;

    public GameScreen(Arborium game) {
        this.game = game;

        spriteBatch = new SpriteBatch();
        spriteBatch.enableBlending();

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        camera = new OrthographicCamera(30 * (w / h), 30);
        if (Gdx.app.getType() == Application.ApplicationType.Desktop)
            viewport = new ExtendViewport(1250, 1250, new OrthographicCamera());
        else
            viewport = new ExtendViewport(750, 750, new OrthographicCamera());
        viewport.update((int)w, (int)h);
        stage = new Stage(viewport);
        UITable = new Table();
        UITable.setFillParent(true);
        stage.addActor(UITable);
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
