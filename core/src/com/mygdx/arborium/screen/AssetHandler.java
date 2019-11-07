package com.mygdx.arborium.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.mygdx.arborium.item.ItemManager;
import com.mygdx.arborium.item.Tree;

import java.util.HashMap;

public class AssetHandler {
    public static final String ATLAS_PATH = "arborium.atlas";
    public static final String EXP_BAR_PATH = "skin/arborium-exp.atlas";
    public static final String SKIN_PATH = "skin/arborium_ui.json";
    public static final String FONT_PATH = "skin/8bt.fnt";

    private static HashMap<Tree, TextureRegion> treeSprites;

    private AssetManager assetManager;

    public AssetHandler() {
        assetManager = new AssetManager();
    }

    // Call this after creating an instance of this class!
    public static void init() {
    }

    // Load assets for the loading screen
    public void preload() {

    }

    public void load() {
        assetManager.load(ATLAS_PATH, TextureAtlas.class);
        assetManager.load(EXP_BAR_PATH, TextureAtlas.class);
        assetManager.load(SKIN_PATH, Skin.class);
        assetManager.load(FONT_PATH, BitmapFont.class);
        assetManager.finishLoading();
    }

    public TextureRegion getTexureRegion(String name) {
        return assetManager.get(ATLAS_PATH, TextureAtlas.class).findRegion(name);
    }

    public TextureAtlas getExpAtlas() {
        return assetManager.get(EXP_BAR_PATH, TextureAtlas.class);
    }

    public Skin getSkin() {
        return assetManager.get(SKIN_PATH, Skin.class);
    }

    public void dispose() {
        assetManager.dispose();
    }
}
