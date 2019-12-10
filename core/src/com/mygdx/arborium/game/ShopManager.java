package com.mygdx.arborium.game;

import com.mygdx.arborium.item.Item;
import com.mygdx.arborium.item.ItemManager;

import java.util.ArrayList;

public class ShopManager {
    private static ShopEntry[] shopItems;

    public static void init() {

        ShopEntry[] trees = {
                // Apple tree
                new ShopEntry(ItemManager.findItemById(1), 50),

                // Orange tree
                new ShopEntry(ItemManager.findItemById(2), 225, 5),

                // Cherry tree
                new ShopEntry(ItemManager.findItemById(3), 1200, 15),

                // Peach tree
                new ShopEntry(ItemManager.findItemById(4), 7500, 25),

                // Lemon tree
                new ShopEntry(ItemManager.findItemById(5), 30000, 35),

                // Green Apple tree
                new ShopEntry(ItemManager.findItemById(6), 350000, 45),

                // Lychee tree
                new ShopEntry(ItemManager.findItemById(7), 150000, 55),

                // Produce amount fertilizer
                new ShopEntry(ItemManager.findItemById(32),150, 10),

                // Grow rate fertilizer
                new ShopEntry(ItemManager.findItemById(33), 750, 20),

                // Produce rate fertilizer
                new ShopEntry(ItemManager.findItemById(34), 2000, 30)
                };

        shopItems = trees;
    }

    public static int getItemPrice(Item item) {
        for (ShopEntry entry : shopItems) {
            if (item == entry.getItem()) {
                return entry.getPrice();
            }
        }
        return -1;
    }

    public static boolean isItemLocked(Item item) {
        for (ShopEntry entry : shopItems) {
            if (item == entry.getItem()) {
                return ExperienceManager.getLevel() < entry.getUnlockLevel();
            }
        }
        return false;
    }

    public static int getUnlockLevel(Item item) {
        for (ShopEntry entry : shopItems) {
            if (item == entry.getItem()) {
                return entry.getUnlockLevel();
            }
        }
        return -1;
    }
}
