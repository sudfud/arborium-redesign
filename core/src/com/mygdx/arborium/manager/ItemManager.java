package com.mygdx.arborium.manager;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.arborium.game.Plot;
import com.mygdx.arborium.item.Fertilizer;
import com.mygdx.arborium.item.Item;
import com.mygdx.arborium.item.Tree;
import com.mygdx.arborium.item.Upgrade;
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
                2 * 60 * 1000, 30 * 1000, 4, 1, 5,
                makeTreeSpriteArray("apple_tree", assetHandler),
                assetHandler.getTextureRegion("apple2x")));

        trees.add(new Tree(2, "Orange Tree",
                "Deliciously citrus.",
                5 * 60 * 1000, (int)(2.5 * 60 * 1000), 2, 15, 50,
                makeTreeSpriteArray("orange_tree", assetHandler),
                assetHandler.getTextureRegion("orange2x")));

        trees.add(new Tree(3, "Cherry Tree",
                "Small, but numerous. Great on sundaes.",
                25 * 60 * 1000, 10 * 60 * 1000, 8, 20, 40,
                makeTreeSpriteArray("cherry_tree", assetHandler),
                assetHandler.getTextureRegion("cherry2x")));

        trees.add(new Tree(4, "Peach Tree",
                "Maaade in Georgiaaaaaaa",
                75 * 60 * 1000, 30 * 60 * 1000, 3, 150, 100,
                makeTreeSpriteArray("peach_tree", assetHandler),
                assetHandler.getTextureRegion("peach8x")));

        trees.add(new Tree(5, "Lemon Tree",
                "Harsh to eat, but makes the best lemonade",
                150 * 60 * 1000, 100 * 60 * 1000, 10, 100, 50,
                makeTreeSpriteArray(assetHandler.getTextureRegion("lemon_tree8x"), assetHandler),
                assetHandler.getTextureRegion("lemon8x")));

        trees.add(new Tree(6, "Sour Green Apple Tree",
                "An upgrade from the original sweet apple, some would say",
                4 * 60 * 60 * 1000, 240 * 60 * 1000, 6, 250, 200,
                makeTreeSpriteArray(assetHandler.getTextureRegion("green_apple_tree8x"), assetHandler),
                assetHandler.getTextureRegion("green_apple8x")));

        trees.add(new Tree(7, "Lychee Tree",
                "Originally from China. The insides are sweet, and they like to grow in bunches.",
                12 * 60 * 60 * 1000, 6 * 60 * 60 * 1000, 12, 200, 150,
                makeTreeSpriteArray(assetHandler.getTextureRegion("lychee_tree8x"), assetHandler),
                assetHandler.getTextureRegion("lychee8x")));

        for (Tree tree : trees) {
            itemMap.put(tree.getId(), tree);
        }

        fertilizers = new ArrayList<>();

        fertilizers.add(new Fertilizer(32, "Fruitful Endeavours",
                "Strengthens the roots, providing more nutrients to the fruit. Reduces produce time by 20%",
                assetHandler.getTextureRegion("fertilizer8x"),
                (Plot plot) -> plot.setProduceTimeMultiplier(0.80f)));

        fertilizers.add(new Fertilizer(33, "Speedy Seeds",
                "Filled with electrolytes to make plants grow faster. Reduces grow time by 20%",
                assetHandler.getTextureRegion("fertilizer8x"),
                (Plot plot) -> plot.setMatureTimeMultiplier(0.80f)));

        fertilizers.add(new Fertilizer(34, "Fruit by the Mile",
                "Contains a small amount of mutagens genetically designed to increase yield. +1 to harvest amount.",
                assetHandler.getTextureRegion("fertilizer8x"),
                (Plot plot) -> plot.setProduceAmountExtra(1)));

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

        fertilizers.add(new Fertilizer(38, "Miracle Grow",
                "Makes a tree grow instantly",
                assetHandler.getTextureRegion("fertilizer8x"),
                (plot) -> plot.setMatureTimeMultiplier(0f)));

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

    private static TextureRegion[] makeTreeSpriteArray(String prefix, AssetHandler assetHandler) {
        return new TextureRegion[] {
                assetHandler.getTextureRegion(prefix + "_baby"),
                assetHandler.getTextureRegion(prefix + "_teen"),
                assetHandler.getTextureRegion(prefix),
                assetHandler.getTextureRegion(prefix + "_harvest")
        };
    }

    private static TextureRegion[] makeTreeSpriteArray(TextureRegion tree, AssetHandler assetHandler) {
        return new TextureRegion[] {
                assetHandler.getTextureRegion("plant8x"),
                assetHandler.getTextureRegion("plant8x"),
                assetHandler.getTextureRegion("tree8x"),
                tree
        };
    }
}
