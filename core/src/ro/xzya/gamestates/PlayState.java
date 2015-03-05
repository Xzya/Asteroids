package ro.xzya.gamestates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.util.ArrayList;

import ro.xzya.entities.Asteroid;
import ro.xzya.entities.Bullet;
import ro.xzya.entities.FlyingSaucer;
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

    private static final int dpSize1 = ((int) (Game.WIDTH * 0.08));//40
    private static final int dpSize2 = ((int) (Game.HEIGHT * 0.975));//390
    private static final int dpSize3 = ((int) (Game.WIDTH * 0.02));//10
    private static final int dpSize4 = ((int) (Game.HEIGHT * 0.9));//360

    private final int multiTouch = 4;
    private Vector3 touchPoint[];

    private SpriteBatch sb;
    private ShapeRenderer sr;

    private BitmapFont font;
    private Player hudPlayer;

    private Player player;
    private float shootTimer;
    private float shootTime;

    private ArrayList<Bullet> bullets;
    private ArrayList<Asteroid> asteroids;
    private ArrayList<Bullet> enemyBullets;

    private FlyingSaucer flyingSaucer;
    private float fsTimer;
    private float fsTime;

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

                if (Game.isMobile) {
//        if (true) {
            setTouchInput();
        }

        sb = new SpriteBatch();
        sr = new ShapeRenderer();

        //set font
        FreeTypeFontGenerator gen = new FreeTypeFontGenerator(

                Gdx.files.internal("fonts/Hyperspace Bold.ttf")
        );
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
//        parameter.size = 20;
        parameter.size = ((int) (Game.HEIGHT * 0.05));
        font = gen.generateFont(parameter);

        bullets = new ArrayList<Bullet>();

        player = new Player(bullets);
        shootTimer = 0;
        shootTime = 0.2f;

        asteroids = new ArrayList<Asteroid>();

        particles = new ArrayList<Particle>();

        level = 1;
        spawnAsteroids();

        hudPlayer = new Player(null);

        fsTimer = 0;
        fsTime = 15;
        enemyBullets = new ArrayList<Bullet>();

        //set up bg music
        maxDelay = 1;
        minDelay = 0.25f;
        currentDelay = maxDelay;
        bgTimer = maxDelay;
        playLowPulse = true;

        //init touch points
        touchPoint = new Vector3[multiTouch];
        for (int i = 0; i < multiTouch; i++) {
            touchPoint[i] = new Vector3();
        }
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
            flyingSaucer = null;
            Jukebox.stop("smallsaucer");
            Jukebox.stop("largesaucer");
            return;
        }

        //update player bullets
        for (int i = 0; i < bullets.size(); i++) {
            bullets.get(i).update(dt);
            if (bullets.get(i).shouldRemove()) {
                bullets.remove(i);
                i--;
            }
        }

        //update flying saucer
        if (flyingSaucer == null) {
            fsTimer += dt;
            if (fsTimer >= fsTime) {
                fsTimer = 0;
                int type = MathUtils.random() < 0.5 ?
                        FlyingSaucer.SMALL : FlyingSaucer.LARGE;
                int direction = MathUtils.random() < 0.5 ?
                        FlyingSaucer.RIGHT : FlyingSaucer.LEFT;
                flyingSaucer = new FlyingSaucer(
                        type,
                        direction,
                        player,
                        enemyBullets
                );
            }
        }
        //if there is a flying saucer already
        else {
            flyingSaucer.update(dt);
            if (flyingSaucer.shouldRemove()) {
                flyingSaucer = null;
                Jukebox.stop("smallsaucer");
                Jukebox.stop("largesaucer");
            }
        }

        //update fs bullets
        for (int i = 0; i < enemyBullets.size(); i++) {
            enemyBullets.get(i).update(dt);
            if (enemyBullets.get(i).shouldRemove()) {
                enemyBullets.remove(i);
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

        //player-flying saucer collision
        if (flyingSaucer != null) {
            if (player.intersects(flyingSaucer)) {
                player.hit();
                createParticles(player.getx(), player.gety());
                createParticles(flyingSaucer.getx(), flyingSaucer.gety());
                flyingSaucer = null;
                Jukebox.stop("smallsaucer");
                Jukebox.stop("largesaucer");
                Jukebox.stop("explode");
            }
        }

        //bullet-flying saucer collision
        if (flyingSaucer != null) {
            for (int i = 0; i < bullets.size(); i++) {
                Bullet b = bullets.get(i);
                if (flyingSaucer.contains(b.getx(), b.gety())) {
                    bullets.remove(i);
                    i--;
                    createParticles(flyingSaucer.getx(), flyingSaucer.gety());
                    player.incrementScore(flyingSaucer.getScore());
                    flyingSaucer = null;
                    Jukebox.stop("smallsaucer");
                    Jukebox.stop("largesaucer");
                    Jukebox.stop("explode");
                    break;
                }
            }
        }

        //player-enemy bullets collision
        if (!player.isHit()) {
            for (int i = 0; i < enemyBullets.size(); i++) {
                Bullet b = enemyBullets.get(i);
                if (player.contains(b.getx(), b.gety())) {
                    player.hit();
                    enemyBullets.remove(i);
                    i--;
                    Jukebox.play("explode");
                    break;
                }
            }
        }

        //flying saucer-asteroids collision
        if (flyingSaucer != null) {
            for (int i = 0; i < asteroids.size(); i++) {
                Asteroid a = asteroids.get(i);
                if (a.intersects(flyingSaucer)) {
                    asteroids.remove(i);
                    i--;
                    splitAsteroids(a);
                    createParticles(a.getx(), a.gety());
                    createParticles(flyingSaucer.getx(), flyingSaucer.gety());
                    flyingSaucer = null;
                    Jukebox.stop("smallsaucer");
                    Jukebox.stop("largesaucer");
                    Jukebox.stop("explode");
                    break;
                }
            }
        }

        //asteroids-enemy bullets collision
        for (int i = 0; i < enemyBullets.size(); i++) {
            Bullet b = enemyBullets.get(i);
            for (int j = 0; j < asteroids.size(); j++) {
                Asteroid a = asteroids.get(j);
                if (a.contains(b.getx(), b.gety())) {
                    asteroids.remove(j);
                    j--;
                    splitAsteroids(a);
                    enemyBullets.remove(i);
                    i--;
                    createParticles(a.getx(), a.gety());
                    Jukebox.stop("explode");
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

        //draw player bullets
        for (int i = 0; i < bullets.size(); i++) {
            bullets.get(i).draw(sr);
        }

        //draw flying saucer
        if (flyingSaucer != null) {
            flyingSaucer.draw(sr);
        }

        //draw fs bullets
        for (int i = 0; i < enemyBullets.size(); i++) {
            enemyBullets.get(i).draw(sr);
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
//        font.draw(sb, Long.toString(player.getScore()), 40, 390);
        font.draw(sb, Long.toString(player.getScore()), dpSize1, dpSize2);
        sb.end();

        //draw lives
        for (int i = 0; i < player.getLives(); i++) {
//            hudPlayer.setPosition(40 + i * 10, 360);
            hudPlayer.setPosition(dpSize1 + i * dpSize3, dpSize4);
            hudPlayer.draw(sr);
        }

        //draw on-screen controls
        if (Game.isMobile) {
//        if (true) {
            sr.begin(ShapeRenderer.ShapeType.Line);
            sr.rect(Game.gui.getWleftBounds().getX(), Game.gui.getWleftBounds().getY(), Game.gui.getWleftBounds().getWidth(), Game.gui.getWleftBounds().getHeight());
            sr.rect(Game.gui.getWrightBounds().getX(), Game.gui.getWrightBounds().getY(), Game.gui.getWrightBounds().getWidth(), Game.gui.getWrightBounds().getHeight());
            sr.rect(Game.gui.getBack().getX(), Game.gui.getBack().getY(), Game.gui.getBack().getWidth(), Game.gui.getBack().getHeight());
            sr.circle(Game.gui.getA().x, Game.gui.getA().y, Game.gui.getA().radius);
            sr.circle(Game.gui.getB().x, Game.gui.getB().y, Game.gui.getB().radius);
            sr.end();
        }

    }

    @Override
    public void handleInput() {
        if (!Game.isMobile) {
//        if (true) {
            if (!player.isHit()) {
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
                Jukebox.stopAll();
                gsm.setState(GameStateManager.MENU);
            }
        }



        //handle touch input
        if (Game.isMobile) {
//        if (true) {
            shootTimer += Gdx.graphics.getDeltaTime();
            player.setLeft(Game.gui.isPressedLeft());
            player.setRight(Game.gui.isPressedRight());
            player.setUp(Game.gui.isPressedB());
            if (Game.gui.isPressedA()) {
                if (shootTimer >= shootTime) {
                    shootTimer = 0;
                    player.shoot();
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
                Game.gui.setPressedLeft(true);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                Game.gui.setPressedLeft(false);
            }
        });

        Game.gui.getaWRightBounds().addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Game.gui.setPressedRight(true);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                Game.gui.setPressedRight(false);
            }
        });

        Game.gui.getaA().addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Game.gui.setPressedA(true);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                Game.gui.setPressedA(false);
            }
        });

        Game.gui.getaB().addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Game.gui.setPressedB(true);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                Game.gui.setPressedB(false);
            }
        });

        Game.gui.getaBack().addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Jukebox.stopAll();
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
