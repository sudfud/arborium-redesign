package com.mygdx.arborium.screen.farm;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Sound;
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
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.arborium.game.Arborium;
import com.mygdx.arborium.manager.CurrencyManager;
import com.mygdx.arborium.manager.ExperienceManager;
import com.mygdx.arborium.game.Plot;
import com.mygdx.arborium.manager.StatsManager;
import com.mygdx.arborium.item.Tree;
import com.mygdx.arborium.screen.GameScreen;
import com.mygdx.arborium.ui.FarmScreenTable;
import com.mygdx.arborium.ui.ScoreLabel;
import com.mygdx.arborium.ui.WelcomeWindow;

import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

public class FarmScreen extends GameScreen {

    private int id;

    private final float MAX_ZOOM = 5.25f;
    
    private static int totalPlotCount = 0;
    private ArrayList<Plot> plots;

    private Arborium arborium;

    private Preferences preferences;

    private boolean locked;
    private int farmCost;

    // Farm map and its renderer
    private TiledMap tileMap;
    private TiledMapRenderer mapRenderer;

    private Plot focusedPlot;   // The plot we're currently looking at;

    // UI
    private Skin skin;
    private HorizontalGroup currencyGroup;
    private Image coin;
    private Label currencyLabel;
    private HorizontalGroup plotInfoGroup;
    private FarmScreenTable farmScreenTable;
    private ImageButton leftButton;
    private ImageButton rightButton;
    private TextButton shopButton;
    private TextButton statsButton;

    private Label lockedLabel;
    private HorizontalGroup priceGroup;
    private Image coin2;
    private Label priceLabel;
    private TextButton unlockButton;

    private int currentLevel;
    private Label levelUpLabel;
    private Label currentLevelLabel;
    private ProgressBar expBar;

    ArrayList<ScoreLabel> scoreLabels;
    Stack<ScoreLabel> scoreLabelStack;

    WelcomeWindow welcomeWindow;

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
    float fromZoom = MAX_ZOOM;
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

