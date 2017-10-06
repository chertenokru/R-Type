package ru.chertenok.games.rtype.entity;

import com.badlogic.gdx.graphics.Color;
import ru.chertenok.games.rtype.collisions.Collisionable;
import ru.chertenok.games.rtype.config.EnemyType;
import ru.chertenok.games.rtype.entity.collections.Enemies;
import ru.chertenok.games.rtype.screens.game.GameScreenController;

public class Enemy extends ru.chertenok.games.rtype.entity.GameInnerObject {
    private Enemies enemies;
    public ru.chertenok.games.rtype.config.EnemyType type = EnemyType.Type1;
    public int textureNo = 0;

    public Enemy() {
    }



    @Override
    public boolean hitStatus_and_IsRemove(GameScreenController game, Collisionable collisionObject, boolean isCollision) {
        if (isHit != isCollision) setHit(isCollision);
        if (!isCollision) return false;
        if ( collisionObject.getObjectType() == ObjectType.BulletPlayer) {
            live -= collisionObject.getDamage();
            game.explosions.addExplosion(position.x, position.y, scale);
            if (live < 0) {
                setActive(false);
                // если пуля игрока, то очки начисляем
                game.setScore(game.getScore() + getScore());
                game.messages.addMessage("+" + getScore(), position.x + originSpriteSize.x * scale, position.y + originSpriteSize.y * scale,
                        1f, Color.GRAY);
                return true;
            }

        } else
        {
            if  (collisionObject.getObjectType() != ObjectType.BulletAI && collisionObject.getObjectType() != ObjectType.Asteroid
                    && collisionObject.getObjectType() != ObjectType.Enemy)
                collisionsObject(this, (ru.chertenok.games.rtype.entity.GameInnerObject) collisionObject, true);
        }
        return false;

    }

    @Override
    public ObjectType getObjectType() {
        return ObjectType.Enemy;
    }
}
