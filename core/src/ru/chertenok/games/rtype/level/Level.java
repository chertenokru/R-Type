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
    private float dtLevelInit = 7 * 60;
    // куда будет меняться
    private float stepVector = -1;
    // таймер  отсчёта для изменения игрового процесса
    private float dtLevelCounter = 0;

    public Level(Map<String, ILevelEvent> eventMap) {
        this.eventMap = eventMap;

        levelEvents = new LevelEvents();
        try {
            levelEvents.loadLevel(GameConfig.LEVEL1_FILE_PATH);
            stepVector = levelEvents.getStepVector();
            dtLevelInit = levelEvents.getDtLevetInitTime();
        } catch (IOException e) {
            log.error(" LevelEvents " + GameConfig.LEVEL1_FILE_PATH + " not load.", e);
            e.printStackTrace();
        }
    }


    public float getDtLevelInit() {
        return dtLevelInit;
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

    public void update(float dt) {
        dtLevelCounter += dt * getStepVector();
        //log.debug("counter: "+counter);
        for (int i = 0; i < levelEvents.getLevelEventList().size(); i++) {
            if ((levelEvents.getLevelEventList().get(i).isActive && levelEvents.getLevelEventList().get(i).time > dtLevelCounter / 60 && stepVector == -1)
                    || (levelEvents.getLevelEventList().get(i).isActive && levelEvents.getLevelEventList().get(i).time < dtLevelCounter / 60 && stepVector == 1)) {
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
        dtLevelCounter = getDtLevelInit() * 60;
    }


    public interface ILevelEvent {
        void event(LevelEvents.LevelEvent event);

        void registerLevelEvents(Map<String, ILevelEvent> eventMap);
    }

    public float getDtLevelCounter() {
        return dtLevelCounter;
    }
}
