package ru.chertenok.games.rtype.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import ru.chertenok.games.rtype.R_Type;
import ru.chertenok.games.rtype.Sprites;
import ru.chertenok.games.rtype.objects.collections.Bullets;

/**
 * Created by 13th on 02.07.2017.
 */
public class ShipControl extends Sprites  {

    private final int MAX_ENERGY = 100;
    private final int MAX_LIVE = 1;
    private final int CONST_DX = 170;
    private final int CONST_DY = 120;
    private int live = MAX_LIVE;
    private Rectangle rectangle = new Rectangle();

    private float dtWait = 0.05f;
    private float dtWait1 = 1f;
    private float dtCounter = 0f;
    private float dtCounter1 = 0f;
    private float dtCounter2 = 0f;
    private boolean isFired = false;
    private Bullets bullets;
    private boolean isEnableFire = true;
    private float fireSpeed = 0.3f;
    private boolean isRecharge = false;
    private boolean isRechargeEnabled = false;
    private float oldx = -1;
    private float oldy = -1;
    public Ship ship = new Ship();

    public ShipControl(R_Type game) {
        super(game,"ship",3);
        this.bullets = game.bullets;
        ship.position = new Vector2(defaultPosition);
        ship.velocity = new Vector2(0, 0);
        ship.originSpriteSize.x = spriteSizeX;
        ship.originSpriteSize.y = spriteSizeY;
        ship.energy = MAX_ENERGY;
    }


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

    public Vector2 defaultPosition = new Vector2(10, 300);

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

    public int getEnergy() {
        return ship.energy;
    }

    public void setEnergy(int energy) {
        this.ship.energy = energy;
    }

    public Rectangle getPosition() {
        return rectangle.set(ship.position.x, ship.position.y + spriteSizeY / 3, spriteSizeX, spriteSizeY - (spriteSizeY / 3) * 2);
    }





    public void render(SpriteBatch batch) {
        if (ship.isDamaging())
            batch.draw(texture[1], ship.position.x-ship.originSpriteSize.x/2, ship.position.y-ship.originSpriteSize.y/2);
        else if (isFired)
            batch.draw(texture[2], ship.position.x-ship.originSpriteSize.x/2, ship.position.y-ship.originSpriteSize.y/2);
        else
            batch.draw(texture[0], ship.position.x-ship.originSpriteSize.x/2, ship.position.y-ship.originSpriteSize.y/2);
    }



    public void reset() {
        live = MAX_LIVE;
        ship.energy = MAX_ENERGY;
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

        if (ship.energy <= 0 && !isFired) {
            isFired = true;
            ship.setDamaging(false);
        }

        ship.position.mulAdd(ship.velocity, -dt);


        if (!isFired) {

            if (Gdx.input.isTouched())
            {
                if (oldx == -1){
                    oldx = Gdx.input.getX();
                    oldy = Gdx.input.getY();
                } else
                {
                    float x = Gdx.input.getX();
                    float y = Gdx.input.getY();
                    if (oldx > x ) ship.position.x -= CONST_DX * dt*(oldx-x)*0.4;
                    if (oldx < x) ship.position.x += CONST_DX * dt*(x- oldx)*0.4;
                    if (oldy < y) ship.position.y -= CONST_DY * dt*(y- oldy)*0.4;
                    if (oldy > y ) ship.position.y += CONST_DY * dt*(oldy-y)*0.4;
                    oldx = Gdx.input.getX();
                    oldy = Gdx.input.getY();
                }



            } else
            {
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
                bullets.addBullet(ship.position.x + spriteSizeX, ship.position.y + spriteSizeY / 3);
                dtCounter2 = 0;
            }
        }


        if (ship.position.y > (game.viewport.getWorldHeight() - spriteSizeY))
            ship.position.y = (game.viewport.getWorldHeight() - spriteSizeY);

        if (ship.position.y < -spriteSizeY / 4) ship.position.y = -spriteSizeY / 4;

        if (ship.position.x < 0) ship.position.x = 0;

        if (ship.position.x > (game.viewport.getWorldWidth() - spriteSizeX))
            ship.position.x = (game.viewport.getWorldWidth() - spriteSizeX);

        //if (dtWait1 < dtCounter1) {
        ship.velocity.scl(0.97f);
        // }
    }




}
