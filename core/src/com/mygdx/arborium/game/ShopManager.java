package com.mygdx.arborium.game;

import com.mygdx.arborium.item.Item;
import com.mygdx.arborium.item.ItemManager;

import java.util.ArrayList;

public class ShopManager {
    private static ShopEntry[] shopItems;

    public static void init() {

        ShopEntry[] trees = {
                // Apple tree
                new ShopEntry(ItemManager.findItemById(1), 15),

                // Orange tree
                new ShopEntry(ItemManager.findItemById(2), 50, 5),

                // Cherry tree
                new ShopEntry(ItemManager.findItemById(3), 250, 15),

                // Peach tree
                new ShopEntry(ItemManager.findItemById(4), 1200, 30)
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
}
