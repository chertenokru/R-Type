package ru.chertenok.games.rtype.level;

import com.badlogic.gdx.utils.Logger;
import ru.chertenok.games.rtype.config.GameConfig;

import java.io.IOException;
import java.util.Map;

/**
 * Created by 13th on 07.07.2017.
 */
public class Level1 {

    private static Logger log = new Logger(Level1.class.getSimpleName(), Logger.DEBUG);
    // массив событий
    LevelEvents levelEvents;
    // private R_Type game;
    private Map<String, ILevelEvent> eventMap;
    //начальный счётчик
    private float dtLevetInit = 7 * 60;
    // куда будет меняться
    private float stepVector = -1;

    public Level1(Map<String, ILevelEvent> eventMap) {
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

        if (eventMap.containsKey(event.Name)) {
            ILevelEvent le = eventMap.get(event.Name);
            if (le != null) le.event(event);
        }


/*                                game.asteroids.setObjectCount(5);
                game.asteroids.setReversiveEnabled(false);
                game.enemies.setActiveObject_count(5);
                game.asteroids.setMaxScale(1f);
                game.asteroids.setFixMaxScale(false);
                break;
            }
            case 1: {
                game.asteroids.setObjectCount(0);
                game.asteroids.setReversiveEnabled(true);
                game.asteroids.setFixOnScreen(true);
                game.enemies.setActiveObject_count(0);
                game.setBossMode(true);
                break;
            }
            case 2: {
                game.asteroids.setObjectCount(10);
                game.shipControl.setEnableFire(false);
                game.explosions.addExplosion(game.shipControl.ship.position.x, game.shipControl.ship.position.y);
                game.messages.addMessage("             Командир, плохая новость.", 150, 540, 0.05f, Color.WHITE,Color.GRAY);
                game.messages.addMessage("Астероид повредил пушку,нужно время на ремонт.", 150, 500, 0.05f, Color.WHITE,Color.GRAY);
                game.messages.addMessage("             Уклоняйтесь от астероидов ...", 150, 460, 0.05f, Color.WHITE,Color.GRAY);
                game.state = R_Type.GameState.Pause;
                break;
            }
            case 3: {
                game.shipControl.setRechargeEnabled(true);
                game.messages.addMessage("        Но есть и хорошая новость ! ИИ научился ", 150, 540, 1, Color.WHITE,Color.GRAY);
                game.messages.addMessage("перезаряжать щиты корабля собранной от взрывов энергией", 100, 500, 1, Color.WHITE,Color.GRAY);
                if (Global.isAndroid())
                    game.messages.addMessage("            Для перезарядки щитов нажмите на иконку шита ", 150, 460, 1, Color.WHITE,Color.GRAY);
                else
                    game.messages.addMessage("            Для автоперезарядки щитов нажмите 1 ", 150, 460, 1, Color.WHITE,Color.GRAY);
                game.state = R_Type.GameState.Pause;
                break;
            }
            case 4: {
                game.asteroids.setObjectCount(20);
                break;
            }
            case 5: {
                //game.asteroids.setEnemys_count(30);
                game.asteroids.setMaxScale(1);
                break;
            }
            case 6: {
                game.shipControl.setEnableFire(true);
                game.messages.addMessage("Командир, орудие исправно !", 200, 540, 2, Color.WHITE,Color.GRAY);
                game.state = R_Type.GameState.Pause;
                break;
            }
            case 7: {
                game.asteroids.setObjectCount(30);
                game.asteroids.setFixMaxScale(true);

                break;
            }

*/

    }

    public void update(float counter) {

        for (int i = 0; i < levelEvents.getLevelEventList().size(); i++) {
            if ((levelEvents.getLevelEventList().get(i).isActive && levelEvents.getLevelEventList().get(i).time > counter && stepVector == -1)
                    || (levelEvents.getLevelEventList().get(i).isActive && levelEvents.getLevelEventList().get(i).time < counter && stepVector == 1))
            {
                execAction(levelEvents.getLevelEventList().get(i));
                levelEvents.getLevelEventList().get(i).isActive = false;
                break;
            }

        }

    }

    public void reset(){
        for (int i = 0; i < levelEvents.getLevelEventList().size(); i++) {
            levelEvents.getLevelEventList().get(i).isActive = true;
        }
    }


    public interface ILevelEvent {
        void event(LevelEvents.LevelEvent event);
    }


}
