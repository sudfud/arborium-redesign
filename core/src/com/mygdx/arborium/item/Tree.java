package com.mygdx.arborium.item;

import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Tree extends Item {

    private long growTime;      // Time it takes for the tree to mature (ms)
    private long produceTime;   // Time for a mature tree to produce fruit (ms)
    private int produceAmount;  // Amount of fruit produced per harvest
    private int produceValue;   // Value of the fruit this tree produces
    private int experience;     // Experience gained per harvest

    private TextureRegion treeTexture;
    private TextureRegion fruitTexture;

    private Preferences preferences;

    public Tree(int id, String name, String description, long growTime, long prodTime,
                int prodAmt, int prodValue, int exp, TextureRegion treeTexture,
                TextureRegion fruitTexture) {

        super(id, name, description, treeTexture);

        this.growTime = growTime;
        this.produceTime = prodTime;
        this.produceAmount = prodAmt;
        this.produceValue = prodValue;
        this.experience = exp;

        this.treeTexture = treeTexture;
        this.fruitTexture = fruitTexture;
    }
    // Getters and setters

    public void setGrowTime(long growTime) {
        this.growTime = growTime;
    }

    public long getGrowTime() {
        return growTime;
    }


    public void setProduceTime(long produceTime) {
        this.produceTime = produceTime;
    }

    public long getProduceTime() {
        return produceTime;
    }


    public void setProduceAmount(int produceAmount) {
        this.produceAmount = produceAmount;
    }

    public int getProduceAmount() {
        return produceAmount;
    }


    public void setProduceValue(int produceValue) {
        this.produceValue = produceValue;
    }

    public int getProduceValue() {
        return produceValue;
    }


    public void setExperience(int experience) {
        this.experience = experience;
    }

    public int getExperience() {
        return experience;
    }

    public TextureRegion getFruitTexture() {
        return fruitTexture;
    }

    @Override
    public String toString() {
        return getName();
    }
}
