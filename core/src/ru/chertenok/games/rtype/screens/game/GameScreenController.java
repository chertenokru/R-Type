package ru.chertenok.games.rtype.screens.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Logger;
import ru.chertenok.games.rtype.FonGround;
import ru.chertenok.games.rtype.FonStars;
import ru.chertenok.games.rtype.Messages;
import ru.chertenok.games.rtype.assests_maneger.Global;
import ru.chertenok.games.rtype.collisions.CollisionChecker;
import ru.chertenok.games.rtype.collisions.Collisionable;
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
    public Array<Collisionable> collObjects;
    public Array<ObjectCollector> collControllers = new Array<ObjectCollector>();
    public BossControl bossControl;
    public Level level;
    // inner object
    public Rectangle rectangle1 = new Rectangle();

    // игровые объекты
    public FonStars fonStars;
    public FonGround fonGround;
    //хар-ки
    private int score = 0;
    private int displayScore = 0;
    private boolean bossMode = false;
    // list of registred LevelEvents handlers
    private Map<String, Level.ILevelEvent> eventMap = new HashMap<String, Level.ILevelEvent>();
    private boolean lastMouseTouch = false;
    private boolean lastMouseTouch1 = false;
    private Music music;
    private Music musicBoss;
    private float dtBtn = 0;
    private int reChargeCost = 5;


    public GameScreenController() {
        Global.load(GameConfig.LEVEL1_PACK_FILE_PATH);


        Global.setMessageLanguage(new Locale("ru"));
        // Global.setMessageLanguage(new Locale("en"));


        this.collObjects = Global.getCollObjects();
        // menu = new Menu(this);
        fonStars = new FonStars(this);
        fonGround = new FonGround(this);

        try {

            enemies = new Enemies(this);
            explosions = new Explosions(this);
            asteroids = new Asteroids(collObjects);
            bullets = new Bullets(this);

        } catch (Exception e) {
            e.printStackTrace();
        }


        shipControl = new ShipControl(this);
        bossControl = new BossControl(this);

        messages = new Messages();

        // регистрируем события уровня register LevelEvent handlers
        this.registerLevelEvents(eventMap);
        asteroids.registerLevelEvents(eventMap);
        enemies.registerLevelEvents(eventMap);
        messages.registerLevelEvents(eventMap);
        shipControl.registerLevelEvents(eventMap);


        if (GameConfig.isMusic()) {
            music = Global.getAssetManager().get(GameConfig.FILE_MAIN_MUSIC_PATH, Music.class);
            musicBoss = Global.getAssetManager().get(GameConfig.FILE_BOSS_MUSIC_PATH, Music.class);
            music.play();
            music.setLooping(true);
        }

        level = new Level(eventMap);

        enemies.setEnemys_count(5);
        enemies.setReversiveEnabled(false);
        asteroids.setObjectCount(5);

        collObjects.add(shipControl.ship);


        level.reset();
        GameConfig.gameState = GameState.Run;

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
                (!Gdx.input.isTouched(0) && lastMouseTouch && GameConfig.isPauseCoord(0)) ||
                (!Gdx.input.isTouched(1) && lastMouseTouch1 && GameConfig.isPauseCoord(1))
        )) {

            restart();
            lastMouseTouch = false;
            lastMouseTouch1 = false;
            GameConfig.gameState = GameState.Run;

        }


        // пауза
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) || Gdx.input.isKeyJustPressed(Input.Keys.P) ||
                ((!Gdx.input.isTouched(0)) && lastMouseTouch && GameConfig.isPauseCoord(0)) ||
                ((!Gdx.input.isTouched(1)) && lastMouseTouch1 && GameConfig.isPauseCoord(1))
                ) {
            if (GameConfig.gameState == GameState.Run) GameConfig.gameState = GameState.Pause;
            else GameConfig.gameState = GameState.Run;
        }

        // обновлялки
        fonStars.update(dt);
        explosions.update(dt);
        if (GameConfig.gameState == GameState.Run) {


            // меняем настройки по времени
            level.update(dt);

            dtBtn += dt;
            // вкл/выкл щита
            if ((Gdx.input.isKeyPressed(Input.Keys.NUM_1) ||
                    (!Gdx.input.isTouched(0) && lastMouseTouch && GameConfig.isShieldCoord(0)) ||
                    (!Gdx.input.isTouched(1) && lastMouseTouch1 && GameConfig.isShieldCoord(1))

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

            CollisionChecker.update(collObjects, this);


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




    private void restart() {

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


    public void dispose() {
        asteroids.dispose();
        enemies.dispose();
        bullets.dispose();
    }
}
