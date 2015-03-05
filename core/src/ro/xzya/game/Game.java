package ro.xzya.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import ro.xzya.managers.GameStateManager;
import ro.xzya.managers.Jukebox;

public class Game extends ApplicationAdapter {

	SpriteBatch batch;
	Texture img;

    public static int WIDTH;
    public static int HEIGHT;

    public static OrthographicCamera cam;
    private GameStateManager gsm;

	@Override
	public void create () {

        WIDTH = Gdx.graphics.getWidth();
        HEIGHT = Gdx.graphics.getHeight();

        cam = new OrthographicCamera(WIDTH, HEIGHT);
        cam.translate(WIDTH/2, HEIGHT/2);
        cam.update();

        loadSoundFiles();

        gsm = new GameStateManager();
//		batch = new SpriteBatch();
//		img = new Texture("../android/assets/badlogic.jpg");
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//		batch.begin();
//		batch.draw(img, 0, 0);
//		batch.end();

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
