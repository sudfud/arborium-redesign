package com.mygdx.arborium.screen.shop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Queue;
import com.mygdx.arborium.game.Arborium;
import com.mygdx.arborium.game.CurrencyManager;
import com.mygdx.arborium.game.ShopManager;
import com.mygdx.arborium.game.UpgradeManager;
import com.mygdx.arborium.item.InventoryManager;
import com.mygdx.arborium.item.Item;
import com.mygdx.arborium.item.ItemManager;
import com.mygdx.arborium.item.Upgrade;
import com.mygdx.arborium.screen.GameScreen;
import com.mygdx.arborium.screen.upgrade.ItemListWindow;
import com.mygdx.arborium.ui.CurrencyLabel;

public class ShopScreen extends GameScreen {
    private Item currentItem;

    private Skin skin;

    private CurrencyLabel priceLabel;
    private TextButton buyButton;
    private TextButton backButton;

    private ButtonGroup categoryButtonGroup;
    private HorizontalGroup categoryHorizGroup;
    private TextButton treeCatButton;
    private TextButton fertilizerCatButton;
    private TextButton upgradeCatButton;

    private int itemSelectIndex;
    private ItemListWindow itemWindow;

    private TreeInfoWindow treeShop;

    private Texture testBg;
    private TextureRegion bgRegion;

    public ShopScreen(final Arborium game) {
        super(game);

        skin = game.getAssetHandler().getSkin();

        categoryButtonGroup = new ButtonGroup();
        categoryHorizGroup = new HorizontalGroup();
        categoryButtonGroup.setMinCheckCount(1);
        categoryButtonGroup.setMaxCheckCount(1);

        treeCatButton = makeCategoryButton("Trees", ItemManager.getTreeList());
        fertilizerCatButton = makeCategoryButton("Fertilizer", ItemManager.getFertilizerList());
        upgradeCatButton = makeCategoryButton("Upgrades", UpgradeManager.getUpgradeArray());

        itemWindow = new ItemListWindow(skin, ItemManager.getTreeList());
        itemSelectIndex = itemWindow.getCheckedButtonIndex();
        currentItem = itemWindow.getSelectedItem();

        treeShop = new TreeInfoWindow(game, skin);
        treeShop.setItem(currentItem);

        buyButton = new TextButton("Buy", skin);
        buyButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (currentItem instanceof Upgrade) {
                    if (CurrencyManager.subtract(((Upgrade) currentItem).getPrice())) {
                        ((Upgrade) currentItem).apply();
                    }
                }
                if (CurrencyManager.subtract(ShopManager.getItemPrice(currentItem)))
                    InventoryManager.addItem(currentItem);
            }
        });

        priceLabel = new CurrencyLabel(game, skin);

        backButton = new TextButton("Back", skin);
        backButton.addListener(new ClickListener() {
           @Override
           public void clicked(InputEvent event, float x, float y) {
               game.popScreen();
           }
        });

        UITable.add(priceLabel).colspan(3);
        UITable.row();
        UITable.add(categoryHorizGroup).expandX();
        UITable.row();
        UITable.add(itemWindow).prefWidth(Gdx.graphics.getWidth()).height(Gdx.graphics.getHeight() / 2.5f);
        UITable.row();
        UITable.add(treeShop).prefWidth(Gdx.graphics.getWidth()).height(Gdx.graphics.getHeight() / 3);
        UITable.row();
        UITable.add(buyButton).colspan(3).pad(15).size(150, 75).expandY();
        UITable.row();
        UITable.add(backButton).colspan(3).size(150, 75).expandY();

        testBg = new Texture("frame1.png");
        testBg.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        bgRegion = new TextureRegion(testBg, 1024, 1024);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        spriteBatch.begin();
        spriteBatch.draw(bgRegion, 0, 0, 2, 2);
        spriteBatch.end();

        if (itemSelectIndex != itemWindow.getCheckedButtonIndex())
        {
            itemSelectIndex = itemWindow.getCheckedButtonIndex();
            currentItem = itemWindow.getSelectedItem();
            updateItem();
        }

        stage.act(delta);
        priceLabel.setPrice(CurrencyManager.getAmount());
        stage.draw();
    }

    private void updateItem() {
        treeShop.setItem(currentItem);
    }

    private TextButton makeCategoryButton(String name, Item[] catItems) {
        TextButton button = new TextButton(name, skin, "toggle");
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                itemWindow.setItems(catItems);
                treeShop.setItem(catItems[0]);
            }
        });

        categoryButtonGroup.add(button);
        categoryHorizGroup.addActor(button);

        return button;
    }
}
