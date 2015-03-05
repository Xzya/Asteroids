package ro.xzya.managers;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import ro.xzya.game.Game;

/**
 * Created by Xzya on 5/3/2015.
 */
public class GUI {

    private volatile boolean pressedLeft;
    private volatile boolean pressedRight;
    private volatile boolean pressedA;
    private volatile boolean pressedB;
    private volatile boolean pressedOK;
    private volatile boolean pressedBack;

    private static final int dpSize1 = ((int)(Game.WIDTH * 0.05)); //button margin
    private static final int dpSize2 = ((int)(Game.HEIGHT * 0.10)); //button margin

    private Rectangle wleftBounds;
    private Rectangle wrightBounds;
    private Rectangle back;
    private Circle A;
    private Circle B;
    private Circle OK;

    private Actor aA;
    private Actor aB;
    private Actor aOK;
    private Actor aWLeftBounds;
    private Actor aWRightBounds;
    private Actor aBack;

    private Stage stage;

    public GUI() {
        this.wleftBounds = new Rectangle(dpSize1, dpSize1, 2 * dpSize2, 2 * dpSize2);
        this.wrightBounds = new Rectangle(dpSize1 + (2 * dpSize1 + dpSize2), dpSize1, 2 * dpSize2, 2 * dpSize2);
//        this.wleftBounds = new Rectangle(20, 20, 80, 80);
//        this.wrightBounds = new Rectangle(80+20, 20, 80, 80);
        this.A = new Circle(Game.WIDTH - 3 * dpSize1, 2 * dpSize1, dpSize1);
        this.B = new Circle(Game.WIDTH - 2 * dpSize1, 4 * dpSize1, dpSize1);
        this.OK = new Circle(Game.WIDTH - 2 * dpSize1, 7 * dpSize1, dpSize1);

//        back = new Rectangle(0, Game.HEIGHT - dpSize1, dpSize1, Game.HEIGHT);
        back = new Rectangle(Game.WIDTH - dpSize1, Game.HEIGHT - dpSize1, dpSize1, dpSize1);

        aWLeftBounds = new Actor();
        aWLeftBounds.setX(wleftBounds.getX());
        aWLeftBounds.setY(wleftBounds.getY());
        aWLeftBounds.setWidth(wleftBounds.getWidth());
        aWLeftBounds.setHeight(wleftBounds.getHeight());
        aWLeftBounds.setTouchable(Touchable.enabled);

        aWRightBounds = new Actor();
        aWRightBounds.setX(wrightBounds.getX());
        aWRightBounds.setY(wrightBounds.getY());
        aWRightBounds.setWidth(wrightBounds.getWidth());
        aWRightBounds.setHeight(wrightBounds.getHeight());
        aWRightBounds.setTouchable(Touchable.enabled);

        aA = new Actor();
        aA.setX(A.x - dpSize1);
        aA.setY(A.y - dpSize1);
        aA.setWidth(A.radius * 1.8f);
        aA.setHeight(A.radius * 1.8f);
        aA.setTouchable(Touchable.enabled);

        aB = new Actor();
        aB.setX(B.x - dpSize1);
        aB.setY(B.y - dpSize1);
        aB.setWidth(B.radius * 1.8f);
        aB.setHeight(B.radius * 1.8f);
        aB.setTouchable(Touchable.enabled);

        aOK = new Actor();
        aOK.setX(OK.x - dpSize1);
        aOK.setY(OK.y - dpSize1);
        aOK.setWidth(OK.radius * 1.8f);
        aOK.setHeight(OK.radius * 1.8f);
        aOK.setTouchable(Touchable.enabled);

        aBack = new Actor();
        aBack.setX(back.getX());
        aBack.setY(back.getY());
        aBack.setWidth(back.getWidth());
        aBack.setHeight(back.getHeight());
        aBack.setTouchable(Touchable.enabled);

        stage = new Stage(new ScreenViewport());
        stage.addActor(aA);
        stage.addActor(aB);
        stage.addActor(aOK);
        stage.addActor(aWLeftBounds);
        stage.addActor(aWRightBounds);
        stage.addActor(aBack);

        pressedLeft = false;
        pressedRight = false;
        pressedA = false;
        pressedB = false;
        pressedOK = false;
        pressedBack = false;
    }

    public Stage getStage() {
        return stage;
    }

    public Actor getaA() {
        return aA;
    }

    public Actor getaB() {
        return aB;
    }

    public Actor getaOK() {
        return aOK;
    }

    public Actor getaWLeftBounds() {
        return aWLeftBounds;
    }

    public Actor getaWRightBounds() {
        return aWRightBounds;
    }

    public Rectangle getWleftBounds() {
        return wleftBounds;
    }

    public Rectangle getWrightBounds() {
        return wrightBounds;
    }

    public Circle getA() {
        return A;
    }

    public Circle getB() {
        return B;
    }

    public Circle getOK() {
        return OK;
    }

    public void clearListeners() {
        Array<EventListener> listeners = Game.gui.getaA().getListeners();
        for (EventListener e : listeners) {
            Game.gui.getaA().removeListener(e);
        }
        listeners = Game.gui.getaB().getListeners();
        for (EventListener e : listeners) {
            Game.gui.getaB().removeListener(e);
        }
        listeners = Game.gui.getaOK().getListeners();
        for (EventListener e : listeners) {
            Game.gui.getaOK().removeListener(e);
        }
        listeners = Game.gui.getaWLeftBounds().getListeners();
        for (EventListener e : listeners) {
            Game.gui.getaWLeftBounds().removeListener(e);
        }
        listeners = Game.gui.getaWRightBounds().getListeners();
        for (EventListener e : listeners) {
            Game.gui.getaWRightBounds().removeListener(e);
        }
    }

    public boolean isPressedA() {
        return pressedA;
    }

    public boolean isPressedB() {
        return pressedB;
    }

    public boolean isPressedLeft() {
        return pressedLeft;
    }

    public boolean isPressedRight() {
        return pressedRight;
    }

    public boolean isPressedOK() {
        return pressedOK;
    }

    public void setPressedA(boolean pressedA) {
        this.pressedA = pressedA;
    }

    public void setPressedB(boolean pressedB) {
        this.pressedB = pressedB;
    }

    public void setPressedLeft(boolean pressedLeft) {
        this.pressedLeft = pressedLeft;
    }

    public void setPressedRight(boolean pressedRight) {
        this.pressedRight = pressedRight;
    }

    public void setPressedOK(boolean pressedOK) {
        this.pressedOK = pressedOK;
    }

    public Actor getaBack() {
        return aBack;
    }

    public Rectangle getBack() {
        return back;
    }
}
