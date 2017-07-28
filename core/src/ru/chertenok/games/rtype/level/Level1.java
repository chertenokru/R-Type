package ru.chertenok.games.rtype.level;

import com.badlogic.gdx.graphics.Color;
import ru.chertenok.games.rtype.R_Type;

import java.util.ArrayList;

/**
 * Created by 13th on 07.07.2017.
 */
public class Level1 {
    class Data {
        float time = 0;
        boolean isActive = true;

        public Data(float time) {
            this.time = time;
        }
    }

    private R_Type game;
    //начальный счётчик
    private float dtLevetInit = 7 * 60;
    // куда будет меняться
    private float stepVector = -1;
    // массив событий
    ArrayList<Data> actions = new ArrayList<Data>();


    public float getDtLevetInit() {
        return dtLevetInit;
    }

    public float getStepVector() {
        return stepVector;
    }


    private void execAction(int index) {
        switch (index) {
            case 0: {
                game.asteroids.setObjectCount(5);
                game.asteroids.setReversiveEnabled(false);
                game.enemies.setActiveObject_count(5);
                game.asteroids.setMaxScale(1f);
                game.asteroids.setFixMaxScale(false);
                break;
            }
            case 1: {
                game.asteroids.setObjectCount(7);
                game.asteroids.setReversiveEnabled(true);
                game.asteroids.setFixOnScreen(true);
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
                if (game.isAndroid)
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

        }
    }

    public Level1(R_Type game) {
        this.game = game;

        actions.add(new Data(7.0f)); // 0
        actions.add(new Data(6.7f)); // 1
        actions.add(new Data(4.5f)); // 2
        actions.add(new Data(4.45f)); // 3
        actions.add(new Data(4f)); // 4
        actions.add(new Data(3f)); // 5
        actions.add(new Data(2.8f)); // 6
        actions.add(new Data(2.0f)); // 7

    }

    public void update(float counter) {

        for (int i = 0; i < actions.size(); i++) {
            if ((actions.get(i).isActive && actions.get(i).time > counter && stepVector==-1)||(actions.get(i).isActive && actions.get(i).time < counter && stepVector==1 ))
            {
                execAction(i);
                actions.get(i).isActive = false;
                break;
            }

        }

    }


    public void reset(){
        for (int i = 0; i < actions.size(); i++) {
                actions.get(i).isActive = true;
            }

        }


}
