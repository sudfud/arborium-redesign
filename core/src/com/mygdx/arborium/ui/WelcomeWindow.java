package com.mygdx.arborium.ui;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.arborium.screen.farm.FarmScreen;

public class WelcomeWindow extends Window {

    private FarmScreen farmScreen;

    private Label welcomeLabel;
    private TextButton closeButton;

    public WelcomeWindow(Skin skin, FarmScreen farmScreen) {
        super("Welcome!", skin);

        this.farmScreen = farmScreen;

        String welcome = "Thanks for playing! This is a very early build so expect some bugs, balance issues, missing content, and a wonky UI. " +
                "If you're playing for the first time, you should have two fertilizers in your inventory to help you get started. " +
                "Also, don't worry about closing the game. Your trees will keep growing even while the game is closed. :) Enjoy!" +
                "\n\n -Oli (sud)";

        welcomeLabel = new Label(welcome, skin);
        closeButton = new TextButton("Close", skin);

        welcomeLabel.setWrap(true);

        closeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                close();
            }
        });

//        this.addListener(new ClickListener() {
//            @Override
//            public void clicked(InputEvent event, float x, float y) {
//                close();
//            }
//        });
        add(welcomeLabel).width(Value.percentWidth(0.8f, this));
        row();
        add(closeButton);
    }

    private void close() {
        getParent().removeActor(this);
        setVisible(false);
        farmScreen.showUI();
    }
}
