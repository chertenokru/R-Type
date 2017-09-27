package ru.chertenok.games.rtype.config;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.Logger;

public class GameConfig {


    // --- const --
    private static final String FILE_PATH = "config/GameConfig.json";
    private final static boolean DEFAULT_SOUND = false;
    private final static boolean DEFAULT_MUSIC = false;
    private final static boolean DEFAULT_IS_DEBUG_DRAW = false;
    public static final int DEFAULT_WORLD_WIDTH = 1024;
    public static final int DEFAULT_WORLD_HEIGHT = 720;
    public static final String LEVEL1_FILE_PATH = "levels/level1.json";
    public static final String LEVEL1_PACK_FILE_PATH = "levels/level1.pack";
    public static final String LOCALIZATION_GAMEBUNDLE_PATH = "localization/GameBundle";
    public static final String LOCALIZATION_LEVEL1_PATH = "localization/level1";

    // ====== TAG ====================
    // Level
    public static final String START_TIME_TAG = "startTime";
    private static final String MUSIC_TAG = "music";
    private static final String IS_DEBUG_DRAW_TAG = "isDebugDraw";
    private static final String WORLD_WIDTH_TAG = "WORLD_WIDTH";
    private static final String WORLD_HEIGHT_TAG = "WORLD_HEIGHT";
    public static final String STEP_VECTOR_TAG = "stepVector";
    public static final String EVENTS_TAG = "events";
    // GameConfig
    private static final String SOUND_TAG = "sound";


    // ========= LevelEventsName
    //Asteroids
    public static final String ASTEROIDS_SET_OBJECT_COUNT = "Asteroids_setObjectCount";
    public static final String ASTEROIDS_SET_REVERSIVE = "Asteroids_setReversiveEnabled";
    public static final String ASTEROIDS_SET_MAX_SCALE = "Asteroids_setMaxScale";
    public static final String ASTEROIDS_SET_FIX_MAX_SCALE = "Asteroids_setFixMaxScale";
    public static final String ASTEROIDS_SET_FIX_ON_SCREEN = "Asteroids_setFixOnScreen";
    //ShipControl
    public static final String SHIP_SET_SHIELD = "Ship_setShieldEnable";
    public static final String SHIP_SET_FIRE = "Ship_setEnableFire";
    //Enemies
    public static final String ENEMIES_SET_OBJECT_COUNT = "Enemies_setObjectCount";
    //Game
    public static final String GAME_BOSS_MODE_ON = "Game_Boss_modeOn";
    public static final String GAME_SET_STATUS = "Game_setStatus";
    //Messages
    public static final String MESSAGE_ADD = "Message_add";


    private static FileHandle fileHandle;
    private static Logger log = new Logger(GameConfig.class.getSimpleName(), Logger.DEBUG);

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
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
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
