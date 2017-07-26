package ru.chertenok.games.rtype.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;
import ru.chertenok.games.rtype.R_Type;

/**
 * Created by 13th on 18-Jul-17.
 */
public class Asteroid extends GameInnerObject {
    final static int DAMAGE = 20;
    final static int SCOPE = 20;
    final static int MAX_LIVE = 20;
    final static float SCALE = 0.5f;
    public static int MAX_ANGLE_INC = 20;
    public static float maxScale = 0.5f;

    public Asteroid() {
        scope = SCOPE;
        damage = DAMAGE;
    }

    @Override
    public int getScope() {
        return super.getScope() * ((int) ((scale - 0.5f) / 0.5f) + 1);
    }

    @Override
    public int getDamage() {
        return super.getDamage() * (int) ((scale - 0.5f) / 0.5f);
    }

    @Override
    public Rectangle getHitAreaRectangle() {
        rectangle = super.getHitAreaRectangle();
        rectangle.height -= 20;
        rectangle.width -= 20;
        rectangle.x += 10;
        rectangle.y += 10;
        return rectangle;
    }

    @Override
    public boolean hitIsRemove(R_Type game, Collisionable collisionObject) {
        if (collisionObject.getObjectType() == ObjectType.BulletAI || collisionObject.getObjectType() == ObjectType.BulletPlayer) {
            live -= collisionObject.getDamage();
            if (live < 0) {
                setActive(false);
                game.explosions.addExplosion(position.x, position.y, scale);
                // если пуля игрока, то очки начисляем
                if (collisionObject.getObjectType() == ObjectType.BulletPlayer) {
                    game.scope += getScope();
                    game.messages.addMessage("+" + getScope(), position.x + originSpriteSize.x * scale, position.y + originSpriteSize.y * scale,
                            1f, Color.GRAY);
                }

                return true;
            }

        } else {
            //   if (collisionObject.getObjectType() == ObjectType.ShipControl)  collisionObject = game.shipControl.getGa

            collisionsObject(this, (GameInnerObject) collisionObject);
        }
        return false;
    }


    @Override
    public ObjectType getObjectType() {
        return ObjectType.Asteroid;
    }


    @Override
    public void reset() {
        super.reset();
        rectangle.setPosition(0, 0);
        position.set(0, 0);
        velocity.set(0, 0);
        this.originSpriteSize = originSpriteSize;
        this.scale = scale;
        scope = DAMAGE;
        damage = DAMAGE;
        scale = SCALE;
        live = (int) (MAX_LIVE * scale);
        isActive = false;
    }

    public static float getMaxScale() {
        return maxScale;
    }

    public static void setMaxScale(float maxScale) {
        Asteroid.maxScale = maxScale;
    }
}
