package ro.xzya.managers;

import java.io.Serializable;

/**
 * Created by Xzya on 4/3/2015.
 */
public class GameData implements Serializable {

    private static final long serialVersionUID = 1L;

    private final int MAX_SCORES = 10;
    private long[] highscores;
    private String[] names;

    private long tentativeScore;

    public GameData() {
        highscores = new long[MAX_SCORES];
        names = new String[MAX_SCORES];
    }

    //sets up an empty high scores table
    public void init() {
        for (int i = 0; i < MAX_SCORES; i++) {
            highscores[i] = 0;
            names[i] = "---";
        }
    }

    public long[] getHighscores() {
        return highscores;
    }

    public String[] getNames() {
        return names;
    }

    public long getTentativeScore() {
        return tentativeScore;
    }

    public void setTentativeScore(long i) {
        tentativeScore = i;
    }

    public boolean isHighScore(long score) {
        return score > highscores[MAX_SCORES - 1];
    }

    public void addHighScore(long newScore, String name) {
        if (isHighScore(newScore)) {
            highscores[MAX_SCORES - 1] = newScore;
            names[MAX_SCORES - 1] = name;
            sortHighscores();
        }
    }

    private void sortHighscores() {
        for (int i = 0; i < MAX_SCORES; i++) {
            long score = highscores[i];
            String name = names[i];
            int j;
            for (j = i - 1;
                 j >= 0 && highscores[j] < score;
                 j++) {
                highscores[j + 1] = highscores[j];
                names[j + 1] = names[j];
            }
            highscores[j + 1] = score;
            names[j + 1] = name;
        }
    }

}
