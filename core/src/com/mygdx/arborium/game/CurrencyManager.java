package com.mygdx.arborium.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class CurrencyManager {
    private static final String AMT_KEY = "Amount";

    private static int amount = 100;

    private static Preferences preferences;

    public static void init() {
        preferences = Gdx.app.getPreferences("Currency");
        load();
    }

    public static int getAmount() {
        return amount;
    }

    public static void add(int amt) {
        amount += amt;
        Gdx.app.log("Currency", "" + amount);
        save();
    }

    public static boolean subtract(int amt) {
        if (amt > amount)
            return false;
        else {
            amount -= amt;
            save();
            return true;
        }

    }

    private static void load() {
        amount = preferences.getInteger(AMT_KEY, 100);
    }

    private static void save() {
        preferences.putInteger(AMT_KEY, amount);
        preferences.flush();
    }
}
