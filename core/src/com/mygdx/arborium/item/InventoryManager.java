package com.mygdx.arborium.item;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

import java.util.HashMap;
import java.util.Set;

public class InventoryManager {
    private static HashMap<Item, Integer> inventory;
    private static Preferences preferences;

    public static void init() {
        inventory = new HashMap<>();
        preferences = Gdx.app.getPreferences("Arborium_Inventory");
        load();
    }

    public static void load() {
        Set<String> itemKeys = preferences.get().keySet();
        for (String key : itemKeys) {
            int parsed = Integer.parseInt(key);
            Item item = ItemManager.findItemById(parsed);
            inventory.put(item, preferences.getInteger(key));
        }
    }

    public static void addItem(Item item) {
        if (inventory.containsKey(item)) {
            int count = inventory.get(item);
            inventory.put(item, ++count);
        }
        else
            inventory.put(item, 1);

        save(item);
    }

    public static void takeItem(Item item) {
        int count = inventory.get(item);
        if (count == 1) {
            inventory.remove(item);
            preferences.remove(String.valueOf(item.getId()));
            preferences.flush();
        }
        else {
            inventory.put(item, --count);
            save(item);
        }

    }

    public static int getItemAmount(Item item) {
        return inventory.get(item);
    }

    public static void save(Item item) {
        preferences.putInteger(String.valueOf(item.getId()), inventory.get(item));
        preferences.flush();
    }

    public static void saveAll() {
        Set<Item> keySet = inventory.keySet();
        for (Item item : keySet) {
            save(item);
        }
    }

    public static Item[] getItems() {
        Item[] items = new Item[inventory.size()];
        return inventory.keySet().toArray(items);
    }
}
