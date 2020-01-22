package com.mygdx.arborium.item;

import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Tree extends Item {

    private long growTime;      // Time it takes for the tree to mature (ms)
    private long produceTime;   // Time for a mature tree to produce fruit (ms)
    private int produceAmount;  // Amount of fruit produced per harvest
    private int produceValue;   // Value of the fruit this tree produces
    private int experience;     // Experience gained per harvest

    private TextureRegion matureTreeTexture;
    private TextureRegion harvestTreeTexture;
    private TextureRegion fruitTexture;

    private Preferences preferences;

    public Tree(int id, String name, String description, long growTime, long prodTime,
                int prodAmt, int prodValue, int exp,
                TextureRegion matureTreeTexture,
                TextureRegion harvestTreeTexture,
                TextureRegion fruitTexture) {

        super(id, name, description, harvestTreeTexture);

        this.growTime = growTime;
        this.produceTime = prodTime;
        this.produceAmount = prodAmt;
        this.produceValue = prodValue;
        this.experience = exp;

        this.matureTreeTexture = matureTreeTexture;
        this.harvestTreeTexture = harvestTreeTexture;
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

    public TextureRegion getMatureTreeTexture() {
        return matureTreeTexture;
    }

    public TextureRegion getHarvestTreeTexture() {
        return harvestTreeTexture;
    }

    @Override
    public String toString() {
        return getName();
    }
}
