package ro.xzya.managers;

import ro.xzya.gamestates.GameOver;
import ro.xzya.gamestates.GameState;
import ro.xzya.gamestates.HighscoreState;
import ro.xzya.gamestates.MenuState;
import ro.xzya.gamestates.PlayState;

/**
 * Created by Xzya on 4/3/2015.
 */
public class GameStateManager {

    public static final int MENU = 0;
    public static final int PLAY = 1;
    public static final int HIGHSCORE = 2;
    public static final int GAME_OVER = 3;

    //current game state
    private GameState gameState;

    public GameStateManager() {
        setState(MENU);
    }

    public void setState(int state) {
        if (gameState != null)  gameState.dispose();
        if (state == MENU) {
            //switch to menu state
            gameState = new MenuState(this);

        }
        if (state == PLAY) {
            //switch to play state
            gameState = new PlayState(this);
        }
        if (state == HIGHSCORE) {
            //switch to highscore state
            gameState = new HighscoreState(this);
        }
        if (state == GAME_OVER) {
            gameState = new GameOver(this);
        }
    }

    public void update(float dt) {
        gameState.update(dt);
    }

    public void draw() {
        gameState.draw();
    }



}
