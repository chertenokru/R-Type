package ru.chertenok.games.rtype.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Logger;
import ru.chertenok.games.rtype.*;
import ru.chertenok.games.rtype.config.GameConfig;
import ru.chertenok.games.rtype.config.GameState;
import ru.chertenok.games.rtype.entity.BossControl;
import ru.chertenok.games.rtype.entity.ShipControl;
import ru.chertenok.games.rtype.entity.collections.*;
import ru.chertenok.games.rtype.level.Level;
import ru.chertenok.games.rtype.level.LevelEvents;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class GameScreenController implements Level.ILevelEvent {
    private static Logger log = new Logger(GameScreenController.class.getSimpleName(), Logger.DEBUG);

    // контроллеры
    public Bullets bullets;
    public ShipControl shipControl;
    public Asteroids asteroids;
    public Enemies enemies;
    public Explosions explosions;
    public Messages messages;
    // список объектов для обработки коллизий
    public Array<Collisionable> collObjects = new Array<Collisionable>();
    public Array<ObjectCollector> collControllers = new Array<ObjectCollector>();
    public BossControl bossControl;
    public Level level;
    // inner object
    public Rectangle rectangle1 = new Rectangle();
    public float dtLevelCounter;
    // игровые объекты
    public FonStars fonStars;
    public FonGround fonGround;
    //хар-ки
    private int score = 0;
    private int displayScore = 0;
    private boolean bossMode = false;
    // list of registred LevelEvents handlers
    private Map<String, Level.ILevelEvent> eventMap = new HashMap<String, Level.ILevelEvent>();
    private Collisionable collisionable1;
    private Collisionable collisionable2;
    private Vector2 vector2 = new Vector2();
    private boolean lastMouseTouch = false;
    private boolean lastMouseTouch1 = false;
    private Music music;
    private Music musicBoss;
    // таймер  отсчёта для изменения игрового процесса
    private float dtLevel1 = 0;
    private float dtBtn = 0;
    private int reChargeCost = 5;
    private boolean isCollision = false;


    public GameScreenController() {
        Global.load(GameConfig.LEVEL1_PACK_FILE_PATH);


        Global.setMessageLanguage(new Locale("ru"));
        // Global.setMessageLanguage(new Locale("en"));


        // menu = new Menu(this);
        fonStars = new FonStars(this);
        fonGround = new FonGround(this);

        try {

            enemies = new Enemies(this);
            explosions = new Explosions(this);
            asteroids = new Asteroids(this);
            bullets = new Bullets(this);

        } catch (Exception e) {
            e.printStackTrace();
        }


        shipControl = new ShipControl(this);
        bossControl = new BossControl(this);

        messages = new Messages(Global.font);

        // регистрируем события уровня register LevelEvent handlers
        this.registerLevelEvents(eventMap);
        asteroids.registerLevelEvents(eventMap);
        enemies.registerLevelEvents(eventMap);
        messages.registerLevelEvents(eventMap);
        shipControl.registerLevelEvents(eventMap);


        if (GameConfig.isMusic()) {
            music = Global.assetManager.get(GameConfig.FILE_MAIN_MUSIC_PATH, Music.class);
            musicBoss = Global.assetManager.get(GameConfig.FILE_BOSS_MUSIC_PATH, Music.class);
            music.play();
            music.setLooping(true);
        }

        level = new Level(eventMap);

        enemies.setEnemys_count(5);
        enemies.setReversiveEnabled(false);
        asteroids.setObjectCount(5);

        collObjects.add(shipControl.ship);


        messages.addMessage(Global.gameBundle.get("start_1"), 150, 540, 2, Color.WHITE, Color.LIGHT_GRAY);
        messages.addMessage(Global.gameBundle.get("start_2"), 150, 500, 2, Color.WHITE, Color.LIGHT_GRAY);
        messages.addMessage(Global.gameBundle.get("start_3"), 150, 460, 2, Color.WHITE, Color.LIGHT_GRAY);
        GameConfig.gameState = GameState.Pause;
        if (GameConfig.isAndroid()) {
            messages.addMessage(Global.gameBundle.get("conf_touch_1"), 200, 230, 2, Color.GREEN, Color.LIGHT_GRAY);
            messages.addMessage(Global.gameBundle.get("conf_touch_2"), 200, 190, 2, Color.GREEN, Color.LIGHT_GRAY);
            messages.addMessage(Global.gameBundle.get("conf_touch_3"), 200, 150, 2, Color.GREEN, Color.LIGHT_GRAY);
            messages.addMessage(Global.gameBundle.get("conf_touch_4"), 200, 110, 2, Color.GREEN, Color.LIGHT_GRAY);

        } else {
            messages.addMessage(Global.gameBundle.get("conf_key_1"), 200, 230, 2, Color.GREEN, Color.LIGHT_GRAY);
            messages.addMessage(Global.gameBundle.get("conf_key_2"), 200, 190, 2, Color.GREEN, Color.LIGHT_GRAY);
            messages.addMessage(Global.gameBundle.get("conf_key_3"), 200, 150, 2, Color.GREEN, Color.LIGHT_GRAY);
            messages.addMessage(Global.gameBundle.get("conf_key_4"), 200, 110, 2, Color.GREEN, Color.LIGHT_GRAY);
        }
        dtLevelCounter = level.getDtLevetInit() * 60;

    }



    public int getDisplayScore() {
        return displayScore;
    }


    @Override
    public void event(LevelEvents.LevelEvent event) {
        log.debug("Game: event - " + event);
        if (event.Name.equals(GameConfig.GAME_BOSS_MODE_ON)) {
            if (event.param.length > 0) setBossMode(Boolean.valueOf(event.param[0]));
            return;
        } else if (event.Name.equals(GameConfig.GAME_SET_STATUS)) {
            if (event.param.length > 0 && event.param[0].equals("pause")) GameConfig.gameState = GameState.Pause;
            return;
        }

    }

    @Override
    public void registerLevelEvents(Map<String, Level.ILevelEvent> eventMap) {
        eventMap.put(GameConfig.GAME_BOSS_MODE_ON, this);
        eventMap.put(GameConfig.GAME_SET_STATUS, this);
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
