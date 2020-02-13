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
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Queue;
import com.mygdx.arborium.game.Arborium;
import com.mygdx.arborium.manager.CurrencyManager;
import com.mygdx.arborium.manager.ShopManager;
import com.mygdx.arborium.manager.UpgradeManager;
import com.mygdx.arborium.manager.InventoryManager;
import com.mygdx.arborium.item.Item;
import com.mygdx.arborium.manager.ItemManager;
import com.mygdx.arborium.item.Upgrade;
import com.mygdx.arborium.screen.GameScreen;
import com.mygdx.arborium.ui.ItemListWindow;
import com.mygdx.arborium.ui.CurrencyLabel;
import com.mygdx.arborium.ui.TreeInfoWindow;

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
                    Upgrade upgrade = (Upgrade) currentItem;
                    if (!upgrade.locked() && !upgrade.enabled() && CurrencyManager.subtract(upgrade.getPrice())) {
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

        // DESKTOP UI

//        UITable.add(priceLabel).colspan(2);
//        UITable.row();
//        UITable.add(categoryHorizGroup).expandX().colspan(2);
//        UITable.row();
//        UITable.add(itemWindow).prefWidth(Value.percentWidth(0.4f, UITable))
//                .height(Value.percentHeight(0.75f, UITable));
//        // UITable.row();
//        UITable.add(treeShop).prefWidth(Value.percentWidth(0.4f, UITable))
//                .height(Value.percentHeight(0.75f, UITable));
//        UITable.row();
//        UITable.add(buyButton).pad(15).size(150, 75).right();
//        UITable.add(backButton).size(150, 75).left();

        // MOBILE UI

        UITable.add(priceLabel).colspan(2);
        UITable.row();
        UITable.add(categoryHorizGroup).expandX().colspan(2);
        UITable.row();
        UITable.add(itemWindow).prefWidth(Value.percentWidth(0.75f, UITable))
                .height(Value.percentHeight(0.45f, UITable)).colspan(2);
        UITable.row();
        UITable.add(treeShop).prefWidth(Value.percentWidth(0.75f, UITable))
                .height(Value.percentHeight(0.35f, UITable)).colspan(2);
        UITable.row();
        UITable.add(buyButton).pad(15).size(150, 75).right();
        UITable.add(backButton).size(150, 75).left();

        testBg = new Texture("frame1.png");
        testBg.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        bgRegion = new TextureRegion(testBg, 1024, 1024);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        spriteBatch.begin();
        spriteBatch.draw(bgRegion, 0, 0, 4, 4);
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
        if (currentItem instanceof Upgrade) {
            Upgrade upgrade = (Upgrade)currentItem;
            if (!upgrade.enabled())
                buyButton.setVisible(!upgrade.locked());
            else
                buyButton.setVisible(false);
        }
        else
            buyButton.setVisible(!ShopManager.isItemLocked(currentItem));
    }

    private TextButton makeCategoryButton(String name, Item[] catItems) {
        TextButton button = new TextButton(name, skin, "toggle");
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                itemWindow.setItems(catItems);
                treeShop.setItem(catItems[0]);
                currentItem = catItems[0];
                updateItem();
            }
        });

        categoryButtonGroup.add(button);
        categoryHorizGroup.addActor(button);

        return button;
    }
}
