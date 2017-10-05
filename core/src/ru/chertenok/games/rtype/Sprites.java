package ru.chertenok.games.rtype;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import ru.chertenok.games.rtype.assests_maneger.Global;
import ru.chertenok.games.rtype.screens.game.GameScreenController;

/**
 * Created by 13th on 08.07.2017.
 */
public abstract  class Sprites {
    // кол-во текстур
    protected   int textureCount;
    // ссылки на текстуры
    protected  TextureAtlas.AtlasRegion[] texture;
    // наименование текстуры
    protected   String textureName;
    // размер спрайта на случай если он отличается от исходного
    protected  int spriteSize;
    // оригинальный размер спрайта
    protected  int spriteOriginSize;
    // если не квадратный спрайт
    public   int spriteSizeX;
    public   int spriteSizeY;
    protected GameScreenController game;

    public Sprites(GameScreenController game, String textureName, int textureCount) {

        this.textureName = textureName;
        this.textureCount = textureCount;
        this.game = game;

        // массив спрайтов
        texture = new TextureAtlas.AtlasRegion[textureCount];

        // заполняем  ссылки на спрайты
        for (int i = 0; i < texture.length; i++) {
            texture[i] = Global.getAssetManager().get(Global.currentLevel).findRegion(textureName, i);
        }

        // размер спрайта
        spriteSize = texture[0].getRegionHeight();
        spriteOriginSize = spriteSize;
        spriteSizeY = texture[0].getRegionHeight();
        spriteSizeX = texture[0].getRegionWidth();

    }


}
