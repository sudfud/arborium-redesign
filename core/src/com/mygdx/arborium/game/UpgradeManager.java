package com.mygdx.arborium.game;

import com.mygdx.arborium.item.ItemManager;
import com.mygdx.arborium.item.Tree;
import com.mygdx.arborium.item.Upgrade;
import com.mygdx.arborium.screen.AssetHandler;

import java.util.ArrayList;

public class UpgradeManager {
    private static ArrayList<Upgrade> upgrades;

    public static void initialize(AssetHandler assetHandler) {
        upgrades = new ArrayList<>();

        Tree appleTree = (Tree)ItemManager.findItemById(1);

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
                }, 375));

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
                }, 1500));
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
