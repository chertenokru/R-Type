/**
 * see Udemy lesson
 * =====================
 *
 * 27.09
 * add score animation
 *
 * 24.09
 * add localization ru + default (eng)
 * moving Level event to json
 * moving level event handler to object controller
 * <p>
 * <p>
 * 23.09
 * add GameConfig class
 * add GameConfig.json
 * <p>
 * <p>
 * geekbrains.ru
 * =======================
 * <p>
 * <p>
 * К уроку 7
 * ---------------
 * черновая реализация босса
 * <p>
 * <p>
 * К уроку 6
 * ---------------
 * 1. альфа версия в гугл-маркете  https://play.google.com/apps/testing/ru.chertenok.games.rtype
 * 2. взрывы при столкновении пуль
 * <p>
 * <p>
 * <p>
 * <p>
 * К уроку 5
 * --------------
 * 1. камера и фитпортвиев, заменил везде Gdx.graphics. на viewport. размер
 * 2. адаптация  разрешения для андроид
 * 3. адаптация управления для андроид
 * 4. замена кнопок 1, пауза на сенсорные экранные
 * 5. игровые объъекты наследуются от gameInnerObject, пока кроме ShipControl игрока
 * 6. интерфейс Collisionable для сталкиваемых объектов, для дальнейшей реализации общего просчёта столкновений
 * <p>
 * К уроку 4
 * -------------------
 * 1. Багофиксы - при перезапуске
 * 2. Тайминги уровня в класс Level
 * 3. Текстура заменена на текстураАтлас с загрузкой через AssestManager
 * 4. Локальные Random вынесены в глобальный Global
 * 5. Создан сомнительный класс Sprites - предок всех объектов или эмиттеров объектов
 * 6. Перезадка энергии корабля за счёт бонусов
 * 7. Иконки щита и паузы на экране - адаптация для андроида
 * 8. Класс врагов с 2-мя разновидностями
 * 9. 3 вида пуль с разными текстурами
 * <p>
 * <p>
 * <p>
 * <p>
 * К уроку 3
 * ------------
 * <p>
 * 1. Добавлены жизни в объект Chip,Enemy
 * 2. Добавлены очки в R_Type
 * 3. добавлены состояния игрового мира - Run,Pause,End
 * 4. добавлен класс Messages - отображающий сообщения на экране во время игры
 * 5. добавлены сообщения о потери жизни, о потерях энергии, о получении очков
 * 6. добавлена при старте игры сюжетная информация и информация об управлении
 * 7. проверка столкновения корабля с "фоновыми коробками"
 * 8. Столкновения астероидов с "коробками" - взрыв и оба объекта уничтожаются
 * 9. Масштаб в астероидах и максимальное кол-во как настраиваемый параметр
 * 10. Урон, уровень прочности  и очки получаемые от астероида зависят от его размера
 * 11. Добавлен таймер уровня и попытка менять настройки среды по таймеру, по окончанию таймера должен быть биг босс, но его нет :)
 * 12. Убрал коробки снизу для следующего уровня
 * 13. Добавил реверсивное движение аастероидов
 **/

// todo полный сброс при перезапуске
// todo класс для летающих объектов
// todo интерфейс для столковений и список объектов


package ru.chertenok.games.rtype;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import ru.chertenok.games.rtype.config.GameConfig;
import ru.chertenok.games.rtype.entity.BossControl;
import ru.chertenok.games.rtype.entity.ShipControl;
import ru.chertenok.games.rtype.entity.collections.Asteroids;
import ru.chertenok.games.rtype.entity.collections.Bullets;
import ru.chertenok.games.rtype.entity.collections.Enemies;
import ru.chertenok.games.rtype.entity.collections.Explosions;
import ru.chertenok.games.rtype.level.Level;
import ru.chertenok.games.rtype.level.LevelEvents;
import ru.chertenok.games.rtype.menu.Menu;

import java.util.HashMap;
import java.util.Map;


public class R_Type extends ApplicationAdapter implements Level.ILevelEvent {

