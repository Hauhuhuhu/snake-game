package snakegame.Model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SnakeModel {
    private static final String BEST_SCORES_FILE = "best_scores.txt";
    private int dots;
    private int apple_x;
    private int apple_y;
    private int point = 0;
    private final int ALL_DOTS = 900;
    private final int DOT_SIZE = 10;
    private final int RANDOM_POSITION = 24;
    private final int[] x = new int[ALL_DOTS];
    private final int[] y = new int[ALL_DOTS];
    private boolean leftDirection = false;
    private boolean rightDirection = false;
    private boolean upDirection = false;
    private boolean downDirection = false;
    private boolean inGame = true;
    private boolean paused = false;
    private Level currentLevel = Level.NORMAL;


    public enum Level {
        NORMAL, HARD, VERY_HARD
    }

    public SnakeModel() {
        initGame();
    }

    public void initGame() {
        dots = 3;
        for (int i = 0; i < dots; i++) {
            y[i] = 260 + i * DOT_SIZE;
            x[i] = 150;
        }
        locateApple();
        paused = false;
        point = 0;
    }

    public void locateApple() {
        int r = (int) (Math.random() * RANDOM_POSITION);
        apple_x = 30 + r * DOT_SIZE;
        r = (int) (Math.random() * RANDOM_POSITION);
        apple_y = 30 + r * DOT_SIZE;
    }

    public void move() {
        if (rightDirection || upDirection || downDirection || leftDirection) {
            for (int i = dots; i > 0; i--) {
                x[i] = x[i - 1];
                y[i] = y[i - 1];
            }
        }
        if (leftDirection) {
            x[0] -= DOT_SIZE;
        }
        if (rightDirection) {
            x[0] += DOT_SIZE;
        }
        if (upDirection) {
            y[0] -= DOT_SIZE;
        }
        if (downDirection) {
            y[0] += DOT_SIZE;
        }
    }

    public void checkApple() {
        if (x[0] == apple_x && y[0] == apple_y) {
            dots++;
            point += 10;
            locateApple();
        }
    }

    public void checkCollision() {
        for (int i = dots; i > 0; i--) {
            if (dots > 4 && x[0] == x[i] && y[0] == y[i]) {
                inGame = false;
            }
        }
        if (x[0] >= 265 || y[0] >= 265 || x[0] < 25 || y[0] < 25) {
            inGame = false;
        }
    }

    public Map<Level, Integer> readBestScoresFromFile() {
        Map<Level, Integer> bestScores = new HashMap<>();
        bestScores.put(Level.NORMAL, 0);
        bestScores.put(Level.HARD, 0);
        bestScores.put(Level.VERY_HARD, 0);
        try {
            File file = new File(BEST_SCORES_FILE);
            if (!file.exists()) {
                return bestScores;
            }
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    Level level = Level.valueOf(parts[0]);
                    int score = Integer.parseInt(parts[1]);
                    bestScores.put(level, score);
                }
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bestScores;
    }

    public void writeBestScoreToFile(Level level, int score) {
        Map<Level, Integer> bestScores = readBestScoresFromFile();
        bestScores.put(level, Math.max(bestScores.get(level), score));
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(BEST_SCORES_FILE));
            for (Map.Entry<Level, Integer> entry : bestScores.entrySet()) {
                writer.write(entry.getKey() + ":" + entry.getValue());
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getBestScoreForLevel(Level level) {
        return readBestScoresFromFile().get(level);
    }

    public void newGame() {
        inGame = true;
        leftDirection = false;
        rightDirection = false;
        upDirection = false;
        downDirection = false;
        point = 0;
        paused = false;
        initGame();
    }


    public int getDots() { return dots; }
    public int getAppleX() { return apple_x; }
    public int getAppleY() { return apple_y; }
    public int getPoint() { return point; }
    public int[] getX() { return x; }
    public int[] getY() { return y; }
    public boolean isInGame() { return inGame; }
    public boolean isLeftDirection() { return leftDirection; }
    public boolean isRightDirection() { return rightDirection; }
    public boolean isUpDirection() { return upDirection; }
    public boolean isDownDirection() { return downDirection; }
    public boolean isPaused() { return paused; }
    public Level getCurrentLevel() { return currentLevel; }

    public void setLeftDirection(boolean left) { leftDirection = left; }
    public void setRightDirection(boolean right) { rightDirection = right; }
    public void setUpDirection(boolean up) { upDirection = up; }
    public void setDownDirection(boolean down) { downDirection = down; }
    public void setPaused(boolean paused) { this.paused = paused; }
    public void setCurrentLevel(Level level) { this.currentLevel = level; }
}