package com.mygdx.arborium.manager;

import com.mygdx.arborium.game.ShopEntry;
import com.mygdx.arborium.item.Item;
import com.mygdx.arborium.manager.ItemManager;

import java.util.ArrayList;

public class ShopManager {
    private static ArrayList<ShopEntry> shopItems;

    public static void init() {

        shopItems = new ArrayList<>();
        // Apple tree
        shopItems.add(new ShopEntry(ItemManager.findItemById(1), 50));

        // Orange tree
        shopItems.add(new ShopEntry(ItemManager.findItemById(2), 175, 5));

        // Cherry tree
        shopItems.add(new ShopEntry(ItemManager.findItemById(3), 1000, 15));

        // Peach tree
        shopItems.add(new ShopEntry(ItemManager.findItemById(4), 5500, 30));

        // Lemon tree
        shopItems.add(new ShopEntry(ItemManager.findItemById(5), 15000, 50));

        // Green Apple tree
        shopItems.add(new ShopEntry(ItemManager.findItemById(6), 50000, 75));

        // Lychee tree
        shopItems.add(new ShopEntry(ItemManager.findItemById(7), 200000, 110));

        // Produce amount fertilizer
        shopItems.add(new ShopEntry(ItemManager.findItemById(32), 100, 2));

        // Grow rate fertilizer
        shopItems.add(new ShopEntry(ItemManager.findItemById(33), 350, 8));

        // Produce rate fertilizer
        shopItems.add(new ShopEntry(ItemManager.findItemById(34), 1350, 16));

        shopItems.add(new ShopEntry(ItemManager.findItemById(35), 7500, 32));

        shopItems.add(new ShopEntry(ItemManager.findItemById(36), 37500, 56));

        shopItems.add(new ShopEntry(ItemManager.findItemById(37), 100000, 100));

        shopItems.add(new ShopEntry(ItemManager.findItemById(38), 100000000, 200));
    }

    public static int getItemPrice(Item item) {
        for (ShopEntry entry : shopItems) {
            if (item == entry.getItem()) {
                return entry.getPrice();
            }
        }
        return -1;
    }

    public static void setItemPrice(Item item, int price) {
        for (ShopEntry entry : shopItems) {
            if (item == entry.getItem()) {
                entry.setPrice(price);
                return;
            }
        }
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
