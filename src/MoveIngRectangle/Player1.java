package MoveIngRectangle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class Player1 extends JComponent {


    @Override
    public void paint(Graphics g) {
        g.setColor(Color.RED);
        g.fillRect(100,100,80,80);

    }

    public Player1() {
    repaint();
    }
}
