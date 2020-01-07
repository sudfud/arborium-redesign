package com.mygdx.arborium.item;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

import java.util.function.Function;
import java.util.function.IntConsumer;
import java.util.function.Supplier;

public class Upgrade extends Item{
    private Preferences preferences;

    private boolean locked;
    private boolean enabled;

    private IntConsumer effectFunction;

    private int price;

    public Upgrade(int id, String name, String description, IntConsumer effectFunction, Supplier<Boolean> unlockCondition, int price) {
        super(id, name, description, null);
        preferences = Gdx.app.getPreferences("Upgrades");
        this.enabled = false;
        this.effectFunction = effectFunction;
        //apply();
        this.price = price;
    }

    public void apply() {
        effectFunction.accept(getId());
    }
}
