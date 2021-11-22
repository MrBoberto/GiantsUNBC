package game;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class GameWindow {
    public GameWindow(int width, int height, String windowName, Controller controller) {
        JFrame frame = new JFrame(windowName);

        frame.setPreferredSize(new Dimension(width, height));
        frame.setMaximumSize(new Dimension(width, height));
        frame.setMinimumSize(new Dimension(width, height));

        Image img;
        try {
            img = ImageIO.read(getClass().getResource("/resources/GUI/icon/icon.png"));
            frame.setIconImage(img);
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }



        frame.add(controller);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
