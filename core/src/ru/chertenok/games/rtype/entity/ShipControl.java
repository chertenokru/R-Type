package ru.chertenok.games.rtype.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Logger;
import ru.chertenok.games.rtype.Sprites;
import ru.chertenok.games.rtype.config.GameConfig;
import ru.chertenok.games.rtype.level.Level;
import ru.chertenok.games.rtype.level.LevelEvents;
import ru.chertenok.games.rtype.screens.game.GameScreenController;

import java.util.Map;

/**
 * Created by 13th on 02.07.2017.
 */
public class ShipControl extends Sprites implements Level.ILevelEvent {

    private static Logger log = new Logger(ShipControl.class.getSimpleName(), Logger.DEBUG);
    private final int MAX_ENERGY = 100;
    private final int MAX_LIVE = 1;
    private final int CONST_DX = 170;
    private final int CONST_DY = 120;
    private int live = MAX_LIVE;
    private Rectangle rectangle = new Rectangle();

    private float dtWait = 0.9f;
    private float dtWait1 = 1f;
    private float dtCounter = 0f;
    private float dtCounter1 = 0f;
    private float dtCounter2 = 0f;
    private boolean isFired = false;
    private ru.chertenok.games.rtype.entity.collections.Bullets bullets;
    private boolean isEnableFire = true;
    private float fireSpeed = 0.3f;
    private boolean isRecharge = false;
    private boolean isRechargeEnabled = false;
    private float oldx = -1;
    private float oldy = -1;
    private Vector2 defaultPosition = new Vector2(70, 300);
    public Ship ship = new Ship();


    public boolean isRechargeEnabled() {
        return isRechargeEnabled;
    }

    public void setRechargeEnabled(boolean rechargeEnabled) {
        isRechargeEnabled = rechargeEnabled;
    }

    public boolean isRecharge() {
        return isRecharge;
    }

    public void setRecharge(boolean recharge) {
        isRecharge = recharge;
    }


    public int getLive() {
        return live;
    }

    public int getMAX_ENERGY() {
        return MAX_ENERGY;
    }

    public void setLive(int live) {
        this.live = live;
    }


    public boolean isEnableFire() {
        return isEnableFire;
    }

    public void setEnableFire(boolean enableFire) {
        isEnableFire = enableFire;
    }

    public ShipControl(GameScreenController game) {
        super(game, "ship", 3);
        this.bullets = game.bullets;
        ship.position = new Vector2(defaultPosition);
        ship.velocity = new Vector2(0, 0);
        ship.originSpriteSize.x = spriteSizeX;
        ship.originSpriteSize.y = spriteSizeY;
        ship.setEnergy(MAX_ENERGY);
    }

    public int getEnergy() {
        return ship.getEnergy();
    }

    public void setEnergy(int energy) {
        this.ship.setEnergy(energy);
    }


    public void render(SpriteBatch batch) {
        if (ship.isDamaging()) {
            //game.camera.zoom -= 0.5 * Gdx.graphics.getDeltaTime();
            //game.camera.update();

            batch.draw(texture[1], ship.position.x - ship.originSpriteSize.x / 2, ship.position.y - ship.originSpriteSize.y / 2);
        } else {
            //if (game.camera.zoom != 1) {
//                game.camera.zoom = 1;
//                game.camera.update();
//}

            if (isFired)
                batch.draw(texture[2], ship.position.x - ship.originSpriteSize.x / 2, ship.position.y - ship.originSpriteSize.y / 2);
            else
                batch.draw(texture[0], ship.position.x - ship.originSpriteSize.x / 2, ship.position.y - ship.originSpriteSize.y / 2);
        }

    }

    public void reset() {
        live = MAX_LIVE;
        ship.setEnergy(MAX_ENERGY);
        isFired = false;
        ship.position.set(defaultPosition);
        isEnableFire = true;
        isRecharge = false;
        isRechargeEnabled = false;

    }

    public void update(float dt) {
        dtCounter += dt;
        dtCounter1 += dt;
        dtCounter2 += dt;

        if (ship.isDamaging() && (dtCounter > dtWait)) {
            ship.setDamaging(false);
            dtCounter = 0;
            Gdx.input.vibrate(500);
        }

        if (ship.getEnergy() <= 0 && !isFired) {
            isFired = true;
            ship.setDamaging(false);
        }

        ship.position.mulAdd(ship.velocity, -dt);


        if (!isFired) {

            if (Gdx.input.isTouched()) {
                if (oldx == -1) {
                    oldx = Gdx.input.getX();
                    oldy = Gdx.input.getY();
                } else {
                    float x = Gdx.input.getX();
                    float y = Gdx.input.getY();
                    if (oldx > x) ship.position.x -= CONST_DX * dt * (oldx - x) * 0.4;
                    if (oldx < x) ship.position.x += CONST_DX * dt * (x - oldx) * 0.4;
                    if (oldy < y) ship.position.y -= CONST_DY * dt * (y - oldy) * 0.4;
                    if (oldy > y) ship.position.y += CONST_DY * dt * (oldy - y) * 0.4;
                    oldx = Gdx.input.getX();
                    oldy = Gdx.input.getY();
                }


            } else {
                oldx = -1;
                oldy = -1;
            }


            if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W))
                ship.position.y += CONST_DY * dt;
            if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S))
                ship.position.y -= CONST_DY * dt;
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A))
                ship.position.x -= CONST_DX * dt;
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D))
                ship.position.x += CONST_DX * dt;
            if ((Gdx.input.isKeyPressed(Input.Keys.SPACE) || Gdx.input.isTouched()) && (fireSpeed <= dtCounter2) && isEnableFire) {
                bullets.addBullet(ship.position.x + ship.originSpriteSize.x / 2, ship.position.y);
                dtCounter2 = 0;
            }
        }


        // check position on screen
        ship.position.y = MathUtils.clamp(ship.position.y, ship.getHitAreaRectangle().height / 2, GameConfig.getWorldHeight() - ship.getHitAreaRectangle().height / 2);
        ship.position.x = MathUtils.clamp(ship.position.x, ship.getHitAreaRectangle().width / 2, GameConfig.getWorldWidth() - ship.getHitAreaRectangle().width / 2);


        // затухание скорости
        //rate decay
        ship.velocity.scl(0.97f);

    }


    @Override
    public void event(LevelEvents.LevelEvent event) {
        log.debug("ShipControl:event - " + event);
        if (event.Name.equals(GameConfig.SHIP_SET_SHIELD)) {
            if (event.param.length > 0) setRechargeEnabled(Boolean.valueOf(event.param[0]));
            return;
        } else if (event.Name.equals(GameConfig.SHIP_SET_FIRE)) {
            if (event.param.length > 0) setEnableFire(Boolean.valueOf(event.param[0]));
            return;
        }
    }

    @Override
    public void registerLevelEvents(Map<String, Level.ILevelEvent> eventMap) {
        eventMap.put(GameConfig.SHIP_SET_SHIELD, this);
        eventMap.put(GameConfig.SHIP_SET_FIRE, this);

    }
}
