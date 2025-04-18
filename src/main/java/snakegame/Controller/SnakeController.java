package snakegame.Controller;

import javax.swing.*;
import java.awt.event.*;
import snakegame.Model.SnakeModel;
import snakegame.View.SnakeView;

public class SnakeController implements ActionListener {
    private SnakeModel model;
    private SnakeView view;
    private Timer timer;
    private boolean processingPause = false; // Cờ chống lặp sự kiện

    public SnakeController(SnakeModel model, SnakeView view) {
        this.model = model;
        this.view = view;
        this.timer = new Timer(400, this);
        setupButtonListeners();
        view.addKeyListener(new TAdapter());
        view.requestFocusInWindow(); // Đảm bảo SnakeView có focus
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
        model.newGame();
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
        view.hideMenuLevel();
        view.showMenu();
        view.revalidate();
        view.repaint();
    }

    public void handleHardLevel() {
        timer.stop();
        timer = new Timer(100, this);
        view.hideMenuLevel();
        view.showMenu();
        view.revalidate();
        view.repaint();
    }

    public void handleVeryHardLevel() {
        timer.stop();
        timer = new Timer(30, this);
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
            int bestScore = model.readBestScoreFromFile();
            if (model.getPoint() > bestScore) {
                bestScore = model.getPoint();
                model.writeBestScoreToFile(bestScore);
            }
            view.loadGameOver(model.getPoint(), bestScore);
            setupGameOverButtonListeners();
        }
        view.repaint();
    }

    private class TAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            try {
                int key = e.getKeyCode();
                if (model == null || timer == null) {
                    System.err.println("Error: model or timer is null in TAdapter");
                    return;
                }
                if (model.isInGame()) {
                    if (key == KeyEvent.VK_ENTER || key == KeyEvent.VK_ESCAPE) {
                        if (!processingPause) {
                            processingPause = true;
                            if (model.isPaused()) {
                                model.setPaused(false);
                                if (!timer.isRunning()) {
                                    timer.start();
                                }
                            } else {
                                model.setPaused(true);
                                if (timer.isRunning()) {
                                    timer.stop();
                                }
                            }
                            view.repaint();
                            // Đặt lại cờ sau 200ms
                            new Timer(200, evt -> {
                                processingPause = false;
                                ((Timer) evt.getSource()).stop();
                            }).start();
                        }
                    } else {
                        boolean directionChanged = false;
                        // Xử lý phím di chuyển bất kể trạng thái paused
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
                                // Tiếp tục trò chơi khi nhấn phím di chuyển
                                if (!processingPause) {
                                    processingPause = true;
                                    model.setPaused(false);
                                    if (!timer.isRunning()) {
                                        timer.start();
                                    }
                                    view.repaint();
                                    // Đặt lại cờ sau 200ms
                                    new Timer(200, evt -> {
                                        processingPause = false;
                                        ((Timer) evt.getSource()).stop();
                                    }).start();
                                }
                            } else if (!timer.isRunning()) {
                                // Khởi động timer nếu chưa chạy
                                timer.start();
                            }
                        }
                    }
                }
            } catch (Exception ex) {
                System.err.println("Error in TAdapter.keyPressed: " + ex.getMessage());
                ex.printStackTrace();
            }
        }
    }
}