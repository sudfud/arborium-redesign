package com.mygdx.arborium.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.arborium.game.Arborium;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.resizable = false;
		config.addIcon("icon/apple2x.png", Files.FileType.Internal);
		config.addIcon("icon/apple_icon_med.png", Files.FileType.Internal);
		config.addIcon("icon/apple_icon_small.png", Files.FileType.Internal);
		config.width = 800;
		config.height = 600;
		new LwjglApplication(new Arborium(), config);
	}
}
