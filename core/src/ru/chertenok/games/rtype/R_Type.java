


/***
 * К уроку 6
 *  1. альфа версия в гугл-маркете  https://play.google.com/apps/testing/ru.chertenok.games.rtype
 *  2. взрывы при столкновении пуль
 *
 *
 *
 *
 * К уроку 5
 * --------------
 * 1. камера и фитпортвиев, заменил везде Gdx.graphics. на viewport. размер
 * 2. адаптация  разрешения для андроид
 * 3. адаптация управления для андроид
 * 4. замена кнопок 1, пауза на сенсорные экранные
 * 5. игровые объъекты наследуются от gameInnerObject, пока кроме ShipControl игрока
 * 6. интерфейс Collisionable для сталкиваемых объектов, для дальнейшей реализации общего просчёта столкновений
 *
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
 *
 *
 *
 *
 * К уроку 3
 * ------------
 *
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
 *
 **/

// todo полный сброс при перезапуске
// todo класс для летающих объектов
// todo интерфейс для столковений и список объектов


package ru.chertenok.games.rtype;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import ru.chertenok.games.rtype.level.Level1;
import ru.chertenok.games.rtype.menu.Menu;
import ru.chertenok.games.rtype.objects.BossControl;
import ru.chertenok.games.rtype.objects.Collisionable;
import ru.chertenok.games.rtype.objects.ShipControl;
import ru.chertenok.games.rtype.objects.collections.Asteroids;
import ru.chertenok.games.rtype.objects.collections.Bullets;
import ru.chertenok.games.rtype.objects.collections.Enemies;
import ru.chertenok.games.rtype.objects.collections.Explosions;


public class R_Type extends ApplicationAdapter {

    public enum GameState {Run, Pause, End, Demo}

    SpriteBatch batch;
    ShapeRenderer shape;
    //    Texture img;
    final String font_chars = "абвгдежзийклмнопрстуфхцчшщъыьэюяabcdefghijklmnopqrstuvwxyzАБВГДЕЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789][_!$%#@|\\/?-+=()*&.;:,{}\"´`'<>";
    BitmapFont font;
    BitmapFont fontBig;

    Rectangle rectangle1 = new Rectangle();
    //    Rectangle rectangle2 = new Rectangle();
//    Circle circle1 = new Circle();
//    Circle circle2 = new Circle();
    Collisionable collisionable1;
    Collisionable collisionable2;

    Vector2 vector2 = new Vector2();
    float dtLevelCounter;
    TextureAtlas.AtlasRegion imgPause;
    TextureAtlas.AtlasRegion imgShield;
    TextureAtlas.AtlasRegion imgRect;
    TextureAtlas.AtlasRegion imgEnergy;

    public Viewport viewport;
    public Camera camera;
    GlyphLayout layout = new GlyphLayout();
    boolean lastMouseTouch = false;
    boolean lastMouseTouch1 = false;
    public boolean isAndroid = false;
    Music music;
    Music musicBoss;
    private boolean isCollision = false;
    private float tempEnergy =0;

    // config
    public boolean isMusicOn = false;
    public boolean isSoundcOn = false;
    private boolean isDebugDraw = false;
    private boolean bossMode = false;


    //хар-ки
    public int scope = 0;
    public GameState state = GameState.Run;
    // таймер  отсчёта для изменения игрового процесса
    float dtLevel1 = 0;
    float dtBtn = 0;
    int reChargeCost = 5;
    private static final float WORLD_WIDTH = 1024;
    private static final float WORLD_HEIGHT = 720;


    // игровые объекты
    FonStars fonStars;
    FonGround fonGround;
    public Bullets bullets;
    Menu menu;
    public ShipControl shipControl;
    public Asteroids asteroids;
    public Enemies enemies;
    //public Asteroids asteroids_big;
    public Explosions explosions;
    public Messages messages;
    public Level1 level1 = new Level1(this);
    public BossControl bossControl;
    // список объектов для обработки коллизий
    public Array<Collisionable> collObjects = new Array<Collisionable>();


    public boolean isBossMode() {
        return bossMode;
    }

