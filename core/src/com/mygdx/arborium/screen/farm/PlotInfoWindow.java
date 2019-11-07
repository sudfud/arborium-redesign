package com.mygdx.arborium.screen.farm;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.mygdx.arborium.game.Plot;
import com.mygdx.arborium.item.InventoryManager;
import com.mygdx.arborium.item.Item;
import com.mygdx.arborium.item.ItemManager;
import com.mygdx.arborium.item.Tree;

public class PlotInfoWindow extends Window {
    private FarmScreen farmScreen;

    private Plot plot;

    private Label plotInfoArea;

    private TextButton plantButton;
    private TextButton plantConfirmButton;
    private TextButton plantCancelButton;
    private TextButton harvestButton;
    private TextButton clearButton;
    private TextButton backButton;

    private List<Tree> treeList;

    private float w = Gdx.graphics.getWidth();
    private float h = Gdx.graphics.getHeight();

    public PlotInfoWindow(final FarmScreen farmScreen, final Plot plot, Skin skin) {
        super("" + plot.getId(), skin);

        this.farmScreen = farmScreen;
        this.plot = plot;

        plotInfoArea = new Label(plot.toString(), skin, "small");

        plantButton = new TextButton("Plant", skin);
        plantButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showPlantingWindow();
            }
        });

        plantConfirmButton = new TextButton("OK", skin);
        plantConfirmButton.addListener(new ClickListener() {
           @Override
           public void clicked(InputEvent event, float x, float y) {
               plantTree();
               showInfoWindow();
           }
        });

        plantCancelButton = new TextButton("Cancel", skin);
        plantCancelButton.addListener(new ClickListener() {
           @Override
           public void clicked(InputEvent event, float x, float y) {
               showInfoWindow();
           }
        });

        harvestButton = new TextButton("Harvest", skin);
        harvestButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                harvest();
                farmScreen.beginHarvest();
            }
        });

        backButton = new TextButton("Back", skin);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                goToFarmScreen();
            }
        });

        treeList = new List<>(skin);
        treeList.setItems(new Array(ItemManager.getTreeList()));

        showInfoWindow();

        this.pack();
    }

    public void setPlot(Plot plot) {
        this.plot = plot;
        this.getTitleLabel().setText("Plot #" + plot.getId());
        plotInfoArea.setText(this.plot.toString());
        this.invalidate();
    }

    public void update() {
        harvestButton.setVisible(plot.getState() == Plot.PlotState.HARVESTABLE);
        plantButton.setVisible(plot.getState() == Plot.PlotState.EMPTY);
        plotInfoArea.setText(plot.toString());
        this.invalidate();
    }

    private void plantTree() {
        plot.plant(treeList.getSelected());
        InventoryManager.takeItem(treeList.getSelected());
    }

    private void showPlantingWindow() {
        updateTreeList();

        this.clear();
        this.add(treeList).expandX().center().pad(15).minWidth(w / 2);
        this.row();
        this.add(plantConfirmButton).pad(15).minWidth(w / 4);
        this.row();
        this.add(plantCancelButton).pad(15).minWidth(w / 4);
    }

    private void showInfoWindow() {
        this.clear();
        this.add(plotInfoArea).minWidth(Gdx.graphics.getWidth() / 2).pad(15);
        this.row();
        this.add(harvestButton).pad(15).minWidth(w / 4);
        this.row();
        this.add(plantButton).pad(15).minWidth(w / 4);
        this.row();
        this.add(backButton).pad(15).minWidth(w / 4);
    }

    private void updateTreeList() {
        Item[] items = InventoryManager.getItems();
        Array<Item> trees = new Array<>();
        for (Item item : items) {
            if (item instanceof Tree) {
                trees.add(item);
            }
        }
        treeList.setItems(trees);
    }

    private void goToFarmScreen() {
        farmScreen.hidePlotInfoWindow();
    }

    private void harvest() {
        plot.harvest();
    }
}
