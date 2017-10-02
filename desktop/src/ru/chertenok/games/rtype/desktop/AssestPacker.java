package ru.chertenok.games.rtype.desktop;

import com.badlogic.gdx.tools.texturepacker.TexturePacker;

public class AssestPacker {
    private static final boolean DRAW_DEBUG_OUTLINE = false;

    private static final String RAW_ASSETS_PATH = "1/images/";
    private static final String ASSETS_PATH = "android/assets/levels/";

    public static void main(String[] args) {
        TexturePacker.Settings settings = new TexturePacker.Settings();
        settings.debug = DRAW_DEBUG_OUTLINE;

        settings.maxWidth = 2048;
        settings.maxHeight = 2048;

        TexturePacker.process(settings,
                RAW_ASSETS_PATH + "/images",
                ASSETS_PATH + "/images",
                "level"
        );
    }
}
