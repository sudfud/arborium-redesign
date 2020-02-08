package com.mygdx.arborium.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.arborium.game.Arborium;
import com.mygdx.arborium.game.Settings;

public class OptionScreen extends GameScreen {

    private Window optionsWindow;
    private ImageButton fullscreenToggle;
    private Label fullscreenLabel;
    private ImageButton soundOnToggle;
    private Label soundLabel;
    private SelectBox<Vector2> resSelectBox;
    private TextButton resApplyButton;

    public OptionScreen(Arborium game) {
        super(game);

        Skin skin = game.getAssetHandler().getSkin();

        optionsWindow = new Window("", skin);
        fullscreenToggle = new ImageButton(skin, "toggle");
        fullscreenLabel = new Label("Fullscreen", skin);
        soundOnToggle = new ImageButton(skin, "toggle");
        soundLabel = new Label("Sound on", skin);
        resSelectBox = new SelectBox<>(skin);
        resApplyButton = new TextButton("Apply", skin);

        fullscreenToggle.setChecked(Settings.isFullscreen());
        soundOnToggle.setChecked(Settings.isSoundOn());

        fullscreenToggle.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Settings.toggleFullscreen();
            }
        });

        soundOnToggle.addListener(new ClickListener() {
           @Override
           public void clicked(InputEvent event, float x, float y) {
               Settings.toggleSound();
           }
        });

        resSelectBox.setItems(Settings.getResolutions());

        optionsWindow.setFillParent(true);

        optionsWindow.add(fullscreenLabel);
        optionsWindow.add(fullscreenToggle);
        optionsWindow.row();
        optionsWindow.add(soundLabel);
        optionsWindow.add(soundOnToggle);
        optionsWindow.row();
        optionsWindow.add(resSelectBox).colspan(2);
        optionsWindow.add(resApplyButton);

        UITable.add(optionsWindow).width(Value.percentWidth(0.8f, UITable));
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        stage.act();
        stage.draw();
    }
}
