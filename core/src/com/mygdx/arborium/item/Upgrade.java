package com.mygdx.arborium.item;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.function.Function;
import java.util.function.IntConsumer;
import java.util.function.Supplier;

/*
 * An upgrade is a special type of item. Upgrades can be bought only once, have special unlock
 * conditions including number of fruit collected, and their effects are permanent.
 *
 * Unlike other items, upgrades do not have an associated ShopEntry, and instead their prices are
 * included in the constructor
 */

public class Upgrade extends Item{
    private Preferences preferences;

    private String unlockDescription;

    private boolean locked;
    private boolean enabled;

    private IntConsumer effectFunction;
    private Supplier<Boolean> unlockCondition;

    private int price;

    public Upgrade(int id, String name, String description, String unlockDescription, TextureRegion texture, IntConsumer effectFunction, Supplier<Boolean> unlockCondition, int price) {
        super(id, name, description, texture);
        this.unlockDescription = unlockDescription;
        this.unlockCondition = unlockCondition;
        preferences = Gdx.app.getPreferences("Upgrades");
        this.enabled = false;
        this.effectFunction = effectFunction;
        this.unlockCondition = unlockCondition;
        locked = !unlockCondition.get();
        //apply();
        this.price = price;

        load();
    }

    // Check to see if we need to unlock
    public void update() {
        if (locked && unlockCondition.get()) {
            locked = false;
            save();
        }
    }

    public void apply() {
        effectFunction.accept(-1);
        enabled = true;
        save();
    }

    public String getUnlockDescription() {
        return unlockDescription;
    }

    public boolean locked() {
        return locked;
    }

    public boolean enabled() {
        return enabled;
    }

    public int getPrice() {
        return price;
    }

    private void load() {
        enabled = preferences.getBoolean(getName() + "Enabled", false);
    }

    private void save() {
        preferences.putBoolean(getName() + "Enabled", enabled);
        preferences.flush();
    }
}
