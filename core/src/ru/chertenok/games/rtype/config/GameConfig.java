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

    private static final String SOUND_TAG = "sound";
    private static final String MUSIC_TAG = "music";
    private static final String IS_DEBUG_DRAW_TAG = "isDebugDraw";


    private static FileHandle fileHandle;
    private static Logger Log = Global.getLogger(GameConfig.class);

    private static boolean sound = false;
    private static boolean music = false;
    private static boolean isDebugDraw = false;

    {
        init();
    }

    private GameConfig() {
    }

    private void init() {
        fileHandle = Gdx.files.internal(FILE_PATH);
        if (fileHandle.exists()) {
            load();
        } else {
            Log.info("Default setting file not found");
            setupDefaults();
        }
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

    private void load() {
        try {

            JsonReader reader = new JsonReader();
            JsonValue root = reader.parse(fileHandle);
            sound = root.getBoolean(SOUND_TAG, DEFAULT_SOUND);
            music = root.getBoolean(MUSIC_TAG, DEFAULT_MUSIC);
            isDebugDraw = root.getBoolean(IS_DEBUG_DRAW_TAG, DEFAULT_IS_DEBUG_DRAW);


        } catch (Exception e) {
            Log.error("Error loading setting " + FILE_PATH + " using defaults. ", e);
        }


    }

    private void setupDefaults() {
        sound = DEFAULT_SOUND;
        music = DEFAULT_MUSIC;
        isDebugDraw = DEFAULT_IS_DEBUG_DRAW;
    }

}
