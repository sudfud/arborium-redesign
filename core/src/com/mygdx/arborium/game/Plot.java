package com.mygdx.arborium.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.TimeUtils;
import com.mygdx.arborium.item.ItemManager;
import com.mygdx.arborium.item.Tree;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class Plot implements ISaveable {

    private final int id;
    private final String stateKey;
    private final String treeIdKey;
    private final String plantTimeKey;
    private final String prodTimeKey;
    private final String matureMultiKey;
    private final String prodTimeMultiKey;
    private final String prodAmtMultiKey;
    private final String prodValMultiKey;
    private final String expMultiKey;

    private Preferences preferences;    // Used to save properties of the plot

    private int treeId;
    private Tree plantedTree;

    private long plantedTimestamp;
    private long timeSincePlanted;

    private long produceTimestamp;
    private long timeSinceLastHarvest;

    // Multipliers
    private float matureTimeMultiplier = 1f;
    private float produceTimeMultiplier = 1f;
    private float produceAmountMultiplier = 1f;
    private float produceValueMultiplier = 1f;
    private float expMultiplier = 1f;

    public enum Multiplier {
        MATURE_TIME, PRODUCE_TIME, PRODUCE_AMT, PRODUCE_VAL, EXP
    }

    private Rectangle bounds;

    public enum PlotState {
        EMPTY, GROWING, MATURE, HARVESTABLE
    }
    private PlotState currentState;

    public Plot(int id, Rectangle bounds) {
        preferences = Gdx.app.getPreferences("Plots");
        this.id = id;
        this.bounds = bounds;

        String head = "Plot" + id;

        stateKey = head + "State";
        treeIdKey = head + "TreeId";
        plantTimeKey = head + "PlantTime";
        prodTimeKey = head + "ProduceTime";
        matureMultiKey = head + "MatureTimeMultiplier";
        prodTimeMultiKey = head + "ProduceTimeMultiplier";
        prodAmtMultiKey = head + "ProduceAmountMultiplier";
        prodValMultiKey = head + "ProduceValueMultiplier";
        expMultiKey = head + "ExpMultiplier";
        load();
    }

    public void update() {
        long time = TimeUtils.millis();
        switch(currentState) {

            case GROWING:
                // Check to see if the tree is ready to mature
                timeSincePlanted = TimeUtils.timeSinceMillis(plantedTimestamp);
                if (timeSincePlanted >= plantedTree.getGrowTime()) {
                    currentState = PlotState.MATURE;
                    produceTimestamp = time;
                    save();
                }
                break;

            case MATURE:
            case HARVESTABLE:
                // Check if the tree is ready to harvest
                timeSinceLastHarvest = TimeUtils.timeSinceMillis(produceTimestamp);
                if (currentState != PlotState.HARVESTABLE && timeSinceLastHarvest >= plantedTree.getProduceTime()) {
                    currentState = PlotState.HARVESTABLE;
                }
        }
    }

    public void plant(Tree tree) {
        currentState = PlotState.GROWING;
        plantedTree = tree;
        plantedTimestamp = TimeUtils.millis();
        produceTimestamp = TimeUtils.millis();
        treeId = tree.getId();
        save();
    }

    public void harvest() {
        currentState = PlotState.MATURE;
        produceTimestamp = TimeUtils.millis();
        save();
    }

    public void clear() {
        currentState = PlotState.EMPTY;
        plantedTree = null;
        treeId = -1;
    }

    @Override
    public void load() {
        currentState = PlotState.values()[preferences.getInteger(stateKey, 0)];
        treeId = preferences.getInteger(treeIdKey, -1);
        plantedTimestamp = preferences.getLong(plantTimeKey, 0);
        produceTimestamp = preferences.getLong(prodTimeKey, 0);

        if (treeId == -1) {
            plantedTree = null;
        }
        else {
            plantedTree = (Tree)ItemManager.findItemById(treeId);
            if (currentState == PlotState.GROWING) {
                timeSincePlanted = TimeUtils.timeSinceMillis(plantedTimestamp);
                if (timeSincePlanted > plantedTree.getGrowTime()) {
                    currentState = PlotState.MATURE;
                }
            }
            if (currentState == PlotState.MATURE) {
                timeSinceLastHarvest = TimeUtils.timeSinceMillis(produceTimestamp);
                if (timeSinceLastHarvest > plantedTree.getProduceTime()) {
                    currentState = PlotState.HARVESTABLE;
                }
            }
        }

        //updateState();
    }

    @Override
    public void save() {
        preferences.putInteger(stateKey, Arrays.asList(PlotState.values()).indexOf(currentState));
        preferences.putInteger(treeIdKey, treeId);
        preferences.putLong(plantTimeKey, plantedTimestamp);
        preferences.putLong(prodTimeKey, produceTimestamp);
        preferences.putFloat(matureMultiKey, matureTimeMultiplier);
        preferences.putFloat(prodTimeMultiKey, produceTimeMultiplier);
        preferences.putFloat(prodAmtMultiKey, produceAmountMultiplier);
        preferences.flush();
    }

    public int getId() {
        return id;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public Tree getPlantedTree() {
        return plantedTree;
    }

    public PlotState getState() {
        return currentState;
    }

    public float getMatureTimeMultiplier() {
        return matureTimeMultiplier;
    }

    public void setMatureTimeMultiplier(float val) {
        matureTimeMultiplier = val;
    }


    public float getProduceTimeMultiplier() {
        return produceTimeMultiplier;
    }

    public void setProduceTimeMultiplier(float val) {
        produceTimeMultiplier = val;
    }


    public float getProduceAmountMultiplier() {
        return produceAmountMultiplier;
    }

    public void setProduceAmountMultiplier(float val) {
        produceAmountMultiplier = val;
    }


    public float getProduceValueMultiplier() {
        return produceValueMultiplier;
    }

    public void setProduceValueMultiplier(float val) {
        produceValueMultiplier = val;
    }


    public float getExpMultiplier() {
        return expMultiplier;
    }

    public void setExpMultiplier(float val) {
        expMultiplier = val;
    }

    // Use this to convert time in milliseconds to a more human-readable format
    private String timeFormat(long millis)
    {
        return String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)), // The change is in this line
                TimeUnit.MILLISECONDS.toSeconds(millis) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
    }

    private void updateState() {
        long time = TimeUtils.millis();

        if (treeId == -1) {
            currentState = PlotState.EMPTY;
        }

        else
        {
            // If it's not either, it's still growing
            if (currentState != PlotState.MATURE && currentState != PlotState.HARVESTABLE) {
                currentState = PlotState.GROWING;
            }
        }
    }

    @Override
    public String toString() {
        String info = "State: ";
        switch (currentState) {
            case EMPTY:
                info += "Empty";
                break;

            case GROWING:
                info += "Growing";
                info += "\nPlanted: " + plantedTree.getName();
                info += "\nMatures in: " +
                        timeFormat(plantedTree.getGrowTime() - timeSincePlanted);
                break;

            case MATURE:
                info += "Mature";
                info += "\nPlanted: " + plantedTree.getName();
                info += "\nHarvest in: " +
                        timeFormat(plantedTree.getProduceTime() - timeSinceLastHarvest);
                break;

            case HARVESTABLE:
                info += "Ready to harvest!";
                info += "\nPlanted: " + plantedTree.getName();
        }

        return info;
    }
}
