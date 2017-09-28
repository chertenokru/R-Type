package ru.chertenok.games.rtype.level;

import com.badlogic.gdx.utils.Logger;
import ru.chertenok.games.rtype.config.GameConfig;

import java.io.IOException;
import java.util.Map;

/**
 * Created by 13th on 07.07.2017.
 */
public class Level {

    private static Logger log = new Logger(Level.class.getSimpleName(), Logger.DEBUG);
    // массив событий
    LevelEvents levelEvents;
    // private GameScreen game;
    private Map<String, ILevelEvent> eventMap;
    //начальный счётчик
    private float dtLevetInit = 7 * 60;
    // куда будет меняться
    private float stepVector = -1;

    public Level(Map<String, ILevelEvent> eventMap) {
        this.eventMap = eventMap;

        levelEvents = new LevelEvents();
        try {
            levelEvents.loadLevel(GameConfig.LEVEL1_FILE_PATH);
            stepVector = levelEvents.getStepVector();
            dtLevetInit = levelEvents.getDtLevetInitTime();
        } catch (IOException e) {
            log.error(" LevelEvents " + GameConfig.LEVEL1_FILE_PATH + " not load.", e);
            e.printStackTrace();
        }
    }


    public float getDtLevetInit() {
        return dtLevetInit;
    }

    public float getStepVector() {
        return stepVector;
    }

    private void execAction(LevelEvents.LevelEvent event) {

        log.debug("eA: " + eventMap.keySet().toString());
        if (eventMap.containsKey(event.Name)) {
            log.debug("eA: " + event.Name);
            ILevelEvent le = eventMap.get(event.Name);
            if (le != null) le.event(event);
        }


    }

    public void update(float counter) {
        //log.debug("counter: "+counter);
        for (int i = 0; i < levelEvents.getLevelEventList().size(); i++) {
            if ((levelEvents.getLevelEventList().get(i).isActive && levelEvents.getLevelEventList().get(i).time > counter && stepVector == -1)
                    || (levelEvents.getLevelEventList().get(i).isActive && levelEvents.getLevelEventList().get(i).time < counter && stepVector == 1)) {
                log.debug("event: " + levelEvents.getLevelEventList().get(i).Name);
                execAction(levelEvents.getLevelEventList().get(i));
                levelEvents.getLevelEventList().get(i).isActive = false;
                return;
            }

        }

    }

    public void reset() {
        for (int i = 0; i < levelEvents.getLevelEventList().size(); i++) {
            levelEvents.getLevelEventList().get(i).isActive = true;
        }
    }


    public interface ILevelEvent {
        void event(LevelEvents.LevelEvent event);

        void registerLevelEvents(Map<String, ILevelEvent> eventMap);
    }


}
