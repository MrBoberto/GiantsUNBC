package game;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.util.Objects;

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
            img = ImageIO.read(Objects.requireNonNull(getClass().getResource("/resources/GUI/character_closeups/character_closeup_blue.png")));
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
