package com.mygdx.arborium.item;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.arborium.game.Plot;

import java.util.function.Consumer;

public class Fertilizer extends Item {

    private Consumer<Plot> effectFunction;

    public Fertilizer(int id, String name, String description, TextureRegion texture, Consumer<Plot> effect) {
        super(id, name, description, texture);
        this.effectFunction = effect;
    }

    public void activate(Plot plot) {
        plot.resetMultipliers();
        effectFunction.accept(plot);
    }

    @Override
    public String toString() {
        return this.getName() + " x" + InventoryManager.getItemAmount(this);
    }
}
