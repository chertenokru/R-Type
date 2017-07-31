package ru.chertenok.games.rtype.objects.collections;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import ru.chertenok.games.rtype.R_Type;
import ru.chertenok.games.rtype.Sprites;
import ru.chertenok.games.rtype.objects.GameInnerObject;

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
    final protected  Class _class;
    private Object obj;
    private GameInnerObject gameInnerObject;

    protected  Array<ru.chertenok.games.rtype.objects.GameInnerObject> activeObject;
    protected Pool<GameInnerObject> objectPool;


    public int getObjectCount() {
        return objectCount;
    }

    public ObjectCollector(Class  newClass, R_Type game, String textureName, int textureCount) throws Exception {
        super(game, textureName, textureCount);
        // проверяем что нам подсовывают
        if (!GameInnerObject.class.isAssignableFrom( newClass)) {
            // фу таким быть
            throw new Exception("Класс подаваеммый в конструктор класса ObjectCollector должен быть наследником "+GameInnerObject.class.getName());
        }

        activeObject = new Array<GameInnerObject>();
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


    public Array<GameInnerObject> getActiveObject() {
        return activeObject;
    }

    public void update(float dt) {

        createWaitDTCounter += dt;
        for (int i = 0; i < activeObject.size; i++) {
            if (activeObject.get(i).isActive()) {
                activeObject.get(i).update(dt, game.viewport.getWorldWidth(), game.viewport.getWorldHeight(),maxSpeed);

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

    protected  void init(GameInnerObject gameInnerObject,float spriteOriginSizeX,float spriteOriginSizeY){
        gameInnerObject.originSpriteSize.y = spriteOriginSizeY;
        gameInnerObject.originSpriteSize.x = spriteOriginSizeX;
        gameInnerObject.isActive = true;
        // добавляем в список сталкиваемых объектов
        if (gameInnerObject.isCollisinable()) game.collObjects.add(gameInnerObject);
    };


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
