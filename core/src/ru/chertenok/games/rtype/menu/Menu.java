package ru.chertenok.games.rtype.menu;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import ru.chertenok.games.rtype.R_Type;

/**
 * Created by 13th on 17-Jul-17.
 */
public class Menu {
    private boolean isVisible = false;
    private R_Type game;





    public Menu(R_Type game) {
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
