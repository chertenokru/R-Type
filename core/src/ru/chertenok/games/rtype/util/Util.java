package ru.chertenok.games.rtype.util;

public class Util {

    private Util() {
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


}
