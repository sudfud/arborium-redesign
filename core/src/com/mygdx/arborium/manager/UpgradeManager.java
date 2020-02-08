package com.mygdx.arborium.manager;

import com.mygdx.arborium.manager.ItemManager;
import com.mygdx.arborium.item.Tree;
import com.mygdx.arborium.item.Upgrade;
import com.mygdx.arborium.screen.AssetHandler;

import java.util.ArrayList;

public class UpgradeManager {
    private static ArrayList<Upgrade> upgrades;

    public static void initialize(AssetHandler assetHandler) {
        upgrades = new ArrayList<>();

        Tree appleTree = (Tree)ItemManager.findItemById(1);
        Tree orangeTree = (Tree)ItemManager.findItemById(2);
        Tree cherryTree = (Tree)ItemManager.findItemById(3);

        upgrades.add(new Upgrade(64, "Apple Upgrade 1",
                "Apples are worth twice as much",
                "Harvest 100 apples to unlock!",
                assetHandler.getTextureRegion("apple2x"),
                (id) -> {
                    int prodValue = appleTree.getProduceValue();
                    appleTree.setProduceValue(prodValue * 2);
                },
                () -> {
                    int count = StatsManager.getFruitCollected(appleTree);
                    return (count >= 100);
                }, 100));

        upgrades.add(new Upgrade(65, "Apple Upgrade 2",
                "Apple trees produce 2 extra apples",
                "Harvest 500 apples to unlock!",
                assetHandler.getTextureRegion("apple2x"),
                (id) -> {
                    int prodAmt = appleTree.getProduceAmount();
                    appleTree.setProduceAmount(prodAmt + 2);
                },

                () -> {
                    int count = StatsManager.getFruitCollected(appleTree);
                    return (count >= 500);
                }, 350));

        upgrades.add(new Upgrade(66, "Orange Upgrade 1",
                "Oranges grow 25% faster",
                "Harvest 100 oranges to unlock!",
                assetHandler.getTextureRegion("orange2x"),
                (id) -> {
                    long prodTime = orangeTree.getProduceTime();
                    orangeTree.setProduceTime((long)(prodTime * 0.75f));
                },
                () -> {
                    int count = StatsManager.getFruitCollected(orangeTree);
                    return (count >= 100);
                }, 675));


        upgrades.add(new Upgrade(67, "Cherry Upgrade 1",
                "Cherries give 50% extra exp",
                "Harvest 100 cherries to unlock!",
                assetHandler.getTextureRegion("cherry2x"),
                (id) -> {
                    int expValue = cherryTree.getExperience();
                    cherryTree.setExperience((int)(expValue * 1.5f));
                },
                () -> {
                    int count = StatsManager.getFruitCollected(cherryTree);
                    return (count >= 100);
                }, 2500));

        upgrades.add(new Upgrade(68, "Orange Upgrade 2",
                "Oranges are worth twice as much",
                "Harvest 500 oranges to unlock!",
                assetHandler.getTextureRegion("orange2x"),
                (id) -> {
                    int prodValue = orangeTree.getProduceValue();
                    orangeTree.setProduceValue(prodValue * 2);
                },
                () -> {
                    int count = StatsManager.getFruitCollected(orangeTree);
                    return (count >= 500);
                }, 3250));

        upgrades.add(new Upgrade(69, "Automated Sprinklers",
                "Reduce grow time of all trees by 5%",
                "Plant 15 trees to unlock!",
                assetHandler.getTextureRegion("grow_time_icon8x"),
                (id) -> {
                    for (Tree tree : ItemManager.getTreeList()) {
                        long growTime = tree.getGrowTime();
                        tree.setGrowTime((long)(growTime * 0.95f));
                    }
                },
                () ->  (StatsManager.getTreesPlanted() >= 15), 3000));
    }

    public static Upgrade get(int index) {
        if (index < upgrades.size()) {
            return upgrades.get(index);
        }
        else
            return null;
    }

    public static Upgrade[] getUpgradeArray() {
        Upgrade[] upArray = new Upgrade[upgrades.size()];
        return upgrades.toArray(upArray);
    }
}
