package com.mygdx.arborium.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.mygdx.arborium.game.Plot;
import com.mygdx.arborium.item.Fertilizer;
import com.mygdx.arborium.manager.InventoryManager;
import com.mygdx.arborium.item.Item;
import com.mygdx.arborium.manager.ItemManager;
import com.mygdx.arborium.item.Tree;
import com.mygdx.arborium.screen.farm.FarmScreen;

public class FarmScreenWindow extends Window {
    private FarmScreen farmScreen;

    private Plot plot;

    private Container infoWindow;

    private Label plotInfoArea;

    private TextButton.TextButtonStyle defaultStyle;
    private TextButton.TextButtonStyle disabledStyle;

    private Table buttonGroup;
    private TextButton plantButton;
    private TextButton plantConfirmButton;
    private TextButton plantCancelButton;
    private TextButton harvestButton;
    private TextButton fertilizeButton;
    private TextButton clearButton;
    private TextButton backButton;

    private List<Item> itemList;

    private float w = Gdx.graphics.getWidth();
    private float h = Gdx.graphics.getHeight();

    public FarmScreenWindow(final FarmScreen farmScreen, final Plot plot, Skin skin) {
        super("", skin);

        this.farmScreen = farmScreen;
        this.plot = plot;

        infoWindow = new Container();

        plotInfoArea = new Label(plot.toString(), skin);

        TextButton butt = new TextButton("", skin);
        TextButton butt2 = new TextButton("", skin, "disabled");
        defaultStyle = butt.getStyle();
        disabledStyle = butt2.getStyle();

        buttonGroup = new Table();

        plantButton = new TextButton("Plant", skin);
        plantButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                updateTreeList();
                showPlantingWindow();
            }
        });

        plantConfirmButton = new TextButton("OK", skin);
        plantConfirmButton.addListener(new ClickListener() {
           @Override
           public void clicked(InputEvent event, float x, float y) {
               Item selectItem = itemList.getSelected();
               if (selectItem instanceof Tree)
                    plantTree();
               else
                    fertilizePlot();
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
                if (!harvestButton.isDisabled()) {
                    harvest();
                    farmScreen.beginHarvest();
                    farmScreen.hidePlotInfoWindow();
                }
            }
        });

        fertilizeButton = new TextButton("Fertilize", skin);
        fertilizeButton.addListener(new ClickListener() {
           @Override
           public void clicked(InputEvent event, float x, float y) {
                updateFertilizerList();
                showPlantingWindow();
           }
        });

        clearButton = new TextButton("Clear", skin);
        clearButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clearPlot();
            }
        });

        backButton = new TextButton("Back", skin);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                goToFarmScreen();
                farmScreen.unfocus();
            }
        });

        itemList = new List<>(skin);
        itemList.setItems(new Array(ItemManager.getTreeList()));

        this.add(infoWindow);
        this.row();
        this.add(buttonGroup).width(Gdx.graphics.getWidth() * 3/4).expandX().space(50);

        showInfoWindow();

        this.pack();
    }

    public void setPlot(Plot plot) {
        this.plot = plot;
        plotInfoArea.setText(this.plot.toString());
        showInfoWindow();
        this.invalidate();
    }

    public void update() {
        harvestButton.setTouchable((plot.getState() == Plot.PlotState.HARVESTABLE ? Touchable.enabled : Touchable.disabled));
        harvestButton.setStyle((plot.getState() == Plot.PlotState.HARVESTABLE ? defaultStyle : disabledStyle));
        plantButton.setVisible(plot.getState() == Plot.PlotState.EMPTY);
        fertilizeButton.setVisible(plot.getState() == Plot.PlotState.EMPTY);
        plotInfoArea.setText(plot.toString());
        this.invalidate();
    }

    private void plantTree() {
        plot.plant((Tree)itemList.getSelected());
        InventoryManager.takeItem(itemList.getSelected());
    }

    private void fertilizePlot() {
        plot.fertilize((Fertilizer)itemList.getSelected());
        InventoryManager.takeItem(itemList.getSelected());
    }

    private void showPlantingWindow() {
        infoWindow.setActor(itemList);
        this.clear();
        this.add(itemList);
        this.row();
        this.add(buttonGroup).padBottom(200);
        buttonGroup.clear();
        buttonGroup.add(plantConfirmButton).pad(25).size(150, 75);
        if (itemList.getItems().size > 0) {
            plantConfirmButton.setTouchable(Touchable.enabled);
            plantConfirmButton.setStyle(defaultStyle);
        }
        else {
            plantConfirmButton.setTouchable(Touchable.disabled);
            plantConfirmButton.setStyle(disabledStyle);
        }
        buttonGroup.add(plantCancelButton).pad(25).size(150, 75);
    }

    private void showInfoWindow() {
        infoWindow.clear();
        infoWindow.setActor(plotInfoArea);
        this.clear();
        this.add(plotInfoArea);
        this.row();
        this.add(buttonGroup);

        buttonGroup.clear();
        if (plot.getState() == Plot.PlotState.EMPTY) {

            buttonGroup.add(plantButton).pad(25).size(150, 75);
            buttonGroup.add(fertilizeButton).pad(25).size(150, 75);

//            plantButton.setTouchable(Touchable.enabled);
//            plantButton.setStyle(defaultStyle);
//            clearButton.setTouchable(Touchable.disabled);
//            clearButton.setStyle(disabledStyle);
//            fertilizeButton.setTouchable(Touchable.enabled);
//            fertilizeButton.setStyle(defaultStyle);
        }
        else {
//            plantButton.setTouchable(Touchable.disabled);
//            plantButton.setStyle(disabledStyle);
//            clearButton.setTouchable(Touchable.enabled);
//            clearButton.setStyle(defaultStyle);
//            fertilizeButton.setTouchable(Touchable.disabled);
//            fertilizeButton.setStyle(disabledStyle);


            buttonGroup.add(harvestButton).pad(25).size(150, 75);
            buttonGroup.add(clearButton).pad(25).size(150, 75);
        }
        buttonGroup.add(backButton).pad(25).size(150, 75);
    }

    private void updateTreeList() {
        Item[] items = InventoryManager.getItems();
        Array<Item> trees = new Array<>();
        for (Item item : items) {
            if (item instanceof Tree) {
                trees.add(item);
            }
        }
        itemList.setItems(trees);
    }

    private void updateFertilizerList() {
        Item[] items = InventoryManager.getItems();
        Array<Fertilizer> fertilizers = new Array<>();
        for (Item item : items) {
            if (item instanceof Fertilizer) {
                fertilizers.add((Fertilizer)item);
            }
        }
        itemList.setItems(fertilizers);
    }

    private void goToFarmScreen() {
        farmScreen.hidePlotInfoWindow();
    }

    private void harvest() {
        plot.harvest();
    }

    private void clearPlot() {
        farmScreen.showConfirmWindow();
    }
}
