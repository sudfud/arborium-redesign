package com.mygdx.arborium.screen.farm;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.arborium.game.Arborium;
import com.mygdx.arborium.game.CurrencyManager;
import com.mygdx.arborium.game.ExperienceManager;
import com.mygdx.arborium.game.Plot;
import com.mygdx.arborium.item.Tree;
import com.mygdx.arborium.screen.GameScreen;

import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

public class FarmScreen extends GameScreen {
    private Arborium arborium;

    // Farm map and its renderer
    private TiledMap tileMap;
    private TiledMapRenderer mapRenderer;

    // The farm's plots
    private Plot[] plots;
    private Plot focusedPlot;   // The plot we're currently looking at;

    // UI
    private Skin skin;
    private HorizontalGroup currencyGroup;
    private Image coin;
    private Label currencyLabel;
    private HorizontalGroup plotInfoGroup;
    private PlotInfoWindow plotInfoWindow;
    private Button leftButton;
    private Button rightButton;
    private TextButton shopButton;

    private int currentLevel;
    private Label levelUpLabel;
    private Label currentLevelLabel;
    private ProgressBar expBar;

    ArrayList<ScoreLabel> scoreLabels;
    Stack<ScoreLabel> scoreLabelStack;

    // Input
    private GestureDetector gestureDetector;

    // Textures
    private TextureRegion sprout;
    private TextureRegion matureTree;

    // Camera Interpolation
    Interpolation cameraLerp;
    Vector3 target;
    Vector3 camPosition;
    float lerpElapsed = 2f;
    float fromZoom = 7.5f;
    float toZoom = 5f;

    // Box2D stuff
    World physWorld;
    private Box2DDebugRenderer physDebug;
    ArrayList<Body> fruitBodies;
    ArrayList<Body> bodyDestroyList;
    private float elapsedTime = 0.0f;
    int fruitCount;
    int fruitCollect;
    private Random random;

    // Sound
    private Sound fruitPickSound;

    // Are we harvesting a tree right now?
    boolean harvesting = false;

    // TEST

