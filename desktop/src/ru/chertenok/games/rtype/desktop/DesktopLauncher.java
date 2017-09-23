package ru.chertenok.games.rtype.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import ru.chertenok.games.rtype.R_Type;
import ru.chertenok.games.rtype.config.GameConfig;


public class DesktopLauncher {
    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.height = GameConfig.DEFAULT_WORLD_HEIGHT;
        config.width = GameConfig.DEFAULT_WORLD_WIDTH;
        config.title = "R-TYPE style demo";
        config.fullscreen = false;
        new LwjglApplication(new R_Type(), config);
    }
}
