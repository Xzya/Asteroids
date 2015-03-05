package ro.xzya.gamestates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;

import java.util.ArrayList;

import ro.xzya.entities.Asteroid;
import ro.xzya.game.Game;
import ro.xzya.managers.GameStateManager;
import ro.xzya.managers.InputManager;
import ro.xzya.managers.Save;

/**
 * Created by Xzya on 4/3/2015.
 */
public class MenuState extends GameState {

    private SpriteBatch sb;
    private ShapeRenderer sr;

    private BitmapFont titleFont;
    private BitmapFont font;

    private final String title = "Asteroids";

    private ArrayList<Asteroid> asteroids;

    private int currentItem;
    private String[] menuItems;

    private InputManager touch;

    public MenuState(GameStateManager gsm) {
        super(gsm);
    }

    @Override
    public void init() {
        touch = new InputManager();
        sb = new SpriteBatch();
        sr = new ShapeRenderer();

        FreeTypeFontGenerator gen = new FreeTypeFontGenerator(

                Gdx.files.internal("fonts/Hyperspace Bold.ttf")
        );
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 56;

        titleFont = gen.generateFont(parameter);
        titleFont.setColor(Color.WHITE);

        parameter.size = 20;
        font = gen.generateFont(parameter);

        menuItems = new String[]{
                "Play",
                "Highscores",
                "Quit"
        };

        Save.load();

        //init bg asteroids
        asteroids = new ArrayList<Asteroid>();
        for (int i = 0; i < 6; i++) {
            asteroids.add(new Asteroid(
                    MathUtils.random(Game.WIDTH),
                    MathUtils.random(Game.HEIGHT),
                    Asteroid.LARGE
            ));
        }

    }

    @Override
    public void update(float dt) {
        //handle input
        handleInput();

        //update asteroids
        for (int i = 0; i < 6; i++) {
            asteroids.get(i).update(dt);
        }

    }

    @Override
    public void draw() {

        sb.setProjectionMatrix(Game.cam.combined);
        sr.setProjectionMatrix(Game.cam.combined);

        //draw asteroids
        for (int i = 0; i < 6; i++) {
            asteroids.get(i).draw(sr);
        }

        //draw title
        sb.begin();
        float width = titleFont.getBounds(title).width;
        titleFont.draw(
                sb,
                title,
                (Game.WIDTH - width) / 2,
                300
        );

        //draw menu
        for (int i = 0; i < menuItems.length; i++) {
            width = font.getBounds(menuItems[i]).width;
            if (currentItem == i) font.setColor(Color.RED);
            else font.setColor(Color.WHITE);
            font.draw(
                    sb,
                    menuItems[i],
                    (Game.WIDTH - width) / 2,
                    180 - 50 * i
            );
        }
        sb.end();
    }

    @Override
    public void handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)
                || Gdx.input.isKeyJustPressed(Input.Keys.W)) {
            if (currentItem > 0) {
                currentItem--;
            }
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)
                || Gdx.input.isKeyJustPressed(Input.Keys.S)) {
            if (currentItem < menuItems.length - 1) {
                currentItem++;
            }
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)
                || Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            select();
        }
    }

    private void select() {
        //play
        if (currentItem == 0) {
            gsm.setState(GameStateManager.PLAY);
        } else if (currentItem == 1) {
            gsm.setState(GameStateManager.HIGHSCORE);
        } else if (currentItem == 2) {
            Gdx.app.exit();
        }
    }

    @Override
    public void dispose() {

        sb.dispose();
        sr.dispose();
        titleFont.dispose();
        font.dispose();

    }
}
