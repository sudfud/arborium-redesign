package com.mygdx.arborium.screen.farm;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.mygdx.arborium.game.CurrencyManager;
import com.mygdx.arborium.game.ExperienceManager;
import com.mygdx.arborium.game.Plot;
import com.mygdx.arborium.game.StatsManager;
import com.mygdx.arborium.item.Tree;

import java.util.ArrayList;


public class FarmScreenInputProcessor implements GestureListener {

    private final int MAP_W;
    private final int MAP_H;

    private FarmScreen farmScreen;
    private MapObjects farmMapObjects;

    private Vector3 worldCoords;
    private Vector3 screenCoords;

    public FarmScreenInputProcessor(FarmScreen farmScreen) {
        this.farmScreen = farmScreen;

        TiledMap map = farmScreen.getFarmMap();
        MAP_W = map.getProperties().get("width", Integer.class);
        MAP_H = map.getProperties().get("height", Integer.class);
        farmMapObjects = map.getLayers().get(2).getObjects();


        worldCoords = new Vector3();
        screenCoords = new Vector3();
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {

        // Check to see if a fruit on screen was tapped
        if (farmScreen.harvesting && farmScreen.physWorld.getBodyCount() > 0) {
            screenCoords.set(x, y, 0);
            worldCoords = farmScreen.getCamera().unproject(screenCoords);
            for (Body body : farmScreen.fruitBodies) {
                Fixture fixture = body.getFixtureList().get(0);
                if (fixture.testPoint(worldCoords.x, worldCoords.y)) {
                    farmScreen.bodyDestroyList.add(body);
                    farmScreen.fruitCollect++;
                    StatsManager.incFruitCollected(farmScreen.getFocusedPlot().getPlantedTree());
                    screenCoords = farmScreen.getCamera().project(worldCoords);
                    farmScreen.createScoreLabel(screenCoords.x, screenCoords.y);

                    Tree tree = farmScreen.getFocusedPlot().getPlantedTree();
                    ExperienceManager.addExperience(tree.getExperience());
                    CurrencyManager.add(tree.getProduceValue());
                    break;  // Only one fruit can be collected per tap
                }
            }
        }

        // If we're not harvesting, check if a plot has been tapped
        else if (!farmScreen.harvesting) {
            ArrayList<Plot> plots = farmScreen.getPlots();
            for (Plot plot : plots) {
                Rectangle rect = plot.getBounds();

                screenCoords.set(x, y, 0);
                worldCoords = farmScreen.getCamera().unproject(screenCoords);

                if (rect.contains(worldCoords.x * 64, worldCoords.y * 64)) {
                    farmScreen.showPlotInfoWindow(plot);
                }
            }
        }
        return true;
    }

    @Override
    public boolean longPress(float x, float y) {

        return true;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        return false;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        if (farmScreen.getFocusedPlot() == null) {
            OrthographicCamera screenCamera = farmScreen.getCamera();

            float viewportHalfX = screenCamera.viewportWidth/2;
            float viewportHalfY = screenCamera.viewportHeight / 2;



            float zoom = screenCamera.zoom;
            screenCamera.translate(-deltaX / 512f * zoom, deltaY / 512f * zoom);
            screenCamera.position.x = MathUtils.clamp(screenCamera.position.x, viewportHalfX * zoom, MAP_W - viewportHalfX * zoom);
            screenCamera.position.y = MathUtils.clamp(screenCamera.position.y, viewportHalfY * zoom, MAP_H - viewportHalfY * zoom);
            farmScreen.getCamera().update();
        }
        return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        return false;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        return false;
    }

    @Override
    public void pinchStop() {

    }
}
