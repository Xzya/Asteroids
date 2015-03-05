package ro.xzya.entities;


import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;

import java.util.ArrayList;

import ro.xzya.game.Game;
import ro.xzya.managers.Jukebox;
import ro.xzya.managers.Point2D;

/**
 * Created by Xzya on 4/3/2015.
 */
public class Player extends SpaceObject {

    private static final float Pi = 3.1415f;
    private static final int dpSize1 = ((int)(Game.HEIGHT * 0.02));
    private static final int dpSize2 = ((int)(Game.HEIGHT * 0.0125));
    private static final int dpSize3 = ((int)(Game.HEIGHT * 0.015));

    private final int MAX_BULLETS = 4;
    private ArrayList<Bullet> bullets;

    private float[] flamex;
    private float[] flamey;

    private boolean left;
    private boolean right;
    private boolean up;

    private float maxSpeed;
    private float acceleration;
    private float deceleration;
    private float accelerationTimer;

    private boolean hit;
    private boolean dead;

    private float hitTimer;
    private float hitTime;

    private Point2D[] hitLines;
    private Point2D[] hitLinesVector;

    private long score;
    private int extraLives;
    private long requiredScore;

    public Player(ArrayList<Bullet> bullets) {

        this.bullets = bullets;

        x = Game.WIDTH / 2;
        y = Game.HEIGHT / 2;

        maxSpeed = 400;
        acceleration = 300;
        deceleration = 10;

        shapex = new float[4];
        shapey = new float[4];

        flamex = new float[3];
        flamey = new float[3];

        hitLines = new Point2D[4];
        hitLinesVector = new Point2D[4];

        radians = Pi / 2;
        rotationSpeed = 3;

        hit = false;
        dead = false;
        hitTimer = 0;
        hitTime = 2;

        score = 0;
        extraLives = 3;
        requiredScore = 10000;

    }

    private void setShape() {
//        shapex[0] = x + MathUtils.cos(radians) * 8;
//        shapey[0] = y + MathUtils.sin(radians) * 8;
//
//        shapex[1] = x + MathUtils.cos(radians - 4 * Pi / 5) * 8;
//        shapey[1] = y + MathUtils.sin(radians - 4 * Pi / 5) * 8;
//
//        shapex[2] = x + MathUtils.cos(radians + Pi) * 5;
//        shapey[2] = y + MathUtils.sin(radians + Pi) * 5;
//
//        shapex[3] = x + MathUtils.cos(radians + 4 * Pi / 5) * 8;
//        shapey[3] = y + MathUtils.sin(radians + 4 * Pi / 5) * 8;

        shapex[0] = x + MathUtils.cos(radians) * dpSize1;
        shapey[0] = y + MathUtils.sin(radians) * dpSize1;

        shapex[1] = x + MathUtils.cos(radians - 4 * Pi / 5) * dpSize1;
        shapey[1] = y + MathUtils.sin(radians - 4 * Pi / 5) * dpSize1;

        shapex[2] = x + MathUtils.cos(radians + Pi) * dpSize2;
        shapey[2] = y + MathUtils.sin(radians + Pi) * dpSize2;

        shapex[3] = x + MathUtils.cos(radians + 4 * Pi / 5) * dpSize1;
        shapey[3] = y + MathUtils.sin(radians + 4 * Pi / 5) * dpSize1;
    }

    private void setFlame() {
//        flamex[0] = x + MathUtils.cos(radians - 5 * Pi / 6) * 5;
//        flamey[0] = y + MathUtils.sin(radians - 5 * Pi / 6) * 5;
//
//        flamex[1] = x + MathUtils.cos(radians - Pi) * (6 + accelerationTimer * 50);
//        flamey[1] = y + MathUtils.sin(radians - Pi) * (6 + accelerationTimer * 50);
//
//        flamex[2] = x + MathUtils.cos(radians + 5 * Pi / 6) * 5;
//        flamey[2] = y + MathUtils.sin(radians + 5 * Pi / 6) * 5;

        flamex[0] = x + MathUtils.cos(radians - 5 * Pi / 6) * dpSize2;
        flamey[0] = y + MathUtils.sin(radians - 5 * Pi / 6) * dpSize2;

        flamex[1] = x + MathUtils.cos(radians - Pi) * (dpSize3 + accelerationTimer * 50);
        flamey[1] = y + MathUtils.sin(radians - Pi) * (dpSize3 + accelerationTimer * 50);

        flamex[2] = x + MathUtils.cos(radians + 5 * Pi / 6) * dpSize2;
        flamey[2] = y + MathUtils.sin(radians + 5 * Pi / 6) * dpSize2;
    }

    public void setLeft(boolean b) {
        left = b;
    }

    public void setRight(boolean b) {
        right = b;
    }

    public void setUp(boolean b) {
        if (b && !up && !hit) {
            Jukebox.loop("thruster");
        } else if (!b) {
            Jukebox.stop("thruster");
        }
        up = b;
    }

