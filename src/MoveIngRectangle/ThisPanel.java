package MoveIngRectangle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class ThisPanel extends JPanel implements ActionListener, KeyListener {



    private int xMove;
    private int yMove;
    private int xVel;
    private int yVel;

    private int x1Move;
    private int y1Move;
    private int x1Vel;
    private int y1Vel;

    private final int up;
    private final   int down;
    private int left;
    private int right;
    private Color color;

    Timer timer;
    public ThisPanel(Color color, int xCordinates, int yCordinates, int Up, int down, int left, int right ){


        this.timer = new Timer(50, this);
        this.xMove = xCordinates;
        this.yMove = yCordinates;
        this.xVel = 0;
        this.yVel   = 0;


        this.up = Up;
        this.down = down;
        this.left = left;
        this.right = right;
        this.color = color;

//        this.x1Move = 500;
//        this.y1Move = 0;
//        this.x1Vel = 0;
//        this.y1Vel   = 0;
        timer.start();

        addKeyListener(this);
        setFocusable(true);// to enable keyListern but check
        setFocusTraversalKeysEnabled(false);// we wont be using shift or tab key
    }


    public void paintComponent(Graphics g){
        super.paintComponent(g);
        g.setColor(this.color);
        g.fillRect(xMove,yMove,80,80);
//        g.setColor(Color.YELLOW);
//        g.fillRect(x1Move,y1Move,80,80);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        xMove = xMove+ xVel;
        yMove = yVel+ yMove;

//        x1Move = x1Move+ x1Vel;
//        y1Move = y1Vel+ y1Move;


        repaint();
    }



    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int c = e.getKeyCode();// in keyEvent each key is assigned a keyCode

        if(c == this.up){
            yVel = -10;

        }
        if(c == this.down){
            yVel = +10;
        }
        if(c == this.left){
            xVel = -10;

        }
        if(c == this.right){
            xVel = +10;
        }
        /*
        switch(c){
            case KeyEvent.VK_UP:

                yVel = -10;
                    break;
            case KeyEvent.VK_DOWN:

                yVel = +10;
                break;
            case KeyEvent.VK_LEFT:
                xVel = -10;

                break;

            case KeyEvent.VK_RIGHT:
                xVel = +10;
                break;


            case KeyEvent.VK_W:

                y1Vel = -10;
                break;
            case KeyEvent.VK_S:

                y1Vel = +10;
                break;
            case KeyEvent.VK_A:
                x1Vel = -10;

                break;

            case KeyEvent.VK_D:
                x1Vel = +10;
                break;
        }


 */
    }

    @Override
    public void keyReleased(KeyEvent e) {
        xVel = 0;
        yVel = 0;
        x1Vel = 0;
        y1Vel = 0;
    }
}



/*
package MoveIngRectangle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class ThisPanel extends JPanel implements ActionListener, KeyListener {



    private int xMove;
    private int yMove;
    private int xVel;
    private int yVel;

    private int x1Move;
    private int y1Move;
    private int x1Vel;
    private int y1Vel;
    Timer timer;
    public ThisPanel(){


        this.timer = new Timer(50, this);
        this.xMove = 0;
        this.yMove = 0;
        this.xVel = 0;
        this.yVel   = 0;

        this.x1Move = 500;
        this.y1Move = 0;
        this.x1Vel = 0;
        this.y1Vel   = 0;
        timer.start();

        addKeyListener(this);
        setFocusable(true);// to enable keyListern but check
        setFocusTraversalKeysEnabled(false);// we wont be using shift or tab key
    }


    public void paintComponent(Graphics g){
        super.paintComponent(g);
        g.setColor(Color.BLUE);
        g.fillRect(xMove,yMove,80,80);
        g.setColor(Color.YELLOW);
        g.fillRect(x1Move,y1Move,80,80);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        xMove = xMove+ xVel;
        yMove = yVel+ yMove;

        x1Move = x1Move+ x1Vel;
        y1Move = y1Vel+ y1Move;


        repaint();
    }



    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int c = e.getKeyCode();// in keyEvent each key is assigned a keyCode

        switch(c){
            case KeyEvent.VK_UP:

                yVel = -10;
                    break;
            case KeyEvent.VK_DOWN:

                yVel = +10;
                break;
            case KeyEvent.VK_LEFT:
                xVel = -10;

                break;

            case KeyEvent.VK_RIGHT:
                xVel = +10;
                break;








            case KeyEvent.VK_W:

                y1Vel = -10;
                break;
            case KeyEvent.VK_S:

                y1Vel = +10;
                break;
            case KeyEvent.VK_A:
                x1Vel = -10;

                break;

            case KeyEvent.VK_D:
                x1Vel = +10;
                break;
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
        xVel = 0;
        yVel = 0;
        x1Vel = 0;
        y1Vel = 0;
    }
}

 */
