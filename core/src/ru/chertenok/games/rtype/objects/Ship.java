package ru.chertenok.games.rtype.objects;

import com.badlogic.gdx.math.Rectangle;
import ru.chertenok.games.rtype.R_Type;

public class Ship extends GameInnerObject {

    public int energy;
    private boolean isDamaging = false;


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
    public boolean hitIsRemove(R_Type game, Collisionable collisionObject) {
        if (collisionObject.getObjectType() == ObjectType.BulletAI || collisionObject.getObjectType() == ObjectType.Asteroid || collisionObject.getObjectType() == ObjectType.Enemy) {
            energy -= collisionObject.getDamage();
            setDamaging(true);
            if  (collisionObject.getObjectType() != ObjectType.BulletAI )
                collisionsObject(this,(GameInnerObject) collisionObject,true);

        }
        return false;
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
}
