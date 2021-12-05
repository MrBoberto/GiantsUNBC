package game;

/**
 * This file is part of a solution to
 *		CPSC300 Term Project Fall 2021
 *
 * Contains the JFrame in which the game is run. Contents swap between a Canvas and JPanel when the pause menu is
 * triggered.
 *
 * @author The Boyz
 * @version 1
 */

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
            System.out.println(e.getMessage());
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

    public boolean canPause() {
        return canPause;
    }

    public void setCanPause(boolean canPause) {
        this.canPause = canPause;
    }
}
