package ru.chertenok.games.rtype.screens.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import ru.chertenok.games.rtype.assests_maneger.Global;
import ru.chertenok.games.rtype.config.GameConfig;
import ru.chertenok.games.rtype.config.GameState;

public class GameScreenRender implements Disposable {

    private static Logger log = new Logger(GameScreenRender.class.getSimpleName(), Logger.DEBUG);

    public Viewport viewport;
    public TextureAtlas.AtlasRegion imgPause;
    public TextureAtlas.AtlasRegion imgShield;
    public TextureAtlas.AtlasRegion imgRect;
    public TextureAtlas.AtlasRegion imgEnergy;
    private OrthographicCamera camera;
    private GlyphLayout layout = new GlyphLayout();
    private SpriteBatch batch;
    private Stage stage;
    private Actor actor;
    private Actor actor1;




    private GameScreenController controller;

    // temp
    private float tempEnergy = 0;


    public GameScreenRender(GameScreenController controller) {
        this.controller = controller;
        init();
    }

    private void init() {
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        viewport = new FitViewport(GameConfig.getWorldWidth(), GameConfig.getWorldHeight(), camera);
        initImages();
        Global.initFonts();
        batch = new SpriteBatch();
        stage = new Stage(viewport, batch);
        actor = new Actor() {
            @Override
            public void draw(Batch batch, float parentAlpha) {

                drawGame();
            }
        };
        actor.setBounds(0, 0, GameConfig.getWorldWidth(), GameConfig.getWorldHeight());

        actor1 = new Actor() {
            @Override
            public void draw(Batch batch, float parentAlpha) {
                batch.setColor(getColor());

                batch.draw(imgShield, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
            }
        };
        actor1.setBounds(100, 100, 200, 200);
        //  actor1.setColor(Color.RED);
        actor1.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                event.getListenerActor().addAction(Actions.rotateBy(90, 2f));
                log.debug("click");
            }


            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                event.getListenerActor().addAction(Actions.alpha(0, 1f));
                log.debug("enter");
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                event.getListenerActor().addAction(Actions.alpha(1, 1f));
            }

        });

        stage.addActor(actor);
        stage.addActor(actor1);
        Gdx.input.setInputProcessor(stage);

        actor.addAction(Actions.sequence(
                Actions.fadeIn(100),
                Actions.fadeOut(200),
                Actions.rotateBy(90, 200),
                Actions.rotateBy(90, 200),
                Actions.rotateBy(90, 200),
                Actions.rotateBy(90, 200)));



    }

    private void initImages() {
        TextureAtlas ta = Global.assetManager.get(Global.currentLevel);
        imgPause = ta.findRegion(GameConfig.TEXTURE_REGION_BUTTON_PAUSE);
        imgShield = ta.findRegion(GameConfig.TEXTURE_REGION_BUTTON_SHIELD);
        imgRect = ta.findRegion(GameConfig.TEXTURE_REGION_BUTTON_RECT);
    }


    public void render(float delta) {
        batch.totalRenderCalls = 0;
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        //   batch.setProjectionMatrix(camera.projection);
        //   batch.setTransformMatrix(camera.view);

        // batch.begin();
        stage.act(delta);
        stage.draw();
        //  batch.end();
    }

    private void drawGame() {
        batch.setColor(1, 1, 1, 1);
        controller.fonStars.render(batch);
        controller.fonGround.render(batch);
        controller.bossControl.render(batch);
        controller.bullets.render(batch);
        controller.shipControl.render(batch);
        controller.asteroids.render(batch);
        controller.enemies.render(batch);
        controller.explosions.render(batch);
        renderDebugCollision();
        renderHUD();
        controller.messages.render(batch);

        log.debug("" + batch.totalRenderCalls);
    }

    private void renderDebugCollision() {
        // отрисовка зон столкновений
        if (GameConfig.isIsDebugDraw()) {
            for (int i = controller.collObjects.size - 1; i >= 0; i--) {
                // если не активен, то просто  выкидываем и чешем дальше
                if (controller.collObjects.get(i).isActive()) {

                    controller.rectangle1.set(controller.collObjects.get(i).getHitAreaRectangle());
                    if (controller.collObjects.get(i).isCollisinable()) batch.setColor(1, 0, 0, 0.6f);
                    else batch.setColor(0, 1, 0, 0.6f);
                    batch.draw(imgRect, controller.rectangle1.x, controller.rectangle1.y, controller.rectangle1.width, controller.rectangle1.height);
                    batch.setColor(1, 1, 1, 1);
                }
            }
        }
    }


    private void renderHUD() {


        if (GameConfig.gameState == GameState.Pause || GameConfig.gameState == GameState.End)
            batch.setColor(Color.GRAY.r, Color.GRAY.g, Color.GRAY.b, 0.9f);
        else batch.setColor(1, 1, 1, 1);

        // полоса здоровья
        renderLiveLine(controller.shipControl.getEnergy(), controller.shipControl.getMAX_ENERGY(), 0, false);
        // полоса здоровья

        if (controller.isBossMode()) {
            renderLiveLine(controller.bossControl.getBoss().live, controller.bossControl.getMAX_ENERGY(), 55, true);
        }

        if (controller.shipControl.isRecharge()) {
            batch.draw(imgShield, viewport.getWorldWidth() - imgShield.getRegionWidth() * 3, 0);
        } else {
            batch.setColor(Color.GRAY.r, Color.GRAY.g, Color.GRAY.b, 0.6f);
            batch.draw(imgShield, viewport.getWorldWidth() - imgShield.getRegionWidth() * 3, 0);
            batch.setColor(1, 1, 1, 1);
        }

        if (GameConfig.gameState == GameState.Pause) {
            //   GlyphLayout layout = new GlyphLayout(Global.fontBig, "PAUSE", Color.GOLD, 100, 1, false);
            //   GlyphLayout layout1 = new GlyphLayout(Global.fontBig, "PAUSE", Color.GRAY, 100, 1, false);
            //   Global.fontBig.draw(batch, layout1, viewport.getWorldWidth() / 2 - layout.width / 2 - 1, viewport.getWorldHeight() / 2 + layout.height / 2 - 1);
            //    Global.fontBig.draw(batch, layout, viewport.getWorldWidth() / 2 - layout.width / 2, viewport.getWorldHeight() / 2 + layout.height / 2);

            batch.draw(imgPause, viewport.getWorldWidth() - imgPause.getRegionWidth() - 20, 20);
        } else {
            batch.setColor(Color.GRAY.r, Color.GRAY.g, Color.GRAY.b, 0.6f);
            batch.draw(imgPause, viewport.getWorldWidth() - imgPause.getRegionWidth() - 20, 20);
            batch.setColor(1, 1, 1, 1);
        }

        if (controller.isBossMode()) {

            Global.font.setColor(Color.YELLOW);
            Global.font.draw(batch, "Boss Energy: " + controller.bossControl.getBoss().live, 20 - 1, viewport.getWorldHeight() - 75 - 1);
            Global.font.setColor(Color.RED);
            Global.font.draw(batch, "Boss Energy: " + controller.bossControl.getBoss().live, 20, viewport.getWorldHeight() - 75);

        }


        Global.font.setColor(Color.RED);
        Global.font.draw(batch, "Energy: " + controller.shipControl.getEnergy(), viewport.getWorldWidth() - 200 - 1, viewport.getWorldHeight() - 10 - 1);

        Global.font.setColor(Color.GOLD);
        Global.font.draw(batch, "Energy: " + controller.shipControl.getEnergy(), viewport.getWorldWidth() - 200, viewport.getWorldHeight() - 10);

        Global.font.setColor(Color.RED);
        Global.font.draw(batch, "Live: " + controller.shipControl.getLive(), viewport.getWorldWidth() - 400 - 1, viewport.getWorldHeight() - 10 - 1);
        Global.font.setColor(Color.GOLD);
        Global.font.draw(batch, "Live: " + controller.shipControl.getLive(), viewport.getWorldWidth() - 400, viewport.getWorldHeight() - 10);

        Global.font.setColor(Color.RED);
        Global.font.draw(batch, "Score: " + controller.getDisplayScore(), viewport.getWorldWidth() - 700 - 1, viewport.getWorldHeight() - 10 - 1);
        Global.font.setColor(Color.GOLD);
        Global.font.draw(batch, "Score: " + controller.getDisplayScore(), viewport.getWorldWidth() - 700, viewport.getWorldHeight() - 10);


        Global.font.setColor(Color.GOLD);
        Global.font.draw(batch, "Time: " + String.format(" %02d:%02d ", (int) controller.level.getDtLevelCounter() / 60, (int) controller.level.getDtLevelCounter() % 60), viewport.getWorldWidth() - 900 - 1, viewport.getWorldHeight() - 10 - 1);
        Global.font.setColor(Color.RED);
        Global.font.draw(batch, "Time: " + String.format(" %02d:%02d ", (int) controller.level.getDtLevelCounter() / 60, (int) controller.level.getDtLevelCounter() % 60), viewport.getWorldWidth() - 900, viewport.getWorldHeight() - 10);


        if (GameConfig.gameState == GameState.End) {
            GlyphLayout layout = new GlyphLayout(Global.fontBig, "GAME OVER", Color.RED, 300, 1, false);
            GlyphLayout layout1 = new GlyphLayout(Global.fontBig, "GAME OVER", Color.WHITE, 300, 1, false);
            if (GameConfig.isAndroid()) {
                GlyphLayout layout2 = new GlyphLayout(Global.font, "нажмите паузу для перезапуска", Color.WHITE, 300, 1, false);
                GlyphLayout layout3 = new GlyphLayout(Global.font, "нажмите паузу для перезапуска", Color.GRAY, 300, 1, false);
                Global.fontBig.draw(batch, layout1, viewport.getWorldWidth() / 2 - layout.width / 2 - 1, viewport.getWorldHeight() / 2 + layout.height / 2 - 1);
                Global.fontBig.draw(batch, layout, viewport.getWorldWidth() / 2 - layout.width / 2, viewport.getWorldHeight() / 2 + layout.height / 2);
                Global.font.draw(batch, layout2, viewport.getWorldWidth() / 2 - layout2.width / 2 - 1 + 50, viewport.getWorldHeight() / 2 - layout.height / 2 - 1 - layout2.height / 2);
                Global.font.draw(batch, layout3, viewport.getWorldWidth() / 2 - layout2.width / 2 + 50, viewport.getWorldHeight() / 2 - layout.height / 2 - layout2.height / 2);
            } else {
                GlyphLayout layout2 = new GlyphLayout(Global.font, "press Enter to Restart", Color.WHITE, 300, 1, false);
                GlyphLayout layout3 = new GlyphLayout(Global.font, "press Enter to Restart", Color.GRAY, 300, 1, false);
                Global.fontBig.draw(batch, layout1, viewport.getWorldWidth() / 2 - layout.width / 2 - 1, viewport.getWorldHeight() / 2 + layout.height / 2 - 1);
                Global.fontBig.draw(batch, layout, viewport.getWorldWidth() / 2 - layout.width / 2, viewport.getWorldHeight() / 2 + layout.height / 2);
                Global.font.draw(batch, layout2, viewport.getWorldWidth() / 2 - layout2.width / 2 - 1, viewport.getWorldHeight() / 2 - layout.height / 2 - 1 - layout2.height / 2);
                Global.font.draw(batch, layout3, viewport.getWorldWidth() / 2 - layout2.width / 2, viewport.getWorldHeight() / 2 - layout.height / 2 - layout2.height / 2);

            }
        }
    }


    private void renderLiveLine(int value, float maxValue, float shiftY, boolean isBoss) {

        batch.setColor(1, 1, 1, 0.6f);
        batch.draw(imgRect, 20, viewport.getWorldHeight() - 70 - shiftY, viewport.getWorldWidth() - 40, 20);
        batch.setColor(0, 0, 0, 0.6f);
        batch.draw(imgRect, 22, viewport.getWorldHeight() - 68 - shiftY, viewport.getWorldWidth() - 44, 16);
        if (!isBoss || controller.bossControl.isHitting()) {
            if ((value / maxValue * 100) >= 75f) batch.setColor(0, 1, 0, 0.6f);
            else if ((value / maxValue * 100) <= 25f) batch.setColor(1, 0, 0, 0.6f);
            else batch.setColor(1, 1, 0, 0.6f);
        } else batch.setColor(Color.GRAY);

        tempEnergy = (viewport.getWorldWidth() - 44) / 100 * (value / maxValue * 100);
        if (tempEnergy < 0) tempEnergy = 0;
        batch.draw(imgRect, 22, viewport.getWorldHeight() - 68 - shiftY, tempEnergy, 16);
        //  batch.draw(imgRect, rectangle1.x, rectangle1.y, rectangle1.width, rectangle1.height);
        batch.setColor(1, 1, 1, 1);
    }

    public void resize(int width, int height) {

        viewport.update(width, height, true);
    }

    @Override
    public void dispose() {
        batch.dispose();
    }


}
