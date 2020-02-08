package com.mygdx.arborium.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.mygdx.arborium.manager.ItemManager;
import com.mygdx.arborium.item.Tree;

import java.util.HashMap;
import java.util.List;

public class StatsManager
{
    private static Preferences preferences;

    private static final String treePlantKey = "TreesPlanted";
    private String[] fruitCollectedKeys;

    private static int treesPlanted;

    private static Tree[] trees;

    private static HashMap<Tree, Integer> totalFruitCollected;
    private static int totalCurrency = 0;

    public static void initialize() {
        preferences = Gdx.app.getPreferences("stats");

        totalFruitCollected = new HashMap<>();

        trees = ItemManager.getTreeList();

        load();
    }

    public static int getTreesPlanted() {
        return treesPlanted;
    }

    public static void incTreesPlanted() {
        treesPlanted++;
        save();
    }

    public static int getFruitCollected(Tree tree) {
        return totalFruitCollected.get(tree);
    }

    public static void incFruitCollected(Tree tree) {
        int frt = totalFruitCollected.get(tree);
        totalFruitCollected.put(tree, frt + 1);
        save();
    }

    public static void load() {
        for(Tree tree : trees) {
            int frtCollect = preferences.getInteger(tree.getId() + "Frt Collect", 0);
            totalFruitCollected.put(tree, frtCollect);
        }
        treesPlanted = preferences.getInteger(treePlantKey, 0);
    }

    public static void save() {
        for (Tree tree : trees) {
            preferences.putInteger(tree.getId() + "Frt Collect", totalFruitCollected.get(tree));
        }
        preferences.putInteger(treePlantKey, treesPlanted);
        preferences.flush();
    }
}
