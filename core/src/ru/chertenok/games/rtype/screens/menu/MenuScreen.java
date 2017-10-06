package ru.chertenok.games.rtype.screens.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import ru.chertenok.games.rtype.FonGround;
import ru.chertenok.games.rtype.FonStars;
import ru.chertenok.games.rtype.assests_maneger.Global;
import ru.chertenok.games.rtype.collisions.CollisionChecker;
import ru.chertenok.games.rtype.config.GameConfig;
import ru.chertenok.games.rtype.entity.collections.Asteroids;
import ru.chertenok.games.rtype.screens.MainScreenManager;

public class MenuScreen extends ScreenAdapter {
    private final MainScreenManager mainScreenManager;
    private final AssetManager assetManager;

    private final String START = "НАЧАТЬ ИГРУ";
    private final String EXIT = "Выход";
    private Viewport viewport;
    private Stage stage;
    private SpriteBatch batch;
    private Texture texture;
    private Actor actor;
    private Actor actor1;
    private Actor fonActor;
    private FonStars fonStars;
    private FonGround fonGround;
    private GlyphLayout txtLayout = new GlyphLayout();
    private Asteroids asteroids;

    public MenuScreen(MainScreenManager mainScreenManager) {
        this.mainScreenManager = mainScreenManager;
        this.assetManager = Global.getAssetManager();
        texture = assetManager.get("button.png");
        batch = Global.getBatch();

    }

    @Override
    public void show() {

        viewport = new FillViewport(GameConfig.getWorldWidth(), GameConfig.getWorldHeight());
        stage = new Stage(viewport, batch);
        Gdx.input.setInputProcessor(stage);
        fonStars = new FonStars(null);
        fonGround = new FonGround(null);
        fonGround.setActive(true);
        try {
            asteroids = new Asteroids(Global.getCollObjects());
        } catch (Exception e) {
            e.printStackTrace();
        }


        asteroids.setFixOnScreen(true);
        asteroids.setObjectCount(10);
        //asteroids.set
        fonActor = new Actor() {
            @Override
            public void draw(Batch batch, float parentAlpha) {
                fonStars.update(Gdx.graphics.getDeltaTime());
                fonGround.update(Gdx.graphics.getDeltaTime());
                asteroids.update(Gdx.graphics.getDeltaTime());
                CollisionChecker.update(Global.getCollObjects());
                fonStars.render((SpriteBatch) batch);
                fonGround.render((SpriteBatch) batch);
                asteroids.render((SpriteBatch) batch);

            }
        };

        fonActor.setBounds(0, 0, GameConfig.getWorldWidth(), GameConfig.getWorldHeight());
        stage.addActor(fonActor);


        actor = new Actor() {

            @Override
            public void draw(Batch batch, float parentAlpha) {
                txtLayout.setText(Global.font, EXIT);
                batch.draw(texture, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation(), 0, 0, (int) getWidth(), (int) getHeight(), false, false);
                Global.font.setColor(Color.YELLOW);
                Global.font.draw(batch, txtLayout, GameConfig.getWorldWidth() / 2 - (getWidth() - txtLayout.width) / 2, (GameConfig.getWorldHeight() - getY()) / 2 - txtLayout.height * 2);
            }
        };
        actor.setBounds(100, 100, texture.getWidth(), texture.getHeight());
        stage.addActor(actor);
        actor1 = new Actor() {

            @Override
            public void draw(Batch batch, float parentAlpha) {

                txtLayout.setText(Global.font, START);
                batch.draw(texture, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation(), 0, 0, (int) getWidth(), (int) getHeight(), false, false);

                Global.font.setColor(Color.YELLOW);
                Global.font.draw(batch, txtLayout, GameConfig.getWorldWidth() / 2 - (getWidth() - txtLayout.width) / 2, (GameConfig.getWorldHeight() - getY()) / 2 - txtLayout.height * 2);
            }
        };
        actor1.setBounds(100, 300, texture.getWidth(), texture.getHeight());

        stage.addActor(actor1);



    }


    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act();
        stage.draw();

    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }


    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        stage.dispose();

    }
}
