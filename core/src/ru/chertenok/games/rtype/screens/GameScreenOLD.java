/**
 * see Udemy lesson
 * =====================
 * 28.09
 * fix error level localization
 * add hit on GameInnerObject
 * reneim  metod hit in Collisionable and add revoke in every
 * <p>
 * 27.09
 * add score animation
 * <p>
 * 24.09
 * add localization ru + default (eng)
 * moving Level event to json
 * moving level event handler to object controller
 * <p>
 * <p>
 * 23.09
 * add GameConfig class
 * add GameConfig.json
 * <p>
 * <p>
 * geekbrains.ru
 * =======================
 * <p>
 * <p>
 * К уроку 7
 * ---------------
 * черновая реализация босса
 * <p>
 * <p>
 * К уроку 6
 * ---------------
 * 1. альфа версия в гугл-маркете  https://play.google.com/apps/testing/ru.chertenok.games.rtype
 * 2. взрывы при столкновении пуль
 * <p>
 * <p>
 * <p>
 * <p>
 * К уроку 5
 * --------------
 * 1. камера и фитпортвиев, заменил везде Gdx.graphics. на viewport. размер
 * 2. адаптация  разрешения для андроид
 * 3. адаптация управления для андроид
 * 4. замена кнопок 1, пауза на сенсорные экранные
 * 5. игровые объъекты наследуются от gameInnerObject, пока кроме ShipControl игрока
 * 6. интерфейс Collisionable для сталкиваемых объектов, для дальнейшей реализации общего просчёта столкновений
 * <p>
 * К уроку 4
 * -------------------
 * 1. Багофиксы - при перезапуске
 * 2. Тайминги уровня в класс Level
 * 3. Текстура заменена на текстураАтлас с загрузкой через AssestManager
 * 4. Локальные Random вынесены в глобальный Global
 * 5. Создан сомнительный класс Sprites - предок всех объектов или эмиттеров объектов
 * 6. Перезадка энергии корабля за счёт бонусов
 * 7. Иконки щита и паузы на экране - адаптация для андроида
 * 8. Класс врагов с 2-мя разновидностями
 * 9. 3 вида пуль с разными текстурами
 * <p>
 * <p>
 * <p>
 * <p>
 * К уроку 3
 * ------------
 * <p>
 * 1. Добавлены жизни в объект Chip,Enemy
 * 2. Добавлены очки в GameScreen
 * 3. добавлены состояния игрового мира - Run,Pause,End
 * 4. добавлен класс Messages - отображающий сообщения на экране во время игры
 * 5. добавлены сообщения о потери жизни, о потерях энергии, о получении очков
 * 6. добавлена при старте игры сюжетная информация и информация об управлении
 * 7. проверка столкновения корабля с "фоновыми коробками"
 * 8. Столкновения астероидов с "коробками" - взрыв и оба объекта уничтожаются
 * 9. Масштаб в астероидах и максимальное кол-во как настраиваемый параметр
 * 10. Урон, уровень прочности  и очки получаемые от астероида зависят от его размера
 * 11. Добавлен таймер уровня и попытка менять настройки среды по таймеру, по окончанию таймера должен быть биг босс, но его нет :)
 * 12. Убрал коробки снизу для следующего уровня
 * 13. Добавил реверсивное движение аастероидов
 **/

// todo полный сброс при перезапуске
// todo класс для летающих объектов
// todo интерфейс для столковений и список объектов


package ru.chertenok.games.rtype.screens;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.utils.Logger;
import ru.chertenok.games.rtype.level.Level;

@Deprecated
public class GameScreenOLD extends ApplicationAdapter implements Level.ILevelEvent {

    private static Logger log = new Logger(GameScreenOLD.class.getSimpleName(), Logger.DEBUG);



    @Override
    public void create() {




    }







    @Override



}
