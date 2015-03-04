package ro.xzya.entities;

import ro.xzya.game.Game;

/**
 * Created by Xzya on 4/3/2015.
 */
public class SpaceObject {

    //position
    protected float x;
    protected float y;

    //vector
    protected float dx;
    protected float dy;

    //direction
    protected float radians;

    //speed
    protected float speed;

    //rotation speed
    protected float rotationSpeed;

    //size
    protected int width;
    protected int height;

    //polygon
    protected float[] shapex;
    protected float[] shapey;

    //wrap
    protected void wrap() {
        if (x < 0) x = Game.WIDTH;
        if (x > Game.WIDTH) x = 0;
        if (y < 0) y = Game.HEIGHT;
        if (y > Game.HEIGHT) y = 0;
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getx() {
        return x;
    }

    public float gety() {
        return y;
    }

    public float[] getShapex() {
        return shapex;
    }

    public float[] getShapey() {
        return shapey;
    }

    public boolean contains(float x, float y) {
        boolean b = false;
        for (int i = 0, j = shapex.length - 1;
             i < shapex.length;
             j = i++) {
            if ((shapey[i] > y) != (shapey[j] > y)
                    && (x < (shapex[j] - shapex[i])
                    * (y - shapey[i]) / (shapey[j] - shapey[i])
                    + shapex[i])) {
                b = !b;
            }
        }
        return b;
    }

    public boolean intersects(SpaceObject other) {
        float[] sx = other.getShapex();
        float[] sy = other.getShapey();
        for (int i = 0; i < sx.length; i++) {
            if (contains(sx[i], sy[i])) {
                return true;
            }
        }
        return false;
    }

}
