package GiantsSidescroller.src;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;

public class Player extends Object {
    private BufferedImage currentImage;
    private Point pos;
    private double velX = 0;
    private double velY = 0;

    // Preset velocities of player actions
    public final int velJump = -5;
    public final int velSneak = 2;
    public final int velJog = 3;
    public final int velDash = 10;

    // Coins in the player's inventory
    private int pocketMoney;
    private boolean upIsHeld = false;
    private boolean rightIsHeld = false;
    private boolean downIsHeld = false;
    private boolean leftIsHeld = false;
    private boolean zIsHeld = false;
    private boolean facingRight = true;
    private double gravity = 0.5;
    private double friction = 0.5;
    private boolean isFalling = true;
    private boolean isJumping = false;
    private boolean isSneaking = false;

    public Player(double x, double y) {
        super(x, y);

        loadImage();

        pos = new Point((int) super.getX(), (int) super.getY());
        pocketMoney = 0;
    }

    private void loadImage() {
        if(facingRight && !rightIsHeld) {
            try {
                currentImage = ImageIO.read(new File("GiantsSidescroller/src/images/player1/right_stand.png"));
            } catch (IOException exc) {
                System.out.println("Could not find image file: " + exc.getMessage());
            }
        } else if(!facingRight && !leftIsHeld) {
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
                pos.x- 40,
                pos.y - 75,
                observer
        );
        g.setColor(new Color(200, 0, 0));
        g.drawRect(pos.x - 10, pos.y - 10, 20, 20);
    }

    public void keyPressed(KeyEvent e) {
        // Determine key code so it can be compared to a key recognized by humans
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_UP) {
            upIsHeld = true;
        }
        if (key == KeyEvent.VK_RIGHT) {
            rightIsHeld = true;
            facingRight = true;
        }
        if (key == KeyEvent.VK_DOWN) {
            downIsHeld = true;
        }
        if (key == KeyEvent.VK_LEFT) {
            leftIsHeld = true;
            facingRight = false;
        }
        if (key == KeyEvent.VK_Z) {
            zIsHeld = true;
            isJumping = true;
        }
    }

    public void keyReleased(KeyEvent e) {
        // Determine key code so that it can be compared to a key recognized by humans
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_UP) {
            upIsHeld = false;
        }
        if (key == KeyEvent.VK_RIGHT) {
            rightIsHeld = false;
        }
        if (key == KeyEvent.VK_DOWN) {
            downIsHeld = false;
        }
        if (key == KeyEvent.VK_LEFT) {
            leftIsHeld = false;
        }
        if (key == KeyEvent.VK_Z) {
            zIsHeld = false;
        }
    }

    public void move() {
        // Determine velocities
        if (zIsHeld) {
            velY = velJump;
        }
        if (rightIsHeld && isSneaking && velX < velSneak) {
            velX = velSneak;
        } else if (rightIsHeld && velX < velJog) {
            velX = velJog;
        }

        if (downIsHeld) {
            isSneaking = true;
        }

        if (leftIsHeld && isSneaking && velX > -velSneak) {
            velX = -velSneak;
        } else if (leftIsHeld && velX > -velJog) {
            velX = -velJog;
        }

        // Determine distance travelled
        super.setX(super.getX() + velX);
        super.setY(super.getY() + velY);
        pos.setLocation(super.getX(), super.getY());
    }

    public void tick() {
        move();

        // Switch from jumping to falling
        if (velY < 0) {
            isFalling = true;
            isJumping = false;
        }
        // Apply gravity
        if(isFalling || isJumping) {
            velY += gravity;
        }
        // Apply friction is the player is not being commanded to move
        if (!rightIsHeld && ! leftIsHeld) {
            if (velX > friction) {
                velX -= friction;
            } else if (velX < -friction) {
                velX += friction;
            } else if (velX != 0) {
                velX = 0;
            }
        }

        if (super.getX() < 40) {
            super.setX(40);
        } else if (super.getX() >= World.getWorld().getMap().WIDTH - 40) {
            super.setX(World.getWorld().getMap().WIDTH - 40);
        }

        if (super.getY() <75) {
            super.setY(75);
            velY = 1;
        } else if (super.getY() >= World.getWorld().getMap().HEIGHT) {
            super.setY(World.getWorld().getMap().HEIGHT);
            velY = 0;
            isFalling = false;
        }
    }

    public Point getPos() {
        return pos;
    }

    public boolean isFalling() {
        return isFalling;
    }

    public void setFalling(boolean falling) {
        isFalling = falling;
    }

    public boolean isJumping() {
        return isJumping;
    }

    public void setJumping(boolean jumping) {
        isJumping = jumping;
    }
}