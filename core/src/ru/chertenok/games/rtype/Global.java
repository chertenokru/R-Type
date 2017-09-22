package ru.chertenok.games.rtype;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Logger;

import java.util.Random;


/**
 * Created by 13th on 08.07.2017.
 */

public class Global {


    final public static AssetManager assestManager = new AssetManager();
    public static AssetDescriptor<TextureAtlas> currentLevel;

    public static Random rnd = new Random();


    public static void load(String packName) {
        currentLevel = new AssetDescriptor<TextureAtlas>(packName, TextureAtlas.class);
        assestManager.load(currentLevel);
        assestManager.load("sound/through_space.mp3", Music.class);
        assestManager.load("sound/xeon6.mp3", Music.class);
        assestManager.load("sound/slimeball.mp3", Sound.class);
        assestManager.load("sound/foom_0.mp3", Sound.class);
        assestManager.load("sound/acid6.mp3", Sound.class);
        assestManager.load("sound/rlaunch.mp3", Sound.class);





        assestManager.finishLoading();
    }

    public static void dispose() {

        assestManager.dispose();
    }

    public static float getAngle(float x1, float y1, float x2, float y2) {
        return (float) Math.atan2(y2 - y1, x2 - x1);
    }

    public static float rotateTo(float from, float to, float rotateSpeed, float dt) {
        if (from > to) {
            if (from - to < Math.PI) {
                from -= rotateSpeed * dt;
            } else {
                from +=rotateSpeed * dt;
            }
        }
        if (from < to){
            if (to - from < Math.PI){
                from +=rotateSpeed * dt;
            } else {
                from -=rotateSpeed *dt;
            }
        }
        return from;
    }

    public static Logger getLogger(Class clazz) {
        return new Logger(clazz.getSimpleName());
    }

}
