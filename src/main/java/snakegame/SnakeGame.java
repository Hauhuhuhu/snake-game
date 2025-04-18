package snakegame;

import javax.swing.*;
import snakegame.Model.SnakeModel;
import snakegame.View.SnakeView;
import snakegame.Controller.SnakeController;


public class SnakeGame extends JFrame {
    public SnakeGame() {
        SnakeModel model = new SnakeModel();
        SnakeView view = new SnakeView(model);
        SnakeController controller = new SnakeController(model, view);

        // Không cần KeyAdapter vì TAdapter trong SnakeController đã xử lý
        view.setFocusable(true);
        view.requestFocusInWindow();

        add(view);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Snake Game");
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SnakeGame::new);
    }
}