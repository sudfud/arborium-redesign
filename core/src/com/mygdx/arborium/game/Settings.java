package com.mygdx.arborium.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Preferences;

public class Settings {
    private static Preferences preferences;

    private static final String fullscreenKey = "fullscreen";
    private static final String soundKey = "soundOn";
    private static final String resKey = "resolution";

    private static boolean fullscreen;
    private static boolean soundOn;
    private static int resIndex;
    
    public static class Resolution {
        public final int width;
        public final int height;
        
        Resolution(int w, int h) {
            width = w;
            height = h;
        }
        
        @Override
        public String toString() {
            return "" + width + "x" + height;
        }
    }

    private static Resolution[] resolutions = {
            new Resolution(800, 600),
            new Resolution(1024, 768),
            new Resolution(1280, 720),
            new Resolution(1280, 800),
            new Resolution(1280, 960),
            new Resolution(1366, 768),
            new Resolution(1440, 900),
            new Resolution(1600, 900),
            new Resolution(1920, 1080)
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

    public static Resolution getResolution() {
        return resolutions[resIndex];
    }

    public static Resolution[] getResolutions() {
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

    public static void update() {
        Resolution r = resolutions[resIndex];

        Gdx.graphics.setWindowedMode(r.width, r.height);
        if (fullscreen)
            Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
    }
}
