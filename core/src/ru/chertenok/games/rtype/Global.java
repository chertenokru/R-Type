package ru.chertenok.games.rtype;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.I18NBundleLoader;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.Logger;
import ru.chertenok.games.rtype.config.GameConfig;

import java.util.Locale;
import java.util.Random;


/**
 * Created by 13th on 08.07.2017.
 */

public class Global {

    final public static AssetManager assetManager = new AssetManager();
    public static AssetDescriptor<TextureAtlas> currentLevel;
    public static Random rnd = new Random();
    public static Locale locale;
    public static I18NBundle gameBundle;
    public static I18NBundle levelBundle;
    private static Logger log = new Logger(Global.class.getSimpleName(), Logger.DEBUG);

    private Global() {
    }

    public static void load(String packName) {
        locale = Locale.getDefault();
        currentLevel = new AssetDescriptor<TextureAtlas>(packName, TextureAtlas.class);
        assetManager.load(currentLevel);
        assetManager.load("sound/through_space.mp3", Music.class);
        assetManager.load("sound/xeon6.mp3", Music.class);
        assetManager.load("sound/slimeball.mp3", Sound.class);
        assetManager.load("sound/foom_0.mp3", Sound.class);
        assetManager.load("sound/acid6.mp3", Sound.class);
        assetManager.load("sound/rlaunch.mp3", Sound.class);
        assetManager.load(GameConfig.LOCALIZATION_GAMEBUNDLE_PATH, I18NBundle.class, new I18NBundleLoader.I18NBundleParameter(locale));
        assetManager.load(GameConfig.LOCALIZATION_LEVEL1_PATH, I18NBundle.class, new I18NBundleLoader.I18NBundleParameter(locale));
        assetManager.finishLoading();
        gameBundle = assetManager.get(GameConfig.LOCALIZATION_GAMEBUNDLE_PATH, I18NBundle.class);
        levelBundle = assetManager.get(GameConfig.LOCALIZATION_GAMEBUNDLE_PATH, I18NBundle.class);
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
        log.debug("Language changed to  " + gameBundle.getLocale().getDisplayName());
    }

    public static void dispose() {

        assetManager.dispose();
    }

    public static float getAngle(float x1, float y1, float x2, float y2) {
        return (float) Math.atan2(y2 - y1, x2 - x1);
    }

    public static float rotateTo(float from, float to, float rotateSpeed, float dt) {
        if (from > to) {
            if (from - to < Math.PI) {
                from -= rotateSpeed * dt;
            } else {
                from += rotateSpeed * dt;
            }
        }
        if (from < to) {
            if (to - from < Math.PI) {
                from += rotateSpeed * dt;
            } else {
                from -= rotateSpeed * dt;
            }
        }
        return from;
    }


    public static boolean isAndroid() {
        return Gdx.app.getType() == Application.ApplicationType.Android;
    }
}