    public FarmScreen(Arborium arborium)
    {
        super(arborium);

        this.arborium = arborium;

        // Map initialization
        tileMap = new TmxMapLoader().load("map/arborMap2.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(tileMap, 1 / 64f, spriteBatch);

        plots = new Plot[16];
        final MapObjects plotObjs = tileMap.getLayers().get(2).getObjects();
        for (int i = 0; i < plots.length; i++) {
            RectangleMapObject rect = (RectangleMapObject) plotObjs.get(i);
            plots[i] = new Plot(i, rect.getRectangle());
        }
        focusedPlot = null;

        // UI initialization
        skin = game.getAssetHandler().getSkin();

        currencyGroup = new HorizontalGroup();
        coin = new Image(game.getAssetHandler().getTexureRegion("coin4x"));
        currencyLabel = new Label("" + CurrencyManager.getAmount(), skin, "large");
        currencyGroup.addActor(coin);
        currencyGroup.addActor(currencyLabel);

        plotInfoWindow = new PlotInfoWindow(this, plots[0], skin);
        plotInfoWindow.setVisible(false);

        leftButton = new Button(skin, "left");
        leftButton.addListener(new ClickListener() {
           @Override
           public void clicked(InputEvent event, float x, float y) {
               int focusId = focusedPlot.getId();
               if (focusId > 0) {
                   focusOn(focusId - 1);
                   plotInfoWindow.setPlot(plots[focusId - 1]);
               }
           }
        });
        leftButton.setVisible(false);

        rightButton = new Button(skin, "right");
        rightButton.addListener(new ClickListener() {
           @Override
           public void clicked(InputEvent event, float x, float y) {
               int focusId = focusedPlot.getId();
               if (focusId < plots.length - 1) {
                   focusOn(focusId + 1);
                   plotInfoWindow.setPlot(plots[focusId + 1]);
               }
           }
        });
        rightButton.setVisible(false);

        shopButton = new TextButton("Shop", skin);
        shopButton.addListener(new ClickListener() {
           @Override
           public void clicked(InputEvent event, float x, float y) {
               game.toShopScreen();
           }
        });

        plotInfoGroup = new HorizontalGroup();
        plotInfoGroup.addActor(leftButton);
        plotInfoGroup.addActor(plotInfoWindow);
        plotInfoGroup.addActor(rightButton);

        currentLevel = ExperienceManager.getLevel();
        currentLevelLabel = new Label("Lvl. " + currentLevel, skin);
        levelUpLabel = new Label("Level Up!!!", skin, "large");

        expBar = new ProgressBar(1, 100, 1, false, skin);
        ProgressBar.ProgressBarStyle barStyle = expBar.getStyle();

        barStyle.knobBefore.setLeftWidth(0);
        barStyle.knobBefore.setRightWidth(0);

        barStyle.background.setLeftWidth(0);
        barStyle.background.setRightWidth(0);

        stage.addActor(UITable);

        UITable.add(currencyGroup).colspan(2).center();
        UITable.row();
        UITable.add(plotInfoGroup).expandY().bottom().colspan(2);
        UITable.row();
        UITable.add(currentLevelLabel);
        UITable.add(expBar).padTop(50).padBottom(15).width(Gdx.graphics.getWidth() * 3/4).height(25);
        UITable.row();
        UITable.add(shopButton);

        scoreLabels = new ArrayList<>();
        scoreLabelStack = new Stack<>();

        gestureDetector = new GestureDetector(new FarmScreenInputProcessor(this));

        // Sprite initialization
        sprout = game.getAssetHandler().getTexureRegion("plant8x");
        matureTree = game.getAssetHandler().getTexureRegion("tree8x");

        cameraLerp = Interpolation.sine;
        target = camera.position;

        // Physics initialization
        physWorld = new World(new Vector2(0, -7.5f), true);
        physDebug = new Box2DDebugRenderer();
        fruitBodies = new ArrayList<>();
        bodyDestroyList = new ArrayList<>();
        random = new Random();

        // Sound initialization
        fruitPickSound = Gdx.audio.newSound(Gdx.files.internal("audio/pop1.wav"));
    }

    @Override
    public void show() {
        resetInputProcessor();
        for (Plot plot : plots) {
            plot.load();
        }
    }

    @Override
    public void hide() {
        for (Plot plot : plots) {
            plot.save();
        }
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);

        int mapWidth = tileMap.getProperties().get("width", Integer.class);
        int mapHeight = tileMap.getProperties().get("height", Integer.class);

        camera.position.set(mapWidth / 2f, mapHeight / 2f, 0);
        camera.zoom = 7.5f;
        camera.update();
        spriteBatch = new SpriteBatch();
        spriteBatch.setProjectionMatrix(camera.combined);
        camPosition = camera.position;
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        for (Plot plot : plots) {
            plot.update();
        }

        if (plotInfoWindow.isVisible()) {
            plotInfoWindow.update();
        }

        // Render map
        mapRenderer.setView(camera);
        mapRenderer.render();

        // Render sprites
        spriteBatch.begin();

        for (Plot plot : plots) {
            Rectangle bounds = plot.getBounds();

            float x = bounds.x / 64;
            float y = bounds.y / 64 + 1/4f;

            switch(plot.getState()) {
                case GROWING:
                    spriteBatch.draw(sprout, x, y, 1, 1);
                    break;
                case MATURE:
                    spriteBatch.draw(matureTree, x, y, 1, 1);
                    break;
                case HARVESTABLE:
                    spriteBatch.draw(plot.getPlantedTree().getTexture(), x, y, 1, 1);
            }
        }

        // Draw fruits
        if (harvesting) {

            if (fruitCount < focusedPlot.getPlantedTree().getProduceAmount()) {

                Rectangle bounds = focusedPlot.getBounds();

                float x = bounds.x / 64;
                float y = bounds.y / 64 + 1/4f;

                spriteBatch.draw(focusedPlot.getPlantedTree().getTexture(), x, y, 1, 1);

                elapsedTime += delta;

                // If enough time has passed, produce another fruit and reset the elapsed time
                if (elapsedTime >= 1 / (float)focusedPlot.getPlantedTree().getProduceAmount() * 2) {
                    makeFruit();
                    elapsedTime = 0f;
                }
            }

            // Iterate thru all the fruits on screen
            for (int i = 0; i < physWorld.getBodyCount(); i++) {
                Vector2 position = fruitBodies.get(i).getPosition();

                // If this body is off screen, set it up to be removed
                if (!bodyDestroyList.contains(fruitBodies.get(i)) &&
                        !camera.frustum.pointInFrustum(position.x, position.y, 0)) {

                    fruitCollect++;
                    bodyDestroyList.add(fruitBodies.get(i));
                }

                // Draw fruit
                position.x -= 0.5f;
                position.y -= 0.5f;
                float rotation = MathUtils.radiansToDegrees * fruitBodies.get(i).getAngle();
                TextureRegion fruitTexture = focusedPlot.getPlantedTree().getFruitTexture();
                spriteBatch.draw(fruitTexture, position.x, position.y, 0.5f, 0.5f, 1, 1, 1, 1, rotation);
            }

            // If we reached the produce amount of this tree, stop harvesting
            if (focusedPlot.getPlantedTree().getProduceAmount() == fruitCollect) {
                harvesting = false;
                Gdx.input.setInputProcessor(stage);
            }
        }
        spriteBatch.end();

        // Update camera
        if (lerpElapsed <= 1f) {
            camera.position.lerp(target, lerpElapsed);

            camera.zoom = MathUtils.lerp(camera.zoom, toZoom, lerpElapsed);
            lerpElapsed += delta;
        }

        // Update stage
        stage.act();

        currencyLabel.setText("" + CurrencyManager.getAmount());

        float a = ExperienceManager.getExperience() - ExperienceManager.getPrevLevelThreshold();
        float b = ExperienceManager.getNextLevelThreshold() - ExperienceManager.getPrevLevelThreshold();
        expBar.setValue(a / b * 100f);

        if (currentLevel < ExperienceManager.getLevel()) {
            levelUpLabel.setPosition(0, 0);
            levelUpLabel.getColor().a = 1f;
            levelUpLabel.addAction(Actions.sequence(Actions.parallel(Actions.moveTo(0, 100, 1), Actions.fadeOut(1)),
                    Actions.run(new Runnable() {
                        @Override
                        public void run() {
                            levelUpLabel.remove();
                        }
                    })));
            stage.addActor(levelUpLabel);
            currentLevel = ExperienceManager.getLevel();
            currentLevelLabel.setText("Lvl. " + currentLevel);
        }

        for (ScoreLabel label : scoreLabels) {
            if (label.getStage() != null)
                scoreLabelStack.push(label);
            else
                scoreLabels.remove(label);
        }
        while (!scoreLabelStack.isEmpty()) {
            scoreLabels.remove(scoreLabelStack.pop());
        }
        stage.draw();

        // Update physics
        //physDebug.render(physWorld, camera.combined);
        physWorld.step(1 / 60f, 1, 6);

        // Destroy bodies we don't need anymore
        for (int i = 0; i < bodyDestroyList.size(); i++) {
            Body body = bodyDestroyList.get(i);
            fruitBodies.remove(body);
            physWorld.destroyBody(body);
            bodyDestroyList.remove(body);
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        for (Plot plot : plots) {
            plot.save();
        }
        tileMap.dispose();
        fruitPickSound.dispose();
    }

    public TiledMap getFarmMap() {
        return tileMap;
    }

    public Plot[] getPlots() {
        return plots;
    }

    void showPlotInfoWindow(int plotId) {
        plotInfoWindow.setPlot(plots[plotId]);
        plotInfoWindow.setVisible(true);
        leftButton.setVisible(true);
        rightButton.setVisible(true);
        Gdx.input.setInputProcessor(stage);
        focusOn(plotId);
    }

    void hidePlotInfoWindow() {
        plotInfoWindow.setVisible(false);
        leftButton.setVisible(false);
        rightButton.setVisible(false);
        Gdx.input.setInputProcessor(gestureDetector);
        unfocus();
    }

    void beginHarvest() {
        harvesting = true;
        resetInputProcessor();
        fruitCount = 0;
        fruitCollect = 0;
        makeFruit();
    }

    void makeFruit() {

        Tree tree = focusedPlot.getPlantedTree();
        Rectangle bounds = focusedPlot.getBounds();
        FruitBody fruit = new FruitBody(physWorld, tree.getFruitTexture(),
                bounds.x / 64 + 0.5f,
                bounds.y / 64 + 1f);
        fruitBodies.add(fruit.body);
        float forceX = MathUtils.random(-3f, 3f);
        float forceY = MathUtils.random(6f, 8f);

        Vector2 center = fruit.body.getWorldCenter();

        fruit.body.applyLinearImpulse(forceX, forceY, center.x, center.y + 0.25f, true);

        long id = fruitPickSound.play();
        fruitPickSound.setVolume(id, 2f);

        fruitCount++;
    }

    void createScoreLabel(float x, float y) {
        Tree tree = focusedPlot.getPlantedTree();
        ScoreLabel label = new ScoreLabel(game, skin, x, y, tree.getExperience(), tree.getProduceValue());
        scoreLabels.add(label);
        stage.addActor(label);
    }

    Plot getFocusedPlot() {
        return focusedPlot;
    }

    // Center and zoom the camera on a specific plot
    public void focusOn(int plotId) {
        focusedPlot = plots[plotId];

        Rectangle bounds = focusedPlot.getBounds();
        target = new Vector3((bounds.x + bounds.width/2) / 64, (bounds.y + bounds.height / 2) / 64, 0);
        lerpElapsed = 0f;

        fromZoom = camera.zoom;
        toZoom = 3f;
    }

    // Return the camera to the center of the screen and zoom out
    public void unfocus() {
        int mapWidth = tileMap.getProperties().get("width", Integer.class);
        int mapHeight = tileMap.getProperties().get("height", Integer.class);

        target = new Vector3(mapWidth / 2f, mapHeight / 2f, 0);
        lerpElapsed = 0;

        fromZoom = camera.zoom;
        toZoom = 7.5f;
    }

    // Call this when you want to regain control of the farm
    public void resetInputProcessor() {
        Gdx.input.setInputProcessor(gestureDetector);
    }
}
