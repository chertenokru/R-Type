package ru.chertenok.games.rtype.entity.collections;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import ru.chertenok.games.rtype.assests_maneger.Global;
import ru.chertenok.games.rtype.config.GameConfig;
import ru.chertenok.games.rtype.config.ObjectOwner;
import ru.chertenok.games.rtype.entity.Bullet;
import ru.chertenok.games.rtype.entity.GameInnerObject;
import ru.chertenok.games.rtype.screens.game.GameScreenController;


/**
 * Created by 13th on 04-Jul-17.
 */
public class Bullets extends ru.chertenok.games.rtype.entity.collections.ObjectCollector {

    public enum BulletsType {Type1, Type2, Type3}


    private ru.chertenok.games.rtype.entity.collections.Asteroids asteroids;
    private ru.chertenok.games.rtype.entity.collections.Explosions explosions;
    private Rectangle rectangle = new Rectangle();
    private Rectangle rectangle1 = new Rectangle();
    private Circle circle = new Circle();
    private boolean isRemoved = false;
    private static int BULLET_SPEED = 300;
    private Bullet bullet;
    private Sound soundFirePlayer;
    private Sound soundFireType1;
    private Sound soundFireType2;

    private int BULLET_DISTANCE = (int) GameConfig.getWorldWidth() - (int) GameConfig.getWorldWidth() / 5;


    public Bullets(GameScreenController game) throws Exception {
        super(Bullet.class, game, "bullet", 4);
        this.asteroids = game.asteroids;
        this.explosions = game.explosions;

        soundFireType1 = Global.getAssetManager().get("sound/slimeball.mp3", Sound.class);
        soundFirePlayer = Global.getAssetManager().get("sound/foom_0.mp3", Sound.class);
        soundFireType2 = Global.getAssetManager().get("sound/acid6.mp3", Sound.class);
    }


    public void update(float dt) {
        super.update(dt);

        checkDistanceAndCollision();
        for (GameInnerObject _bullet : activeObject) {
            bullet = (Bullet) _bullet;
        //    bullet.position.mulAdd(bullet.velocity, dt);
            if (bullet.type == BulletsType.Type3) {
                bullet.dtCouner += dt;
                if (bullet.dtWait < bullet.dtCouner) {
                    bullet.dtCouner = 0;
                    if (bullet.textureNo == 2) bullet.textureNo = 3;
                    else bullet.textureNo = 2;
                }

            }
        }

    }


    public void render(SpriteBatch batch) {
        for (GameInnerObject _bullet : activeObject) {
            bullet = (Bullet) _bullet;
            batch.draw(texture[bullet.textureNo], bullet.position.x-texture[bullet.textureNo].getRegionWidth() / 2, bullet.position.y-texture[bullet.textureNo].getRegionHeight() / 2,
                    texture[bullet.textureNo].getRegionWidth() / 2, texture[bullet.textureNo].getRegionHeight() / 2,
                    texture[bullet.textureNo].getRegionWidth(), texture[bullet.textureNo].getRegionHeight(), 1, 1, bullet.angle);
        }

    }

