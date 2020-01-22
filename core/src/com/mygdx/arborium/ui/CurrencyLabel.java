package com.mygdx.arborium.ui;

import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.mygdx.arborium.game.Arborium;

public class CurrencyLabel extends HorizontalGroup {
    private Image coinImage;
    private Label currencyLabel;

    public CurrencyLabel(Arborium game, Skin skin) {
        coinImage = new Image(new TextureRegionDrawable(game.getAssetHandler().getTextureRegion("coin4x")));
        currencyLabel = new Label("", skin);

        this.addActor(currencyLabel);
        this.addActor(coinImage);
    }

    public void setPrice(int price) {
        currencyLabel.setText("" + price);
    }
}
