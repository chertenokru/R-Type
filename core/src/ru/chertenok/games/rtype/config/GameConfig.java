package ru.chertenok.games.rtype.config;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.Logger;
import ru.chertenok.games.rtype.Global;

public class GameConfig {
    // --- const --
    private static final String FILE_PATH = "config/GameConfig.json";
    private final static boolean DEFAULT_SOUND = false;
    private final static boolean DEFAULT_MUSIC = false;
    private final static boolean DEFAULT_IS_DEBUG_DRAW = false;
    public static final int DEFAULT_WORLD_WIDTH = 1024;
    public static final int DEFAULT_WORLD_HEIGHT = 720;

    private static final String SOUND_TAG = "sound";
    private static final String MUSIC_TAG = "music";
    private static final String IS_DEBUG_DRAW_TAG = "isDebugDraw";
    private static final String WORLD_WIDTH_TAG = "WORLD_WIDTH";
    private static final String WORLD_HEIGHT_TAG = "WORLD_HEIGHT";


    private static FileHandle fileHandle;
    private static Logger log = Global.getLogger(GameConfig.class);

    private static boolean sound = false;
    private static boolean music = false;
    private static boolean isDebugDraw = false;

    private static float WORLD_WIDTH;
    private static float WORLD_HEIGHT;


    static {
        init();
    }

    private GameConfig() {
    }

    private static void init() {
        fileHandle = Gdx.files.internal(FILE_PATH);
        if (fileHandle.exists()) {
            load();
        } else {
            log.info("Default setting file not found");
            setupDefaults();
        }
    }


    private static void load() {
        try {

            JsonReader reader = new JsonReader();
            JsonValue root = reader.parse(fileHandle);
            sound = root.getBoolean(SOUND_TAG, DEFAULT_SOUND);
            music = root.getBoolean(MUSIC_TAG, DEFAULT_MUSIC);
            isDebugDraw = root.getBoolean(IS_DEBUG_DRAW_TAG, DEFAULT_IS_DEBUG_DRAW);
            WORLD_WIDTH = root.getInt(WORLD_WIDTH_TAG, DEFAULT_WORLD_WIDTH);
            WORLD_HEIGHT = root.getInt(WORLD_HEIGHT_TAG, DEFAULT_WORLD_HEIGHT);
        } catch (Exception e) {
            log.error("Error loading setting " + FILE_PATH + " using defaults. ", e);
        }


    }

    private static void setupDefaults() {
        sound = DEFAULT_SOUND;
        music = DEFAULT_MUSIC;
        isDebugDraw = DEFAULT_IS_DEBUG_DRAW;
        WORLD_WIDTH = DEFAULT_WORLD_WIDTH;
        WORLD_HEIGHT = DEFAULT_WORLD_HEIGHT;
    }

    public static boolean isSound() {
        return sound;
    }

    public static boolean isMusic() {
        return music;
    }

    public static boolean isIsDebugDraw() {
        return isDebugDraw;
    }

    public static float getWorldWidth() {
        return WORLD_WIDTH;
    }

    public static float getWorldHeight() {
        return WORLD_HEIGHT;
    }
}