    private void checkDistanceAndCollision() {

        for (int i = activeObject.size; --i >= 0; ) {
            Bullet b = (Bullet) activeObject.get(i);
            if (b.startPosition.cpy().sub(b.position).len() > BULLET_DISTANCE || !b.isActive) {
                activeObject.removeIndex(i);
                objectPool.free(b);
//            } else {
//                rectangle.set(b.position.x, b.position.y, spriteSizeX, spriteSizeY);
//
//                isRemoved = false;
//                for (int j = i; --j >= 0; ) {
//                    if (b.owner == activeObject.get(j).owner) continue;
//                    rectangle1.set(activeObject.get(j).position.x, activeObject.get(j).position.y, spriteSizeX, spriteSizeY);
//                    if (Intersector.overlaps(rectangle1, rectangle)) {
//                        activeObject.get(j).isActive = false;
//                        explosions.addExplosion(activeObject.get(i).position.x + spriteSizeX / 2, activeObject.get(i).position.y + spriteSizeY / 2, 0.25f);
//                        activeObject.removeIndex(i);
//                        objectPool.free(b);
//                        isRemoved = true;
//
//                        break;
//                    }
//
//                }

                /*            if (!isRemoved) {

                    for (int j = 0; j < asteroids.getAsteroid_count(); j++) {
                        circle.set(asteroids.getAsteroids()[j].position, asteroids.asteroids[j].getTEXTURE_SIZE() / 2);
                        if (Intersector.overlaps(circle, rectangle)) {

                            if (asteroids.asteroids[j].live == 0) {
                                // если пуля игрока, то очки начисляем
                                if (b.owner == ObjectOwner.Gamer) {
                                    game.score += asteroids.asteroids[j].getSCOPE();
                                    game.messages.addMessage("+" + asteroids.asteroids[j].getSCOPE(), b.position.x + spriteSizeX, b.position.y + spriteSizeY, 1f, Color.GRAY);
                                }
                                explosions.addExplosion(asteroids.asteroids[j].position.x, asteroids.asteroids[j].position.y, asteroids.asteroids[j].scale);

                                asteroids.init(asteroids.getAsteroids()[j]);
                            } else {
                                asteroids.asteroids[j].live--;
                                explosions.addExplosion(b.position.x + spriteSizeX, b.position.y + spriteSizeY);
                            }
                            bullets.removeIndex(i);
                            isRemoved = true;
                            bulletPool.free(b);
                            break;
                        }
                    }
                }

                if (!isRemoved && b.owner == ObjectOwner.AI) {
                    rectangle1.set(game.shipControl.getPosition());
                    if (Intersector.overlaps(rectangle1, rectangle)) {
                        // уменьшаем энергию
                        game.shipControl.setEnergy(game.shipControl.getEnergy() - b.damage);
                        game.messages.addMessage("-" + b.damage + "HP",
                                game.shipControl.getPosition().x, game.shipControl.getPosition().y, 2f, Color.RED);
                        // признак отрисовки попадения
                        game.shipControl.setDamaging(true);
                        // проверяем жив ли корабль и если нет, то есть ли жизни
                        if (game.shipControl.getEnergy() <= 0 && game.shipControl.getLive() > 0) {
                            // уменьшаем жизни
                            game.shipControl.setLive(game.shipControl.getLive() - 1);
                            game.messages.addMessage("-1 Life", game.shipControl.getPosition().x, game.shipControl.getPosition().y, 3f, Color.RED);
                            // state = GameState.Pause;
                            // восстанавливаем энергию
                            game.shipControl.setEnergy(game.shipControl.getMAX_ENERGY());
                            explosions.addExplosion(game.shipControl.getPosition().x + 64, game.shipControl.getPosition().y, 1.0f);
                        }
                        bullets.removeIndex(i);
                        bulletPool.free(b);
                        isRemoved = true;

                    }
                }


                if (!isRemoved && b.owner == ObjectOwner.Gamer) {
                    // c кораблями врага
                    for (int j = 0; j < game.enemies.getEnemys_count(); j++) {
                        circle.set(game.enemies.enemies[j].position, game.enemies.enemies[j].getTEXTURE_SIZE() / 2);
                        if (Intersector.overlaps(circle, rectangle)) {

                            if (game.enemies.enemies[j].live == 0) {
                                // если пуля игрока, то очки начисляем
                                if (b.owner == ObjectOwner.Gamer) game.score += game.enemies.enemies[j].getSCOPE();

                                explosions.addExplosion(game.enemies.enemies[j].position.x, game.enemies.enemies[j].position.y, 1);
                                game.messages.addMessage("+" + game.enemies.enemies[j].getSCOPE(), b.position.x + spriteSizeX, b.position.y + spriteSizeY, 1f, Color.GRAY);
                                game.enemies.init(game.enemies.enemies[j]);
                            } else {
                                game.enemies.enemies[j].live--;
                                explosions.addExplosion(b.position.x + spriteSizeX, b.position.y + spriteSizeY);
                            }
                            bullets.removeIndex(i);
                            bulletPool.free(b);
                            break;
                        }
                    }
                }
*/
//

            }


        }
    }


    public void addBullet(float x, float y) {
        Bullet b = (Bullet) objectPool.obtain();
        b.activate(x, y,- BULLET_SPEED, 0, ObjectOwner.Gamer, BulletsType.Type1);
        activeObject.add(b);
        super.init(b,texture[b.textureNo].getRegionWidth(),texture[b.textureNo].getRegionHeight());
        if (GameConfig.isSound()) soundFirePlayer.play(0.3f);

    }

    void addBotBullet(float x, float y) {
        Bullet b = (Bullet) objectPool.obtain();

        b.activate(x, y, BULLET_SPEED, 0, ObjectOwner.AI, BulletsType.Type2);
        activeObject.add(b);
        super.init(b,texture[b.textureNo].getRegionWidth(),texture[b.textureNo].getRegionHeight());
        if (GameConfig.isSound()) soundFireType1.play(0.2f);
    }

    void addBotBulletVector(float x, float y, float dx, float dy, float angle) {
        Bullet b = (Bullet) objectPool.obtain();

        b.activate(x, y, -dx, -dy, ObjectOwner.AI, BulletsType.Type3);
        b.angle = angle;
        activeObject.add(b);
        super.init(b,texture[b.textureNo].getRegionWidth(),texture[b.textureNo].getRegionHeight());
        if (GameConfig.isSound()) soundFireType2.play(1f);

    }

    public void reset() {
        for (int i = activeObject.size - 1; i >= 0; i--) {
            objectPool.free(activeObject.get(i));
            activeObject.removeIndex(i);
        }


    }

}
