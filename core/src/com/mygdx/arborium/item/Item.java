package com.mygdx.arborium.item;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Item {
    private final int id;
    private final String name;
    private final TextureRegion texture;

    public Item(int id, String name, TextureRegion texture) {
        this.id = id;
        this.name = name;
        this.texture = texture;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public TextureRegion getTexture() {
        return texture;
    }
}
