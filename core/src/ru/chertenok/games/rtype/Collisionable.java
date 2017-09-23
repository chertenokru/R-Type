package ru.chertenok.games.rtype;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;


/**
 * Created by 13th on 14-Jul-17.
 */
public interface Collisionable {
    enum HitAreaType {Rectangle, Circle}

    enum ObjectType {Enemy, Ship, BulletAI, BulletPlayer, Box, Explosion, Asteroid, Boss}

    // вид области объекта
    HitAreaType getHitAreaType();
    // круглая область
    Circle getHitAreaCircle();
    // квадратная область
    Rectangle getHitAreaRectangle();
    // столкновение, нужно ли удалить объект, параметр - объект столкновения
    //  и кривизна в виде ссылки на главный класс для реализации реакции на столкновение
    boolean hitIsRemove(R_Type game, Collisionable collisionObject);
    // надо ли для объекта обрабатывать столкновения
    boolean isCollisinable();
    // тип объекта
    ObjectType getObjectType();
    //  сколько он наносит повреждения
    int getDamage( );
    boolean isActive();
    void setNoActive();
}
