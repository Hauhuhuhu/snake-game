package snakegame;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Board extends JPanel implements ActionListener {
    private Timer timer = new Timer(400, this);
    private JPanel menu = new JPanel();
    private JPanel menuOver;
    private JLabel result;
    private JLabel resultMax;
    private static final String BEST_SCORE_FILE = "best_score.txt";
    private int dots;
    private int apple_x;
    private int apple_y;
    private int point = 0;
    private Image apple;
    private Image dot;
    private Image head;
    private final int ALL_DOTS = 900;
    private final int DOT_SIZE = 10;
    private final int RANDOM_POSITION = 24;
    private final int x[] = new int[ALL_DOTS];
    private final int y[] = new int[ALL_DOTS];
    private boolean leftDirection = false;
    private boolean rightDirection = false;
    private boolean upDirection = false;
    private boolean downDirection = false;
    private boolean inGame = true;
    private Image backgroundImage;
    private Color colorBacground =  new Color(241, 247, 254);

    Board() {
        addKeyListener(new TAdapter());
        backgroundImage = new ImageIcon(ClassLoader.getSystemResource("snakegame/icon/map.jpg")).getImage();
        setPreferredSize(new Dimension(300, 300));
        setFocusable(true);
        loadMenu();
        loadImages();
        initGame();
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
    public void loadMenu() {
        menu.setBackground(colorBacground); // Màu nền nhẹ hơn
        menu.setPreferredSize(new Dimension(250, 200));
        menu.setLayout(new BorderLayout());
        // Tiêu đề
        JLabel lb = new JLabel("Bảng Điều Khiển", SwingConstants.CENTER);
        lb.setFont(new Font("Arial", Font.BOLD, 18));
        lb.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        menu.add(lb, BorderLayout.NORTH);
        // Panel chứa các nút
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(2, 1, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        buttonPanel.setBackground(colorBacground);
        JButton b1 = createButton("Chơi");
        JButton b2 = createButton("Mức độ");
        b1.addActionListener(e -> {
            newGame();
            menu.setVisible(false);
            revalidate();
            repaint();
        });
        b2.addActionListener(e -> {
            menu.setVisible(false);
            menuLevel();
        });
        buttonPanel.add(b1);
        buttonPanel.add(b2);

        menu.add(buttonPanel, BorderLayout.CENTER);
        add(menu);
    }
    public void pauseGame() {
        timer.stop();
    }
    public void menuLevel() {

        JPanel mLevel = new JPanel();
        mLevel.setBackground(colorBacground);
        mLevel.setPreferredSize(new Dimension(250, 200));
        mLevel.setLayout(new BorderLayout());

        // Tiêu đề
        JLabel lb = new JLabel("Mức Độ", SwingConstants.CENTER);
        lb.setFont(new Font("Arial", Font.BOLD, 18));
        lb.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mLevel.add(lb, BorderLayout.NORTH);
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(3, 1, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        buttonPanel.setBackground(colorBacground);
        JButton b1 = createButton("Bình Thường");
        JButton b2 = createButton("Khó");
        JButton b3 = createButton("Rất Khó");
        b1.addActionListener(e -> {
            timer.stop();
            timer = new Timer(400, this);
            mLevel.setVisible(false);
            menu.setVisible(true);
        });
        b2.addActionListener(e -> {
            timer.stop();
            timer = new Timer(100, this);
            mLevel.setVisible(false);
            menu.setVisible(true);
        });
        b3.addActionListener(e -> {
            timer.stop();
            timer = new Timer(30, this);
            mLevel.setVisible(false);
            menu.setVisible(true);
        });
        buttonPanel.add(b1);
        buttonPanel.add(b2);
        buttonPanel.add(b3);

        mLevel.add(buttonPanel, BorderLayout.CENTER);
        add(mLevel);
    }

    public void loadImages() {
        ImageIcon headIcon = new ImageIcon(ClassLoader.getSystemResource("snakegame/icon/head.png"));
        head = headIcon.getImage();
        ImageIcon bodyIcon = new ImageIcon(ClassLoader.getSystemResource("snakegame/icon/body.png"));
        dot = bodyIcon.getImage();
        ImageIcon appleIcon = new ImageIcon(ClassLoader.getSystemResource("snakegame/icon/apple.png"));
        apple = appleIcon.getImage();
    }
    public void initGame() {
        dots = 3;
        for(int i = 0; i < dots; i++) {
            y[i] = 260 + i * DOT_SIZE;
            x[i] = 150;
        }
        locateApple();
        timer.start();
    }
    public void locateApple() {
        int r = (int)(Math.random() * RANDOM_POSITION);
        apple_x = 30 + r * DOT_SIZE;
        r = (int)(Math.random() * RANDOM_POSITION);
        apple_y = 30 +  r * DOT_SIZE;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        draw(g);
    }
    public void draw(Graphics g) {
        if(inGame) {
            g.drawImage(apple, apple_x, apple_y, this);
            for(int i = 0; i < dots; i++) {
                if(i == 0) {
                    g.drawImage(head, x[i], y[i], this);
                } else {
                    g.drawImage(dot, x[i], y[i], this);
                }
            }
        } else {
            gameOver();
        }
        Toolkit.getDefaultToolkit().sync();
    }

    public void move() {
        if(rightDirection || upDirection || downDirection || leftDirection) {
            for(int i = dots; i > 0; i--) {
                x[i] = x[i - 1];
                y[i] = y[i - 1];
            }
        }
        if(leftDirection) {
            x[0] = x[0] - DOT_SIZE;
        }
        if(rightDirection) {
            x[0] = x[0] + DOT_SIZE;
        }
        if(upDirection) {
            y[0] = y[0] - DOT_SIZE;
        }
        if(downDirection) {
            y[0] = y[0] + DOT_SIZE;
        }
    }
    public void checkApple() {
        if(x[0] == apple_x && y[0] == apple_y) {
            dots++;
            point+=10;
            locateApple();
        }
    }
    public void checkCollistion() {
        for(int i = dots; i > 0; i--) {
            if(dots > 4 && x[0] == x[i] && y[0] == y[i]) {
                inGame = false;
            }
        }
        if(x[0] >= 265 || y[0] >= 265) {
            inGame = false;
        }
        if(x[0] < 25 || y[0] < 25) {
            inGame = false;
        }
        if(!inGame) {
            timer.stop();
        }
    }
    public void actionPerformed(ActionEvent ae) {
        if(inGame) {
            checkApple();
            checkCollistion();
            move();
        }
        repaint();
    }
    public class TAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();
            if((key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) && (!rightDirection)) {
                leftDirection = true;
                downDirection = false;
                upDirection = false;
                timer.start();
            }
            if((key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) && (!leftDirection)) {
                rightDirection = true;
                downDirection = false;
                upDirection = false;
                timer.start();
            }
            if((key == KeyEvent.VK_UP || key == KeyEvent.VK_W) && (!downDirection)) {
                upDirection = true;
                leftDirection = false;
                rightDirection = false;
                timer.start();
            }
            if((key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S) && (!upDirection)) {
                downDirection = true;
                leftDirection = false;
                rightDirection = false;
                timer.start();
            }
            if (key == KeyEvent.VK_ENTER || key == KeyEvent.VK_ESCAPE) {
                if (timer.isRunning()) {
                    timer.stop();
                } else {
                    timer.start();
                }
            }
        }
    }

    public void newGame() {
        int temp = timer.getDelay();
        inGame = true;
        leftDirection = false;
        rightDirection = false;
        upDirection = false;
        downDirection = false;
        point = 0;
        locateApple();
        initGame();
        timer.setDelay(temp);
    }
    private int readBestScoreFromFile() {
        try {
            File file = new File(BEST_SCORE_FILE);
            if (!file.exists()) {
                return 0; // Nếu file chưa có thì mặc định là 0
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
    private void writeBestScoreToFile(int score) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(BEST_SCORE_FILE));
            writer.write(String.valueOf(score));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void gameOver() {
        if (menuOver == null) {
            menuOver = new JPanel();
            menuOver.setBackground(colorBacground);
            menuOver.setPreferredSize(new Dimension(250, 220));
            menuOver.setLayout(new BorderLayout());

            JLabel lb = new JLabel("THUA", SwingConstants.CENTER);
            lb.setFont(new Font("Arial", Font.BOLD, 18));
            lb.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            menuOver.add(lb, BorderLayout.NORTH);
            // Bảng kết quả (Result)
            JPanel resultPanel = new JPanel(new GridLayout(2, 2));
            resultPanel.setBackground(colorBacground);

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

            resultPanel.add(resultLabel);
            resultPanel.add(result);
            resultPanel.add(resultMaxLabel);
            resultPanel.add(resultMax);
            menuOver.add(resultPanel, BorderLayout.CENTER);
//            Nút bấm
            JPanel buttonPanel = new JPanel();
            buttonPanel.setBackground(colorBacground);
            buttonPanel.setLayout(new GridLayout(2, 1, 10, 10));
            buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            JButton b1 = createButton("Chơi lại");
            JButton b2 = createButton("Bảng điều khiển");
            b1.addActionListener(e -> {
                newGame();
                menuOver.setVisible(false);
                repaint();
            });

            // Xử lý sự kiện khi nhấn "Menu"
            b2.addActionListener(e -> {
                newGame();
                menuOver.setVisible(false);
                menu.setVisible(true);
                repaint();
            });

            buttonPanel.add(b1);
            buttonPanel.add(b2);
            menuOver.add(buttonPanel, BorderLayout.SOUTH);

            add(menuOver);
        }
        result.setText(String.valueOf(point) + " điểm");
        int bestScore = readBestScoreFromFile();
        if (point > bestScore) {
            bestScore = point;
            writeBestScoreToFile(bestScore);
        }
        resultMax.setText(bestScore + " điểm");
        menuOver.setVisible(true);
        revalidate();
        repaint();
    }
}
