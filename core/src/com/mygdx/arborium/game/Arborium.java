package com.mygdx.arborium.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.mygdx.arborium.item.InventoryManager;
import com.mygdx.arborium.screen.AssetHandler;
import com.mygdx.arborium.item.ItemManager;
import com.mygdx.arborium.screen.GameScreen;
import com.mygdx.arborium.screen.farm.FarmScreen;
import com.mygdx.arborium.screen.shop.ShopScreen;
import com.mygdx.arborium.screen.upgrade.UpgradeScreen;

import java.util.ArrayList;
import java.util.Stack;

public class Arborium extends Game {

	private final String SMALL_MAP = "map/FUCKMAP.tmx";
	private final String MED_MAP = "map/arborMedMap.tmx";
	private final String LARGE_MAP = "map/arborLargeMap1.tmx";
	private final String HUGE_MAP = "map/arborHugeMap.tmx";

	AssetHandler assetHandler;

	private Stack<GameScreen> screenStack;

	private ArrayList<FarmScreen> farmScreens;
	private int farmScreenIndex = 0;

	ShopScreen shopScreen;

	UpgradeScreen upgradeScreen;

	@Override
	public void create () {
        assetHandler = new AssetHandler();
        assetHandler.load();
		ItemManager.init(assetHandler);
		StatsManager.initialize();
		UpgradeManager.initialize(assetHandler);
		ExperienceManager.init();
		CurrencyManager.init();
		InventoryManager.init();
		ShopManager.init();

		screenStack = new Stack<>();

		farmScreens = new ArrayList<>();
		farmScreens.add(new FarmScreen(1,this, 4, "map/arborSmallMap.tmx"));
		farmScreens.add(new FarmScreen(1, this, 8,  MED_MAP, 500));
		farmScreens.add(new FarmScreen(2, this, 16, LARGE_MAP, 5000));
		farmScreens.add(new FarmScreen(3, this, 32, HUGE_MAP, 50000));

		shopScreen = new ShopScreen(this);
		upgradeScreen = new UpgradeScreen(this);
		pushScreen(farmScreens.get(farmScreenIndex));
	}

	@Override
	public void dispose () {
		for (FarmScreen farmScreen : farmScreens) {
			farmScreen.dispose();
		}
		assetHandler.dispose();
	}

	public void pushScreen(GameScreen gameScreen) {
		screenStack.push(gameScreen);
		setScreen(gameScreen);
	}

	public void popScreen() {
		screenStack.pop();
		setScreen(screenStack.peek());
		Gdx.app.log("Arborium", "" + screenStack.size());
	}

	public void toShopScreen() {
		pushScreen(shopScreen);
	}

	public void previousFarmScreen() {
		if (farmScreenIndex > 0) {
			farmScreenIndex--;
			screenStack.pop();
			screenStack.push(farmScreens.get(farmScreenIndex));
			setScreen(screenStack.peek());
		}
	}

	public void nextFarmScreen() {
		if (farmScreenIndex < farmScreens.size() - 1) {
			farmScreenIndex++;
			screenStack.pop();
			screenStack.push(farmScreens.get(farmScreenIndex));
			setScreen(screenStack.peek());
		}
	}

	public int getFarmScreenIndex() {
		return farmScreenIndex;
	}

	public AssetHandler getAssetHandler() {
		return assetHandler;
	}
}