    private static Logger log = new Logger(R_Type.class.getSimpleName(), Logger.DEBUG);
    //    Texture img;
    private final String font_chars = "абвгдежзийклмнопрстуфхцчшщъыьэюяabcdefghijklmnopqrstuvwxyzАБВГДЕЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789][_!$%#@|\\/?-+=()*&.;:,{}\"´`'<>";
    public Viewport viewport;

    //хар-ки
    private int score = 0;
    private int displayScore = 0;
    public GameState state = GameState.Run;
    public Bullets bullets;
    public ShipControl shipControl;
    public Asteroids asteroids;
    public Enemies enemies;
    //public Asteroids asteroids_big;
    public Explosions explosions;
    public Messages messages;
    // список объектов для обработки коллизий
    public Array<Collisionable> collObjects = new Array<Collisionable>();
    private OrthographicCamera camera;
    private Level level;
    private BossControl bossControl;
    // list of registred LevelEvents handlers
    private Map<String, Level.ILevelEvent> eventMap = new HashMap<String, Level.ILevelEvent>();


    private Rectangle rectangle1 = new Rectangle();
    //    Rectangle rectangle2 = new Rectangle();
//    Circle circle1 = new Circle();
//    Circle circle2 = new Circle();
    private Collisionable collisionable1;
    private Collisionable collisionable2;
    private Vector2 vector2 = new Vector2();
    private float dtLevelCounter;
    private TextureAtlas.AtlasRegion imgPause;
    private TextureAtlas.AtlasRegion imgShield;
    private TextureAtlas.AtlasRegion imgRect;
    private TextureAtlas.AtlasRegion imgEnergy;
    private GlyphLayout layout = new GlyphLayout();
    private boolean lastMouseTouch = false;
    private boolean lastMouseTouch1 = false;
    private Music music;
    private Music musicBoss;
    // таймер  отсчёта для изменения игрового процесса
    private float dtLevel1 = 0;
    private float dtBtn = 0;
    private int reChargeCost = 5;
    // игровые объекты
    private FonStars fonStars;
    private FonGround fonGround;
    private Menu menu;
    private SpriteBatch batch;
    private BitmapFont font;
    private BitmapFont fontBig;
    private boolean isCollision = false;
    private float tempEnergy = 0;
    // config
    private boolean bossMode = false;

