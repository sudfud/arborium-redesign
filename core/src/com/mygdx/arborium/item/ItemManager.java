package com.mygdx.arborium.item;

import com.badlogic.gdx.Gdx;
import com.mygdx.arborium.game.Plot;
import com.mygdx.arborium.game.StatsManager;
import com.mygdx.arborium.screen.AssetHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Consumer;

public class ItemManager {
    private static ArrayList<Tree> trees;
    private static ArrayList<Fertilizer> fertilizers;
    private static ArrayList<Upgrade> upgrades;
    private static HashMap<Integer, Item> itemMap;

    public static void init(AssetHandler assetHandler) {
        trees = new ArrayList<>();
        itemMap = new HashMap<>();
        trees.add(new Tree(1, "Apple Tree",
                "The hallmark of any orchard.",
                1 * 60 * 1000, 30 * 1000, 4, 1, 25,
                assetHandler.getTexureRegion("apple_tree2x"),
                assetHandler.getTexureRegion("apple2x")));

        trees.add(new Tree(2, "Orange Tree",
                "Deliciously citrus.",
                5 * 60 * 1000, 2 * 60 * 1000, 2, 15, 200,
                assetHandler.getTexureRegion("orange_tree2x"),
                assetHandler.getTexureRegion("orange2x")));

        trees.add(new Tree(3, "Cherry Tree",
                "Small, but numerous. Great on sundaes.",
                20 * 60 * 1000, 15 * 60 * 1000, 8, 35, 325,
                assetHandler.getTexureRegion("cherry_tree2x"),
                assetHandler.getTexureRegion("cherry2x")));

        trees.add(new Tree(4, "Peach Tree",
                "Maaade in Georgiaaaaaaa",
                15 * 60 * 1000, 75 * 60 * 1000, 3, 900, 5000,
                assetHandler.getTexureRegion("peach_tree8x"),
                assetHandler.getTexureRegion("peach8x")));

        trees.add(new Tree(5, "Lemon Tree",
                "Harsh to eat, but makes the best lemonade",
                150 * 60 * 1000,  20 * 60 * 1000, 10, 100, 500,
                assetHandler.getTexureRegion("lemon_tree8x"),
                assetHandler.getTexureRegion("lemon8x")));

        trees.add(new Tree(6, "Sour Green Apple Tree",
                "An upgrade from the original sweet apple, some would say",
                4 * 60 * 60 * 1000, 150 * 60 * 1000, 6, 7500, 100,
                assetHandler.getTexureRegion("green_apple_tree8x"),
                assetHandler.getTexureRegion("green_apple8x")));

        trees.add(new Tree(7, "Lychee Tree",
                "Originally from China. The insides are sweet, and they like to grow in bunches.",
                145 * 60 * 1000, 45 * 60 * 1000, 12, 100, 150,
                assetHandler.getTexureRegion("lychee_tree8x"),
                assetHandler.getTexureRegion("lychee8x")));

        for (Tree tree : trees) {
            itemMap.put(tree.getId(), tree);
        }

        fertilizers = new ArrayList<>();

        fertilizers.add(new Fertilizer(32, "Fruit by the Mile",
                "Contains a small amount of mutagens genetically designed to increase yield. +1 to harvest amount.",
                assetHandler.getTexureRegion("fertilizer8x"),
                (Plot plot) -> plot.setProduceAmountExtra(1)));

        fertilizers.add(new Fertilizer(33, "Speedy Seeds",
                "Filled with electrolytes to make plants grow faster. Reduces grow time by 20%",
                assetHandler.getTexureRegion("fertilizer8x"),
                (plot) -> plot.setMatureTimeMultiplier(0.80f)));

        fertilizers.add(new Fertilizer(34, "Fruitful Endeavours",
                "Strengthens the roots, providing more nutrients to the fruit. Reduces produce time by 20%",
                assetHandler.getTexureRegion("fertilizer8x"),
                (Plot plot) -> plot.setProduceTimeMultiplier(0.80f)));


        for (Fertilizer fert : fertilizers) {
            itemMap.put(fert.getId(), fert);
        }

        upgrades = new ArrayList<>();

        Tree appleTree = (Tree)findItemById(1);

        upgrades.add(new Upgrade(64, "Test Upgrade",

                (id) -> {
            int prodAmount = appleTree.getProduceAmount();
            int prodValue = appleTree.getProduceValue();
            int exp = appleTree.getExperience();
            appleTree.setProduceAmount(prodAmount * 2);
            appleTree.setProduceValue(prodValue * 2);
            appleTree.setExperience(exp * 2);
            },

                () -> {
            int count = StatsManager.getFruitCollected(appleTree);
            return (count >= 100);
            }, 375));
    }

    public static Item findItemById(int id) {
        return itemMap.get(id);
    }

    public static Tree[] getTreeList() {
        Tree[] treeArray = new Tree[trees.size()];
        for (int i = 0; i < treeArray.length; i++) {
            treeArray[i] = trees.get(i);
        }
        return treeArray;
    }

    public static Fertilizer[] getFertilizerList() {
        Fertilizer[] fertArray = new Fertilizer[fertilizers.size()];
        for (int i = 0; i < fertArray.length; i++) {
            fertArray[i] = fertilizers.get(i);
        }
        return fertArray;
    }
}
