package com.mygdx.arborium.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.math.MathUtils;

public class ExperienceManager {

    private static final int LVL_EXP = 100;

    private static final String EXP_KEY = "Experience";
    private static final String LVL_KEY = "Level";
    private static final String THRESHOLD_KEY = "Threshold";

    private static Preferences preferences;

    private static int experience = 0;
    private static int nextLevelThreshold = LVL_EXP;
    private static int level = 1;

    public static void init() {
        preferences = Gdx.app.getPreferences("Experience");
        load();
    }

    public static void addExperience(int exp) {
        experience += exp;
        if (experience >= nextLevelThreshold) {
            level++;
            nextLevelThreshold = (int) (LVL_EXP * Math.pow((double) level, 1.5d));

            Gdx.app.log("ExpMang", "Level up!!! Lvl: " + level + ", Exp. to next level: " + (nextLevelThreshold - experience));
        }
        save();
    }

    public static int getExperience() {
        return experience;
    }

    public static int getLevel() {
        return level;
    }

    public static int getNextLevelThreshold() {
        return nextLevelThreshold;
    }

    public static int getPrevLevelThreshold() {
        if (level == 1) {
            return 0;
        }
        else
            return (int) (LVL_EXP * Math.pow((double) level - 1, 1.5d));
    }

    public static int getExpToNextLevel() {
        return nextLevelThreshold - experience;
    }

    public static void load() {
        experience = preferences.getInteger(EXP_KEY, 0);
        level = preferences.getInteger(LVL_KEY, 1);
        nextLevelThreshold = preferences.getInteger(THRESHOLD_KEY,  LVL_EXP);
    }

    public static void save() {
        preferences.putInteger(EXP_KEY, experience);
        preferences.putInteger(LVL_KEY, level);
        preferences.putInteger(THRESHOLD_KEY, nextLevelThreshold);
        preferences.flush();
    }
}
