package ro.xzya.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.io.File;

import ro.xzya.managers.GUI;
import ro.xzya.managers.GameStateManager;
import ro.xzya.managers.Jukebox;

public class Game extends ApplicationAdapter {

    public static File sdcard;

    public static boolean isMobile = false;

	SpriteBatch batch;
	Texture img;

    public static int WIDTH;
    public static int HEIGHT;

    public static OrthographicCamera cam;
    public static OrthographicCamera guicam;

    public static volatile GUI gui;

    private GameStateManager gsm;

    public Game(File sdcard) {
        this.sdcard = sdcard;
    }

	@Override
	public void create () {

        Gdx.graphics.setDisplayMode(800, 600, false);
        WIDTH = Gdx.graphics.getWidth();
        HEIGHT = Gdx.graphics.getHeight();

        if (Gdx.app.getType().equals(Application.ApplicationType.Android)){
//        if (true) {
            //init guicamera
            guicam = new OrthographicCamera(WIDTH, HEIGHT);
            guicam.translate(WIDTH/2, HEIGHT/2);
            guicam.update();

            gui = new GUI();

            isMobile = true;
        }

        //init camera
        cam = new OrthographicCamera(WIDTH, HEIGHT);
        cam.translate(WIDTH/2, HEIGHT/2);
        cam.update();

        //load sound files
        loadSoundFiles();

        gsm = new GameStateManager();
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        gsm.update(Gdx.graphics.getDeltaTime());
        gsm.draw();

	}

    private void loadSoundFiles() {
        Jukebox.load("sounds/explode.ogg", "explode");
        Jukebox.load("sounds/extralife.ogg", "extralife");
        Jukebox.load("sounds/largesaucer.ogg", "largesaucer");
        Jukebox.load("sounds/pulsehigh.ogg", "pulsehigh");
        Jukebox.load("sounds/pulselow.ogg", "pulselow");
        Jukebox.load("sounds/saucershoot.ogg", "saucershoot");
        Jukebox.load("sounds/shoot.ogg", "shoot");
        Jukebox.load("sounds/smallsaucer.ogg", "smallsaucer");
        Jukebox.load("sounds/thruster.ogg", "thruster");
    }

}
