package ru.chertenok.games.rtype.objects;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;
import ru.chertenok.games.rtype.ObjectOwner;
import ru.chertenok.games.rtype.R_Type;
import ru.chertenok.games.rtype.objects.Collisionable;
import ru.chertenok.games.rtype.objects.GameInnerObject;
import ru.chertenok.games.rtype.objects.collections.Bullets;

public class Bullet extends GameInnerObject {
    public Vector2 startPosition;
    public Bullets.BulletsType type = Bullets.BulletsType.Type1;
    public int textureNo;

    public float dtWait = 0.1f;
    public float dtCouner = 0;

    public Bullet() {
        this.position = new Vector2(0, 0);
        this.startPosition = new Vector2(0, 0);
        velocity = new Vector2(0, 0);
        type = Bullets.BulletsType.Type1;
        setDamage(21);
        setScope(0);


    }

    public void activate(float x, float y, float dx, float dy, ObjectOwner owner, Bullets.BulletsType type) {
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
    public boolean hitIsRemove(R_Type game,Collisionable collisionObject) {
        if ((collisionObject.getObjectType() == ObjectType.BulletPlayer && owner == ObjectOwner.AI) ||
                (collisionObject.getObjectType() == ObjectType.BulletAI && owner == ObjectOwner.Gamer)){
            setActive(false);
            if (collisionObject.getObjectType() == ObjectType.BulletPlayer)
                game.explosions.addExplosion(position.x + originSpriteSize.x / 2, position.y + originSpriteSize.y / 2, 0.25f);
            return true;
        }

        if ((collisionObject.getObjectType() == ObjectType.Asteroid && owner == ObjectOwner.Gamer) ||
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