    public boolean isBossMode() {
        return bossMode;
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

    @Override
    public void create() {

        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        //camera.position.set(WORLD_WIDTH / 2, WORLD_HEIGHT / 2, 0);
        //camera.update();

        viewport = new FitViewport(GameConfig.getWorldWidth(), GameConfig.getWorldHeight(), camera);


        Global.load(GameConfig.LEVEL1_PACK_FILE_PATH);

        // Global.setMessageLanguage(new Locale("ru"));
        // Global.setMessageLanguage(new Locale("en"));
        imgPause = Global.assetManager.get(Global.currentLevel).findRegion("pause");
        imgShield = Global.assetManager.get(Global.currentLevel).findRegion("shield");
        imgRect = Global.assetManager.get(Global.currentLevel).findRegion("rect");
        batch = new SpriteBatch();


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
        initFonts();
        messages = new Messages(font);

        // регистрируем события уровня register LevelEvent handlers
        this.registerLevelEvents(eventMap);
        asteroids.registerLevelEvents(eventMap);
        enemies.registerLevelEvents(eventMap);
        messages.registerLevelEvents(eventMap);
        shipControl.registerLevelEvents(eventMap);



        if (GameConfig.isMusic()) {
            music = Global.assetManager.get("sound/through_space.mp3", Music.class);
            musicBoss = Global.assetManager.get("sound/xeon6.mp3", Music.class);
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
        state = GameState.Pause;
        if (Global.isAndroid()) {
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

    private void initFonts() {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/alt.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.characters = font_chars;
        parameter.size = 35;
        parameter.color = Color.WHITE;
        font = generator.generateFont(parameter);
        parameter.color = Color.WHITE;
        parameter.shadowColor = Color.GRAY;
        parameter.shadowOffsetX = 3;
        parameter.shadowOffsetY = 3;
        parameter.borderStraight = true;
        parameter.size = 75;

        fontBig = generator.generateFont(parameter);
        //  fontBig.getData().markupEnabled
        generator.dispose();
    }

    @Override
    public void render() {
        update(Gdx.graphics.getDeltaTime());

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(camera.projection);
        batch.setTransformMatrix(camera.view);
        batch.begin();
        batch.setColor(1, 1, 1, 1);
        fonStars.render(batch);

        renderHUD();

        fonGround.render(batch);
        bossControl.render(batch);
        bullets.render(batch);
        shipControl.render(batch);
        asteroids.render(batch);
        enemies.render(batch);
        explosions.render(batch);


        messages.render(batch);

        // отрисовка зон столкновений
        if (GameConfig.isIsDebugDraw()) {
            for (int i = collObjects.size - 1; i >= 0; i--) {
                // если не активен, то просто  выкидываем и чешем дальше
                if (collObjects.get(i).isActive()) {

                    rectangle1.set(collObjects.get(i).getHitAreaRectangle());
                    if (collObjects.get(i).isCollisinable()) batch.setColor(1, 0, 0, 0.6f);
                    else batch.setColor(0, 1, 0, 0.6f);
                    batch.draw(imgRect, rectangle1.x, rectangle1.y, rectangle1.width, rectangle1.height);
                    batch.setColor(1, 1, 1, 1);
                }
            }
        }

        batch.end();
    }

    private void renderHUD() {

        if (state == GameState.Pause || state == GameState.End)
            batch.setColor(Color.GRAY.r, Color.GRAY.g, Color.GRAY.b, 0.9f);
        else batch.setColor(1, 1, 1, 1);

        // полоса здоровья
        renderLiveLine(shipControl.getEnergy(), shipControl.getMAX_ENERGY(), 0, false);
        // полоса здоровья

        if (bossMode) {

            font.setColor(Color.YELLOW);
            font.draw(batch, "Boss Energy: " + bossControl.getBoss().live, 20 - 1, viewport.getWorldHeight() - 75 - 1);
            font.setColor(Color.RED);
            font.draw(batch, "Boss Energy: " + bossControl.getBoss().live, 20, viewport.getWorldHeight() - 75);
            renderLiveLine(bossControl.getBoss().live, bossControl.getMAX_ENERGY(), 55, true);
        }


        font.setColor(Color.RED);
        font.draw(batch, "Energy: " + shipControl.getEnergy(), viewport.getWorldWidth() - 200 - 1, viewport.getWorldHeight() - 10 - 1);

        font.setColor(Color.GOLD);
        font.draw(batch, "Energy: " + shipControl.getEnergy(), viewport.getWorldWidth() - 200, viewport.getWorldHeight() - 10);

        font.setColor(Color.RED);
        font.draw(batch, "Live: " + shipControl.getLive(), viewport.getWorldWidth() - 400 - 1, viewport.getWorldHeight() - 10 - 1);
        font.setColor(Color.GOLD);
        font.draw(batch, "Live: " + shipControl.getLive(), viewport.getWorldWidth() - 400, viewport.getWorldHeight() - 10);

        font.setColor(Color.RED);
        font.draw(batch, "Score: " + displayScore, viewport.getWorldWidth() - 700 - 1, viewport.getWorldHeight() - 10 - 1);
        font.setColor(Color.GOLD);
        font.draw(batch, "Score: " + displayScore, viewport.getWorldWidth() - 700, viewport.getWorldHeight() - 10);


        font.setColor(Color.GOLD);
        font.draw(batch, "Time: " + String.format(" %02d:%02d ", (int) dtLevelCounter / 60, (int) dtLevelCounter % 60), viewport.getWorldWidth() - 900 - 1, viewport.getWorldHeight() - 10 - 1);
        font.setColor(Color.RED);
        font.draw(batch, "Time: " + String.format(" %02d:%02d ", (int) dtLevelCounter / 60, (int) dtLevelCounter % 60), viewport.getWorldWidth() - 900, viewport.getWorldHeight() - 10);


        if (shipControl.isRecharge()) {
            batch.draw(imgShield, viewport.getWorldWidth() - imgShield.getRegionWidth() * 3, 0);
        } else {
            batch.setColor(Color.GRAY.r, Color.GRAY.g, Color.GRAY.b, 0.6f);
            batch.draw(imgShield, viewport.getWorldWidth() - imgShield.getRegionWidth() * 3, 0);
            batch.setColor(1, 1, 1, 1);
        }

        if (state == GameState.Pause) {
            //   GlyphLayout layout = new GlyphLayout(fontBig, "PAUSE", Color.GOLD, 100, 1, false);
            //   GlyphLayout layout1 = new GlyphLayout(fontBig, "PAUSE", Color.GRAY, 100, 1, false);
            //   fontBig.draw(batch, layout1, viewport.getWorldWidth() / 2 - layout.width / 2 - 1, viewport.getWorldHeight() / 2 + layout.height / 2 - 1);
            //    fontBig.draw(batch, layout, viewport.getWorldWidth() / 2 - layout.width / 2, viewport.getWorldHeight() / 2 + layout.height / 2);

            batch.draw(imgPause, viewport.getWorldWidth() - imgPause.getRegionWidth() - 20, 20);
        } else {
            batch.setColor(Color.GRAY.r, Color.GRAY.g, Color.GRAY.b, 0.6f);
            batch.draw(imgPause, viewport.getWorldWidth() - imgPause.getRegionWidth() - 20, 20);
            batch.setColor(1, 1, 1, 1);
        }
        if (state == GameState.End) {
            GlyphLayout layout = new GlyphLayout(fontBig, "GAME OVER", Color.RED, 300, 1, false);
            GlyphLayout layout1 = new GlyphLayout(fontBig, "GAME OVER", Color.WHITE, 300, 1, false);
            if (Global.isAndroid()) {
                GlyphLayout layout2 = new GlyphLayout(font, "нажмите паузу для перезапуска", Color.WHITE, 300, 1, false);
                GlyphLayout layout3 = new GlyphLayout(font, "нажмите паузу для перезапуска", Color.GRAY, 300, 1, false);
                fontBig.draw(batch, layout1, viewport.getWorldWidth() / 2 - layout.width / 2 - 1, viewport.getWorldHeight() / 2 + layout.height / 2 - 1);
                fontBig.draw(batch, layout, viewport.getWorldWidth() / 2 - layout.width / 2, viewport.getWorldHeight() / 2 + layout.height / 2);
                font.draw(batch, layout2, viewport.getWorldWidth() / 2 - layout2.width / 2 - 1 + 50, viewport.getWorldHeight() / 2 - layout.height / 2 - 1 - layout2.height / 2);
                font.draw(batch, layout3, viewport.getWorldWidth() / 2 - layout2.width / 2 + 50, viewport.getWorldHeight() / 2 - layout.height / 2 - layout2.height / 2);
            } else {
                GlyphLayout layout2 = new GlyphLayout(font, "press Enter to Restart", Color.WHITE, 300, 1, false);
                GlyphLayout layout3 = new GlyphLayout(font, "press Enter to Restart", Color.GRAY, 300, 1, false);
                fontBig.draw(batch, layout1, viewport.getWorldWidth() / 2 - layout.width / 2 - 1, viewport.getWorldHeight() / 2 + layout.height / 2 - 1);
                fontBig.draw(batch, layout, viewport.getWorldWidth() / 2 - layout.width / 2, viewport.getWorldHeight() / 2 + layout.height / 2);
                font.draw(batch, layout2, viewport.getWorldWidth() / 2 - layout2.width / 2 - 1, viewport.getWorldHeight() / 2 - layout.height / 2 - 1 - layout2.height / 2);
                font.draw(batch, layout3, viewport.getWorldWidth() / 2 - layout2.width / 2, viewport.getWorldHeight() / 2 - layout.height / 2 - layout2.height / 2);

            }
        }
    }

    private void renderLiveLine(int value, float maxValue, float shiftY, boolean isBoss) {

        batch.setColor(1, 1, 1, 0.6f);
        batch.draw(imgRect, 20, viewport.getWorldHeight() - 70 - shiftY, viewport.getWorldWidth() - 40, 20);
        batch.setColor(0, 0, 0, 0.6f);
        batch.draw(imgRect, 22, viewport.getWorldHeight() - 68 - shiftY, viewport.getWorldWidth() - 44, 16);
        if ((isBoss && bossControl.isHitting()) || (!isBoss)) {
            if ((value / maxValue * 100) >= 75f) batch.setColor(0, 1, 0, 0.6f);
            else if ((value / maxValue * 100) <= 25f) batch.setColor(1, 0, 0, 0.6f);
            else batch.setColor(1, 1, 0, 0.6f);
        } else font.setColor(Color.GRAY);

        tempEnergy = (viewport.getWorldWidth() - 44) / 100 * (value / maxValue * 100);
        if (tempEnergy < 0) tempEnergy = 0;
        batch.draw(imgRect, 22, viewport.getWorldHeight() - 68 - shiftY, tempEnergy, 16);
        //  batch.draw(imgRect, rectangle1.x, rectangle1.y, rectangle1.width, rectangle1.height);
        batch.setColor(1, 1, 1, 1);
    }

    public void update(float dt) {

        //перезапуск игры
        if (state == GameState.End && (Gdx.input.isKeyJustPressed(Input.Keys.ENTER) ||
                (!Gdx.input.isTouched(0) && lastMouseTouch && isPauseCoord(0)) ||
                (!Gdx.input.isTouched(1) && lastMouseTouch1 && isPauseCoord(1))
        )) {

            restart();
            lastMouseTouch = false;
            lastMouseTouch1 = false;
            state = GameState.Run;

        }


        // пауза
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) || Gdx.input.isKeyJustPressed(Input.Keys.P) ||
                ((!Gdx.input.isTouched(0)) && lastMouseTouch && isPauseCoord(0)) ||
                ((!Gdx.input.isTouched(1)) && lastMouseTouch1 && isPauseCoord(1))
                ) {
            if (state == GameState.Run) state = GameState.Pause;
            else state = GameState.Run;
        }

        // обновлялки
        fonStars.update(dt);
        explosions.update(dt);
        if (state == GameState.Run) {

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

        if (state == GameState.Run) {
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
                        // столкнулись
                        if (isCollision) {
                            // этот в помойку
                            if (collisionable1.hitIsRemove(this, collisionable2)) {
                                collObjects.removeIndex(i);
                            }
                            // этот не активный и потом в помойку
                            if (collisionable2.hitIsRemove(this, collisionable1)) {
                                collisionable2.setNoActive();
                            }

                            break;
                        }
                    }
                }
            }
        }


        if (shipControl.getEnergy() <= 0 && shipControl.getLive() == 0) {
            state = GameState.End;
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
            displayScore = Math.min(score, displayScore + (int) (60 * dt));
        } else if (displayScore > score) {
            displayScore = Math.max(score, displayScore - (int) (60 * dt));
        }

    }

    private boolean isPauseCoord(int pointerNum) {
        vector2.set(Gdx.input.getX(pointerNum), Gdx.input.getY(pointerNum));
        vector2.set(viewport.unproject(vector2));

        return vector2.x > (viewport.getWorldWidth() - imgPause.getRegionWidth() - 20)
                && (vector2.y < (imgPause.getRegionHeight()) + 20) && vector2.x < viewport.getWorldWidth();
    }

    private boolean isShieldCoord(int pointerNum) {
        vector2.set(Gdx.input.getX(pointerNum), Gdx.input.getY(pointerNum));
        vector2.set(viewport.unproject(vector2));

        return vector2.x > (viewport.getWorldWidth() - imgShield.getRegionWidth() * 3)
                && (vector2.y < imgShield.getRegionHeight()) && vector2.x < viewport.getWorldWidth() - imgShield.getRegionWidth() * 2;
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

    @Override
    public void resize(int width, int height) {

        viewport.update(width, height, true);
    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
        fontBig.dispose();
        Global.dispose();
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

    public enum GameState {Run, Pause, End, Demo}
}
