package ru.chertenok.games.rtype;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import ru.chertenok.games.rtype.assests_maneger.Global;
import ru.chertenok.games.rtype.config.GameConfig;
import ru.chertenok.games.rtype.screens.game.GameScreenController;

/**
 * Created by 13th on 02.07.2017.
 */
public class FonStars extends Sprites {

    // кол-во звёзд
    private final int START_COUNT = GameConfig.STAR_COUNT;
    // массив звезд
    private Star[] stars = new Star[START_COUNT];
    // движени звезд
    private boolean isStop = false;
    // animation
    private int targetSpeed = 0;
    public FonStars(GameScreenController game) {
        super(game, "star", 3);
        this.game = game;

        // создаём звезды
        for (int i = 0; i < START_COUNT; i++) {
            stars[i] = new Star();
            if (i < GameConfig.BIG_STAR_COUNT) stars[i].textureNo = 0;
            else if (i < GameConfig.MIDDLE_STAR_COUNT + GameConfig.BIG_STAR_COUNT) stars[i].textureNo = 1;
            else stars[i].textureNo = 2;
            initStar(i, true);
        }
    }

    public boolean isStop() {
        return isStop;
    }

    public void setStop(boolean stop) {
        isStop = stop;
        targetSpeed = 0;
    }

    private void initStar(int i, boolean start) {
        stars[i].position.set((start) ? Global.rnd.nextInt((int) GameConfig.getWorldWidth()) : GameConfig.getWorldWidth(), Global.rnd.nextInt((int) GameConfig.getWorldHeight()));
        stars[i].alpha = 1;//Global.rnd.nextFloat();
        if (stars[i].textureNo == 2) {
            stars[i].color = Color.WHITE;
        } else {
            int g = Global.rnd.nextInt(3);
            switch (g) {
                case 0: {
                    stars[i].color = Color.NAVY;
                    break;
                }
                case 1: {
                    stars[i].color = Color.YELLOW;
                    break;
                }
                case 2: {
                    stars[i].color = Color.WHITE;
                    break;
                }
            }
        }

        // Разные скорости для звезд разного размера
        switch (stars[i].textureNo) {
            case 0: {
                stars[i].vx = Global.rnd.nextFloat() * 50 + GameConfig.BIG_STAR_SPEED;
                break;
            }
            case 1: {
                stars[i].vx = Global.rnd.nextFloat() * 50 + GameConfig.MIDDLE_STAR_SPEED;
                break;
            }
            case 2: {
                stars[i].vx = Global.rnd.nextFloat() * GameConfig.LITTLE_STAR_SPEED + GameConfig.LITTLE_STAR_SPEED;
                break;
            }
        }


    }

    public void update(float dt) {

        if (!isStop) {
            for (int i = 0; i < START_COUNT; i++) {
                stars[i].position.x -= stars[i].vx * dt;
                if (stars[i].position.x < -spriteSize) initStar(i, false);
            }
        }
    }

    public void render(SpriteBatch batch) {
        for (int i = 0; i < START_COUNT; i++) {
            batch.setColor(stars[i].color);
            batch.draw(texture[stars[i].textureNo], stars[i].position.x, stars[i].position.y);
            batch.setColor(Color.WHITE);
        }
    }

    // звезда
    class Star {
        Vector2 position;
        // ускорение по х
        float vx;
        // прозрачность
        float alpha;
        // номер спрайта
        int textureNo;
        // color
        Color color;

        public Star() {
            position = new Vector2();
        }
    }

}
