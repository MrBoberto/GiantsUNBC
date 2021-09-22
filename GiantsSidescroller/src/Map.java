package GiantsSidescroller.src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Map extends JPanel implements ActionListener, KeyListener {
    // delay between each frame
    public final int FRAMEDELAY = 20;
    public final int WIDTH = 1920;
    public final int HEIGHT = 1080;

    private Timer timer;
    private Player player;

    public Map() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(new Color(100, 100, 100));

        timer = new Timer(FRAMEDELAY, this);
        timer.start();

        player = new Player();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        player.tick();
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        drawBackground(g);

        player.draw(g, this);

        Toolkit.getDefaultToolkit().sync();
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        player.keyPressed(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        player.keyReleased(e);
    }

    private void drawBackground(Graphics g) {

    }
}