    public FarmScreen(int id, Arborium arborium, int plotCount, String mapDir)
    {
        super(arborium);

        this.id = id;

        this.arborium = arborium;

        preferences = Gdx.app.getPreferences("Farm " + id);

        locked = false;
        farmCost = 0;

        // Map initialization

       tileMap = new TmxMapLoader().load(mapDir);
        mapRenderer = new OrthogonalTiledMapRenderer(tileMap, 1 / 64f, spriteBatch);

        if (plots == null) {
            plots = new ArrayList<>();
        }
        final MapObjects plotObjs = tileMap.getLayers().get(3).getObjects();
        for (int i = 0; i < plotCount; i++) {
            RectangleMapObject rect = (RectangleMapObject) plotObjs.get(i);
            plots.add(new Plot(totalPlotCount, rect.getRectangle()));
            totalPlotCount++;
        }
        focusedPlot = null;

        for (Plot plot : plots) {
            plot.load();
        }

        // UI initialization
        skin = game.getAssetHandler().getSkin();

        currencyGroup = new HorizontalGroup();
        coin = new Image(game.getAssetHandler().getTextureRegion("coin4x"));
        currencyLabel = new Label("" + CurrencyManager.getAmount(), skin);
        currencyGroup.addActor(coin);
        currencyGroup.addActor(currencyLabel);

        farmScreenTable = new FarmScreenTable(this, plots.get(0), skin);
        farmScreenTable.setVisible(false);

        leftButton = new ImageButton(skin, "left");
        leftButton.setScale(2);
        leftButton.addListener(new ClickListener() {
           @Override
           public void clicked(InputEvent event, float x, float y) {
               game.previousFarmScreen();
           }
        });

        rightButton = new ImageButton(skin, "right");
        rightButton.setScale(3);
        rightButton.addListener(new ClickListener() {
           @Override
           public void clicked(InputEvent event, float x, float y) {
               game.nextFarmScreen();
           }
        });

        shopButton = new TextButton("Shop", skin);
        shopButton.addListener(new ClickListener() {
           @Override
           public void clicked(InputEvent event, float x, float y) {
               game.toShopScreen();
           }
        });

        statsButton = new TextButton("Stats", skin);
        statsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.toStatsScreen();
            }
        });

        currentLevel = ExperienceManager.getLevel();
        currentLevelLabel = new Label("Lvl. " + currentLevel, skin);
        levelUpLabel = new Label("Level Up!!!", skin);

        expBar = new ProgressBar(1, 100, 1, false, skin);
        ProgressBar.ProgressBarStyle barStyle = expBar.getStyle();

        barStyle.knobBefore.setLeftWidth(0);
        barStyle.knobBefore.setRightWidth(0);

        barStyle.background.setLeftWidth(0);
        barStyle.background.setRightWidth(0);

        lockedLabel = new Label("[LOCKED]", skin);

        priceGroup = new HorizontalGroup();
        coin2 = new Image(game.getAssetHandler().getTextureRegion("coin4x"));
        priceLabel = new Label("", skin);

        unlockButton = new TextButton("Unlock", skin);
        unlockButton.addListener(new ClickListener() {
           @Override
           public void clicked(InputEvent event, float x, float y) {
               if (CurrencyManager.getAmount() >= farmCost) {
                   CurrencyManager.subtract(farmCost);
                   locked = false;
                   save();
                   showUI();
               }
           }
        });

        priceGroup.addActor(coin2);
        priceGroup.addActor(priceLabel);

        scoreLabels = new ArrayList<>();
        scoreLabelStack = new Stack<>();

        gestureDetector = new GestureDetector(new FarmScreenInputProcessor(this));

        // Sprite initialization
        sprout = game.getAssetHandler().getTextureRegion("plant8x");
        matureTree = game.getAssetHandler().getTextureRegion("tree_blue");

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

        //showUI();

        welcomeWindow = new WelcomeWindow(skin, this);
        UITable.add(welcomeWindow);
    }

    public FarmScreen(int id, Arborium arborium, int plotCount, String mapDir, int farmCost) {
        this(id, arborium, plotCount, mapDir);

        locked = preferences.getBoolean("locked", true);
        this.farmCost = farmCost;
        save();
    }

    @Override
    public void show() {
        resetInputProcessor();
        if (locked)
            showLockedUI();
        else
            showUI();

        int mapWidth = tileMap.getProperties().get("width", Integer.class);
        int mapHeight = tileMap.getProperties().get("height", Integer.class);

        camera.position.set(mapWidth / 2f, mapHeight / 2f, 0);
        camera.zoom = MAX_ZOOM;
        camera.update();
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
        camera.zoom = MAX_ZOOM;
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

        if (farmScreenTable.isVisible()) {
            farmScreenTable.update();
        }

        // Render map
        mapRenderer.setView(camera);
        mapRenderer.render();

        // Render sprites
        spriteBatch.begin();

        // Render each plot's tree, if it has one
        for (Plot plot : plots) {
            Rectangle bounds = plot.getBounds();

            float x = bounds.x / 64;
            float y = bounds.y / 64 + 3/8f;

            Tree plantedTree = plot.getPlantedTree();

            if (plantedTree != null)
                switch(plot.getState()) {
                case GROWING:
                    spriteBatch.draw(plantedTree.getSpriteFrame(plot.getTimeSincePlanted()), x, y, 1, 1);
                    break;
                case MATURE:
                    spriteBatch.draw(plot.getPlantedTree().getMatureTreeTexture(), x, y, 1, 1);
                    break;
                case HARVESTABLE:
                    spriteBatch.draw(plot.getPlantedTree().getHarvestTreeTexture(), x, y, 1, 1);
            }
        }

        // Draw fruits
        if (harvesting) {

            if (fruitCount < focusedPlot.getPlantedTree().getProduceAmount() + focusedPlot.getProduceAmountExtra()) {

                Rectangle bounds = focusedPlot.getBounds();

                float x = bounds.x / 64;
                float y = bounds.y / 64 + 3/8f;

                spriteBatch.draw(focusedPlot.getPlantedTree().getHarvestTreeTexture(), x, y, 1, 1);

                elapsedTime += delta;

                // If enough time has passed, produce another fruit and reset the elapsed time
                if (elapsedTime >= 1 / (float)focusedPlot.getPlantedTree().getProduceAmount() * 3/2 ) {
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
            if (focusedPlot.getPlantedTree().getProduceAmount() + focusedPlot.getProduceAmountExtra() == fruitCollect) {
                harvesting = false;
                StatsManager.save();
            }
        }
        spriteBatch.end();

        // Update camera
        if (lerpElapsed <= 1f) {
            camera.position.lerp(target, lerpElapsed);

            camera.zoom = MathUtils.lerp(camera.zoom, toZoom, lerpElapsed);
            lerpElapsed += delta / 2f;
        }
        else if (lerpElapsed > 1f && toZoom == MAX_ZOOM && focusedPlot != null) {
            focusedPlot = null;
        }

        // Update stage
        stage.act();

        currencyLabel.setText("" + CurrencyManager.getAmount());

        // Update exp bar
        float a = ExperienceManager.getExperience() - ExperienceManager.getPrevLevelThreshold();
        float b = ExperienceManager.getNextLevelThreshold() - ExperienceManager.getPrevLevelThreshold();
        expBar.setValue(a / b * 100f);

        // Level up if needed
        if (currentLevel < ExperienceManager.getLevel()) {
            levelUpLabel.setPosition(0, 0);
            levelUpLabel.getColor().a = 1f;
            levelUpLabel.addAction(Actions.sequence(Actions.parallel(Actions.moveTo(0, 100, 1), Actions.fadeOut(1)),
                    Actions.run(() -> {
                            levelUpLabel.remove();
                        }
                    )));
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

    public ArrayList<Plot> getPlots() {
        return plots;
    }

    void showPlotInfoWindow(Plot plot) {
        farmScreenTable.setPlot(plot);
        farmScreenTable.setVisible(true);
        focusOn(plot);
    }

    public void hidePlotInfoWindow() {
        farmScreenTable.setVisible(false);
    }

    public void beginHarvest() {
        harvesting = true;
        fruitCount = 0;
        fruitCollect = 0;
        makeFruit();
    }

    private void makeFruit() {

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
    private void focusOn(Plot plot) {
        focusedPlot = plot;

        Rectangle bounds = focusedPlot.getBounds();
        target = new Vector3((bounds.x + bounds.width/2) / 64, (bounds.y + bounds.height / 2) / 64, 0);
        lerpElapsed = 0f;

        fromZoom = camera.zoom;
        toZoom = 3f;
    }

    // Return the camera to the center of the screen and zoom out
    public void unfocus() {
        //target = new Vector3(mapWidth / 2f, mapHeight / 2f, 0);
        lerpElapsed = 0;

        fromZoom = camera.zoom;
        toZoom = MAX_ZOOM;
    }

    // Call this when you want to regain control of the farm
    public void resetInputProcessor() {
        InputMultiplexer inputMulti = new InputMultiplexer();
        inputMulti.addProcessor(stage);
        inputMulti.addProcessor(gestureDetector);
        Gdx.input.setInputProcessor(inputMulti);
    }

    private void save() {
        preferences.putBoolean("locked", locked);
        preferences.putInteger("cost", farmCost);
        preferences.flush();
    }

     public void showUI() {
        UITable.clear();

        if (welcomeWindow.isVisible())
            UITable.add(welcomeWindow).width(Value.percentWidth(0.8f, UITable));

        else {
            UITable.add(currencyGroup).colspan(4).center();
            UITable.row();
            UITable.add(farmScreenTable).colspan(4).expand().bottom();
            UITable.row();
            UITable.add(currentLevelLabel);
            UITable.add(expBar).padTop(50).padBottom(15).width(Value.percentWidth(.5f, UITable)).height(25).expandX();
            UITable.row();
            UITable.add(leftButton);
            UITable.add(shopButton).size(150, 75).expandX();
            UITable.add(statsButton).size(150, 75).expandX();
            UITable.add(rightButton);
        }
    }

    private void showLockedUI() {
        priceLabel.setText("" + farmCost);

        UITable.clear();
        UITable.add(currencyGroup).colspan(4).center();
        UITable.row();
        UITable.add(lockedLabel).colspan(4).expand();
        UITable.row();
        UITable.add(priceGroup).colspan(4);
        UITable.row();
        UITable.add(unlockButton).colspan(4);
        UITable.row();
        UITable.add(leftButton);
        UITable.add(shopButton).size(150, 75).expandX();
        UITable.add(rightButton);
    }
}
