package ro.xzya.gamestates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;

import java.util.ArrayList;

import ro.xzya.entities.Asteroid;
import ro.xzya.entities.Bullet;
import ro.xzya.entities.Particle;
import ro.xzya.entities.Player;
import ro.xzya.game.Game;
import ro.xzya.managers.GameStateManager;
import ro.xzya.managers.Jukebox;
import ro.xzya.managers.Save;

/**
 * Created by Xzya on 4/3/2015.
 */
public class PlayState extends GameState {

    private SpriteBatch sb;
    private ShapeRenderer sr;

    private BitmapFont font;
    private Player hudPlayer;

    private Player player;
    private ArrayList<Bullet> bullets;
    private ArrayList<Asteroid> asteroids;

    private ArrayList<Particle> particles;

    private int level;
    private int totalAsteroids;
    private int numAsteroidsLeft;

    private float maxDelay;
    private float minDelay;
    private float currentDelay;
    private float bgTimer;
    private boolean playLowPulse;

    public PlayState(GameStateManager _gsm) {
        super(_gsm);
    }

    @Override
    public void init() {
        sb = new SpriteBatch();
        sr = new ShapeRenderer();

        //set font
        FreeTypeFontGenerator gen = new FreeTypeFontGenerator(
                Gdx.files.internal("../android/assets/fonts/Hyperspace Bold.ttf")
        );
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 20;
        font = gen.generateFont(parameter);

        bullets = new ArrayList<Bullet>();

        player = new Player(bullets);

        asteroids = new ArrayList<Asteroid>();

        particles = new ArrayList<Particle>();

        level = 1;
        spawnAsteroids();

        hudPlayer = new Player(null);

        //set up bg music
        maxDelay = 1;
        minDelay = 0.25f;
        currentDelay = maxDelay;
        bgTimer = maxDelay;
        playLowPulse = true;
    }

    private void createParticles(float x, float y) {
        for (int i = 0; i < 6; i++) {
            particles.add(new Particle(x, y));
        }
    }

    private void spawnAsteroids() {
        asteroids.clear();

        int numToSpawn = 4 + level - 1;
        totalAsteroids = numToSpawn * 7; //because they split when destroyed
        numAsteroidsLeft = totalAsteroids;
        currentDelay = maxDelay;

        for (int i = 0; i < numToSpawn; i++) {
            float x, y, dx, dy, dist;

            do {
                x = MathUtils.random(Game.WIDTH);
                y = MathUtils.random(Game.HEIGHT);
                dx = x - player.getx();
                dy = y - player.gety();
                dist = (float) Math.sqrt(dx * dx + dy * dy);
            } while (dist < 100);
            asteroids.add(new Asteroid(x, y, Asteroid.LARGE));

        }

    }

    @Override
    public void update(float dt) {
        //get user input
        handleInput();

        //next level
        if (asteroids.size() == 0) {
            level++;
            spawnAsteroids();
        }

        //update player
        player.update(dt);
        if (player.isDead()) {
            if (player.getLives() == 0) {
                Jukebox.stopAll();
                Save.gd.setTentativeScore(player.getScore());
                gsm.setState(GameStateManager.GAME_OVER);
                return;
            }
            player.reset();
            player.loseLife();
            return;
        }

        //update bullets
        for (int i = 0; i < bullets.size(); i++) {
            bullets.get(i).update(dt);
            if (bullets.get(i).shouldRemove()) {
                bullets.remove(i);
                i--;
            }
        }

        //update asteroids
        for (int i = 0; i < asteroids.size(); i++) {
            asteroids.get(i).update(dt);
            if (asteroids.get(i).shouldRemove()) {
                asteroids.remove(i);
                i--;
            }
        }

        //update particles
        for (int i = 0; i < particles.size(); i++) {
            particles.get(i).update(dt);
            if (particles.get(i).shouldRemove()) {
                particles.remove(i);
                i--;
            }
        }

        //check collisions
        checkCollisions();

        //play bg music
        bgTimer += dt;
        if (!player.isHit() && bgTimer >= currentDelay) {
            if (playLowPulse) {
                Jukebox.play("pulselow");
            } else {
                Jukebox.play("pulsehigh");
            }
            playLowPulse = !playLowPulse;
            bgTimer = 0;
        }
    }

    private void checkCollisions() {
        //player-asteroid collision
        if (!player.isHit()) {
            for (int i = 0; i < asteroids.size(); i++) {
                Asteroid a = asteroids.get(i);
                //if player intersects asteroid
                if (a.intersects(player)) {
                    player.hit();
                    asteroids.remove(i);
                    i--;
                    splitAsteroids(a);

                    Jukebox.play("explode");

                    break;
                }
            }
        }

        //bullet-asteroid collision
        for (int i = 0; i < bullets.size(); i++) {
            Bullet b = bullets.get(i);
            for (int j = 0; j < asteroids.size(); j++) {
                Asteroid a = asteroids.get(j);
                //if the asteroid polygon contains the point b
                if (a.contains(b.getx(), b.gety())) {
                    bullets.remove(i);
                    i--;
                    asteroids.remove(j);
                    j--;
                    splitAsteroids(a);

                    //increment player score
                    player.incrementScore(a.getScore());

                    Jukebox.play("explode");

                    break;
                }
            }
        }
    }

    private void splitAsteroids(Asteroid a) {
        createParticles(a.getx(), a.gety());

        numAsteroidsLeft--;
        currentDelay = ((maxDelay - minDelay) * numAsteroidsLeft / totalAsteroids) + minDelay;
        if (a.getType() == Asteroid.LARGE) {
            asteroids.add(new Asteroid(a.getx(), a.gety(), Asteroid.MEDIUM));
            asteroids.add(new Asteroid(a.getx(), a.gety(), Asteroid.MEDIUM));
        } else if (a.getType() == Asteroid.MEDIUM) {
            asteroids.add(new Asteroid(a.getx(), a.gety(), Asteroid.SMALL));
            asteroids.add(new Asteroid(a.getx(), a.gety(), Asteroid.SMALL));
        }
    }

    @Override
    public void draw() {

        sb.setProjectionMatrix(Game.cam.combined);
        sr.setProjectionMatrix(Game.cam.combined);

        //draw player
        player.draw(sr);

        //draw bullets
        for (int i = 0; i < bullets.size(); i++) {
            bullets.get(i).draw(sr);
        }

        //draw asteroids
        for (int i = 0; i < asteroids.size(); i++) {
            asteroids.get(i).draw(sr);
        }

        //draw particles
        for (int i = 0; i < particles.size(); i++) {
            particles.get(i).draw(sr);
        }

        //draw score
        sb.setColor(1, 1, 1, 1);
        sb.begin();
        font.draw(sb, Long.toString(player.getScore()), 40, 390);
        sb.end();

        //draw lives
        for (int i = 0; i < player.getLives(); i++) {
            hudPlayer.setPosition(40 + i * 10, 360);
            hudPlayer.draw(sr);
        }


    }

    @Override
    public void handleInput() {
        if(!player.isHit()) {
            player.setLeft(Gdx.input.isKeyPressed(Input.Keys.LEFT)
                    || Gdx.input.isKeyPressed(Input.Keys.A));
            player.setRight(Gdx.input.isKeyPressed(Input.Keys.RIGHT)
                    || Gdx.input.isKeyPressed(Input.Keys.D));
            player.setUp(Gdx.input.isKeyPressed(Input.Keys.UP)
                    || Gdx.input.isKeyPressed(Input.Keys.W));
            if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                player.shoot();
            }
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            gsm.setState(GameStateManager.MENU);
        }
    }

    @Override
    public void dispose() {

    }
}
