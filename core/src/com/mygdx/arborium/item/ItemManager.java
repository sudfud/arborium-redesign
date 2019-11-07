package com.mygdx.arborium.item;

import com.mygdx.arborium.screen.AssetHandler;

import java.util.ArrayList;
import java.util.HashMap;

public class ItemManager {
    private static ArrayList<Tree> trees;
    private static HashMap<Integer, Item> itemMap;

    public static void init(AssetHandler assetHandler) {
        trees = new ArrayList<>();
        itemMap = new HashMap<>();
        trees.add(new Tree(1, "Apple",
                7 * 60 * 1000, 2 * 60 * 1000, 4, 1, 100,
                assetHandler.getTexureRegion("apple_tree2x"),
                assetHandler.getTexureRegion("apple2x")));

        trees.add(new Tree(2, "Orange",
                25 * 60 * 1000, 10 * 60 * 1000, 2, 15, 500,
                assetHandler.getTexureRegion("orange_tree2x"),
                assetHandler.getTexureRegion("orange2x")));

        trees.add(new Tree(3, "Cherry",
                10 * 1000, 10 * 1000, 8, 25, 75,
                assetHandler.getTexureRegion("cherry_tree2x"),
                assetHandler.getTexureRegion("cherry2x")));

        trees.add(new Tree(4, "Peach",
                10 * 1000, 10 * 1000, 3, 150, 275,
                assetHandler.getTexureRegion("peach_tree8x"),
                assetHandler.getTexureRegion("peach8x")));

        for (Tree tree : trees) {
            itemMap.put(tree.getId(), tree);
        }
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
}
