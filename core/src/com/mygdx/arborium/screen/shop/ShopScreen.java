package com.mygdx.arborium.screen.shop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
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

    private Queue<String> categoryQueue;
    private Queue<Item> itemQueue;
    private Item currentItem;

    private String category;

    private HorizontalGroup categoryGroup;
    private Label categoryLabel;
    private Button categoryLeft;
    private Button categoryRight;

    private HorizontalGroup currencyGroup;
    private Image coinImage;
    private Label currencyLabel;
    private TextButton buyButton;
    private ImageButton itemSelectLeft;
    private ImageButton itemSelectRight;
    private TextButton backButton;

    private ShopEntryWindow treeShop;

    public ShopScreen(final Arborium game) {
        super(game);

        Skin skin = game.getAssetHandler().getSkin();

        category = "Trees";

        categoryQueue = new Queue<>();
        categoryQueue.addLast("Trees");
        categoryQueue.addLast("Fertilizers");

        categoryLabel = new Label(category, skin);

        itemQueue = new Queue<>();
        for (Item item : ItemManager.getTreeList()) {
            itemQueue.addLast(item);
        }

        currentItem = itemQueue.removeFirst();
        itemQueue.addLast(currentItem);

        treeShop = new ShopEntryWindow(game, skin);
        treeShop.setItem(currentItem);

        categoryGroup = new HorizontalGroup();

        categoryLeft = new ImageButton(skin, "left");
        categoryLeft.addListener(new ClickListener() {
           @Override
           public void clicked(InputEvent event, float x, float y) {
               category = categoryQueue.removeLast();
               categoryQueue.addFirst(category);
               updateCategory();
           }
        });

        categoryRight = new ImageButton(skin, "right");
        categoryRight.addListener(new ClickListener() {
           @Override
           public void clicked(InputEvent event, float x, float y) {
               category = categoryQueue.removeFirst();
               categoryQueue.addLast(category);
               updateCategory();
           }
        });

        categoryGroup.addActor(categoryLeft);
        categoryGroup.addActor(categoryLabel);
        categoryGroup.addActor(categoryRight);

        buyButton = new TextButton("Buy", skin);
        buyButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (CurrencyManager.subtract(ShopManager.getItemPrice(currentItem)))
                    InventoryManager.addItem(currentItem);
            }
        });

        updateCategory();

        coinImage = new Image(new TextureRegionDrawable(game.getAssetHandler().getTexureRegion("coin4x")));
        currencyLabel = new Label("" + CurrencyManager.getAmount(), skin);
        currencyGroup = new HorizontalGroup();

        currencyGroup.addActor(coinImage);
        currencyGroup.addActor(currencyLabel);


        itemSelectLeft = new ImageButton(skin, "left");
        itemSelectLeft.addListener(new ClickListener() {
           @Override
           public void clicked(InputEvent event, float x, float y) {
               currentItem = itemQueue.removeLast();
               itemQueue.addFirst(currentItem);
               updateItem();
           }
        });

        itemSelectRight = new ImageButton(skin, "right");
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
               game.popScreen();
           }
        });
        UITable.add(currencyGroup).colspan(3);
        UITable.row();

        UITable.add(categoryGroup).colspan(3).pad(50).top();
        UITable.row();

        UITable.add(itemSelectLeft);
        UITable.add(treeShop).width(Gdx.graphics.getWidth() * 3/4);
        UITable.add(itemSelectRight);
        UITable.row();
        UITable.add(buyButton).colspan(3).pad(15).size(150, 75);
        UITable.row();
        UITable.add(backButton).colspan(3).size(150, 75);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        stage.act(delta);
        currencyLabel.setText("" + CurrencyManager.getAmount());
        stage.draw();
    }

    private void updateCategory() {
        categoryLabel.setText(category);

        itemQueue.clear();
        Item[] items;
        switch(category) {
            case "Trees":
            default:
                items = ItemManager.getTreeList();
                break;
            case "Fertilizers":
                items = ItemManager.getFertilizerList();
                break;
        }

        for (Item item : items) {
                itemQueue.addLast(item);
        }

        currentItem = itemQueue.removeFirst();
        itemQueue.addLast(currentItem);
        updateItem();
    }

    private void updateItem() {
        treeShop.setItem(currentItem);
    }
}
