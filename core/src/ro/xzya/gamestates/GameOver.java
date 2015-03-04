package ro.xzya.gamestates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import ro.xzya.game.Game;
import ro.xzya.managers.GameStateManager;
import ro.xzya.managers.Save;

/**
 * Created by Xzya on 4/3/2015.
 */
public class GameOver extends GameState {

    private SpriteBatch sb;
    private ShapeRenderer sr;

    private BitmapFont gameOverFont;
    private BitmapFont font;

    private boolean newHighScore;
    private char[] newName;
    private int currentChar;

    public GameOver(GameStateManager gsm) {
        super(gsm);
    }


    @Override
    public void init() {
        sb = new SpriteBatch();
        sr = new ShapeRenderer();

        newHighScore = Save.gd.isHighScore(Save.gd.getTentativeScore());
        if (newHighScore) {
            newName = new char[]{'A', 'A', 'A'};
            currentChar = 0;
        }

        FreeTypeFontGenerator gen = new FreeTypeFontGenerator(
                Gdx.files.internal("../android/assets/fonts/Hyperspace Bold.ttf")
        );
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 20;
        font = gen.generateFont(parameter);
        parameter.size = 32;
        gameOverFont = gen.generateFont(parameter);

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

        s = "Game Over";
        w = gameOverFont.getBounds(s).width;
        gameOverFont.draw(
                sb,
                s,
                (Game.WIDTH - w) / 2,
                220
        );

        if (!newHighScore) {
            sb.end();
            return;
        }

        s = "New High Score: " + Save.gd.getTentativeScore();
        w = font.getBounds(s).width;
        font.draw(
                sb,
                s,
                (Game.WIDTH - w) / 2,
                180
        );

        for (int i = 0; i < newName.length; i++) {
            font.draw(
                    sb,
                    Character.toString(newName[i]),
                    230 + 14 * i,
                    120
            );
        }

        sb.end();

        sr.begin(ShapeRenderer.ShapeType.Line);
        sr.line(
                230 + 14 * currentChar,
                100,
                244 + 14 * currentChar,
                100
        );

        sr.end();
    }

    @Override
    public void handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)
                || Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            if (newHighScore) {
                Save.gd.addHighScore(Save.gd.getTentativeScore(), new String(newName));
                Save.save();
            }
            gsm.setState(GameStateManager.MENU);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)
                || Gdx.input.isKeyJustPressed(Input.Keys.W)) {
            if (newName[currentChar] == ' ') {
                newName[currentChar] = 'Z';
            } else {
                newName[currentChar]--;
                if (newName[currentChar] < 'A') {
                    newName[currentChar] = ' ';
                }
            }
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)
                || Gdx.input.isKeyJustPressed(Input.Keys.S)) {
            if (newName[currentChar] == ' ') {
                newName[currentChar] = 'A';
            } else {
                newName[currentChar]++;
                if (newName[currentChar] > 'Z') {
                    newName[currentChar] = ' ';
                }
            }
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)
                || Gdx.input.isKeyJustPressed(Input.Keys.D)) {
            if (currentChar < newName.length - 1) {
                currentChar++;
            }
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)
                || Gdx.input.isKeyJustPressed(Input.Keys.A)) {
            if (currentChar > 0) {
                currentChar--;
            }
        }
    }

    @Override
    public void dispose() {

    }
}
