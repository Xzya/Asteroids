package ro.xzya.gamestates;


import ro.xzya.managers.GameStateManager;

/**
 * Created by Xzya on 4/3/2015.
 */
public abstract class GameState {

    protected GameStateManager gsm;

    protected GameState(GameStateManager _gsm) {
        gsm = _gsm;
        init();
    }

    public abstract void init();

    public abstract void update(float dt);

    public abstract void draw();

    public abstract void handleInput();

    public abstract void dispose();

}
