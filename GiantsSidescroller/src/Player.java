package GiantsSidescroller.src;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;

public class Player {
    private BufferedImage currentImage;
    private Point pos;
    // Coins in the player's inventory
    private int pocketMoney;
    private boolean wIsHeld = false;
    private boolean dIsHeld = false;
    private boolean sIsHeld = false;
    private boolean aIsHeld = false;
    private boolean facingRight = true;

    public Player() {
        loadImage();

        pos = new Point(500, 500);
        pocketMoney = 0;
    }

    private void loadImage() {
        if(facingRight && !dIsHeld) {
            try {
                currentImage = ImageIO.read(new File("GiantsSidescroller/src/images/player1/right_stand.png"));
            } catch (IOException exc) {
                System.out.println("Could not find image file: " + exc.getMessage());
            }
        } else if(!facingRight && !aIsHeld) {
            try {
                currentImage = ImageIO.read(new File("GiantsSidescroller/src/images/player1/left_stand.png"));
            } catch (IOException exc) {
                System.out.println("Could not find image file: " + exc.getMessage());
            }
        } else if(facingRight) {
            try {
                currentImage = ImageIO.read(new File("GiantsSidescroller/src/images/player1/right_jog_1.png"));
            } catch (IOException exc) {
                System.out.println("Could not find image file: " + exc.getMessage());
            }
        } else if(!facingRight) {
            try {
                currentImage = ImageIO.read(new File("GiantsSidescroller/src/images/player1/left_jog_1.png"));
            } catch (IOException exc) {
                System.out.println("Could not find image file: " + exc.getMessage());
            }
        }
    }

    public void draw(Graphics g, ImageObserver observer) {
        loadImage();
        g.drawImage(
                currentImage,
                pos.x + 40,
                pos.y + 75,
                observer
        );
    }

    public void keyPressed(KeyEvent e) {
        // Determine key code so it can be compared to a key recognized by humans
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_W) {
            wIsHeld = true;
        }
        if (key == KeyEvent.VK_D) {
            dIsHeld = true;
            facingRight = true;
        }
        if (key == KeyEvent.VK_S) {
            sIsHeld = true;
        }
        if (key == KeyEvent.VK_A) {
            aIsHeld = true;
            facingRight = false;
        }
    }

    public void keyReleased(KeyEvent e) {
        // Determine key code so it can be compared to a key recognized by humans
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_W) {
            wIsHeld = false;
        }
        if (key == KeyEvent.VK_D) {
            dIsHeld = false;

        }
        if (key == KeyEvent.VK_S) {
            sIsHeld = false;
        }
        if (key == KeyEvent.VK_A) {
            aIsHeld = false;
        }
    }

    public void move() {
        if (wIsHeld) {
            pos.translate(0, -1);
        }
        if (dIsHeld) {
            pos.translate(1, 0);
        }
        if (sIsHeld) {
            pos.translate(0, 1);
        }
        if (aIsHeld) {
            pos.translate(-1, 0);
        }
    }

    public void tick() {
        move();

        if (pos.x <40) {
            pos.x = 40;
        } else if (pos.x >= World.getWorld().getMap().WIDTH) {
            pos.x = World.getWorld().getMap().WIDTH - 1;
        }

        if (pos.y <75) {
            pos.y = 75;
        } else if (pos.y >= World.getWorld().getMap().HEIGHT - 4) {
            pos.y = World.getWorld().getMap().HEIGHT - 5;
        }
    }

    public Point getPos() {
        return pos;
    }
}