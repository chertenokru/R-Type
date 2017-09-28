package ru.chertenok.games.rtype.menu;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import ru.chertenok.games.rtype.GameScreen;

/**
 * Created by 13th on 17-Jul-17.
 */
public class Menu {
    private boolean isVisible = false;
    private GameScreen game;


    public Menu(GameScreen game) {
        this.game = game;
    }



    public void update(float dt){

    }

    public void render(ShapeRenderer shape){


    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }
}
