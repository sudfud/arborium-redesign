package com.mygdx.arborium.game;

import com.mygdx.arborium.item.Item;

public class ShopEntry {

    private int price;

    private boolean locked;
    private int unlockLevel;

    private Item item;

    public ShopEntry(Item item, int price) {
        this.item = item;
        this.price = price;
        locked = false;
        unlockLevel = 1;
    }

    public ShopEntry(Item item, int price, int unlockLevel) {
        this(item, price);
        this.unlockLevel = unlockLevel;
        locked = true;
    }

    public Item getItem() {
        return item;
    }

    public int getPrice() {
        return price;
    }

    public boolean isLocked() {
        return locked;
    }

    public int getUnlockLevel() {
        return unlockLevel;
    }

    public void unlock() {
        locked = false;
    }
}
