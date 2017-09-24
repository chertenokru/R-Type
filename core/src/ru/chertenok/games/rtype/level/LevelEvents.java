package ru.chertenok.games.rtype.level;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.Logger;
import ru.chertenok.games.rtype.config.GameConfig;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LevelEvents {


    private static FileHandle fileHandle;
    private static Logger log = new Logger(LevelEvents.class.getSimpleName(), Logger.DEBUG);

    // list of events
    private List<LevelEvent> levelEventList = new ArrayList<LevelEvent>();
    //начальный счётчик
    private float dtLevetInitTime;
    // куда будет меняться
    private float stepVector;

    public List<LevelEvent> getLevelEventList() {
        return levelEventList;
    }

    public float getDtLevetInitTime() {
        return dtLevetInitTime;
    }

    public float getStepVector() {
        return stepVector;
    }

    public void loadLevel(String levelFileName) throws IOException {
        fileHandle = Gdx.files.internal(levelFileName);
        if (!fileHandle.exists()) {
            log.error("Level file - " + levelFileName + " not found");
            throw new IOException("Level file - " + levelFileName + "not found");
        } else {
            log.debug("Loading level...");

            JsonReader reader = new JsonReader();
            JsonValue root = reader.parse(fileHandle);
            Json json = new Json();
            dtLevetInitTime = root.getFloat(GameConfig.START_TIME_TAG);
            stepVector = root.getFloat(GameConfig.STEP_VECTOR_TAG);
            JsonValue events = root.get(GameConfig.EVENTS_TAG);
            for (int i = 0; i < events.size; i++) {
                levelEventList.add(json.fromJson(LevelEvent.class, events.get(i).toString()));
            }

            for (LevelEvent le : levelEventList) {
                log.debug(le.toString());
            }
        }
    }

    public static class LevelEvent {
        float time;
        String Name;
        String[] param;
        boolean isActive = true;

        @Override
        public String toString() {
            return "Time: " + time + ", name: " + Name + ", params :" + Arrays.toString(param);
        }
    }
}
