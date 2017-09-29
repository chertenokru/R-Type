package ru.chertenok.games.rtype.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.Logger;
import ru.chertenok.games.rtype.Global;

public class GameScreen implements Screen {
    private static Logger log = new Logger(GameScreen.class.getSimpleName(), Logger.DEBUG);

    private GameScreenController controller;
    private GameScreenRender renderer;


    @Override
    public void show() {
        controller = new GameScreenController();
        renderer = new GameScreenRender(controller);
    }

    @Override
    public void render(float delta) {
        controller.update(delta);
        renderer.render(delta);
    }

    @Override
    public void resize(int width, int height) {
        renderer.resize(width, height);

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        renderer.dispose();
        Global.dispose();

    }


}
