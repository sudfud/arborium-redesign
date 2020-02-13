package com.mygdx.arborium.ui;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.util.function.Function;

public class ConfirmWindow extends Window {

    private Function confirmFunction;

    private Label dialogueLabel;
    private TextButton confirmButton;
    private TextButton cancelButton;

    public ConfirmWindow(String text, Skin skin, Function<Void, Void> confirmFunction, Stack UIStack) {
        super("", skin);


        this.confirmFunction = confirmFunction;

        dialogueLabel = new Label(text, skin);
        dialogueLabel.setWrap(true);
       // dialogueLabel.setWidth(5);

        confirmButton = new TextButton("OK", skin);
        cancelButton = new TextButton("Cancel", skin);

        confirmButton.addListener(new ClickListener() {
           @Override
           public void clicked(InputEvent event, float x, float y) {
               confirmFunction.apply(null);
               remove(UIStack);
           }
        });

        cancelButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                remove(UIStack);
            }
        });

        add(dialogueLabel).colspan(2).width(Value.percentWidth(0.75f, this)).expandY();
        row();
        add(confirmButton).size(150, 75);
        add(cancelButton).size(150, 75);
    }

    private void remove(Stack stack) {
        stack.removeActor(this.getParent());
    }
}
