package ru.chertenok.games.rtype.screens;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
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

        messages = new Messages(font);

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

    public int getScore() {
        return score;
    }

    public int getDisplayScore() {
        return displayScore;
    }

    public boolean isBossMode() {
        return bossMode;
    }

    @Override
    public void event(LevelEvents.LevelEvent event) {
        log.debug("Game: event - " + event);
        if (event.Name.equals(GameConfig.GAME_BOSS_MODE_ON)) {
            if (event.param.length > 0) setBossMode(Boolean.valueOf(event.param[0]));
            return;
        } else if (event.Name.equals(GameConfig.GAME_SET_STATUS)) {
            if (event.param.length > 0 && event.param[0].equals("pause")) state = GameState.Pause;
            return;
        }

    }

    @Override
    public void registerLevelEvents(Map<String, Level.ILevelEvent> eventMap) {
        eventMap.put(GameConfig.GAME_BOSS_MODE_ON, this);
        eventMap.put(GameConfig.GAME_SET_STATUS, this);
    }
}
