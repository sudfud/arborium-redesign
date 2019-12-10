package com.mygdx.arborium.item;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Item {
    private final int id;
    private final String name;
    private final String description;
    private final TextureRegion texture;

    public Item(int id, String name, String description, TextureRegion texture) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.texture = texture;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public TextureRegion getTexture() {
        return texture;
    }
}
