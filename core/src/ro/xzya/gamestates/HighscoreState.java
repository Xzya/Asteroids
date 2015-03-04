package ro.xzya.gamestates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

import ro.xzya.game.Game;
import ro.xzya.managers.GameStateManager;

/**
 * Created by Xzya on 4/3/2015.
 */
public class HighscoreState extends GameState {

    private SpriteBatch sb;

    private BitmapFont font;

    private long[] highScores;
    private String[] names;

    public HighscoreState(GameStateManager gsm) {
        super(gsm);
    }

    @Override
    public void init() {
        sb = new SpriteBatch();

        FreeTypeFontGenerator gen = new FreeTypeFontGenerator(
                Gdx.files.internal("../android/assets/fonts/Hyperspace Bold.ttf")
        );
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 20;
        font = gen.generateFont(parameter);
    }

    @Override
    public void update(float dt) {
        handleInput();
    }

    @Override
    public void draw() {

        sb.setProjectionMatrix(Game.cam.combined);
        sb.begin();
        font.draw(
                sb,
                "Highscores",
                Game.WIDTH / 2,
                Game.HEIGHT - 100
        );

        sb.end();

    }

    @Override
    public void handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)
                || Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)
                || Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            gsm.setState(GameStateManager.MENU);
        }
    }

    @Override
    public void dispose() {

    }
}
