package ru.chertenok.games.rtype.entity;

import com.badlogic.gdx.math.Vector2;
import ru.chertenok.games.rtype.Collisionable;
import ru.chertenok.games.rtype.config.ObjectOwner;
import ru.chertenok.games.rtype.screens.GameScreenController;

public class Bullet extends GameInnerObject {
    public Vector2 startPosition;
    public ru.chertenok.games.rtype.entity.collections.Bullets.BulletsType type = ru.chertenok.games.rtype.entity.collections.Bullets.BulletsType.Type1;
    public int textureNo;

    public float dtWait = 0.1f;
    public float dtCouner = 0;

    public Bullet() {
        this.position = new Vector2(0, 0);
        this.startPosition = new Vector2(0, 0);
        velocity = new Vector2(0, 0);
        type = ru.chertenok.games.rtype.entity.collections.Bullets.BulletsType.Type1;
        setDamage(21);
        setScore(0);


    }

    public void activate(float x, float y, float dx, float dy, ObjectOwner owner, ru.chertenok.games.rtype.entity.collections.Bullets.BulletsType type) {
        position.set(x, y);
        startPosition.set(x, y);
        velocity.set(dx, dy);
        this.owner = owner;
        this.textureNo = type.ordinal();
        isActive = true;
        this.type = type;
        scale = 1;
    }


    @Override
    public void reset() {
        isActive = false;
        position.set(0, 0);
        startPosition.set(0, 0);
        velocity.set(0, 0);
        angle = 0;
    }



    @Override
    public boolean hitStatus_and_IsRemove(GameScreenController game, Collisionable collisionObject, boolean isCollision) {
        if (isHit != isCollision) setHit(isCollision);
        if (!isCollision) return false;
        if ((collisionObject.getObjectType() == ObjectType.BulletPlayer && owner == ObjectOwner.AI) ||
                (collisionObject.getObjectType() == ObjectType.BulletAI && owner == ObjectOwner.Gamer)){
            setActive(false);
            if (collisionObject.getObjectType() == ObjectType.BulletPlayer)
                game.explosions.addExplosion(position.x + originSpriteSize.x / 2, position.y + originSpriteSize.y / 2, 0.25f);
            return true;
        }

        if ((collisionObject.getObjectType() == ObjectType.Asteroid ) ||
                (collisionObject.getObjectType() == ObjectType.Enemy && owner == ObjectOwner.Gamer))
        {
            setActive(false);

            return true;
        }

        if (collisionObject.getObjectType() == ObjectType.Ship && owner == ObjectOwner.AI)
        {
            setActive(false);

            return true;
        }

        return false;
    }

    @Override
    public ObjectType getObjectType() {
        return (owner == ObjectOwner.AI) ? ObjectType.BulletAI : ObjectType.BulletPlayer;
    }


}
