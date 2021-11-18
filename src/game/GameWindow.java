package game;

import javax.swing.*;
import java.awt.*;

public class GameWindow {
    public GameWindow(int width, int height, String windowName, Controller controller) {
        JFrame frame = new JFrame(windowName);

        frame.setPreferredSize(new Dimension(width, height));
        frame.setMaximumSize(new Dimension(width, height));
        frame.setMinimumSize(new Dimension(width, height));

        frame.add(controller);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
