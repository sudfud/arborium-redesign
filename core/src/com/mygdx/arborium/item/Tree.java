package com.mygdx.arborium.item;

import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Tree extends Item {

    private long growTime;      // Time it takes for the tree to mature (ms)
    private long produceTime;   // Time for a mature tree to produce fruit (ms)
    private int produceAmount;  // Amount of fruit produced per harvest
    private int produceValue;   // Value of the fruit this tree produces
    private int experience;     // Experience gained per harvest

    private TextureRegion[] sprites;

    private TextureRegion matureTreeTexture;
    private TextureRegion harvestTreeTexture;
    private TextureRegion fruitTexture;

    private Preferences preferences;

    public Tree(int id, String name, String description, long growTime, long prodTime,
                int prodAmt, int prodValue, int exp,
                TextureRegion[] sprites, TextureRegion fruitTexture) {

        super(id, name, description, sprites[sprites.length - 1]);

        this.growTime = growTime;
        this.produceTime = prodTime;
        this.produceAmount = prodAmt;
        this.produceValue = prodValue;
        this.experience = exp;

        this.matureTreeTexture = sprites[sprites.length - 2];
        this.harvestTreeTexture = sprites[sprites.length - 1];
        this.fruitTexture = fruitTexture;

        this.sprites = sprites;
    }
    // Getters and setters

    public TextureRegion getSpriteFrame(long time) {
        float ratio = (float)time / (float)growTime;
        if (ratio >= 1)
            return matureTreeTexture;
        else if (ratio <= 1 / 3f)
            return sprites[0];
        else
            return sprites[1];
    }

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
