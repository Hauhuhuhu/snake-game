package snakegame.Model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class SnakeModel {
    private static final String BEST_SCORE_FILE = "best_score.txt";
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

    public int readBestScoreFromFile() {
        try {
            File file = new File(BEST_SCORE_FILE);
            if (!file.exists()) {
                return 0;
            }
            BufferedReader reader = new BufferedReader(new FileReader(file));
            int bestScore = Integer.parseInt(reader.readLine());
            reader.close();
            return bestScore;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public void writeBestScoreToFile(int score) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(BEST_SCORE_FILE));
            writer.write(String.valueOf(score));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    // Getters and setters
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

    public void setLeftDirection(boolean left) { leftDirection = left; }
    public void setRightDirection(boolean right) { rightDirection = right; }
    public void setUpDirection(boolean up) { upDirection = up; }
    public void setDownDirection(boolean down) { downDirection = down; }
    public void setPaused(boolean paused) { this.paused = paused; }
}