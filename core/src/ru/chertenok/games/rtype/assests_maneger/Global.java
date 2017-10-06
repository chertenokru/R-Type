package ru.chertenok.games.rtype.assests_maneger;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.I18NBundleLoader;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.Logger;
import ru.chertenok.games.rtype.collisions.Collisionable;
import ru.chertenok.games.rtype.config.GameConfig;

import java.util.Locale;
import java.util.Random;


/**
 * Created by 13th on 08.07.2017.
 */

public class Global {

    private final static AssetManager assetManager = new AssetManager();
    private final static SpriteBatch batch = new SpriteBatch();
    public static AssetDescriptor<TextureAtlas> currentLevel;
    public static Random rnd = new Random();
    public static Locale locale;
    public static I18NBundle gameBundle;
    public static I18NBundle levelBundle;
    private static Logger log = new Logger(Global.class.getSimpleName(), Logger.DEBUG);
    public static BitmapFont fontBig;
    public static BitmapFont font;
    // список объектов для обработки коллизий
    final static private Array<Collisionable> collObjects = new Array<Collisionable>();

    private Global() {
    }

    public static void load(String packName) {
        locale = Locale.getDefault();
        //assetManager.getLogger().setLevel(Logger.DEBUG);
        assetManager.setLogger(log);
        currentLevel = new AssetDescriptor<TextureAtlas>(packName, TextureAtlas.class);
        assetManager.load(currentLevel);
        assetManager.load(GameConfig.FILE_MAIN_MUSIC_PATH, Music.class);
        assetManager.load(GameConfig.FILE_BOSS_MUSIC_PATH, Music.class);
        assetManager.load("sound/slimeball.mp3", Sound.class);
        assetManager.load("sound/foom_0.mp3", Sound.class);
        assetManager.load("sound/acid6.mp3", Sound.class);
        assetManager.load("button.png", Texture.class);
        assetManager.load(GameConfig.FILE_BOSS_SOUND_PATH, Sound.class);
        assetManager.load(GameConfig.LOCALIZATION_GAMEBUNDLE_PATH, I18NBundle.class, new I18NBundleLoader.I18NBundleParameter(locale));
        assetManager.load(GameConfig.LOCALIZATION_LEVEL1_PATH, I18NBundle.class, new I18NBundleLoader.I18NBundleParameter(locale));
        initFonts();
        assetManager.finishLoading();
        gameBundle = assetManager.get(GameConfig.LOCALIZATION_GAMEBUNDLE_PATH, I18NBundle.class);
        levelBundle = assetManager.get(GameConfig.LOCALIZATION_LEVEL1_PATH, I18NBundle.class);
        log.debug(gameBundle.getLocale().getDisplayName());
        log.debug(gameBundle.getLocale().getDisplayName());

    }

    public static void setMessageLanguage(Locale locale) {
        if (Global.locale.getLanguage().equals(locale.getLanguage())) return;
        assetManager.unload(GameConfig.LOCALIZATION_GAMEBUNDLE_PATH);
        assetManager.unload(GameConfig.LOCALIZATION_LEVEL1_PATH);
        Global.locale = locale;
        assetManager.load(GameConfig.LOCALIZATION_GAMEBUNDLE_PATH, I18NBundle.class, new I18NBundleLoader.I18NBundleParameter(locale));
        assetManager.load(GameConfig.LOCALIZATION_LEVEL1_PATH, I18NBundle.class, new I18NBundleLoader.I18NBundleParameter(locale));

        assetManager.finishLoading();
        gameBundle = assetManager.get(GameConfig.LOCALIZATION_GAMEBUNDLE_PATH, I18NBundle.class);
        levelBundle = assetManager.get(GameConfig.LOCALIZATION_LEVEL1_PATH, I18NBundle.class);
        log.debug("Language changed to  " + levelBundle.getLocale().getDisplayName());
    }

    public static void dispose() {

        assetManager.dispose();
        font.dispose();
        fontBig.dispose();
        batch.dispose();
    }


    public static void initFonts() {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(GameConfig.FILE_FONT_PATH));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.characters = GameConfig.FONT_CHARS;
        parameter.size = 35;
        parameter.color = Color.WHITE;
        font = generator.generateFont(parameter);
        parameter.color = Color.WHITE;
        parameter.size = 75;
        fontBig = generator.generateFont(parameter);
        generator.dispose();
    }

    public static AssetManager getAssetManager() {
        return assetManager;
    }

    public static SpriteBatch getBatch() {
        return batch;
    }

    public static Array<Collisionable> getCollObjects() {
        return collObjects;
    }
}
