package ru.chertenok.games.rtype.entity;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;
import ru.chertenok.games.rtype.Collisionable;
import ru.chertenok.games.rtype.ObjectOwner;

/**
 * Created by 13th on 14-Jul-17.
 */
public abstract class GameInnerObject implements Collisionable, Pool.Poolable {
    public Vector2 position;
    public Vector2 velocity;
    public Vector2 originSpriteSize;
    public Vector2 vector = new Vector2();
    public float scale = 1;
    public boolean reversive = false;
    public ObjectOwner owner = ObjectOwner.System;
    public int live;
    public float angle;
    public float angleInc;
    public boolean isActive = true;
    public boolean isFixOnScreen = false;
    protected Rectangle rectangle = new Rectangle();
    protected Circle circle;
    protected int scope = 0;
    protected int damage = 0;
    public float maxSpeed = 200;

    public GameInnerObject() {
        this.position = new Vector2();
        this.velocity = new Vector2();
        this.originSpriteSize = new Vector2();
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public int getScope() {
        return scope;
    }

    public void setScope(int scope) {
        this.scope = scope;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public void update(float dt) {
        update(dt, -1, -1, -1);
    }

    public void update(float dt, float worldWidth, float worldHeight, float maxSpeed) {
        // ограничиваем скорость
        if (velocity.x > maxSpeed && maxSpeed != -1) velocity.x = maxSpeed;
        if (velocity.y > maxSpeed && maxSpeed != -1) velocity.y = maxSpeed;
        // сдвигаем
        position.mulAdd(velocity, -dt);
        //  System.out.printf("upd %f,%f",velocity.x,velocity.y);

        if (isFixOnScreen){
            // отражаем
            if (worldWidth != -1 && (position.x <= 0))
            {
                position.x = 0;
                velocity.x = -velocity.x;
            }

            if ( worldWidth !=-1 &&(position.x >= worldWidth  ))
            {
                position.x = worldWidth;
                velocity.x = - velocity.x;
            }
            if (worldHeight != -1 && (position.y <= 0 ))
            {
                position.y = 0;
                velocity.y = -velocity.y;
            }

            if ( worldHeight !=-1 &&(position.y >= worldHeight  ))
            {
                position.y = worldHeight;
                velocity.y = - velocity.y;
            }



        } else {
            // если за пределами экрана, то вырубаем
            if (worldHeight != -1 && ((position.x < -originSpriteSize.x * scale * 2) ||
                    (position.x > worldWidth + originSpriteSize.x * scale * 2) ||
                    (position.y < -originSpriteSize.y * scale * 2) || (position.y > worldHeight + originSpriteSize.y * scale * 2))) {
                setActive(false);
            }
        }
        // крутим
        angle += angleInc * dt;
    }

    @Override
    public void reset() {
        position.set(0, 0);
        velocity.set(0, 0);
        scale = 1;
        reversive = false;
        owner = ObjectOwner.System;
        live = 0;
        angle = 0;
        angleInc = 0;
        isActive = false;
        isFixOnScreen = false;
    }


    @Override
    public Rectangle getHitAreaRectangle() {
        return rectangle.set(position.x - originSpriteSize.x * scale / 2, position.y - originSpriteSize.y * scale / 2, originSpriteSize.x * scale, originSpriteSize.y * scale);
    }

    @Override
    public HitAreaType getHitAreaType() {
        return HitAreaType.Rectangle;
    }

    @Override
    public Circle getHitAreaCircle() {
        return null;
    }

    @Override
    public void setNoActive() {
        setActive(false);
    }

    @Override
    public boolean isCollisinable() {
        return true;
    }

    // разбегаем объекты в разные стороны
    public void collisionsObject(GameInnerObject o1,GameInnerObject o2, boolean fistOnly)
    {
        vector.set(o1.position);
        o1.velocity.sub(vector.sub(o2.position));
        checkSpeedVector(o1.velocity);
        if (!fistOnly) {
            vector.set(o2.position);
            o2.velocity.sub(vector.sub(o1.position));
            checkSpeedVector(o2.velocity);
        }
    }


    public void checkSpeedVector(Vector2 velocity){
        if (velocity.x > maxSpeed) velocity.x = maxSpeed;
        if (velocity.y > maxSpeed) velocity.y = maxSpeed;
    }


}
