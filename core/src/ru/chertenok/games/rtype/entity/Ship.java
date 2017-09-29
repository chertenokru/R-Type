package ru.chertenok.games.rtype.entity;

import com.badlogic.gdx.math.Rectangle;
import ru.chertenok.games.rtype.Collisionable;
import ru.chertenok.games.rtype.screens.GameScreenController;


public class Ship extends ru.chertenok.games.rtype.entity.GameInnerObject {

    private int energy;
    private boolean isDamaging = false;

    public int getEnergy() {
        return energy;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public Ship() {
        damage = 21;

    }


    public boolean isDamaging() {
        return isDamaging;
    }

    public void setDamaging(boolean damaging) {
        isDamaging = damaging;
    }


    @Override
    public ObjectType getObjectType() {
        return ObjectType.Ship;
    }

    @Override
    public Rectangle getHitAreaRectangle() {
        rectangle = super.getHitAreaRectangle();
        rectangle.y += 15;
        rectangle.height -= 30;
        return rectangle;
    }

    @Override
    public boolean hitStatus_and_IsRemove(GameScreenController game, Collisionable collisionObject, boolean isCollision) {
        if (isHit != isCollision) setHit(isCollision);
        if (!isCollision) return false;

        if (!collisionObject.isHit() && collisionObject.getObjectType() == ObjectType.BulletAI || collisionObject.getObjectType() == ObjectType.Asteroid || collisionObject.getObjectType() == ObjectType.Enemy) {
            energy -= collisionObject.getDamage();
            setDamaging(true);
            if (collisionObject.getObjectType() != ObjectType.BulletAI)
                collisionsObject(this, (ru.chertenok.games.rtype.entity.GameInnerObject) collisionObject, true);

        }
        return false;
    }
}
