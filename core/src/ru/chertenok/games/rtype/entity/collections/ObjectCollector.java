package ru.chertenok.games.rtype.entity.collections;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import ru.chertenok.games.rtype.Sprites;
import ru.chertenok.games.rtype.config.GameConfig;
import ru.chertenok.games.rtype.screens.GameScreenController;

/**
 * Created by 13th on 18-Jul-17.
 */
public abstract class ObjectCollector extends Sprites {
    protected int objectCount = 0;
    protected  float createWaitDT = 1;
    protected  float createWaitDTCounter = 1;
    protected float maxSpeed = -1;
    protected  float minSpeed = -1;
    protected int  currentTextureNo = 0;
    private final Class _class;
    private Object obj;
    protected Array<ru.chertenok.games.rtype.entity.GameInnerObject> activeObject;
    protected Pool<ru.chertenok.games.rtype.entity.GameInnerObject> objectPool;
    private ru.chertenok.games.rtype.entity.GameInnerObject gameInnerObject;


    public int getObjectCount() {
        return objectCount;
    }

    public ObjectCollector(Class newClass, GameScreenController game, String textureName, int textureCount) throws Exception {
        super(game, textureName, textureCount);
        // проверяем что нам подсовывают
        if (!ru.chertenok.games.rtype.entity.GameInnerObject.class.isAssignableFrom(newClass)) {
            // фу таким быть
            throw new Exception("Класс подаваеммый в конструктор класса ObjectCollector должен быть наследником " + ru.chertenok.games.rtype.entity.GameInnerObject.class.getName());
        }

        activeObject = new Array<ru.chertenok.games.rtype.entity.GameInnerObject>();
        this._class = newClass;
        objectPool =
                new Pool() {
                    @Override
                    protected Object newObject() {
                        obj = null;
                        try {
                            obj = _class.newInstance();
                        } catch (InstantiationException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                        return obj;
                    }
                };
    }


    public Array<ru.chertenok.games.rtype.entity.GameInnerObject> getActiveObject() {
        return activeObject;
    }

    public void update(float dt) {

        createWaitDTCounter += dt;
        for (int i = 0; i < activeObject.size; i++) {
            if (activeObject.get(i).isActive()) {
                activeObject.get(i).update(dt, GameConfig.getWorldWidth(), GameConfig.getWorldHeight(), maxSpeed);

            }
            // если улетел, но обещал вернуться, то в пул его
            if (!activeObject.get(i).isActive()){
                objectPool.free(activeObject.get(i));
                // выпиливаем из списка объектов для проверки коллизий
                if (activeObject.get(i).isCollisinable()) game.collObjects.removeValue(activeObject.get(i),true);
                activeObject.removeIndex(i);
                //      System.out.println("-1");
            }
        }

        // если число объектов меньше запрошенного и прошла задержка то создаём новый
        if (activeObject.size < objectCount && createWaitDTCounter>createWaitDT ){
            gameInnerObject =  objectPool.obtain();
            init(gameInnerObject,gameInnerObject.originSpriteSize.x,gameInnerObject.originSpriteSize.y);
            activeObject.add(gameInnerObject);
            createWaitDTCounter =0;
            //    System.out.println("+1");
        }
    }

    protected void init(ru.chertenok.games.rtype.entity.GameInnerObject gameInnerObject, float spriteOriginSizeX, float spriteOriginSizeY) {
        gameInnerObject.originSpriteSize.y = spriteOriginSizeY;
        gameInnerObject.originSpriteSize.x = spriteOriginSizeX;
        gameInnerObject.isActive = true;
        // добавляем в список сталкиваемых объектов
        if (gameInnerObject.isCollisinable()) game.collObjects.add(gameInnerObject);
    }


    public void reset() {
        for (int i = 0; i < activeObject.size; i++) {
            activeObject.get(i).isActive = false;
            activeObject.get(i).reset();
            objectPool.free(activeObject.get(i));
            // если сталкиваемый, то удаляем из списка
            if (gameInnerObject.isCollisinable())  game.collObjects.removeValue(activeObject.get(i),true);
            activeObject.removeIndex(i);

        }
    }


}
