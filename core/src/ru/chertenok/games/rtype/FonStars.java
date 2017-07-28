package ru.chertenok.games.rtype;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by 13th on 02.07.2017.
 */
public class FonStars extends Sprites{

    // звезда
    class Star {
        Vector2 position;
        // ускорение по х
        float vx;
        // прозрачность
        float alpha;
        // номер спрайта
        int textureNo;

        public Star() {
            position = new Vector2();
        }
    }


    // кол-во звёзд
    private final int START_COUNT = 80;
    // массив звезд
    private Star[] stars = new Star[START_COUNT];
    // движени звезд
    private boolean isStop = false;


    public boolean isStop() {
        return isStop;
    }

    public void setStop(boolean stop) {
        isStop = stop;
    }

    public FonStars(R_Type game) {
        super(game,"star",3);
        this.game = game;

        // создаём звезды
        for (int i = 0; i < START_COUNT; i++) {
            stars[i] = new Star();
            if (i<10) stars[i].textureNo = 0;
            else if (i<20) stars[i].textureNo = 1;
            else  stars[i].textureNo = 2;
            initStar(i,true);
        }
    }


    private void initStar(int i,boolean start) {
        stars[i].position.set( (start)? Global.rnd.nextInt((int)game.viewport.getWorldWidth()):game.viewport.getWorldWidth(), Global.rnd.nextInt((int)game.viewport.getWorldHeight()));
        stars[i].alpha = Global.rnd.nextFloat();
        // Разные скорости для звезд разного размера
        switch (stars[i].textureNo){
            case 0:{
                stars[i].vx = Global.rnd.nextFloat() * 10 + 15;
                break;
            }
            case 1:{
                stars[i].vx = Global.rnd.nextFloat() * 10 + 150;
                break;
            }
            case 2:{
                stars[i].vx = Global.rnd.nextFloat()*200+200;
                break;
            }
        }


    }



    public void update(float dt){

        if (!isStop) {
            for (int i = 0; i < START_COUNT; i++) {
                stars[i].position.x -= stars[i].vx * dt;
                if (stars[i].position.x < -spriteSize) initStar(i, false);
            }
        }
    }

    public void render(SpriteBatch batch){
        for (int i = 0; i < START_COUNT; i++) {
            batch.draw(texture[stars[i].textureNo],stars[i].position.x,stars[i].position.y);
        }
    }

}
