package ru.chertenok.games.rtype.entity.collections;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import ru.chertenok.games.rtype.entity.Explosion;
import ru.chertenok.games.rtype.entity.GameInnerObject;
import ru.chertenok.games.rtype.screens.GameScreen;

/**
 * Created by 13th on 04-Jul-17.
 */
public class Explosions extends ru.chertenok.games.rtype.entity.collections.ObjectCollector {

    private static final float dtWait = 0.08f;
    private static final int All_STAGE = 8;
    private Explosion explosion;

    public Explosions(GameScreen game) throws Exception {
        super(Explosion.class,game,"explosion",3);
    }

    public void addExplosion(float x, float y) {
       addExplosion(x,y,0.5f);
    }

    public void addExplosion(float x, float y,float scale)
    {
        Explosion ex = (Explosion) objectPool.obtain();
        ex.init(x,y,scale);
        init(ex,spriteSizeX,spriteSizeY);
        activeObject.add(ex);
        }

    @Override
    public void update(float dt) {
        for (int i = 0 ; i < activeObject.size;i++){

            explosion = (Explosion) activeObject.get(i);
            explosion.counterDT += dt;
            if (explosion.counterDT > dtWait) {
                explosion.stage++;
                explosion.counterDT = 0;
                explosion.angle +=30;
            }

            if (explosion.stage >= All_STAGE) {
                activeObject.removeIndex(i);
                objectPool.free(explosion);
            }
        }
    }

    public void render(SpriteBatch batch) {
        for (GameInnerObject ex : activeObject) {
            explosion = (Explosion)ex;
            if (explosion.stage>= textureCount)
                batch.setColor(1,1,1,1.00f-(All_STAGE-explosion.stage-(All_STAGE-explosion.stage)*0.16f));

            batch.draw(texture[(explosion.stage < textureCount)?explosion.stage: textureCount-1],ex.position.x - spriteOriginSize*ex.scale / 2,
                    ex.position.y - spriteOriginSize*ex.scale / 2,spriteOriginSize*ex.scale / 2, spriteOriginSize*ex.scale / 2,
                    spriteOriginSize*ex.scale, spriteOriginSize*ex.scale, 1, 1, ex.angle);
        }
    }
}


