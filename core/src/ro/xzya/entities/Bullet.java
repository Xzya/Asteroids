package ro.xzya.entities;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;

import ro.xzya.game.Game;

/**
 * Created by Xzya on 4/3/2015.
 */
public class Bullet extends SpaceObject {

    private static final int dpSize1 = ((int)(Game.WIDTH * 0.6));

    private float lifeTime;
    private float lifeTimer;

    private boolean remove;

    /**
     * @param x       - starting position x
     * @param y       - starting position y
     * @param radians - direction
     */
    public Bullet(float x, float y, float radians) {
        this.x = x;
        this.y = y;
        this.radians = radians;

//        float speed = 350;
        float speed = dpSize1;

        dx = MathUtils.cos(radians) * speed;
        dy = MathUtils.sin(radians) * speed;

//        width = height = 2;
        width = height = ((int)(Game.HEIGHT * 0.005));

        lifeTimer = 0;
//        lifeTime = 1f;
        lifeTime = 1f;
    }

    public boolean shouldRemove() {
        return remove;
    }

    public void update(float dt) {
        x += dx * dt;
        y += dy * dt;

        wrap();

        lifeTimer += dt;

        if (lifeTimer > lifeTime) {
            remove = true;
        }
    }

    public void draw(ShapeRenderer sr) {
        sr.setColor(1, 1, 1, 1);
        sr.begin(ShapeRenderer.ShapeType.Filled);
        sr.circle(x - width / 2, y - height / 2, width / 2);
        sr.end();
    }

}
