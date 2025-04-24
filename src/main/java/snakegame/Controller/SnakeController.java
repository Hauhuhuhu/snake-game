package snakegame.Controller;

import javax.swing.*;
import java.awt.event.*;
import snakegame.Model.SnakeModel;
import snakegame.View.SnakeView;

public class SnakeController implements ActionListener {
    private SnakeModel model;
    private SnakeView view;
    private Timer timer;

    public SnakeController(SnakeModel model, SnakeView view) {
        this.model = model;
        this.view = view;
        this.timer = new Timer(400, this);
        setupButtonListeners();
        view.addKeyListener(new TAdapter());
        view.requestFocusInWindow();
        timer.start();
    }

    private void setupButtonListeners() {
        if (view.getPlayButton() != null) {
            view.getPlayButton().addActionListener(e -> handlePlay());
        }
        if (view.getLevelButton() != null) {
            view.getLevelButton().addActionListener(e -> handleLevel());
        }
    }

    public void setupLevelButtonListeners() {
        if (view.getNormalLevelButton() != null) {
            view.getNormalLevelButton().addActionListener(e -> handleNormalLevel());
        }
        if (view.getHardLevelButton() != null) {
            view.getHardLevelButton().addActionListener(e -> handleHardLevel());
        }
        if (view.getVeryHardLevelButton() != null) {
            view.getVeryHardLevelButton().addActionListener(e -> handleVeryHardLevel());
        }
    }

    public void setupGameOverButtonListeners() {
        if (view.getReplayButton() != null) {
            view.getReplayButton().addActionListener(e -> handleReplay());
        }
        if (view.getMenuButton() != null) {
            view.getMenuButton().addActionListener(e -> handleMenu());
        }
    }

    public void handlePlay() {
//        model.newGame();
        view.hideMenu();
        timer.start();
        view.revalidate();
        view.repaint();
        view.requestFocusInWindow();
    }

    public void handleLevel() {
        view.showMenuLevel();
        setupLevelButtonListeners();
        view.revalidate();
        view.repaint();
    }

    public void handleNormalLevel() {
        timer.stop();
        timer = new Timer(400, this);
        model.setCurrentLevel(SnakeModel.Level.NORMAL);
        view.hideMenuLevel();
        view.showMenu();
        view.revalidate();
        view.repaint();
    }

    public void handleHardLevel() {
        timer.stop();
        timer = new Timer(100, this);
        model.setCurrentLevel(SnakeModel.Level.HARD);
        view.hideMenuLevel();
        view.showMenu();
        view.revalidate();
        view.repaint();
    }

    public void handleVeryHardLevel() {
        timer.stop();
        timer = new Timer(30, this);
        model.setCurrentLevel(SnakeModel.Level.VERY_HARD);
        view.hideMenuLevel();
        view.showMenu();
        view.revalidate();
        view.repaint();
    }

    public void handleReplay() {
        model.newGame();
        if (timer.isRunning()) timer.stop();
        timer.start();
        view.hideMenuOver();
        view.revalidate();
        view.repaint();
        view.requestFocusInWindow();
    }

    public void handleMenu() {
        model.newGame();
        view.showMenu();
        if (timer.isRunning()) timer.stop();
        view.revalidate();
        view.repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (model.isInGame() && !model.isPaused()) {
            model.checkApple();
            model.checkCollision();
            model.move();
        } else if (!model.isInGame()) {
            timer.stop();
            int bestScore = model.getBestScoreForLevel(model.getCurrentLevel());
            if (model.getPoint() > bestScore) {
                model.writeBestScoreToFile(model.getCurrentLevel(), model.getPoint());
                bestScore = model.getPoint();
            }
            view.loadGameOver(model.getPoint(), bestScore);
            setupGameOverButtonListeners();
        }
        view.repaint();
    }

    private class TAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();
            if (model == null || timer == null) {
                System.err.println("Error model or timer is null");
                return;
            }
            if (model.isInGame()) {
                if (key == KeyEvent.VK_ENTER || key == KeyEvent.VK_ESCAPE) {
                    model.setPaused(!model.isPaused());
                    if (model.isPaused()) timer.stop(); else timer.start();
                    view.repaint();
                } else {
                    boolean directionChanged = false;
                    if ((key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) && !model.isRightDirection()) {
                        model.setLeftDirection(true);
                        model.setDownDirection(false);
                        model.setUpDirection(false);
                        directionChanged = true;
                    }
                    if ((key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) && !model.isLeftDirection()) {
                        model.setRightDirection(true);
                        model.setDownDirection(false);
                        model.setUpDirection(false);
                        directionChanged = true;
                    }
                    if ((key == KeyEvent.VK_UP || key == KeyEvent.VK_W) && !model.isDownDirection()) {
                        model.setUpDirection(true);
                        model.setLeftDirection(false);
                        model.setRightDirection(false);
                        directionChanged = true;
                    }
                    if ((key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S) && !model.isUpDirection()) {
                        model.setDownDirection(true);
                        model.setLeftDirection(false);
                        model.setRightDirection(false);
                        directionChanged = true;
                    }
                    if (directionChanged) {
                        if (model.isPaused()) {
                            model.setPaused(!model.isPaused());
                            timer.start();
                            view.repaint();
                        } else if (!timer.isRunning()) {
                            timer.start();
                        }
                    }
                }
            }
        }
    }
}