package ru.chertenok.games.rtype.desktop;

import com.badlogic.gdx.tools.texturepacker.TexturePacker;

public class AssestPacker {
    private static final boolean DRAW_DEBUG_OUTLINE = false;

    private static final String RAW_ASSETS_PATH = "1/images/";
    private static final String ASSETS_PATH = "android/assets/levels/";

    public static void main(String[] args) {
        TexturePacker.Settings settings = new TexturePacker.Settings();
        settings.debug = DRAW_DEBUG_OUTLINE;

        settings.maxWidth = 1024;
        settings.maxHeight = 1024;
        settings.fast = false;
        settings.paddingX = 0;
        settings.paddingY = 0;
        //settings.

        TexturePacker.process(settings,
                RAW_ASSETS_PATH + "/images",
                ASSETS_PATH,
                "level1"
        );
    }
}
