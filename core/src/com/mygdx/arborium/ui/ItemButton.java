package com.mygdx.arborium.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.mygdx.arborium.manager.ShopManager;
import com.mygdx.arborium.item.Fertilizer;
import com.mygdx.arborium.item.Item;
import com.mygdx.arborium.item.Tree;
import com.mygdx.arborium.item.Upgrade;

public class ItemButton extends ImageButton {

    Item item;

    public ItemButton(Skin skin, Item item) {
        super(skin, "upgradebutton");
        TextureRegionDrawable textureDrawable = new TextureRegionDrawable(item.getTexture());
        ImageButtonStyle style = new ImageButtonStyle(getStyle());
        style.imageUp = textureDrawable;
        setStyle(style);
        this.item = item;

        if (item instanceof Tree || item instanceof Fertilizer) {
            if (ShopManager.isItemLocked(item)) {
                getImage().setColor(Color.BLACK);
            }
        }

        else if (item instanceof Upgrade) {
            Upgrade upgrade = (Upgrade) item;

            upgrade.update();
            if (upgrade.locked())
                getImage().setColor(Color.BLACK);
        }

        //update();
    }

    public Item getItem() {
        return item;
    }
}
