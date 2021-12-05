package game;

import utilities.BufferedImageLoader;

import javax.swing.*;
import java.awt.*;

public class GameWindow {
    protected final JFrame frame;
    protected boolean canPause = true;

    public GameWindow(int width, int height, String windowName, Controller controller) {

        frame = new JFrame(windowName);

        frame.setPreferredSize(new Dimension(width, height));
        frame.setMaximumSize(new Dimension(width, height));
        frame.setMinimumSize(new Dimension(width, height));

        Image img;
        try {
            img = BufferedImageLoader.loadImage("/resources/GUI/character_closeups/character_closeup_blue.png");
            frame.setIconImage(img);
        } catch (Exception e) {
            e.printStackTrace();
        }

        frame.add(controller);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public JFrame getFrame() {
        return frame;
    }

    public void setCanPause(boolean canPause) {
        this.canPause = canPause;
    }
}
