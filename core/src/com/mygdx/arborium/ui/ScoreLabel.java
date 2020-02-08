package com.mygdx.arborium.ui;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.parallel;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.mygdx.arborium.game.Arborium;

public class ScoreLabel extends Table {

    private HorizontalGroup coinGroup;
    private Image coinImage;
    private Label currencyLabel;
    private Label expLabel;

    public ScoreLabel(Arborium game, Skin skin, float x, float y, int exp, int currency) {
        super();

        coinGroup = new HorizontalGroup();
        coinImage = new Image(game.getAssetHandler().getTextureRegion("coin4x"));
        currencyLabel = new Label("+" + currency, skin);
        expLabel = new Label ("+" + exp + "XP", skin);

        coinGroup.addActor(currencyLabel);
        coinGroup.addActor(coinImage);

        this.add(coinGroup);
        this.row();
        this.add(expLabel);

        this.addAction(sequence(parallel(
                fadeOut(1),
                Actions.moveTo(x, y  + 100, 1)),
                run(new Runnable() {
                    @Override
                    public void run() {
                        remove();
                    }
        })));

        setPosition(x, y);
    }
}
