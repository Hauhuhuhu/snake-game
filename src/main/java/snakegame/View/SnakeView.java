package snakegame.View;

import javax.swing.*;
import java.awt.*;
import snakegame.Model.SnakeModel;

public class SnakeView extends JPanel {
    private Image apple;
    private Image dot;
    private Image head;
    private Image backgroundImage;
    private JPanel menu;
    private JPanel menuOver;
    private JPanel menuLevel;
    private JLabel result;
    private JLabel resultMax;
    private JLabel level; // Thêm biến instance để cập nhật nhãn mức độ
    private Color colorBackground = new Color(241, 247, 254);
    private SnakeModel model;
    private JButton playButton;
    private JButton levelButton;
    private JButton normalLevelButton;
    private JButton hardLevelButton;
    private JButton veryHardLevelButton;
    private JButton replayButton;
    private JButton menuButton;
    private JLabel pauseLabel;

    public SnakeView(SnakeModel model) {
        this.model = model;
        setPreferredSize(new Dimension(300, 300));
        setFocusable(true);
        backgroundImage = new ImageIcon(ClassLoader.getSystemResource("snakegame/icon/map.jpg")).getImage();
        loadImages();
        loadMenu();
        pauseLabel = new JLabel("Paused", SwingConstants.CENTER);
        pauseLabel.setFont(new Font("Arial", Font.BOLD, 24));
        pauseLabel.setForeground(Color.YELLOW);
        pauseLabel.setVisible(false);
        add(pauseLabel);
    }

