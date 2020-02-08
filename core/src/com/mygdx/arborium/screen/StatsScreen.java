package com.mygdx.arborium.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.arborium.game.Arborium;
import com.mygdx.arborium.manager.ExperienceManager;
import com.mygdx.arborium.manager.ShopManager;
import com.mygdx.arborium.manager.StatsManager;
import com.mygdx.arborium.manager.ItemManager;
import com.mygdx.arborium.item.Tree;

public class StatsScreen extends GameScreen {

    private Window statWindow;
    private ScrollPane scrollPane;
    private Label statLabel;

    private Texture bgTexture;
    private TextureRegion bgRegion;

    private TextButton backButton;

    public StatsScreen(Arborium game) {
        super(game);

        Skin skin = game.getAssetHandler().getSkin();

        statWindow = new Window("", skin);
        statLabel = new Label("", skin);
        scrollPane = new ScrollPane(statLabel, skin);

        backButton = new TextButton("Back", skin);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.popScreen();
            }
        });

        statWindow.add(scrollPane);
        UITable.add(statWindow);
        UITable.row();
        UITable.add(backButton).size(150, 75);

        bgTexture = new Texture("frame1.png");
        bgTexture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        bgRegion = new TextureRegion(bgTexture, 1024, 1024);
    }

    @Override
    public void show() {
        super.show();
        updateStatLabel();
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        spriteBatch.begin();
        spriteBatch.draw(bgRegion, 0, 0, 4, 4);
        spriteBatch.end();

        stage.act(delta);
        stage.draw();
    }

    private void updateStatLabel() {
        String stats = "";
        stats += "Experience gained: " + ExperienceManager.getExperience() + "\n";
        stats += "Exp. to next level: " + ExperienceManager.getExpToNextLevel() + "\n\n";
        stats += "Trees planted: " + StatsManager.getTreesPlanted() + "\n\n";
        for (Tree tree : ItemManager.getTreeList()) {
            int fruitCollect = StatsManager.getFruitCollected(tree);
            if (!ShopManager.isItemLocked(tree) && fruitCollect > 0) {
                String treeName = tree.getName();
                String fruitName = treeName.substring(0, treeName.indexOf(" ")) + "s";
                stats += fruitName + " harvested: " + fruitCollect + "\n";
            }
        }
        statLabel.setText(stats);
    }
}
