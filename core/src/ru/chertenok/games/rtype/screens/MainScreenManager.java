package ru.chertenok.games.rtype.screens;

import com.badlogic.gdx.Game;
import ru.chertenok.games.rtype.assests_maneger.Global;
import ru.chertenok.games.rtype.config.GameConfig;
import ru.chertenok.games.rtype.screens.game.GameScreen;
import ru.chertenok.games.rtype.screens.menu.MenuScreen;

public class MainScreenManager extends Game {
    private final ru.chertenok.games.rtype.screens.game.GameScreen gameScreen = new GameScreen();

    public GameScreen getGameScreen() {
        return gameScreen;
    }

    @Override
    public void create() {

        Global.load(GameConfig.LEVEL1_PACK_FILE_PATH);

        MenuScreen menuScreen = new MenuScreen(this);
        // setScreen(menuScreen);
        setScreen(gameScreen);



    }
}
