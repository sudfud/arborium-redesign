package com.mygdx.arborium.screen.upgrade;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.mygdx.arborium.game.Arborium;
import com.mygdx.arborium.manager.UpgradeManager;
import com.mygdx.arborium.item.Upgrade;
import com.mygdx.arborium.screen.GameScreen;
import com.mygdx.arborium.ui.CurrencyLabel;
import com.mygdx.arborium.ui.ItemListWindow;

public class UpgradeScreen extends GameScreen {

    private int checkedButtonIndex = 0;

    private ItemListWindow itemListWindow;
    private Window infoWindow;
    private Label descriptionLabel;
    private TextButton buyButton;
    private CurrencyLabel priceLabel;

    private Texture testBg;

    float sX = 0;

    public UpgradeScreen(Arborium game) {
        super(game);

        Skin skin = game.getAssetHandler().getSkin();

        itemListWindow = new ItemListWindow(skin, UpgradeManager.getUpgradeArray());
        infoWindow = new Window("", skin);
        checkedButtonIndex = itemListWindow.getCheckedButtonIndex();

        descriptionLabel = new Label("This is a test description", skin);
        priceLabel = new CurrencyLabel(game, skin);
        priceLabel.setPrice(500);
        buyButton = new TextButton("Buy", skin);

        infoWindow.add(descriptionLabel);
        infoWindow.row();
        infoWindow.add(priceLabel).pad(25);
        infoWindow.row();
        infoWindow.add(buyButton).size(150, 50);

        UITable.add(itemListWindow).prefWidth(Gdx.graphics.getWidth()).prefHeight(Gdx.graphics.getHeight() * 3 / 4);
        UITable.row();
        UITable.add(infoWindow);

        testBg = new Texture("frame1.png");
        testBg.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
    }

    @Override
    public void render(float delta) {
        if (checkedButtonIndex != itemListWindow.getCheckedButtonIndex()) {
            checkedButtonIndex = itemListWindow.getCheckedButtonIndex();
            update();
        }
        sX+=0.1f;

        camera.zoom = 0.05f;

        super.render(delta);

        spriteBatch.begin();
        spriteBatch.draw(testBg,0, 0, 0.5f, 0.5f);
        spriteBatch.end();

        stage.act();
        stage.draw();
    }

    private void update() {
        Upgrade upgrade = (Upgrade) itemListWindow.getSelectedItem();
        descriptionLabel.setText(upgrade.getDescription());
        priceLabel.setPrice(upgrade.getPrice());
    }
}
