package ro.xzya.gamestates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

import ro.xzya.game.Game;
import ro.xzya.managers.GameStateManager;
import ro.xzya.managers.Save;

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

        String s = "";
        if (Game.client.equals("desktop")) {
            s += Game.BASE_URL;
        }
        FreeTypeFontGenerator gen = new FreeTypeFontGenerator(

                Gdx.files.internal(s + "fonts/Hyperspace Bold.ttf")
        );
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 20;
        font = gen.generateFont(parameter);

        Save.load();

        highScores = Save.gd.getHighscores();
        names = Save.gd.getNames();
    }

    @Override
    public void update(float dt) {
        handleInput();
    }

    @Override
    public void draw() {

        sb.setProjectionMatrix(Game.cam.combined);
        sb.begin();

        String s;
        float w;

        s = "Highscores";
        w = font.getBounds(s).width;
        font.draw(
                sb,
                s,
                (Game.WIDTH - w) / 2,
                300
        );

        for (int i = 0; i < highScores.length; i++) {
            s = String.format(
                    "%2d. %7s %s",
                    i + 1,
                    highScores[i],
                    names[i]
            );
            w = font.getBounds(s).width;
            font.draw(
                    sb,
                    s,
                    (Game.WIDTH - w) / 2,
                    270 - 20 * i
            );
        }

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

        sb.dispose();
        font.dispose();

    }
}
