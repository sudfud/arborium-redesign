package com.mygdx.arborium.ui;

import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.mygdx.arborium.item.Item;

public class ItemListWindow extends Window {

    private ScrollPane scrollPane;
    private Table buttonTable;
    private ButtonGroup buttons;

    public ItemListWindow(Skin skin, Item[] items) {
        super("", skin);

        buttonTable = new Table();
        buttonTable.setFillParent(true);

        scrollPane = new ScrollPane(buttonTable, skin);
        scrollPane.setScrollbarsVisible(false);
        scrollPane.setOverscroll(false, false);

        buttons = new ButtonGroup();
        buttons.setMaxCheckCount(1);
        buttons.setMinCheckCount(1);

        setItems(items);

        this.add(scrollPane).size(Value.percentWidth(0.85f, this));

        setMovable(false);
    }

    public void setItems(Item[] items) {
        buttonTable.clear();
        buttons.clear();

        for (int i = 0; i < items.length; i++) {
            ItemButton upButton = new ItemButton(getSkin(), items[i]);
            buttons.add(upButton);
            buttonTable.add(upButton).size(Value.percentWidth(0.15f, this)).pad(15);
            if ((i + 1) % 4 == 0)
                buttonTable.row();
        }
    }

    public int getCheckedButtonIndex() {
        return buttons.getCheckedIndex();
    }

    public Item getSelectedItem() {
        ItemButton b = (ItemButton)buttons.getChecked();
        return b.getItem();
    }
}
