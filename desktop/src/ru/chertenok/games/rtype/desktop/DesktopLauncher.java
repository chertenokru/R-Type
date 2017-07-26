package ru.chertenok.games.rtype.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import ru.chertenok.games.rtype.R_Type;


public class DesktopLauncher {
    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.height = 720;
        config.width = 1024;
        config.title = "R-TYPE style demo";
        config.fullscreen = false;
        new LwjglApplication(new R_Type(), config);
    }
}
