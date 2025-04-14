package snakegame;

import javax.swing.*;

public class SnakeGame extends JFrame {
    SnakeGame() {
        super("Snake Game");
        add(new Board());
        pack();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        ImageIcon img = new ImageIcon(getClass().getResource("/snakegame/icon/logo.png"));
        setIconImage(img.getImage());
    }
    public static void main(String[] args) {
        new SnakeGame().setVisible(true);
    }
}