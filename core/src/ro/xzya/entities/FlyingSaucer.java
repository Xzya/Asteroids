package ro.xzya.entities;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;

import java.util.ArrayList;

import ro.xzya.game.Game;
import ro.xzya.managers.Jukebox;

/**
 * Created by Xzya on 5/3/2015.
 */
public class FlyingSaucer extends SpaceObject {

    private static final int dpSize1 = ((int)(Game.HEIGHT * 0.025)); //10px
    private static final int dpSize2 = ((int)(Game.HEIGHT * 0.0075)); //3px
    private static final int dpSize3 = ((int)(Game.HEIGHT * 0.0125)); //5px
    private static final int dpSize4 = ((int)(Game.HEIGHT * 0.015)); //6px
    private static final int dpSize5 = ((int)(Game.HEIGHT * 0.005)); //2px

    private ArrayList<Bullet> bullets;

    private int type;

    public static final int LARGE = 0;
    public static final int SMALL = 1;

    private int score;

    private float fireTimer;
    private float fireTime;

    private Player player;

    private float pathTimer;
    private float pathTime1;
    private float pathTime2;

    private int direction;
    public static final int LEFT = 0;
    public static final int RIGHT = 1;

    private boolean remove;

    public FlyingSaucer(
            int type,
            int direction,
            Player player,
            ArrayList<Bullet> bullets) {
        this.type = type;
        this.direction = direction;
        this.player = player;
        this.bullets = bullets;

//        speed = 70;
        speed = ((int)(Game.WIDTH * 0.13));
        if (direction == LEFT) {
            dx = -speed;
            x = Game.WIDTH;
        } else if (direction == RIGHT) {
            dx = speed;
            x = 0;
        }
        y = MathUtils.random(Game.HEIGHT);

        shapex = new float[6];
        shapey = new float[6];
        setShape();

        if (type == LARGE) {
            score = 200;
            Jukebox.loop("largesaucer");
        } else if (type == SMALL) {
            score = 1000;
            Jukebox.loop("smallsaucer");
        }

        fireTimer = 0;
        fireTime = 1;
                        // ---
        pathTimer = 0;  //    \____
        pathTime1 = 2;  //goes left or right 2 seconds
        pathTime2 = pathTime1 + 2;  //changes direction and moves for 2 seconds

    }

    private void setShape() {
        if (type == LARGE) {
//            shapex[0] = x - 10;
//            shapey[0] = y;
//
//            shapex[1] = x - 3;
//            shapey[1] = y - 5;
//
//            shapex[2] = x + 3;
//            shapey[2] = y - 5;
//
//            shapex[3] = x + 10;
//            shapey[3] = y;
//
//            shapex[4] = x + 3;
//            shapey[4] = y + 5;
//
//            shapex[5] = x - 3;
//            shapey[5] = y + 5;

            shapex[0] = x - dpSize1;
            shapey[0] = y;

            shapex[1] = x - dpSize2;
            shapey[1] = y - dpSize3;

            shapex[2] = x + dpSize2;
            shapey[2] = y - dpSize3;

            shapex[3] = x + dpSize1;
            shapey[3] = y;

            shapex[4] = x + dpSize2;
            shapey[4] = y + dpSize3;

            shapex[5] = x - dpSize2;
            shapey[5] = y + dpSize3;

        } else if (type == SMALL) {
//            shapex[0] = x - 6;
//            shapey[0] = y;
//
//            shapex[1] = x - 2;
//            shapey[1] = y - 3;
//
//            shapex[2] = x + 2;
//            shapey[2] = y - 3;
//
//            shapex[3] = x + 6;
//            shapey[3] = y;
//
//            shapex[4] = x + 2;
//            shapey[4] = y + 3;
//
//            shapex[5] = x - 2;
//            shapey[5] = y + 3;

            shapex[0] = x - dpSize4;
            shapey[0] = y;

            shapex[1] = x - dpSize5;
            shapey[1] = y - dpSize2;

            shapex[2] = x + dpSize5;
            shapey[2] = y - dpSize2;

            shapex[3] = x + dpSize4;
            shapey[3] = y;

            shapex[4] = x + dpSize5;
            shapey[4] = y + dpSize2;

            shapex[5] = x - dpSize5;
            shapey[5] = y + dpSize2;
        }
    }

    public int getScore() {
        return score;
    }

    public boolean shouldRemove() {
        return remove;
    }

    public void update(float dt) {

        //fire
        if (!player.isHit()) {
            fireTimer += dt;
            if (fireTimer > fireTime) {
                fireTimer = 0;
                if (type == LARGE) {
                    radians = MathUtils.random(2 * 3.1415f);
                } else if (type == SMALL) {
                    //use atan2 to get the angle twards the player
                    radians = MathUtils.atan2(
                            player.gety() - y,
                            player.getx() - x
                    );
                }
                bullets.add(new Bullet(x, y, radians));
                Jukebox.play("saucershoot");
            }
        }

        //move along path
        pathTimer += dt;

        //move forward
        if (pathTimer < pathTime1) {
            dy = 0;
        }

        //move downward
        if (pathTimer > pathTime1 && pathTimer < pathTime2) {
            dy = -speed;
        }

        //move forward again
        if (pathTimer > pathTime1 + pathTime2) {
            dy = 0;
        }

        x += dx * dt;
        y += dy * dt;

        //screen wrap
        if (y < 0) y = Game.HEIGHT;

        //set shape
        setShape();

        //check if remove
        if ((direction == RIGHT && x > Game.WIDTH) ||
                (direction == LEFT && x < 0)){
            remove = true;
        }

    }

    public void draw(ShapeRenderer sr) {
        sr.setProjectionMatrix(Game.cam.combined);
        sr.begin(ShapeRenderer.ShapeType.Line);

        for(int i = 0, j = shapex.length - 1;
                i < shapex.length;
                j = i++) {
            sr.line(shapex[i], shapey[i], shapex[j], shapey[j]);
        }

        sr.line(shapex[0], shapey[0], shapex[3], shapey[3]);

        sr.end();
    }




}
