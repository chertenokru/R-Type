package ru.chertenok.games.rtype.entity;

import com.badlogic.gdx.math.Circle;
import ru.chertenok.games.rtype.Collisionable;
import ru.chertenok.games.rtype.R_Type;

public class Boss extends GameInnerObject {
    private Circle circle = new Circle();
    public float koef = 1;


    @Override
    public ObjectType getObjectType() {
        return ObjectType.Boss;
    }

    @Override
    public void update(float dt, float worldWidth, float worldHeight, float maxSpeed) {
        super.update(dt, worldWidth, worldHeight, maxSpeed);
    }

    @Override
    public HitAreaType getHitAreaType() {
        return HitAreaType.Circle;
    }

    @Override
    public Circle getHitAreaCircle() {
        circle.set(position.x, position.y, originSpriteSize.y / 2 * koef);
        return circle;
    }

    @Override
    public boolean hitStatus_and_IsRemove(R_Type game, Collisionable collisionObject, boolean isCollision) {
        if (isHit != isCollision) setHit(isCollision);
        if (!isCollision) return false;
        return false;
    }


    @Override
    public void update(float dt) {
        super.update(dt);
    }

}
