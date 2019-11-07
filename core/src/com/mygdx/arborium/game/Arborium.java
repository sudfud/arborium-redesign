package com.mygdx.arborium.game;

import com.badlogic.gdx.Game;
import com.mygdx.arborium.item.InventoryManager;
import com.mygdx.arborium.screen.AssetHandler;
import com.mygdx.arborium.item.ItemManager;
import com.mygdx.arborium.screen.farm.FarmScreen;
import com.mygdx.arborium.screen.shop.ShopScreen;

public class Arborium extends Game {

	AssetHandler assetHandler;

	FarmScreen farmScreen;
	ShopScreen shopScreen;

	@Override
	public void create () {
        assetHandler = new AssetHandler();
        assetHandler.load();
		ItemManager.init(assetHandler);
		ExperienceManager.init();
		CurrencyManager.init();
		InventoryManager.init();
		ShopManager.init();

		farmScreen = new FarmScreen(this);
		shopScreen = new ShopScreen(this);
		setScreen(farmScreen);
	}

	@Override
	public void dispose () {
		farmScreen.dispose();
		assetHandler.dispose();
	}

	public void toShopScreen() {
		setScreen(shopScreen);
	}

	public void toFarmScreen() {
		setScreen(farmScreen);
	}

	public AssetHandler getAssetHandler() {
		return assetHandler;
	}
}
