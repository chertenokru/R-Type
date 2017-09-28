package ru.chertenok.games.rtype.entity;

import com.badlogic.gdx.math.Vector2;
import ru.chertenok.games.rtype.Collisionable;
import ru.chertenok.games.rtype.GameScreen;
import ru.chertenok.games.rtype.Global;

public class Explosion extends ru.chertenok.games.rtype.entity.GameInnerObject {
    public int stage;
    public float counterDT = 0;

    public Explosion() {
        position = new Vector2(0,0);
    }

    public void init(float x, float y) {
        init(x,y,0.5f);
    }

    public void init(float x, float y,float scale){
        this.position.set(x,y);
        stage = 0;
        angle = Global.rnd.nextInt(360);
        this.scale = scale;
        this.counterDT=0;
    }





    @Override
    public void reset() {
        position.set(0,0);
        stage = 0;
        counterDT = 0;
        angle = 0;
        scale = 0.5f;

    }



    @Override
    public boolean hitStatus_and_IsRemove(GameScreen game, Collisionable collisionObject, boolean isCollision) {
        if (isHit != isCollision) setHit(isCollision);
        if (!isCollision) return false;
        return false;
    }

    @Override
    public boolean isCollisinable() {
        return false;
    }

    @Override
    public ObjectType getObjectType() {
        return ObjectType.Explosion;
    }

}
