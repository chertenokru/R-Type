package ru.chertenok.games.rtype.screens;

import com.badlogic.gdx.Game;
import ru.chertenok.games.rtype.screens.game.GameScreen;

public class MainScreenManager extends Game {
    private ru.chertenok.games.rtype.screens.game.GameScreen gameScreen;

    @Override
    public void create() {
        gameScreen = new GameScreen();
        setScreen(gameScreen);

    }
}
