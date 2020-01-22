package com.mygdx.arborium.item;

import com.mygdx.arborium.game.Plot;
import com.mygdx.arborium.game.StatsManager;
import com.mygdx.arborium.screen.AssetHandler;

import java.util.ArrayList;
import java.util.HashMap;

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
                1 * 60 * 1000, 30 * 1000, 4, 1, 5,
                assetHandler.getTextureRegion("apple_tree"),
                assetHandler.getTextureRegion("apple_tree_harvest"),
                assetHandler.getTextureRegion("apple2x")));

        trees.add(new Tree(2, "Orange Tree",
                "Deliciously citrus.",
                5 * 60 * 1000, 2 * 60 * 1000, 2, 15, 15,
                assetHandler.getTextureRegion("orange_tree"),
                assetHandler.getTextureRegion("orange_tree_harvest"),
                assetHandler.getTextureRegion("orange2x")));

        trees.add(new Tree(3, "Cherry Tree",
                "Small, but numerous. Great on sundaes.",
                35 * 60 * 1000, 15 * 60 * 1000, 8, 75, 10,
                assetHandler.getTextureRegion("cherry_tree"),
                assetHandler.getTextureRegion("cherry_tree_harvest"),
                assetHandler.getTextureRegion("cherry2x")));

        trees.add(new Tree(4, "Peach Tree",
                "Maaade in Georgiaaaaaaa",
                75 * 60 * 1000, 45 * 60 * 1000, 3, 800, 100,
                assetHandler.getTextureRegion("peach_tree"),
                assetHandler.getTextureRegion("peach_tree_harvest"),
                assetHandler.getTextureRegion("peach8x")));

        trees.add(new Tree(5, "Lemon Tree",
                "Harsh to eat, but makes the best lemonade",
                150 * 60 * 1000, 100 * 60 * 1000, 10, 350, 50,
                assetHandler.getTextureRegion("lemon_tree8x"),
                assetHandler.getTextureRegion("tree8x"),
                assetHandler.getTextureRegion("lemon8x")));

        trees.add(new Tree(6, "Sour Green Apple Tree",
                "An upgrade from the original sweet apple, some would say",
                4 * 60 * 60 * 1000, 240 * 60 * 1000, 6, 1500, 200,
                assetHandler.getTextureRegion("green_apple_tree8x"),
                assetHandler.getTextureRegion("tree8x"),
                assetHandler.getTextureRegion("green_apple8x")));

        trees.add(new Tree(7, "Lychee Tree",
                "Originally from China. The insides are sweet, and they like to grow in bunches.",
                12 * 60 * 60 * 1000, 6 * 60 * 60 * 1000, 12, 2000, 150,
                assetHandler.getTextureRegion("lychee_tree8x"),
                assetHandler.getTextureRegion("tree8x"),
                assetHandler.getTextureRegion("lychee8x")));

        for (Tree tree : trees) {
            itemMap.put(tree.getId(), tree);
        }

        fertilizers = new ArrayList<>();

        fertilizers.add(new Fertilizer(32, "Fruit by the Mile",
                "Contains a small amount of mutagens genetically designed to increase yield. +1 to harvest amount.",
                assetHandler.getTextureRegion("fertilizer8x"),
                (Plot plot) -> plot.setProduceAmountExtra(1)));

        fertilizers.add(new Fertilizer(33, "Speedy Seeds",
                "Filled with electrolytes to make plants grow faster. Reduces grow time by 20%",
                assetHandler.getTextureRegion("fertilizer8x"),
                (plot) -> plot.setMatureTimeMultiplier(0.80f)));

        fertilizers.add(new Fertilizer(34, "Fruitful Endeavours",
                "Strengthens the roots, providing more nutrients to the fruit. Reduces produce time by 20%",
                assetHandler.getTextureRegion("fertilizer8x"),
                (Plot plot) -> plot.setProduceTimeMultiplier(0.80f)));

        fertilizers.add(new Fertilizer(35, "Fruit Frenzy",
                "+5 fruit per harvest",
                assetHandler.getTextureRegion("fertilizer8x"),
                (plot) -> plot.setProduceAmountExtra(5)));

        fertilizers.add(new Fertilizer(36, "Mach 3",
                "-40% grow time",
                assetHandler.getTextureRegion("fertilizer8x"),
                (plot) -> plot.setMatureTimeMultiplier(0.60f)));

        fertilizers.add(new Fertilizer(37, "Big & Bountiful",
                "-40% produce time",
                assetHandler.getTextureRegion("fertilizer8x"),
                (plot) -> plot.setProduceTimeMultiplier(0.60f)));


        for (Fertilizer fert : fertilizers) {
            itemMap.put(fert.getId(), fert);
        }

        upgrades = new ArrayList<>();
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
