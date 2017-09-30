package ru.chertenok.games.rtype.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import ru.chertenok.games.rtype.Collisionable;
import ru.chertenok.games.rtype.screens.game.GameScreenController;

/**
 * Created by 13th on 18-Jul-17.
 */
public class Asteroid extends GameInnerObject {
    public final static int MAX_ANGLE_INC = 20;
    private final static int DAMAGE = 20;
    private final static int SCOPE = 20;
    private final static int MAX_LIVE = 20;
    private final static float SCALE = 0.5f;
    private static float maxScale = 0.5f;

    public Asteroid() {
        score = SCOPE;
        damage = DAMAGE;
    }


    @Override
    public int getScore() {
        return super.getScore() * ((int) ((scale - 0.5f) / 0.5f) + 1);
    }

    @Override
    public int getDamage() {
        return super.getDamage() * ((int) ((scale - 0.5f) / 0.5f)+1);
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
    public boolean hitStatus_and_IsRemove(GameScreenController game, Collisionable collisionObject, boolean isCollision) {
        if (isHit != isCollision) setHit(isCollision);
        if (!isCollision) return false;

        if (collisionObject.getObjectType() == ObjectType.BulletAI || collisionObject.getObjectType() == ObjectType.BulletPlayer) {
            live -= collisionObject.getDamage();
            if (live < 0) {
                setActive(false);
                game.explosions.addExplosion(position.x, position.y, scale);
                // если пуля игрока, то очки начисляем
                if (collisionObject.getObjectType() == ObjectType.BulletPlayer) {
                    game.setScore(game.getScore() + getScore());
                    game.messages.addMessage("+" + getScore(), position.x + originSpriteSize.x * scale, position.y + originSpriteSize.y * scale,
                            1f, Color.GRAY);
                }

                return true;
            }

        } else {
            //   if (collisionObject.getObjectType() == ObjectType.ShipControl)  collisionObject = game.shipControl.getGa

            collisionsObject(this, (GameInnerObject) collisionObject,true);
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
        score = DAMAGE;
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
