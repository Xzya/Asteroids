package ro.xzya.gamestates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import ro.xzya.game.Game;
import ro.xzya.managers.GameStateManager;
import ro.xzya.managers.Save;

/**
 * Created by Xzya on 4/3/2015.
 */
public class GameOver extends GameState {

    private static final int dpSize1 = ((int) (Game.HEIGHT * 0.05)); //20dp
    private static final int dpSize2 = ((int) (Game.HEIGHT * 0.08)); //32dp
    private static final int dpSize3 = ((int) (Game.HEIGHT * 0.55)); //220dp
    private static final int dpSize4 = ((int) (Game.HEIGHT * 0.45)); //220dp
    private static final int dpSize5 = ((int) (Game.WIDTH * 0.44));
    private static final int dpSize6 = ((int) (Game.WIDTH * 0.023)); //14
    private static final int dpSize7 = ((int) (Game.HEIGHT * 0.3)); //120
    private static final int dpSize8 = ((int) (Game.HEIGHT * 0.25)); //100
    private static final int dpSize9 = ((int) (Game.WIDTH * 0.45396)); //244

    private Vector3 touchPoint;

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

                if (Game.isMobile) {
//        if (true) {
            setTouchInput();
        }

        sb = new SpriteBatch();
        sr = new ShapeRenderer();

        newHighScore = Save.gd.isHighScore(Save.gd.getTentativeScore());
        if (newHighScore) {
            newName = new char[]{'A', 'A', 'A'};
            currentChar = 0;
        }

        FreeTypeFontGenerator gen = new FreeTypeFontGenerator(

                Gdx.files.internal("fonts/Hyperspace Bold.ttf")
        );
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
//        parameter.size = 20;
        parameter.size = dpSize1;
        font = gen.generateFont(parameter);
//        parameter.size = 32;
        parameter.size = dpSize2;
        gameOverFont = gen.generateFont(parameter);

        touchPoint = new Vector3();

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
//                220
                dpSize3
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
//                180
                dpSize4
        );

        for (int i = 0; i < newName.length; i++) {
            font.draw(
                    sb,
                    Character.toString(newName[i]),
//                    230 + 14 * i,
                    dpSize5 + dpSize6 * i,
//                    120
                    dpSize7
            );
        }

        sb.end();

        sr.begin(ShapeRenderer.ShapeType.Line);
        sr.line(
//                230 + 14 * currentChar,
                dpSize5 + dpSize6 * currentChar,
//                100,
                dpSize8,
//                244 + 14 * currentChar,
                dpSize9 + dpSize6 * currentChar,
//                100
                dpSize8
        );

        //draw on-screen controls
        if (Game.isMobile) {
//        if (true) {
            sr.rect(Game.gui.getWleftBounds().getX(), Game.gui.getWleftBounds().getY(), Game.gui.getWleftBounds().getWidth(), Game.gui.getWleftBounds().getHeight());
            sr.rect(Game.gui.getWrightBounds().getX(), Game.gui.getWrightBounds().getY(), Game.gui.getWrightBounds().getWidth(), Game.gui.getWrightBounds().getHeight());
            sr.circle(Game.gui.getA().x, Game.gui.getA().y, Game.gui.getA().radius);
            sr.circle(Game.gui.getB().x, Game.gui.getB().y, Game.gui.getB().radius);
            sr.circle(Game.gui.getOK().x, Game.gui.getOK().y, Game.gui.getOK().radius);
        }
        sr.end();
    }

    @Override
    public void handleInput() {
        if (!Game.isMobile) {
//        if (true) {
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
    }

    private void setTouchInput() {
        Gdx.input.setInputProcessor(Game.gui.getStage());
        //handle touch input

        Game.gui.getaWLeftBounds().addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (currentChar > 0) {
                    currentChar--;
                }
                return true;
            }
        });

        Game.gui.getaWRightBounds().addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (currentChar < newName.length - 1) {
                    currentChar++;
                }
                return true;
            }
        });

        Game.gui.getaA().addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (newName[currentChar] == ' ') {
                    newName[currentChar] = 'A';
                } else {
                    newName[currentChar]++;
                    if (newName[currentChar] > 'Z') {
                        newName[currentChar] = ' ';
                    }
                }
                return true;
            }
        });

        Game.gui.getaB().addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (newName[currentChar] == ' ') {
                    newName[currentChar] = 'Z';
                } else {
                    newName[currentChar]--;
                    if (newName[currentChar] < 'A') {
                        newName[currentChar] = ' ';
                    }
                }
                return true;
            }
        });

        Game.gui.getaOK().addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (newHighScore) {
                    Save.gd.addHighScore(Save.gd.getTentativeScore(), new String(newName));
                    Save.save();
                }
                gsm.setState(GameStateManager.MENU);
                return true;
            }
        });
    }

    @Override
    public void dispose() {
        sb.dispose();
        sr.dispose();
        font.dispose();
        gameOverFont.dispose();

        if (Game.isMobile){
            Game.gui.clearListeners();
        }
    }
}
