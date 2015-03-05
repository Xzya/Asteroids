package ro.xzya.managers;

/**
 * Created by Xzya on 5/3/2015.
 *
 * This is a replacement for the default Point2D and Line2D
 * because android doesn't support it
 */
public class Point2D {

    public float x1;
    public float x2;
    public float y1;
    public float y2;

    public Point2D() {

    }

    public Point2D(float x1, float y1){
        this.x1 = x1;
        this.y1 = y1;
    }

    public Point2D(float x1, float y1, float x2, float y2){
        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;
    }
}
