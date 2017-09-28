package ru.chertenok.games.rtype.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Logger;
import ru.chertenok.games.rtype.Collisionable;
import ru.chertenok.games.rtype.Global;
import ru.chertenok.games.rtype.config.GameConfig;
import ru.chertenok.games.rtype.config.GameState;
import ru.chertenok.games.rtype.level.Level;

import java.util.Map;

public class GameScreen implements Screen {
    private static Logger log = new Logger(GameScreen.class.getSimpleName(), Logger.DEBUG);

    private GameScreenController controller;
    private GameScreenRender render;


    @Override
    public void show() {
        controller = new GameScreenController();
        render = new GameScreenRender();


    }

    @Override
    public void render(float delta) {
        update(delta);

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        Global.dispose();

    }


    public void update(float dt) {

        //перезапуск игры
        if (GameConfig.gameState == GameState.End && (Gdx.input.isKeyJustPressed(Input.Keys.ENTER) ||
                (!Gdx.input.isTouched(0) && lastMouseTouch && isPauseCoord(0)) ||
                (!Gdx.input.isTouched(1) && lastMouseTouch1 && isPauseCoord(1))
        )) {

            restart();
            lastMouseTouch = false;
            lastMouseTouch1 = false;
            GameConfig.gameState = GameState.Run;

        }


        // пауза
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) || Gdx.input.isKeyJustPressed(Input.Keys.P) ||
                ((!Gdx.input.isTouched(0)) && lastMouseTouch && isPauseCoord(0)) ||
                ((!Gdx.input.isTouched(1)) && lastMouseTouch1 && isPauseCoord(1))
                ) {
            if (GameConfig.gameState == GameState.Run) GameConfig.gameState = GameState.Pause;
            else GameConfig.gameState = GameState.Run;
        }

        // обновлялки
        fonStars.update(dt);
        explosions.update(dt);
        if (GameConfig.gameState == GameState.Run) {

            dtLevelCounter += dt * level.getStepVector();

            // меняем настройки по времени
            level.update(dtLevelCounter / 60);

            dtBtn += dt;
            // вкл/выкл щита
            if ((Gdx.input.isKeyPressed(Input.Keys.NUM_1) ||
                    (!Gdx.input.isTouched(0) && lastMouseTouch && isShieldCoord(0)) ||
                    (!Gdx.input.isTouched(1) && lastMouseTouch1 && isShieldCoord(1))

            ) && shipControl.isRechargeEnabled() && dtBtn > 0.3f) {
                shipControl.setRecharge(!shipControl.isRecharge());
                dtBtn = 0;
            }

            // работа щита
            if (shipControl.isRecharge() && score / reChargeCost > 0 && shipControl.getMAX_ENERGY() > shipControl.getEnergy()) {
                int scoreNeed = (shipControl.getMAX_ENERGY() - shipControl.getEnergy()) * reChargeCost;
                if (scoreNeed >= score) {
                    messages.addMessage("+" + shipControl.getEnergy() + score / reChargeCost + "HP  -" + (score - score % reChargeCost) + "XM",
                            shipControl.ship.position.x, shipControl.ship.position.y, 2f, Color.ORANGE);
                    shipControl.setEnergy(shipControl.getEnergy() + score / reChargeCost);
                    score = (score % reChargeCost);
                } else {
                    messages.addMessage("+" + (shipControl.getMAX_ENERGY() - shipControl.getEnergy()) + " HP  -" + ((shipControl.getMAX_ENERGY() - shipControl.getEnergy()) * reChargeCost) + "XM",
                            shipControl.ship.position.x, shipControl.ship.position.y, 2f, Color.ORANGE);

                    shipControl.setEnergy(shipControl.getMAX_ENERGY());
                    score -= scoreNeed;
                }
            }

            fonGround.update(dt);
            bossControl.update(dt);
            asteroids.update(dt);
            enemies.update(dt);
            shipControl.update(dt);

            bullets.update(dt);
            messages.update(dt);
            displayScoreUpdate(dt);
        }
        lastMouseTouch = Gdx.input.isTouched(0);
        lastMouseTouch1 = Gdx.input.isTouched(1);

        if (GameConfig.gameState == GameState.Run) {
            // обработка столкновений всего со всем 8-)
            for (int i = collObjects.size - 1; i > 0; i--) {
                // если не активен, то просто  выкидываем и чешем дальше
                if (!collObjects.get(i).isActive()) {
                    collObjects.removeIndex(i);
                    continue;
                }

                for (int j = i - 1; j >= 0; j--) {
                    // если не активен, то просто  чешем дальше
                    if (!collObjects.get(j).isActive()) continue;


                    isCollision = false;
                    collisionable1 = collObjects.get(i);
                    collisionable2 = collObjects.get(j);
                    if (collisionable1.isCollisinable() && collisionable2.isCollisinable()) {

                        // круги
                        if (collisionable1.getHitAreaType() == Collisionable.HitAreaType.Circle && collisionable2.getHitAreaType() == Collisionable.HitAreaType.Circle)
                            if (collisionable1.getHitAreaCircle().overlaps(collisionable2.getHitAreaCircle()))
                                isCollision = true;
                        // прямоугольники
                        if (collisionable1.getHitAreaType() == Collisionable.HitAreaType.Rectangle && collisionable2.getHitAreaType() == Collisionable.HitAreaType.Rectangle)
                            if (collisionable1.getHitAreaRectangle().overlaps(collisionable2.getHitAreaRectangle()))
                                isCollision = true;
                        // прямоугольник и круг
                        if (collisionable1.getHitAreaType() == Collisionable.HitAreaType.Rectangle && collisionable2.getHitAreaType() == Collisionable.HitAreaType.Circle)
                            if (Intersector.overlaps(collisionable2.getHitAreaCircle(), collisionable1.getHitAreaRectangle()))
                                isCollision = true;
                        //  круг и прямоугольник
                        if (collisionable2.getHitAreaType() == Collisionable.HitAreaType.Rectangle && collisionable1.getHitAreaType() == Collisionable.HitAreaType.Circle)
                            if (Intersector.overlaps(collisionable1.getHitAreaCircle(), collisionable2.getHitAreaRectangle()))
                                isCollision = true;

                        // тот не активный и потом в помойку
                        if (collisionable1.hitStatus_and_IsRemove(this, collisionable2, isCollision)) {
                            collisionable1.setNoActive();
                        }
                        // этот не активный и потом в помойку
                        if (collisionable2.hitStatus_and_IsRemove(this, collisionable1, isCollision)) {
                            collisionable2.setNoActive();
                        }
                    }
                }
                if (!collisionable1.isActive()) collObjects.removeIndex(i);

            }
        }


        if (shipControl.getEnergy() <= 0 && shipControl.getLive() == 0) {
            GameConfig.gameState = GameState.End;
            explosions.addExplosion(shipControl.ship.position.x + 64, shipControl.ship.position.y, 1.0f);
        }

        // проверяем жив ли корабль и если нет, то есть ли жизни
        if (shipControl.getEnergy() <= 0 && shipControl.getLive() > 0) {
            // уменьшаем жизни
            shipControl.setLive(shipControl.getLive() - 1);
            messages.addMessage("-1 Life", shipControl.ship.position.x, shipControl.ship.position.y, 3f, Color.RED);

            // восстанавливаем энергию
            shipControl.setEnergy(shipControl.getMAX_ENERGY());
            explosions.addExplosion(shipControl.ship.position.x + 64, shipControl.ship.position.y, 1.0f);
        }
    }

    private void displayScoreUpdate(float dt) {
        if (displayScore < score) {
            displayScore = Math.min(score, displayScore + (int) (GameConfig.SCORE_MOVE * dt));
        } else if (displayScore > score) {
            displayScore = Math.max(score, displayScore - (int) (GameConfig.SCORE_MOVE * dt));
        }

    }


    public boolean isBossMode() {
        return bossMode;
    }

    public void setBossMode(boolean bossMode) {
        this.bossMode = bossMode;
        fonStars.setStop(bossMode);
        bossControl.setActive();
        if (bossMode) {
            if (GameConfig.isMusic()) {
                music.stop();
                musicBoss.play();
                musicBoss.setVolume(0.7f);
                musicBoss.setLooping(true);
            }
        } else {
            if (GameConfig.isMusic()) {
                {
                    musicBoss.stop();
                    music.play();
                    music.setLooping(true);
                }
            }
        }
    }

    public Map<String, Level.ILevelEvent> getEventMap() {
        return eventMap;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }


    private boolean isPauseCoord(int pointerNum) {
        vector2.set(Gdx.input.getX(pointerNum), Gdx.input.getY(pointerNum));
        vector2.set(viewport.unproject(vector2));

        return vector2.x > (GameConfig.getWorldWidth() - imgPause.getRegionWidth() - 20)
                && (vector2.y < (imgPause.getRegionHeight()) + 20) && vector2.x < viewport.getWorldWidth();
    }

    private boolean isShieldCoord(int pointerNum) {
        vector2.set(Gdx.input.getX(pointerNum), Gdx.input.getY(pointerNum));
        vector2.set(viewport.unproject(vector2));

        return vector2.x > (GameConfig.getWorldWidth() - imgShield.getRegionWidth() * 3)
                && (vector2.y < imgShield.getRegionHeight()) && vector2.x < GameConfig.getWorldWidth() - imgShield.getRegionWidth() * 2;
    }

    private Vector2 getWorldCoord(int pointerNum) {
        vector2.set(Gdx.input.getX(pointerNum), Gdx.input.getY(pointerNum));
        vector2.set(viewport.unproject(vector2));
        return vector2;
    }

    private void restart() {
        dtLevelCounter = level.getDtLevetInit();
        score = 0;
        messages.restart();
        asteroids.setFixMaxScale(false);
        asteroids.setMaxScale(0);
        asteroids.setFixOnScreen(false);
        asteroids.setObjectCount(5);
        asteroids.setReversiveEnabled(false);
        shipControl.reset();
        level.reset();
        asteroids.reset();
        enemies.reset();
        bullets.reset();
        setBossMode(false);
        bossControl.reset();


    }


}
