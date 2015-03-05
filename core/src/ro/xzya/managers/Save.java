package ro.xzya.managers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import ro.xzya.game.Game;

/**
 * Created by Xzya on 4/3/2015.
 */
public class Save {

    public static GameData gd;

    public static void save() {
        try {
            ObjectOutputStream out;
            if (!Game.isMobile) {
                out = new ObjectOutputStream(
                        new FileOutputStream("highscores.sav")
                );
            } else {
                out = new ObjectOutputStream(
                        new FileOutputStream(Game.sdcard.getAbsolutePath() + "/Download/highscores.sav")
                );
            }
            out.writeObject(gd);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void load() {
        try {
            if (!saveFileExists()) {
                init();
                return;
            }
            ObjectInputStream in;
            if (!Game.isMobile) {
                in = new ObjectInputStream(
                        new FileInputStream("highscores.sav")
                );
            } else {
                in = new ObjectInputStream(
                        new FileInputStream(Game.sdcard.getAbsolutePath() + "/Download/highscores.sav")
                );
            }
            gd = (GameData) in.readObject();
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static boolean saveFileExists() {
        File f;
        if (!Game.isMobile) {
            f = new File("highscores.sav");
        } else {
            f = new File(Game.sdcard.getAbsolutePath() + "/Download/highscores.sav");
        }
        return f.exists();
    }

    public static void init() {
        gd = new GameData();
        gd.init();
        save();
    }

}
