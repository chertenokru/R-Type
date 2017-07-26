package ru.chertenok.games.rtype;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

/**
 * Created by 13th on 02.07.2017.
 */
public class FonGround extends Sprites{

    private final int MAX_HEIGHT = 3;
    private final float vx = 10f;
    private float shift = 0f;
    int[] ground;
    private boolean isActive = false;


    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public FonGround(R_Type game) {
        super(game,"block",1);

        ground = new int[(((int)game.viewport.getWorldWidth() / spriteOriginSize) + 1)];
        for (int i = 0; i < ground.length; i++) {
            ground[i] = Global.rnd.nextInt(MAX_HEIGHT) + 1;
        }
    }

    public void render(SpriteBatch batch) {
        if (isActive) {
            for (int w = 0; w < ground.length; w++) {
                for (int h = 0; h < ground[w]; h++) {
                    batch.draw(texture[spriteSize-1], w * spriteOriginSize - shift, h * spriteOriginSize );
                }
            }
        }
    }


    public void update(float dt) {
        if (isActive) {
            shift += vx * dt;
            if (shift > spriteOriginSize ) {
                copyAndAdd();
                shift = 0;
            }
        }
    }

    private void copyAndAdd() {
        for (int i = 1; i < ground.length; i++) {
            ground[i - 1] = ground[i];
        }
        ground[ground.length - 1] = Global.rnd.nextInt(MAX_HEIGHT) + 1;
    }


    public boolean checkCollision(Rectangle rectangle) {
        boolean res = false;
        Rectangle rectangle1 = new Rectangle();
        for (int i = 0; i < ground.length; i++) {
            rectangle1.set(i * spriteOriginSize  - shift, i, spriteOriginSize , (ground[i]) * spriteOriginSize );
            if (rectangle.overlaps(rectangle1)) {
                res = true;
                break;
            }
        }
        return res;
    }


    public Rectangle checkCollision(Circle circle) {
        boolean res = false;
        Rectangle rectangle1 = new Rectangle();
        for (int i = 0; i < ground.length; i++) {
            rectangle1.set(i * spriteOriginSize  - shift, i, spriteOriginSize , (ground[i]) * spriteOriginSize );
            if (Intersector.overlaps(circle, rectangle1)) {
                res = true;
                if (ground[i] > 0) ground[i]--;
                return rectangle1;
            }
        }
        return null;
    }


}
