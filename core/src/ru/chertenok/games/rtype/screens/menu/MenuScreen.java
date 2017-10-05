package ru.chertenok.games.rtype.screens.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import ru.chertenok.games.rtype.assests_maneger.Global;
import ru.chertenok.games.rtype.config.GameConfig;
import ru.chertenok.games.rtype.screens.MainScreenManager;

public class MenuScreen extends ScreenAdapter {
    private final MainScreenManager mainScreenManager;
    private final AssetManager assetManager;

    private Viewport viewport;
    private Stage stage;
    private SpriteBatch batch;

    public MenuScreen(MainScreenManager mainScreenManager) {
        this.mainScreenManager = mainScreenManager;
        this.assetManager = Global.getAssetManager();
        batch = Global.getBatch();
    }

    @Override
    public void show() {

        viewport = new FillViewport(GameConfig.getWorldWidth(), GameConfig.getWorldHeight());
        stage = new Stage(viewport, batch);
        Gdx.input.setInputProcessor(stage);


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
