package ru.chertenok.games.rtype.collisions;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.utils.Array;

public class CollisionChecker {

    private static Collisionable collisionable1;
    private static Collisionable collisionable2;


    private CollisionChecker() {
    }

    public static void update(Array<Collisionable> collObjects) {
        // обработка столкновений всего со всем 8-)
        for (int i = collObjects.size - 1; i > 0; i--) {
            // если не активен, то просто  выкидываем и чешем дальше
            if (!collObjects.get(i).isActive()) {
                collObjects.removeIndex(i);
                continue;
            }

            for (int j = i - 1; j >= 0; j--) {
                // если не активен, то просто  чешем дальше
                if (!collObjects.get(j).isActive()) continue;


                boolean isCollision = false;
                collisionable1 = collObjects.get(i);
                collisionable2 = collObjects.get(j);
                if (collisionable1.isCollisinable() && collisionable2.isCollisinable()) {

                    // круги
                    if (collisionable1.getHitAreaType() == Collisionable.HitAreaType.Circle && collisionable2.getHitAreaType() == Collisionable.HitAreaType.Circle)
                        if (collisionable1.getHitAreaCircle().overlaps(collisionable2.getHitAreaCircle()))
                            isCollision = true;
                    // прямоугольники
                    if (collisionable1.getHitAreaType() == Collisionable.HitAreaType.Rectangle && collisionable2.getHitAreaType() == Collisionable.HitAreaType.Rectangle)
                        if (collisionable1.getHitAreaRectangle().overlaps(collisionable2.getHitAreaRectangle()))
                            isCollision = true;
                    // прямоугольник и круг
                    if (collisionable1.getHitAreaType() == Collisionable.HitAreaType.Rectangle && collisionable2.getHitAreaType() == Collisionable.HitAreaType.Circle)
                        if (Intersector.overlaps(collisionable2.getHitAreaCircle(), collisionable1.getHitAreaRectangle()))
                            isCollision = true;
                    //  круг и прямоугольник
                    if (collisionable2.getHitAreaType() == Collisionable.HitAreaType.Rectangle && collisionable1.getHitAreaType() == Collisionable.HitAreaType.Circle)
                        if (Intersector.overlaps(collisionable1.getHitAreaCircle(), collisionable2.getHitAreaRectangle()))
                            isCollision = true;

                    // тот не активный и потом в помойку
                    if (collisionable1.hitStatus_and_IsRemove(null, collisionable2, isCollision)) {
                        collisionable1.setNoActive();
                    }
                    // этот не активный и потом в помойку
                    if (collisionable2.hitStatus_and_IsRemove(null, collisionable1, isCollision)) {
                        collisionable2.setNoActive();
                    }
                }
            }
            if (!collisionable1.isActive()) collObjects.removeIndex(i);

        }
    }

}


