package ru.chertenok.games.rtype.objects.collections;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import ru.chertenok.games.rtype.Global;
import ru.chertenok.games.rtype.R_Type;
import ru.chertenok.games.rtype.objects.Enemy;
import ru.chertenok.games.rtype.objects.EnemyType;
import ru.chertenok.games.rtype.objects.GameInnerObject;


/**
 * Created by 13th on 02.07.2017.
 */
public class Enemys extends ObjectCollector {



    private final int MAX_ANGLE_INC = 0;
    private final int SCOPE = 20;
    private final int DAMAGE = 20;

    private float waitCounter = 0f;
    private float waitTime = 2.0f;

    private int maxScale = 0;

    private boolean fixMaxScale = false;
    private boolean isReversiveEnabled = false;
    private Enemy enemy;

    private Vector2 vector = new Vector2(0, 0);


    public Enemys(R_Type game) throws Exception {
        super(Enemy.class, game, "alien1", 2);
        maxSpeed = 120;
        minSpeed = 30;
        createWaitDT = 2;
        setActiveObject_count(5);


//        spriteSize = 64;        

        for (int i = 0; i < getActiveObject_count()-1; i++) {
            enemy = (Enemy)objectPool.obtain();
            enemy.textureNo = 0;
            enemy.type=EnemyType.Type1;
            init(enemy,texture[enemy.textureNo].getRegionWidth(),texture[enemy.textureNo].getRegionHeight());
            activeObject.add(enemy);
        }
        enemy = (Enemy)objectPool.obtain();
        enemy.textureNo = 1;
        enemy.type=EnemyType.Type2;
        init(enemy,texture[enemy.textureNo].getRegionWidth(),texture[enemy.textureNo].getRegionHeight());
        activeObject.add(enemy);
    }

    public boolean isReversiveEnabled() {
        return isReversiveEnabled;
    }

    public void setReversiveEnabled(boolean reversiveEnabled) {
        isReversiveEnabled = reversiveEnabled;
    }

    public boolean isFixMaxScale() {
        return fixMaxScale;
    }

    public void setFixMaxScale(boolean fixMaxScale) {
        this.fixMaxScale = fixMaxScale;
    }

    public int getActiveObject_count() {
        return objectCount;
    }

    public void setActiveObject_count(int activeObject_count) {
        objectCount = activeObject_count;
    }


    @Override
    public void update(float dt) {
        waitCounter += dt;
        super.update(dt);
        for (int i = 0; i < activeObject.size; i++) {
            // а не выстрелить ли нам ...
            if (waitCounter > waitTime) {
                if (Global.rnd.nextBoolean()) {
                    // корабль тип 1 по прямой стреляем
                    if (((Enemy) activeObject.get(i)).type == EnemyType.Type1)
                        game.bullets.addBotBullet(activeObject.get(i).position.x - spriteSizeX, activeObject.get(i).position.y);
                    else {
                        // корабль тип 2 стреляем по углу разворота посудины
                        game.bullets.addBotBulletVector(activeObject.get(i).position.x - spriteSizeX / 2, activeObject.get(i).position.y - spriteSizeY / 2,
                                maxSpeed * (float) Math.cos(Math.toRadians(activeObject.get(i).angle)), maxSpeed * (float) Math.sin(Math.toRadians(activeObject.get(i).angle)), activeObject.get(i).angle);
                    }
                }

            }

            // нацеливаем корабль типа 2 на игрока
            if (((Enemy) activeObject.get(i)).type == EnemyType.Type2) {
                activeObject.get(i).angle = (float) Math.toDegrees(
                        Global.rotateTo((float) Math.toRadians(activeObject.get(i).angle),
                                Global.getAngle(activeObject.get(i).position.x, activeObject.get(i).position.y, game.shipControl.ship.position.x, game.shipControl.ship.position.y), (float) Math.toRadians(60), dt)
                );
            }
        }
        // когда все выстрелили
        if (waitCounter > waitTime)   waitCounter = 0;

    }

    @Override
    protected void init(GameInnerObject gameInnerObject,float width,float height) {
        //  enemy.scale  = 0.5f+(rnd.nextFloat()/2);
        enemy = (Enemy) gameInnerObject;
        enemy.reversive = false;
        if (isReversiveEnabled) {
            if (Global.rnd.nextBoolean())
                enemy.reversive = Global.rnd.nextBoolean();
        }


        enemy.position.set(
                game.viewport.getWorldWidth() + spriteOriginSize,
                Global.rnd.nextInt((int) game.viewport.getWorldHeight() - spriteOriginSize) + spriteOriginSize / 2);
        enemy.velocity.set(minSpeed + Global.rnd.nextInt((int)(maxSpeed - minSpeed)), 0);

        enemy.angle = 0;
        enemy.angleInc = 0;
        //  if (fixMaxScale) enemy.scale = 0.5f + 0.5f * maxScale;
        //  else enemy.scale = 0.5f + (0.5f * (Global.rnd.nextInt(maxScale + 1)));
        enemy.scale = 1;
        if (enemy.type == EnemyType.Type1) {
            enemy.live = (int) 50;
            enemy.setScope(50);
        }
        else {
            enemy.live = 20;
            enemy.setScope(20);
        }
        enemy.setDamage(40);
       super.init(gameInnerObject,width,height);
           }

    public int getMaxScale() {
        return maxScale;
    }

    public void setMaxScale(int maxScale) {
        this.maxScale = maxScale;
    }


    public void reset() {
        for (int i = 0; i < activeObject.size; i++) {
            activeObject.get(i).scale = 0;
            activeObject.get(i).isActive = false;
            activeObject.get(i).velocity.set(0, 0);
            activeObject.get(i).angle = 0;
            ((Enemy) activeObject.get(i)).type = EnemyType.Type1;
            ((Enemy) activeObject.get(i)).textureNo = 0;
            fixMaxScale = false;
            isReversiveEnabled = false;
        }
        ((Enemy) activeObject.get(0)).textureNo = 1;
        ((Enemy) activeObject.get(0)).type = EnemyType.Type2;


    }

    public void render(SpriteBatch batch) {
        for (int i = 0; i < activeObject.size; i++) {
            if (activeObject.get(i).isActive)
                batch.draw(texture[((Enemy) activeObject.get(i)).textureNo], activeObject.get(i).position.x - texture[((Enemy) activeObject.get(i)).textureNo].getRegionWidth() * activeObject.get(i).scale / 2, activeObject.get(i).position.y - texture[((Enemy) activeObject.get(i)).textureNo].getRegionHeight() * activeObject.get(i).scale / 2,
                        texture[((Enemy) activeObject.get(i)).textureNo].getRegionWidth() * activeObject.get(i).scale / 2, texture[((Enemy) activeObject.get(i)).textureNo].getRegionHeight() * activeObject.get(i).scale / 2,
                        texture[((Enemy) activeObject.get(i)).textureNo].getRegionWidth() * activeObject.get(i).scale, texture[((Enemy) activeObject.get(i)).textureNo].getRegionHeight() * activeObject.get(i).scale, 1, 1, activeObject.get(i).angle);
        }
    }


    public void setEnemys_count(int i) {
        setActiveObject_count(i);
    }
}