    public void reset() {
        x = Game.WIDTH / 2;
        y = Game.HEIGHT / 2;
        setShape();
        hit = dead = false;
    }

    @Override
    public void setPosition(float x, float y) {
        super.setPosition(x, y);
        setShape();
    }

    public boolean isHit() {
        return hit;
    }

    public boolean isDead() {
        return dead;
    }

    public long getScore() {
        return score;
    }

    public int getLives() {
        return extraLives;
    }

    public boolean isLeft() {
        return left;
    }

    public boolean isRight() {
        return right;
    }

    public boolean isUp() {
        return up;
    }

    public void loseLife() {
        extraLives--;
    }

    public void incrementScore(long l) {
        score += l;
    }

    public void shoot() {
        if ((bullets.size() == MAX_BULLETS) || hit) {
            return;
        }
        bullets.add(new Bullet(x, y, radians));
        Jukebox.play("shoot");
    }

    public void hit() {

        if (hit) return;

        hit = true;

        dx = dy = 0;
        left = right = up = false;
        Jukebox.stop("thruster");

        for (int i = 0, j = hitLines.length - 1;
             i < hitLines.length;
             j = i++) {
            hitLines[i] = new Point2D(
                    shapex[i], shapey[i],
                    shapex[j], shapey[j]
            );
        }

        hitLinesVector[0] = new Point2D(
                MathUtils.cos(radians + 1.5f),
                MathUtils.sin(radians + 1.5f)
        );
        hitLinesVector[1] = new Point2D(
                MathUtils.cos(radians - 1.5f),
                MathUtils.sin(radians - 1.5f)
        );
        hitLinesVector[2] = new Point2D(
                MathUtils.cos(radians - 2.8f),
                MathUtils.sin(radians - 2.8f)
        );
        hitLinesVector[3] = new Point2D(
                MathUtils.cos(radians + 2.8f),
                MathUtils.sin(radians + 2.8f)
        );
    }

    public void update(float dt) {

        //check if hit
        if (hit) {
            hitTimer += dt;
            if (hitTimer > hitTime) {
                dead = true;
                hitTimer = 0;
            }
            for (int i = 0; i < hitLines.length; i++) {
                hitLines[i].x1 = hitLines[i].x1 + hitLinesVector[i].x1 * 10 * dt;
                hitLines[i].y1 = hitLines[i].y1 + hitLinesVector[i].y1 * 10 * dt;
                hitLines[i].x2 = hitLines[i].x2 + hitLinesVector[i].x1 * 10 * dt;
                hitLines[i].y2 = hitLines[i].y2 + hitLinesVector[i].y1 * 10 * dt;
            }
            return;
        }

        //check extra lives
        if (score >= requiredScore) {
            extraLives++;
            requiredScore += 10000;
            Jukebox.play("extralife");
        }

        //turning
        if (left) {
            radians += rotationSpeed * dt;
        } else if (right) {
            radians -= rotationSpeed * dt;
        }

        //accelerating
        if (up) {
            dx += MathUtils.cos(radians) * acceleration * dt;
            dy += MathUtils.sin(radians) * acceleration * dt;
            accelerationTimer += dt;
            if (accelerationTimer > 0.1f) {
                accelerationTimer = 0;
            }
        } else {
            accelerationTimer = 0;
        }

        //deceleration
        float vec = (float) Math.sqrt(dx * dx + dy * dy);
        if (vec > 0) {
            dx -= (dx / vec) * deceleration * dt;
            dy -= (dy / vec) * deceleration * dt;
        }
        if (vec > maxSpeed) {
            dx = (dx / vec) * maxSpeed;
            dy = (dy / vec) * maxSpeed;
        }

        //set position
        x += dx * dt;
        y += dy * dt;

        //set shape
        setShape();

        //set flame
        if (up) {
            setFlame();
        }

        //screen wrap
        wrap();
    }

    public void draw(ShapeRenderer sr) {
        sr.setColor(1, 1, 1, 1);

        sr.begin(ShapeRenderer.ShapeType.Line);

        //check if hit
        if (hit) {
            for (int i = 0; i < hitLines.length; i++) {
                sr.line(
                        hitLines[i].x1,
                        hitLines[i].y1,
                        hitLines[i].x2,
                        hitLines[i].y2
                );
            }
            sr.end();
            return;
        }

        //draw player
        for (int i = 0, j = shapex.length - 1;
             i < shapex.length;
             j = i++) {
            sr.line(shapex[i], shapey[i], shapex[j], shapey[j]);
        }

        //draw flames
        if (up) {
            for (int i = 0, j = flamex.length - 1;
                 i < flamex.length;
                 j = i++) {
                sr.line(flamex[i], flamey[i], flamex[j], flamey[j]);
            }
        }

        sr.end();
    }


}
