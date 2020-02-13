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
        Tree peachTree = (Tree)ItemManager.findItemById(4);
        Tree lemonTree = (Tree)ItemManager.findItemById(5);
        Tree greenAppleTree = (Tree)ItemManager.findItemById(6);
        Tree lycheeTree = (Tree)ItemManager.findItemById(7);

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
                }, 75));

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
                "Oranges take 25% less time to grow",
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


        upgrades.add(new Upgrade(67, "Orange Upgrade 2",
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
                }, 2500));

        upgrades.add(new Upgrade(68, "Cherry Upgrade 1",
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
                }, 3250));

        upgrades.add(new Upgrade(69, "Cherry Upgrade 2",
                "Cherry trees produce +4 cherries",
                "Harvest 500 cherries to unlock!",
                assetHandler.getTextureRegion("cherry2x"),
                (id) -> {
                    int prodAmount = cherryTree.getProduceAmount();
                    cherryTree.setProduceAmount(prodAmount + 4);
                },
                () -> {
                    int count = StatsManager.getFruitCollected(cherryTree);
                    return (count >= 500);
                }, 6000));

        upgrades.add(new Upgrade(70, "Peach Upgrade 1",
                "Peach trees take 35% less time to grow",
                "Harvest 500 peaches to unlock!",
                assetHandler.getTextureRegion("peach8x"),
                (id) -> {
                    long growTime = peachTree.getGrowTime();
                    peachTree.setGrowTime((long)(growTime * 0.65f));
                },
                () -> {
                    int count = StatsManager.getFruitCollected(peachTree);
                    return (count >= 100);
                }, 7250));

        upgrades.add(new Upgrade(71, "Peach Upgrade 2",
                "Peach trees cost half as much",
                "Harvest 100 peaches to unlock!",
                assetHandler.getTextureRegion("peach8x"),
                (id) -> {
                    int price = ShopManager.getItemPrice(peachTree);
                    ShopManager.setItemPrice(peachTree, (int)(price * 0.5f));
                },
                () -> {
                    int count = StatsManager.getFruitCollected(peachTree);
                    return (count >= 500);
                }, 9000));

        upgrades.add(new Upgrade(72, "Lemon Upgrade 1",
                "+6 lemons per harvest",
                "Harvest 100 lemons to unlock!",
                assetHandler.getTextureRegion("lemon8x"),
                (id) -> {
                    int prodCount = lemonTree.getProduceAmount();
                    lemonTree.setProduceAmount(prodCount + 6);
                },
                () -> {
                    int count = StatsManager.getFruitCollected(lemonTree);
                    return (count >= 100);
                }, 15000));

        upgrades.add(new Upgrade(73, "Lemon Upgrade 2",
                "Lemons take 40% less time to grow",
                "Harvest 500 lemons to unlock",
                assetHandler.getTextureRegion("lemon8x"),
                (id) -> {
                    long harvestTime = lemonTree.getProduceTime();
                    lemonTree.setProduceTime((long)(harvestTime * 0.60f));
                },
                () -> {
                    int count = StatsManager.getFruitCollected(lemonTree);
                    return (count >= 500);
                }, 17500));

        upgrades.add(new Upgrade(74, "Green Apple Upgrade 1",
                "Green apples are worth twice as much",
                "Harvest 100 green apples to unlock!",
                assetHandler.getTextureRegion("green_apple8x"),
                (id) -> {
                    int prodValue = greenAppleTree.getProduceValue();
                    greenAppleTree.setProduceValue((prodValue * 2));
                },
                () -> {
                    int count = StatsManager.getFruitCollected(greenAppleTree);
                    return (count >= 100);
                }, 52000));

        upgrades.add(new Upgrade(75, "Green Apple Upgrade 2",
                "+4 apples per harvest",
                "Harvest 500 green apples to unlock!",
                assetHandler.getTextureRegion("green_apple8x"),
                (id) -> {
                    int prodAmount = greenAppleTree.getProduceAmount();
                    greenAppleTree.setProduceAmount(prodAmount + 4);
                },
                () -> {
                    int count = StatsManager.getFruitCollected(greenAppleTree);
                    return (count >= 500);
                }, 64000));

        upgrades.add(new Upgrade(76, "Lychee Upgrade 1",
                "Lychee take 65% less time to grow",
                "Harvest 100 lychee to unlock!",
                assetHandler.getTextureRegion("lychee8x"),
                (id) -> {
                    long growTime = lycheeTree.getGrowTime();
                    lycheeTree.setGrowTime((long)(growTime * 0.35f));
                },
                () -> {
                    int count = StatsManager.getFruitCollected(lycheeTree);
                    return (count >= 100);
                }, 230000));

        upgrades.add(new Upgrade(77, "Lychee Upgrade 2",
                "Lychee give 75% extra exp",
                "Harvest 500 lychee to unlock!",
                assetHandler.getTextureRegion("lychee8x"),
                (id) -> {
                    int expAmt = lycheeTree.getExperience();
                    lycheeTree.setExperience((int)(expAmt * 1.75f));
                },
                () -> {
                    int count = StatsManager.getFruitCollected(lycheeTree);
                    return (count >= 500);
                }, 275000));

        upgrades.add(new Upgrade(78, "Automated Sprinklers",
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


        upgrades.add(new Upgrade(79, "Definitely Safe Pesticides",
                "Reduce harvest time of all trees by 10%",
                "Plant 40 trees to unlock!",
                assetHandler.getTextureRegion("produce_time_icon8x"),
                (id) -> {
                    for (Tree tree : ItemManager.getTreeList()) {
                        long prodTime = tree.getProduceTime();
                        tree.setProduceTime((long)(prodTime * 0.9f));
                    }
                },
                () -> (StatsManager.getTreesPlanted() >= 40), 15000));
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
