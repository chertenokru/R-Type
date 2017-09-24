package ru.chertenok.games.rtype.entity.collections;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Logger;
import ru.chertenok.games.rtype.Global;
import ru.chertenok.games.rtype.R_Type;
import ru.chertenok.games.rtype.config.GameConfig;
import ru.chertenok.games.rtype.level.Level;
import ru.chertenok.games.rtype.level.LevelEvents;

import java.util.Map;

/**
 * Created by 13th on 02.07.2017.
 */
public class Asteroids extends ObjectCollector implements Level.ILevelEvent {

    private static Logger log = new Logger(Asteroids.class.getSimpleName(), Logger.DEBUG);

    private float waitTime = 10f;
    private float waitCounter = 0f;
    private boolean fixMaxScale = false;
    private boolean isReversiveEnabled = false;
    private float maxScale = 0.5f;
    private ru.chertenok.games.rtype.entity.Asteroid asteroid;
    private boolean fixOnScreen = false;

    public Asteroids(R_Type game) throws Exception {
        super(ru.chertenok.games.rtype.entity.Asteroid.class, game, "asteroid", 1);
        maxSpeed = 120;
        minSpeed = 30;
    }

    public boolean isFixOnScreen() {
        return fixOnScreen;
    }

    public void setFixOnScreen(boolean fixOnScreen) {
        this.fixOnScreen = fixOnScreen;
    }

    public void addAsteroid(float x, float y, float velocityX, float velocityY, float scl) {
        asteroid = (ru.chertenok.games.rtype.entity.Asteroid) objectPool.obtain();
        init(asteroid, spriteSizeX, spriteSizeY);
        asteroid.position.x = x;
        asteroid.position.y = y;
        asteroid.velocity.x = velocityX;
        asteroid.velocity.y = velocityY;
        asteroid.scale = scl;
        activeObject.add(asteroid);
        game.collObjects.add(asteroid);
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

    public void setObjectCount(int objectCount) {
        this.objectCount = objectCount;
    }


    @Override
    public void update(float dt) {
        super.update(dt);
    }


    @Override
    protected void init(ru.chertenok.games.rtype.entity.GameInnerObject gameInnerObject, float width, float height) {

        asteroid = (ru.chertenok.games.rtype.entity.Asteroid) gameInnerObject;
        asteroid.reversive = false;
        if (fixMaxScale) asteroid.scale = 0.5f + 0.5f * maxScale;
        else
            asteroid.scale = 0.5f + (0.5f * (Global.rnd.nextInt((int) ru.chertenok.games.rtype.entity.Asteroid.getMaxScale() + 1)));
        if (isReversiveEnabled) {
            if (Global.rnd.nextBoolean())
                asteroid.reversive = Global.rnd.nextBoolean();
        }

        if (asteroid.reversive) {
            asteroid.position.set(
                    -asteroid.originSpriteSize.x * asteroid.scale,
                    Global.rnd.nextInt((int) game.viewport.getWorldHeight() - spriteOriginSize) + spriteOriginSize / 2);
            asteroid.velocity.set(minSpeed + Global.rnd.nextInt((int) (maxSpeed - minSpeed)), 0).scl(-1);
        } else {
            asteroid.position.set(
                    game.viewport.getWorldWidth() + spriteOriginSize,
                    Global.rnd.nextInt((int) game.viewport.getWorldHeight() - spriteOriginSize) + spriteOriginSize / 2);
            asteroid.velocity.set(minSpeed + Global.rnd.nextInt((int) (maxSpeed - minSpeed)), 0);
        }
        asteroid.angle = Global.rnd.nextInt(360);
        asteroid.angleInc = Global.rnd.nextInt(ru.chertenok.games.rtype.entity.Asteroid.MAX_ANGLE_INC);

        asteroid.isFixOnScreen = fixOnScreen;

        asteroid.live = (int) asteroid.scale * 2;
        if (width == 0) {
            width = spriteOriginSize;
            height = spriteOriginSize;
        }
        asteroid.maxSpeed = 200;
        super.init(gameInnerObject, width, height);
    }

    public float getMaxScale() {
        return maxScale;
    }

    public void setMaxScale(float maxScale) {
        this.maxScale = maxScale;
    }

    public void render(SpriteBatch batch) {
        for (int i = 0; i < activeObject.size; i++) {

            if (activeObject.get(i).isActive) {
                //          System.out.printf("draw %d, x = %f, y = %f,scale = %f", i,activeObject.get(i).position.x ,activeObject.get(i).position.y,activeObject.get(i).scale );
                batch.draw(texture[currentTextureNo], activeObject.get(i).position.x - activeObject.get(i).originSpriteSize.x * activeObject.get(i).scale / 2,
                        activeObject.get(i).position.y - activeObject.get(i).originSpriteSize.y * activeObject.get(i).scale / 2,
                        activeObject.get(i).originSpriteSize.x * activeObject.get(i).scale / 2, activeObject.get(i).originSpriteSize.y * activeObject.get(i).scale / 2,
                        activeObject.get(i).originSpriteSize.x * activeObject.get(i).scale, activeObject.get(i).originSpriteSize.y * activeObject.get(i).scale, 1, 1,
                        activeObject.get(i).angle);
            }
        }
    }


    @Override
    public void registerLevelEvents(Map<String, Level.ILevelEvent> eventMap) {
        eventMap.put(GameConfig.ASTEROIDS_SET_OBJECT_COUNT, this);
        eventMap.put(GameConfig.ASTEROIDS_SET_REVERSIVE, this);
        eventMap.put(GameConfig.ASTEROIDS_SET_FIX_ON_SCREEN, this);
        eventMap.put(GameConfig.ASTEROIDS_SET_FIX_MAX_SCALE, this);


    }

    @Override
    public void event(LevelEvents.LevelEvent event) {
        log.debug("Asteroid:event - " + event.toString());
        if (event.Name.equals(GameConfig.ASTEROIDS_SET_OBJECT_COUNT)) {
            if (event.param.length > 0) setObjectCount(Integer.valueOf(event.param[0]));
            return;
        } else if (event.Name.equals(GameConfig.ASTEROIDS_SET_REVERSIVE)) {
            if (event.param.length > 0) setReversiveEnabled(Boolean.valueOf(event.param[0]));
            return;
        } else if (event.Name.equals(GameConfig.ASTEROIDS_SET_MAX_SCALE)) {
            if (event.param.length > 0) setMaxScale(Float.valueOf(event.param[0]));
            return;
        } else if (event.Name.equals(GameConfig.ASTEROIDS_SET_FIX_MAX_SCALE)) {
            if (event.param.length > 0) setFixMaxScale(Boolean.valueOf(event.param[0]));
            return;
        } else if (event.Name.equals(GameConfig.ASTEROIDS_SET_FIX_ON_SCREEN)) {
            if (event.param.length > 0) setFixOnScreen(Boolean.valueOf(event.param[0]));
            return;
        }


    }
}
