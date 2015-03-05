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
public class HighscoreState extends GameState {

    private static final int dpSize1 = ((int) (Game.HEIGHT * 0.75)); //300dp
    private static final int dpSize2 = ((int) (Game.HEIGHT * 0.675)); //270dp
    private static final int dpSize3 = ((int) (Game.HEIGHT * 0.05)); //20dp

    private Vector3 touchPoint;

    private SpriteBatch sb;
    private ShapeRenderer sr;

    private BitmapFont font;

    private long[] highScores;
    private String[] names;

    public HighscoreState(GameStateManager gsm) {
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

        FreeTypeFontGenerator gen = new FreeTypeFontGenerator(

                Gdx.files.internal("fonts/Hyperspace Bold.ttf")
        );
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 20;
        font = gen.generateFont(parameter);

        Save.load();

        highScores = Save.gd.getHighscores();
        names = Save.gd.getNames();

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

        s = "Highscores";
        w = font.getBounds(s).width;
        font.draw(
                sb,
                s,
                (Game.WIDTH - w) / 2,
//                300
                dpSize1
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
//                    270 - 20 * i
                    dpSize2 - dpSize3 * i
            );
        }

        sb.end();

        //draw on-screen controls
        if (Game.isMobile) {
//        if (true) {
            sr.begin(ShapeRenderer.ShapeType.Line);
            sr.rect(Game.gui.getWleftBounds().getX(), Game.gui.getWleftBounds().getY(), Game.gui.getWleftBounds().getWidth(), Game.gui.getWleftBounds().getHeight());
            sr.rect(Game.gui.getWrightBounds().getX(), Game.gui.getWrightBounds().getY(), Game.gui.getWrightBounds().getWidth(), Game.gui.getWrightBounds().getHeight());
            sr.circle(Game.gui.getA().x, Game.gui.getA().y, Game.gui.getA().radius);
            sr.circle(Game.gui.getB().x, Game.gui.getB().y, Game.gui.getB().radius);
            sr.end();
        }
    }

    @Override
    public void handleInput() {
        if (!Game.isMobile) {
//        if (true) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)
                    || Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)
                    || Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                gsm.setState(GameStateManager.MENU);
            }
        }
    }

    private void setTouchInput() {
        Gdx.input.setInputProcessor(Game.gui.getStage());
        //handle touch input

        Game.gui.getaA().addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                gsm.setState(GameStateManager.MENU);
                return true;
            }
        });

        Game.gui.getaB().addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
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

        if (Game.isMobile){
            Game.gui.clearListeners();
        }

    }
}