    public static JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Inter", Font.BOLD, 16));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(0, 102, 254));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private void loadImages() {
        ImageIcon headIcon = new ImageIcon(ClassLoader.getSystemResource("snakegame/icon/head.png"));
        head = headIcon.getImage();
        ImageIcon bodyIcon = new ImageIcon(ClassLoader.getSystemResource("snakegame/icon/body.png"));
        dot = bodyIcon.getImage();
        ImageIcon appleIcon = new ImageIcon(ClassLoader.getSystemResource("snakegame/icon/apple.png"));
        apple = appleIcon.getImage();
    }

    private void loadMenu() {
        menu = new JPanel();
        menu.setBackground(colorBackground);
        menu.setPreferredSize(new Dimension(250, 200));
        menu.setLayout(new BorderLayout());

        JLabel lb = new JLabel("Bảng Điều Khiển", SwingConstants.CENTER);
        lb.setFont(new Font("Arial", Font.BOLD, 18));
        lb.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        menu.add(lb, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(2, 1, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        buttonPanel.setBackground(colorBackground);
        playButton = createButton("Chơi");
        levelButton = createButton("Mức độ");
        buttonPanel.add(playButton);
        buttonPanel.add(levelButton);
        menu.add(buttonPanel, BorderLayout.CENTER);
        add(menu);
    }

    public void loadMenuLevel() {
        if (menuLevel == null) {
            menuLevel = new JPanel();
            menuLevel.setBackground(colorBackground);
            menuLevel.setPreferredSize(new Dimension(250, 200));
            menuLevel.setLayout(new BorderLayout());

            JLabel lb = new JLabel("Mức Độ", SwingConstants.CENTER);
            lb.setFont(new Font("Arial", Font.BOLD, 18));
            lb.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            menuLevel.add(lb, BorderLayout.NORTH);

            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new GridLayout(3, 1, 10, 10));
            buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            buttonPanel.setBackground(colorBackground);
            normalLevelButton = createButton("Bình Thường");
            hardLevelButton = createButton("Khó");
            veryHardLevelButton = createButton("Rất Khó");
            buttonPanel.add(normalLevelButton);
            buttonPanel.add(hardLevelButton);
            buttonPanel.add(veryHardLevelButton);
            menuLevel.add(buttonPanel, BorderLayout.CENTER);
            add(menuLevel);
        }
        menuLevel.setVisible(true);
    }

    public void loadGameOver(int point, int bestScore) {
        if (menuOver == null) {
            menuOver = new JPanel();
            menuOver.setBackground(colorBackground);
            menuOver.setPreferredSize(new Dimension(250, 220));
            menuOver.setLayout(new BorderLayout());

            JLabel lb = new JLabel("THUA", SwingConstants.CENTER);
            lb.setFont(new Font("Arial", Font.BOLD, 18));
            lb.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            menuOver.add(lb, BorderLayout.NORTH);

            JPanel resultPanel = new JPanel(new GridLayout(3, 2));
            resultPanel.setBackground(colorBackground);
            JLabel levelLabel = new JLabel("Mức độ:", SwingConstants.CENTER);
            levelLabel.setFont(new Font("Arial", Font.BOLD, 16));
            level = new JLabel("", SwingConstants.CENTER); // Khởi tạo nhãn mức độ
            level.setFont(new Font("Arial", Font.BOLD, 16));
            level.setForeground(Color.BLUE);
            JLabel resultLabel = new JLabel("Kết quả:", SwingConstants.CENTER);
            resultLabel.setFont(new Font("Arial", Font.BOLD, 16));
            result = new JLabel();
            result.setHorizontalAlignment(SwingConstants.CENTER);
            result.setFont(new Font("Arial", Font.BOLD, 16));
            result.setForeground(Color.RED);
            JLabel resultMaxLabel = new JLabel("Điểm cao nhất:", SwingConstants.CENTER);
            resultMaxLabel.setFont(new Font("Arial", Font.BOLD, 16));
            resultMax = new JLabel();
            resultMax.setHorizontalAlignment(SwingConstants.CENTER);
            resultMax.setFont(new Font("Arial", Font.BOLD, 16));
            resultMax.setForeground(Color.RED);
            resultPanel.add(levelLabel);
            resultPanel.add(level);
            resultPanel.add(resultLabel);
            resultPanel.add(result);
            resultPanel.add(resultMaxLabel);
            resultPanel.add(resultMax);
            menuOver.add(resultPanel, BorderLayout.CENTER);

            JPanel buttonPanel = new JPanel();
            buttonPanel.setBackground(colorBackground);
            buttonPanel.setLayout(new GridLayout(2, 1, 10, 10));
            buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            replayButton = createButton("Chơi lại");
            menuButton = createButton("Bảng điều khiển");
            buttonPanel.add(replayButton);
            buttonPanel.add(menuButton);
            menuOver.add(buttonPanel, BorderLayout.SOUTH);
            add(menuOver);
        }
        // Cập nhật nhãn mức độ mỗi lần gọi
        String levelText = model.getCurrentLevel() != null ? model.getCurrentLevel().toString() : "UNKNOWN";
        if(levelText == "NORMAL") levelText = "Bình Thường";
        if(levelText == "HARD") levelText = "Khó";
        if(levelText == "VERY_HARD") levelText = "Rất Khó";
        level.setText(levelText);
        result.setText(point + " điểm");
        resultMax.setText(bestScore + " điểm");
        menuOver.setVisible(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        drawGame(g, model);
        pauseLabel.setVisible(model.isPaused() && model.isInGame());
    }

    public void drawGame(Graphics g, SnakeModel model) {
        if (model.isInGame()) {
            g.drawImage(apple, model.getAppleX(), model.getAppleY(), this);
            for (int i = 0; i < model.getDots(); i++) {
                if (i == 0) {
                    g.drawImage(head, model.getX()[i], model.getY()[i], this);
                } else {
                    g.drawImage(dot, model.getX()[i], model.getY()[i], this);
                }
            }
        }
        Toolkit.getDefaultToolkit().sync();
    }

    // Methods to control visibility
    public void showMenu() {
        menu.setVisible(true);
        if (menuLevel != null) menuLevel.setVisible(false);
        if (menuOver != null) menuOver.setVisible(false);
        pauseLabel.setVisible(false);
    }

    public void hideMenu() {
        menu.setVisible(false);
    }

    public void showMenuLevel() {
        loadMenuLevel();
        menu.setVisible(false);
        if (menuOver != null) menuOver.setVisible(false);
        pauseLabel.setVisible(false);
    }

    public void hideMenuLevel() {
        if (menuLevel != null) menuLevel.setVisible(false);
    }

    public void hideMenuOver() {
        if (menuOver != null) menuOver.setVisible(false);
    }

    // Getters để controller gắn sự kiện
    public JButton getPlayButton() { return playButton; }
    public JButton getLevelButton() { return levelButton; }
    public JButton getNormalLevelButton() { return normalLevelButton; }
    public JButton getHardLevelButton() { return hardLevelButton; }
    public JButton getVeryHardLevelButton() { return veryHardLevelButton; }
    public JButton getReplayButton() { return replayButton; }
    public JButton getMenuButton() { return menuButton; }
}