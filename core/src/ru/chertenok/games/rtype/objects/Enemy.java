package ru.chertenok.games.rtype.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Circle;
import ru.chertenok.games.rtype.R_Type;
import ru.chertenok.games.rtype.objects.collections.Enemys;

public class Enemy extends GameInnerObject {
    private Enemys enemys;
    public EnemyType type = EnemyType.Type1;
    public int textureNo = 0;

    public Enemy() {




    }


    @Override
    public boolean hitIsRemove(R_Type game,Collisionable collisionObject) {

        if ( collisionObject.getObjectType() == ObjectType.BulletPlayer) {
            live -= collisionObject.getDamage();
            game.explosions.addExplosion(position.x, position.y, scale);
            if (live < 0) {
                setActive(false);
                // если пуля игрока, то очки начисляем
                    game.scope += getScope();
                    game.messages.addMessage("+" + getScope(), position.x + originSpriteSize.x * scale, position.y + originSpriteSize.y * scale,
                            1f, Color.GRAY);
                return true;
                }

        } else
        {
            if  (collisionObject.getObjectType() != ObjectType.BulletAI)
            collisionsObject(this,(GameInnerObject) collisionObject);
        }
        return false;
    }


    @Override
    public ObjectType getObjectType() {
        return ObjectType.Enemy;
    }
}
