package com.mygdx.arborium.screen.shop;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Queue;
import com.mygdx.arborium.game.Arborium;
import com.mygdx.arborium.game.CurrencyManager;
import com.mygdx.arborium.game.ShopManager;
import com.mygdx.arborium.item.InventoryManager;
import com.mygdx.arborium.item.Item;
import com.mygdx.arborium.item.ItemManager;
import com.mygdx.arborium.item.Tree;
import com.mygdx.arborium.screen.GameScreen;

public class ShopScreen extends GameScreen {

    private Queue<Item> itemQueue;
    private Item currentItem;

    private HorizontalGroup currencyGroup;
    private Label currencyLabel;
    private Window shopWindow;
    private Image itemImage;
    private TextureRegionDrawable itemDrawable;
    private TextButton buyButton;
    private HorizontalGroup priceGroup;
    private Image coinImage;
    private Label priceLabel;
    private Button itemSelectLeft;
    private Button itemSelectRight;
    private TextButton backButton;

    public ShopScreen(final Arborium game) {
        super(game);

        Skin skin = game.getAssetHandler().getSkin();

        itemQueue = new Queue<>();
        for (Tree tree : ItemManager.getTreeList()) {
            itemQueue.addLast(tree);
        }

        currentItem = (Tree)itemQueue.removeFirst();
        itemQueue.addLast(currentItem);

        shopWindow = new Window("", skin);
        itemImage = new Image();

        buyButton = new TextButton("Buy", skin);
        buyButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (CurrencyManager.subtract(ShopManager.getItemPrice(currentItem)))
                    InventoryManager.addItem(currentItem);
            }
        });

        itemDrawable = new TextureRegionDrawable(currentItem.getTexture());
        itemImage.setDrawable(itemDrawable);

        priceGroup = new HorizontalGroup();
        coinImage = new Image(game.getAssetHandler().getTexureRegion("coin4x"));
        priceLabel = new Label("" + ShopManager.getItemPrice(currentItem), skin);
        currencyLabel = new Label("" + CurrencyManager.getAmount(), skin);
        currencyGroup = new HorizontalGroup();

        currencyGroup.addActor(coinImage);
        currencyGroup.addActor(currencyLabel);

        priceGroup.addActor(coinImage);
        priceGroup.addActor(priceLabel);

        itemSelectLeft = new Button(skin, "left");
        itemSelectLeft.addListener(new ClickListener() {
           @Override
           public void clicked(InputEvent event, float x, float y) {
               currentItem = itemQueue.removeLast();
               itemQueue.addFirst(currentItem);
               updateItem();
           }
        });

        itemSelectRight = new Button(skin, "right");
        itemSelectRight.addListener(new ClickListener() {
           @Override
           public void clicked(InputEvent event, float x, float y) {
               currentItem = itemQueue.removeFirst();
               itemQueue.addLast(currentItem);
               updateItem();
           }
        });

        backButton = new TextButton("Back", skin);
        backButton.addListener(new ClickListener() {
           @Override
           public void clicked(InputEvent event, float x, float y) {
               game.toFarmScreen();
           }
        });

        shopWindow.add(itemImage);
        shopWindow.row();
        shopWindow.add(priceGroup);
        shopWindow.row();
        shopWindow.add(buyButton);
        shopWindow.pack();

        UITable.add(currencyGroup).colspan(3);
        UITable.row();

        UITable.add(itemSelectLeft);
        UITable.add(shopWindow);
        UITable.add(itemSelectRight);
        UITable.row();
        UITable.add(backButton);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        stage.act(delta);
        currencyLabel.setText("" + CurrencyManager.getAmount());
        stage.draw();
    }

    private void updateItem() {
        itemDrawable.setRegion(currentItem.getTexture());
        priceLabel.setText(ShopManager.getItemPrice(currentItem));
    }
}
