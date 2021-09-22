package GiantsSidescroller.src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Map extends JPanel implements ActionListener, KeyListener {
    // delay between each frame
    public final int FRAMEDELAY = 20;
    public final int WIDTH = 1280;
    public final int HEIGHT = WIDTH / 16 * 9;


    private Timer timer;
    private Player player;

    public Map() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(new Color(0, 150, 0));

        timer = new Timer(FRAMEDELAY, this);
        timer.start();

        player = new Player(100, 100);
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

    public Player getPlayer() {
        return player;
    }
}
