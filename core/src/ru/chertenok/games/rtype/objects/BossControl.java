package ru.chertenok.games.rtype.objects;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import ru.chertenok.games.rtype.Global;
import ru.chertenok.games.rtype.R_Type;
import ru.chertenok.games.rtype.Sprites;

import static com.badlogic.gdx.scenes.scene2d.InputEvent.Type.exit;

public class BossControl extends Sprites {

    private Boss boss;
    private float counterFireDt = 0;
    private float dtScale = 0;

    private float fireDt = 3f;

    private float dtColor = 0f;
    private float angle;
    private Color colorBoss = new Color(1, 1, 1, 1);
    private final int MAX_ENERGY = 500;
    public Vector2 defaultPosition = new Vector2(70, 300);
    private Sound soundFire;


    public int getMAX_ENERGY() {
        return MAX_ENERGY;
    }

    public BossControl(R_Type game) {
        super(game, "boss", 1);
        boss = new Boss();
        soundFire = Global.assestManager.get("rlaunch.mp3", Sound.class);
        reset();
    }


    public Boss getBoss() {
        return boss;
    }


    public void update(float dt) {
        if (!boss.isActive) return;
        dtColor += dt;
        counterFireDt += dt;

        colorBoss.g -= (1 / fireDt) * dt;
        colorBoss.b -= (1 / fireDt) * dt;

        if (counterFireDt > fireDt) {
            counterFireDt = 0;
            boss.vector.set(boss.position);
            angle = Global.getAngle(boss.position.x, boss.position.y, game.shipControl.ship.position.x, game.shipControl.ship.position.y);
            game.asteroids.addAsteroid(boss.position.x, boss.position.y, 150 * (float) Math.cos(angle) * -1, 150 * (float) Math.sin(angle) * -1, 0.5f);
            angle = Global.getAngle(boss.position.x, boss.position.y + game.asteroids.spriteSizeY, game.shipControl.ship.position.x, game.shipControl.ship.position.y);
            game.asteroids.addAsteroid(boss.position.x + game.asteroids.spriteSizeX * (float) Math.sin(angle), boss.position.y + game.asteroids.spriteSizeY * (float) Math.cos(angle), 250 * (float) Math.cos(angle - 0.79) * -1, 250 * (float) Math.sin(angle - 0.79) * -1, 0.5f);
            angle = Global.getAngle(boss.position.x, boss.position.y - game.asteroids.spriteSizeY, game.shipControl.ship.position.x, game.shipControl.ship.position.y);
            game.asteroids.addAsteroid(boss.position.x - game.asteroids.spriteSizeX * (float) Math.sin(angle), boss.position.y - game.asteroids.spriteSizeY * (float) Math.cos(angle), 250 * (float) Math.cos(angle + 0.79) * -1, 250 * (float) Math.sin(angle + 0.79) * -1, 0.5f);
            if (game.isSoundcOn) soundFire.play();
            colorBoss.g = 1;
            colorBoss.b = 1;
        }
        if (dtColor > .1f) {
            colorBoss.b -= 0.1f;
            dtColor = 0;
        }

        if (boss.position.x > game.viewport.getWorldWidth() - boss.originSpriteSize.x * 1.5) {
            boss.position.x -= 50 * dt;
        }
        boss.update(dt);
    }


    public void render(SpriteBatch batch) {

        if (!boss.isActive) return;
        batch.setColor(colorBoss);
        //System.out.printf("x %f, y %f",boss.position.x,boss.position.y);
        batch.draw(texture[textureCount - 1], boss.position.x - spriteSizeX / 2, boss.position.y - spriteSizeY / 2, spriteSizeX / 2, spriteSizeY / 2, spriteSizeX, spriteSizeY, 0.9f, 0.9f, boss.angle, false);
        batch.draw(texture[textureCount - 1], boss.position.x - spriteSizeX / 2, boss.position.y - spriteSizeY / 2, spriteSizeX / 2, spriteSizeY / 2, spriteSizeX, spriteSizeY, 0.5f, 0.5f, boss.angle + 90, false);
        batch.draw(texture[textureCount - 1], boss.position.x - spriteSizeX / 2, boss.position.y - spriteSizeY / 2, spriteSizeX / 2, spriteSizeY / 2, spriteSizeX, spriteSizeY, 0.3f, 0.3f, boss.angle + 270, false);
        batch.setColor(1, 1, 1, 1);
        // batch.draw(t,500,300,514/2,324/2,514,324,scaleT,scaleT,dtT,0,0,514,324,false,false);
    }

    public void reset() {
        boss.angleInc = 60;
        boss.damage = 50;
        boss.scope = 2000;
        boss.originSpriteSize.x = spriteSizeX;
        boss.originSpriteSize.y = spriteSizeY;
        boss.isActive = false;
        boss.position.x = game.viewport.getWorldWidth() + spriteSizeY;
        boss.position.y = game.viewport.getWorldHeight() / 2;
        boss.live = getMAX_ENERGY();
    }

    public void setActive() {
        boss.setActive(true);
    }

}
