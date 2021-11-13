package MoveIngRectangle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class Window extends JFrame {
    public Window(){
        ThisPanel thisPanel = new ThisPanel(Color.BLUE,0,0, KeyEvent.VK_W,KeyEvent.VK_S,KeyEvent.VK_A,KeyEvent.VK_D);

        setTitle("My Rectangle");
        setSize(600,600);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        thisPanel.add(new Player1());
        add(thisPanel);

    }
}
