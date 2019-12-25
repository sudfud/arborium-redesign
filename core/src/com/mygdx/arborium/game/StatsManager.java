package com.mygdx.arborium.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.mygdx.arborium.item.ItemManager;
import com.mygdx.arborium.item.Tree;

import java.util.HashMap;
import java.util.List;

public class StatsManager
{
    private static Preferences preferences;

    private String[] fruitCollectedKeys;

    private static Tree[] trees;

    private static HashMap<Tree, Integer> totalFruitCollected;
    private static int totalCurrency = 0;

    public static void initialize() {
        preferences = Gdx.app.getPreferences("stats");

        totalFruitCollected = new HashMap<>();

        trees = ItemManager.getTreeList();

        load();
    }

    public static int getFruitCollected(Tree tree) {
        return totalFruitCollected.get(tree);
    }

    public static void incFruitCollected(Tree tree) {
        int frt = totalFruitCollected.get(tree);
        totalFruitCollected.put(tree, frt + 1);
    }

    public static void load() {
        for(Tree tree : trees) {
            int frtCollect = preferences.getInteger(tree.getId() + "Frt Collect", 0);
            totalFruitCollected.put(tree, frtCollect);
        }
    }

    public static void save() {
        for (Tree tree : trees) {
            preferences.putInteger(tree.getId() + "Frt Collect", totalFruitCollected.get(tree));
        }
        preferences.flush();
    }
}