    public void setBossMode(boolean bossMode) {
        this.bossMode = bossMode;
        fonStars.setStop(bossMode);
        bossControl.setActive();
        if (bossMode) {
            if (isMusicOn) {
                music.stop();
                musicBoss.play();
                musicBoss.setVolume(0.7f);
                musicBoss.setLooping(true);
            }
        } else
        {
            if (isMusicOn) {
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
        camera.position.set(WORLD_WIDTH / 2, WORLD_HEIGHT / 2, 0);
        camera.update();

        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);


        Global.load("level1.pack");
        imgPause = Global.assestManager.get(Global.currentLevel).findRegion("pause");
        imgShield = Global.assestManager.get(Global.currentLevel).findRegion("shield");
        imgRect = Global.assestManager.get(Global.currentLevel).findRegion("rect");
        batch = new SpriteBatch();
        shape = new ShapeRenderer();


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
        if (isMusicOn) {
            music = Global.assestManager.get("through_space.mp3", Music.class);
            musicBoss = Global.assestManager.get("xeon6.mp3", Music.class);
            music.play();
            music.setLooping(true);
        }
        enemies.setEnemys_count(5);
        enemies.setReversiveEnabled(false);
        asteroids.setObjectCount(5);


        shipControl = new ShipControl(this);
        bossControl = new BossControl(this);

        collObjects.add(shipControl.ship);


        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("alt.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.characters = font_chars;
        parameter.size = 35;
        parameter.color = Color.WHITE;
        font = generator.generateFont(parameter);
        parameter.color = Color.WHITE;
        parameter.size = 75;
        fontBig = generator.generateFont(parameter);
        generator.dispose();


        messages = new Messages(font);
        messages.addMessage("Командир, мы приветствуем Вас на борту корабля!", 150, 540, 2, Color.WHITE, Color.LIGHT_GRAY);
        messages.addMessage("Получена информация из командного центра:", 150, 500, 2, Color.WHITE, Color.LIGHT_GRAY);
        messages.addMessage("- Вам нужно пробиться через поле астероидов.", 150, 460, 2, Color.WHITE, Color.LIGHT_GRAY);
        state = GameState.Pause;
        if (isAndroid) {
            messages.addMessage("Клавиши управления: пауза - кнопка пауза", 200, 230, 2, Color.GREEN, Color.LIGHT_GRAY);
            messages.addMessage("движение - двигайте палец по экрану", 200, 190, 2, Color.GREEN, Color.LIGHT_GRAY);
            messages.addMessage("стрельба - прикоснитесь к экрану", 200, 150, 2, Color.GREEN, Color.LIGHT_GRAY);
            messages.addMessage("нажмите кнопку паузы когда будете готовы", 200, 110, 2, Color.GREEN, Color.LIGHT_GRAY);

        } else {
            messages.addMessage("Клавиши управления: пауза - P или ESC", 200, 230, 2, Color.GREEN, Color.LIGHT_GRAY);
            messages.addMessage("движение - ADSW или стрелки", 200, 190, 2, Color.GREEN, Color.LIGHT_GRAY);
            messages.addMessage("стрельба - пробел или кнопка мыши", 200, 150, 2, Color.GREEN, Color.LIGHT_GRAY);
            messages.addMessage("нажмите P когда будете готовы...", 200, 110, 2, Color.GREEN, Color.LIGHT_GRAY);
        }
        dtLevelCounter = level1.getDtLevetInit();

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
        if (isDebugDraw) {
            for (int i = collObjects.size - 1; i >= 0; i--) {
                // если не активен, то просто  выкидываем и чешем дальше
                if (collObjects.get(i).isActive() ) {

                    rectangle1.set(collObjects.get(i).getHitAreaRectangle());
                    if (collObjects.get(i).isCollisinable()) batch.setColor(1, 0, 0, 0.6f); else  batch.setColor(0, 1, 0, 0.6f);
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
        renderLiveLine(shipControl.getEnergy(),shipControl.getMAX_ENERGY(),0);
        // полоса здоровья

        if (bossMode) {

            font.setColor(Color.YELLOW);
            font.draw(batch, "Boss Energy: " + bossControl.getBoss().live, 20-1, viewport.getWorldHeight() - 75 - 1);
            font.setColor(Color.RED);
            font.draw(batch, "Boss Energy: " + bossControl.getBoss().live, 20, viewport.getWorldHeight() - 75 );
            renderLiveLine(bossControl.getBoss().live,bossControl.getMAX_ENERGY(),55);
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
        font.draw(batch, "Scope: " + scope, viewport.getWorldWidth() - 700 - 1, viewport.getWorldHeight() - 10 - 1);
        font.setColor(Color.GOLD);
        font.draw(batch, "Scope: " + scope, viewport.getWorldWidth() - 700, viewport.getWorldHeight() - 10);


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
            if (isAndroid) {
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

    private void renderLiveLine(int value, float maxValue,float shiftY) {

        batch.setColor(1, 1, 1, 0.6f);
        batch.draw(imgRect, 20, viewport.getWorldHeight() - 70-shiftY, viewport.getWorldWidth() - 40, 20);
        batch.setColor(0, 0, 0, 0.6f);
        batch.draw(imgRect, 22, viewport.getWorldHeight() - 68-shiftY, viewport.getWorldWidth() - 44, 16);
        if ((value/maxValue*100) >= 75f) batch.setColor(0, 1, 0, 0.6f);
        else if ((value/maxValue*100) <= 25f) batch.setColor(1, 0, 0, 0.6f);
        else batch.setColor(1, 1, 0, 0.6f);

        tempEnergy = (viewport.getWorldWidth() - 44) / 100 * (value/maxValue*100);
        if (tempEnergy < 0) tempEnergy = 0;
        batch.draw(imgRect, 22, viewport.getWorldHeight() - 68-shiftY, tempEnergy, 16);
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

            dtLevelCounter += dt * level1.getStepVector();

            // меняем настройки по времени
            level1.update(dtLevelCounter / 60);

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
            if (shipControl.isRecharge() && scope / reChargeCost > 0 && shipControl.getMAX_ENERGY() > shipControl.getEnergy()) {
                int scopeNeed = (shipControl.getMAX_ENERGY() - shipControl.getEnergy()) * reChargeCost;
                if (scopeNeed >= scope) {
                    messages.addMessage("+" + shipControl.getEnergy() + scope / reChargeCost + "HP  -" + (scope - scope % reChargeCost) + "XM",
                            shipControl.ship.position.x, shipControl.ship.position.y, 2f, Color.ORANGE);
                    shipControl.setEnergy(shipControl.getEnergy() + scope / reChargeCost);
                    scope = (scope % reChargeCost);
                } else {
                    messages.addMessage("+" + (shipControl.getMAX_ENERGY() - shipControl.getEnergy()) + " HP  -" + ((shipControl.getMAX_ENERGY() - shipControl.getEnergy()) * reChargeCost) + "XM",
                            shipControl.ship.position.x, shipControl.ship.position.y, 2f, Color.ORANGE);

                    shipControl.setEnergy(shipControl.getMAX_ENERGY());
                    scope -= scopeNeed;
                }
            }

            fonGround.update(dt);
            bossControl.update(dt);
            asteroids.update(dt);
            enemies.update(dt);
            shipControl.update(dt);

            bullets.update(dt);
            messages.update(dt);
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

    private boolean isPauseCoord(int pointerNum) {
        vector2.set(Gdx.input.getX(pointerNum), Gdx.input.getY(pointerNum));
        vector2.set(viewport.unproject(vector2));

        if (vector2.x > (viewport.getWorldWidth() - imgPause.getRegionWidth() - 20)
                && (vector2.y < (imgPause.getRegionHeight()) + 20) && vector2.x < viewport.getWorldWidth())
            return true;
        else return false;
    }


    private boolean isShieldCoord(int pointerNum) {
        vector2.set(Gdx.input.getX(pointerNum), Gdx.input.getY(pointerNum));
        vector2.set(viewport.unproject(vector2));

        if (vector2.x > (viewport.getWorldWidth() - imgShield.getRegionWidth() * 3)
                && (vector2.y < imgShield.getRegionHeight()) && vector2.x < viewport.getWorldWidth() - imgShield.getRegionWidth() * 2)
            return true;
        else return false;
    }


    private Vector2 getWorldCoord(int pointerNum) {
        vector2.set(Gdx.input.getX(pointerNum), Gdx.input.getY(pointerNum));
        vector2.set(viewport.unproject(vector2));
        return vector2;
    }


    private void restart() {
        dtLevelCounter = level1.getDtLevetInit();
        scope = 0;
        messages.restart();
        asteroids.setFixMaxScale(false);
        asteroids.setMaxScale(0);
        asteroids.setFixOnScreen(false);
        asteroids.setObjectCount(5);
        asteroids.setReversiveEnabled(false);
        shipControl.reset();
        level1.reset();
        asteroids.reset();
        enemies.reset();
        bullets.reset();
        setBossMode(false);
        bossControl.reset();


    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        viewport.update(width, height);
    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
        fontBig.dispose();
        Global.dispose();
    }
}
