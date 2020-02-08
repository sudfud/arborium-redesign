package com.mygdx.arborium.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.math.Vector2;

public class Settings {
    private static Preferences preferences;

    private static final String fullscreenKey = "fullscreen";
    private static final String soundKey = "soundOn";
    private static final String resKey = "resolution";

    private static boolean fullscreen;
    private static boolean soundOn;
    private static int resIndex;

    private static Vector2[] resolutions = {
            new Vector2(800, 600),
            new Vector2(1024, 768),
            new Vector2(1280, 720),
            new Vector2(1280, 800),
            new Vector2(1280, 960),
            new Vector2(1366, 768),
            new Vector2(1440, 900),
            new Vector2(1600, 900),
            new Vector2(1920, 1080)
    };

    public static void init() {
        preferences = Gdx.app.getPreferences("Settings");

        load();
    }

    public static boolean isFullscreen() {
        return fullscreen;
    }

    public static void toggleFullscreen() {
        fullscreen = !fullscreen;
        save();
    }

    public static boolean isSoundOn() {
        return soundOn;
    }

    public static void toggleSound() {
        soundOn = !soundOn;
        save();
    }

    public static Vector2[] getResolutions() {
        return resolutions;
    }

    public static void setResolutionIndex(int index) {
        resIndex = index;
        save();
    }

    public static void load() {
        fullscreen = preferences.getBoolean(fullscreenKey, false);
        soundOn = preferences.getBoolean(soundKey, true);
        resIndex = preferences.getInteger(resKey, 0);
    }

    public static void save() {
        preferences.putBoolean(fullscreenKey, fullscreen);
        preferences.putBoolean(soundKey, soundOn);
        preferences.putInteger(resKey, resIndex);
        preferences.flush();
    }

    private static void update() {
        Vector2 r = resolutions[resIndex];

        Gdx.graphics.setWindowedMode((int)r.x, (int)r.y);
        if (fullscreen)
            Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
    }
}
