package ru.chertenok.games.rtype.objects;

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
            if (live < 0) {
                setActive(false);
                return true;
            }
        }
        return false;
    }


    @Override
    public ObjectType getObjectType() {
        return ObjectType.Enemy;
    }
}
