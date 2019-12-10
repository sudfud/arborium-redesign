package com.mygdx.arborium.screen.shop;

import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.mygdx.arborium.game.Arborium;
import com.mygdx.arborium.game.ExperienceManager;
import com.mygdx.arborium.game.ShopEntry;
import com.mygdx.arborium.game.ShopManager;
import com.mygdx.arborium.item.Item;
import com.mygdx.arborium.item.Tree;

public class ShopEntryWindow extends Window {

    private enum Category {
        GROW_TIME, PRODUCE_TIME, PRODUCE_AMOUNT, PRODUCE_VALUE
    }

    private String[] statIconNames = {"grow_time_icon8x", "produce_time_icon8x", "produce_count_icon8x", "produce_value_icon"};

    private Arborium game;

    protected ShopEntry currentShopEntry;

    private HorizontalGroup priceGroup;
    private Image coinImage;
    private Label priceLabel;

    private Image itemImage;
    private TextureRegionDrawable itemDrawable;

    private Label descriptionLabel;

    private Image[] treeStatIcons;
    private Label[] treeStatLabels;

    public ShopEntryWindow(Arborium game, Skin skin) {
        super("", skin);
        currentShopEntry = null;

        this.game = game;

        priceGroup = new HorizontalGroup();
        coinImage = new Image(new TextureRegionDrawable(game.getAssetHandler().getTexureRegion("coin4x")));
        priceLabel = new Label("", skin);
        priceGroup.addActor(coinImage);
        priceGroup.addActor(priceLabel);

        itemDrawable = new TextureRegionDrawable();
        itemImage = new Image(itemDrawable);

        descriptionLabel = new Label("", skin);
        descriptionLabel.setWrap(true);
        descriptionLabel.setWidth(10f);
        int catCount = Category.values().length;

        treeStatIcons = new Image[catCount];
        treeStatLabels = new Label[catCount];

        resetWindow();

        this.row();
        for (int i = 0; i < catCount; i++) {
            treeStatIcons[i] = new Image(new TextureRegionDrawable(game.getAssetHandler().getTexureRegion(statIconNames[i])));
            this.add(treeStatIcons[i]).pad(5).expandX();

            treeStatLabels[i] = new Label("", skin);
            this.add(treeStatLabels[i]).minWidth(200);
            this.row();
        }
    }

    public void setItem(Item item) {

        resetWindow();

        if (!ShopManager.isItemLocked(item)) {

            getTitleLabel().setText(item.getName());

            itemDrawable.setRegion(item.getTexture());
            itemImage.setDrawable(itemDrawable);

            descriptionLabel.setText(item.getDescription());

            if (item instanceof Tree) {
                Tree tree = (Tree) item;
                treeStatLabels[0].setText("" + (tree.getGrowTime() / 1000 / 60) + " mins");
                treeStatLabels[1].setText("" + (tree.getProduceTime() / 1000 / 60) + " mins");
                treeStatLabels[2].setText("" + tree.getProduceAmount());
                treeStatLabels[3].setText("" + tree.getProduceValue());

                this.row();

                for (int i = 0; i < treeStatIcons.length; i++) {
                    this.add(treeStatIcons[i]);
                    this.add(treeStatLabels[i]);
                    this.row();
                }
            }
        }

        else {
            getTitleLabel().setText("[LOCKED]");
            itemDrawable.setRegion(game.getAssetHandler().getTexureRegion("locked8x"));
            itemImage.setDrawable(itemDrawable);

            descriptionLabel.setText("Unlocked at level " + ShopManager.getUnlockLevel(item));
        }

        priceLabel.setText(ShopManager.getItemPrice(item));
    }

    private void resetWindow() {
        this.clear();
        this.add(itemImage).colspan(2).expandX();
        this.row();
        this.add(descriptionLabel).colspan(2).pad(15).width(500);
        this.row();
        this.add(priceGroup).colspan(2).pad(15);
    }
}
