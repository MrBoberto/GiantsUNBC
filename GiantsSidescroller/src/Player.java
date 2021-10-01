package GiantsSidescroller.src;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Player extends Object implements Creature {
    // The texture of the player being used in the current frame
    private ImageFrame currentImage;
    // The integer representation of the player's current position
    private Point pos;
    private double velX = 0;
    private double velY = 0;
    private Rectangle boundRect;

    // Preset velocities of player actions
    public final double velJump = -15.5;
    public final double velSneak = 3;
    public final double velJog = 7;
    public final double velDash = 25;
    public final double velSuperDash = 40;

    // Determines what the player did last frame to help determine what animation to play.
    private int lastAction = 1;
    public final int LANDINGTIMERMAX = 20;
    public final int SUPERDASHTIMERMAX = 50;
    private int landingTimer = LANDINGTIMERMAX;
    private int superDashTimer = SUPERDASHTIMERMAX;
    private int dashTimer = 0;
    private int jumpTimer = 0;

    // Animation strips for player. Action number is next to each.
    private ImageStrip rightStanding;   // 1
    private ImageStrip rightJogging;    // 2
    private ImageStrip rightCrouching;  // 3
    private ImageStrip rightSneaking;   // 4
    private ImageStrip rightJumping;    // 5
    private ImageStrip rightJumped;     // 5 Animation loop at end of jump
    private ImageStrip rightDashing;    // 6
    private ImageStrip rightLanding;    // 7

    private ImageStrip leftStanding;    // -1
    private ImageStrip leftJogging;     // -2
    private ImageStrip leftCrouching;   // -3
    private ImageStrip leftSneaking;    // -4
    private ImageStrip leftJumping;     // -5
    private ImageStrip leftJumped;      // -5 Animation loop at end of jump
    private ImageStrip leftDashing;     // -6
    private ImageStrip leftLanding;     // -7
    
    // Coins in the player's inventory
    private int pocketMoney = 0;
    private boolean upIsHeld = false;
    private boolean rightIsHeld = false;
    private boolean downIsHeld = false;
    private boolean leftIsHeld = false;
    private boolean zIsHeld = false;
    private boolean cIsHeld = false;
    private boolean sIsHeld = false;
    private boolean facingRight = true;
    private boolean isFalling = true;
    private boolean isJumping = false;
    private boolean isSneaking = false;
    // Prevents dash from being held
    private boolean canDash = true;

    public Player(double x, double y) {
        super(x, y);

        loadImageStrips();
        currentImage = rightStanding.getHead();

        pos = new Point((int) super.getX(), (int) super.getY());
        pocketMoney = 0;
        boundRect = new Rectangle(pos.x - 40, pos.y - 95, 80, 95);
    }

    private void loadImage() {
        if (facingRight && dashTimer > 0) {
            if (lastAction == 6) {
                currentImage = rightDashing.getNext(currentImage);
            } else {
                currentImage = rightDashing.getHead();
            }
            lastAction = 6;
            // Don't continue jump animation even if in midair
            jumpTimer = 0;
            dashTimer--;
        } else if (!facingRight && dashTimer > 0) {
            if (lastAction == -6) {
                currentImage = leftDashing.getNext(currentImage);
            } else {
                currentImage = leftDashing.getHead();
            }
            lastAction = -6;
            // Don't continue jump animation even if in midair
            jumpTimer = 0;
            dashTimer--;
        } else if (velY != 0 && facingRight && (jumpTimer > 0 || lastAction == 5 || lastAction == -5)) {
            if (jumpTimer == rightJumping.getLength()) {
                currentImage = rightJumping.getHead();
                jumpTimer--;
            } else if (lastAction == 5 && jumpTimer > 0) {
                currentImage = rightJumping.getNext(currentImage);
                jumpTimer--;
            } else {
                currentImage = rightJumped.getHead();
            }
            lastAction = 5;
        } else if (velY != 0 && !facingRight && (jumpTimer > 0 || lastAction == -5 || lastAction == 5)) {
            if (jumpTimer == leftJumping.getLength()) {
                currentImage = leftJumping.getHead();
                jumpTimer--;
            } else if (lastAction == -5 && jumpTimer > 0) {
                currentImage = leftJumping.getNext(currentImage);
                jumpTimer--;
            } else {
                currentImage = leftJumped.getHead();
            }
            lastAction = -5;
        } else if(facingRight && !rightIsHeld) {
            if (lastAction == 1) {
                currentImage = rightStanding.getNext(currentImage);
            } else {
                currentImage = rightStanding.getHead();
            }
            lastAction = 1;
        } else if(!facingRight && !leftIsHeld) {
            if (lastAction == -1) {
                currentImage = leftStanding.getNext(currentImage);
            } else {
                currentImage = leftStanding.getHead();
            }
            lastAction = -1;
        } else if(facingRight) {
            if (lastAction == 2) {
                currentImage = rightJogging.getNext(currentImage);
            } else {
                currentImage = rightJogging.getHead();
            }
            lastAction = 2;
        } else if(!facingRight) {
            if (lastAction == -2) {
                currentImage = leftJogging.getNext(currentImage);
            } else {
                currentImage = leftJogging.getHead();
            }
            lastAction = -2;
        }
    }

    public void draw(Graphics g, ImageObserver imgObs) {
        loadImage();
        g.drawImage(
                currentImage.getImage(),
                pos.x- 50,
                pos.y - 96,
                imgObs
        );
        g.setColor(new Color(0, 100, 0));
        g.drawRect(pos.x - 40, pos.y - 95, 80, 95);
    }

    public void keyPressed(KeyEvent e) {
        // Determine key code so it can be compared to a key recognized by humans
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_UP) {
            upIsHeld = true;
        }
        if (key == KeyEvent.VK_RIGHT) {
            rightIsHeld = true;
            if (!sIsHeld) {
                facingRight = true;
            }
        }
        if (key == KeyEvent.VK_DOWN) {
            downIsHeld = true;
            isSneaking = true;
        }
        if (key == KeyEvent.VK_LEFT) {
            leftIsHeld = true;
            if (!sIsHeld) {
                facingRight = false;
            }
        }
        if (key == KeyEvent.VK_Z) {
            zIsHeld = true;
            isJumping = true;
        }
        if (key == KeyEvent.VK_C) {
            cIsHeld = true;
        }
        if (key == KeyEvent.VK_S) {
            sIsHeld = true;
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
            isSneaking = false;
        }
        if (key == KeyEvent.VK_LEFT) {
            leftIsHeld = false;
        }
        if (key == KeyEvent.VK_Z) {
            zIsHeld = false;
        }
        if (key == KeyEvent.VK_C) {
            cIsHeld = false;
            canDash = true;
        }
        if (key == KeyEvent.VK_S) {
            sIsHeld = false;
            superDashTimer = SUPERDASHTIMERMAX;
        }
    }

    public void move() {
        // Determine velocities
        if (zIsHeld && velY == 0 && !sIsHeld) {
            velY = velJump;
            jumpTimer = rightJumping.getLength();
        }

        if (sIsHeld && facingRight) {
            if (superDashTimer <= 0) {
                velX = velSuperDash;
                velY = 0;
            } else {
                isJumping = false;
                superDashTimer -= 1;
                if (rightIsHeld) {
                    velX = velSneak / 2;
                } else {
                    velX = 0;
                }
            }
        } else if (sIsHeld && !facingRight) {
            if (superDashTimer <= 0) {
                velX = -velSuperDash;
                velY = 0;
            } else {
                isJumping = false;
                superDashTimer -= 1;
                if (leftIsHeld) {
                    velX = -velSneak;
                } else {
                    velX = 0;
                }
            }
        } else if (cIsHeld && facingRight && canDash && velX <= velJog) {
            velX = velDash;
            dashTimer = rightDashing.getLength();
        } else if (cIsHeld && !facingRight && canDash && velX >= -velJog) {
            velX = -velDash;
            dashTimer = leftDashing.getLength();
        } else if (rightIsHeld && downIsHeld) {
            velX = velSneak;
        } else if (rightIsHeld && !downIsHeld && velX < velJog) {
            velX = velJog;
        } else if (leftIsHeld && downIsHeld) {
            velX = -velSneak;
        } else if (leftIsHeld && !downIsHeld && velX > -velJog) {
            velX = -velJog;
        } else if (downIsHeld) {
            velX = 0;
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
            velY += World.getWorld().getController().GRAVITY;
        }
        // Apply World.getWorld().getController().FRICTION
        if (velX > World.getWorld().getController().FRICTION) {
            velX -= World.getWorld().getController().FRICTION;
        } else if (velX < -World.getWorld().getController().FRICTION) {
            velX += World.getWorld().getController().FRICTION;
        } else if (velX != 0) {
            velX = 0;
        }

        if (super.getX() <= 50) {
            super.setX(50);
        } else if (super.getX() >= World.getWorld().getController().WIDTH - 50) {
            super.setX(World.getWorld().getController().WIDTH - 50);
        }

        if (super.getY() <= 96) {
            super.setY(96);
            velY = 1;
        } else if (super.getY() >= World.getWorld().getController().HEIGHT) {
            super.setY(World.getWorld().getController().HEIGHT);
            velY = 0;
            isFalling = false;
        }
        boundRect = new Rectangle(pos.x - 40, pos.y - 95, 80, 95);
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

    public Rectangle getBounds() {
        return new Rectangle();
    }

    public void loadImageStrips() {
        ArrayList<String> imgLocStr = new ArrayList<String>();
        ArrayList<BufferedImage> images = new ArrayList<BufferedImage>();

        // Saves amount of text to be used
        String defR = "GiantsSidescroller/src/images/player1/right_";
        String defL = "GiantsSidescroller/src/images/player1/left_";

        // Builds image strip for standing facing right
        for (int i = 1; i <= 4; i++) {
            imgLocStr.add("stand (" + i + ").png");
        }
        rightStanding = buildImageList(imgLocStr, images, defR);
        System.out.println(rightStanding.toString());
        imgLocStr.clear();
        images.clear();

        // Builds image strip for jogging facing right
        for (int i = 1; i <= 20; i++) {
            imgLocStr.add("jog (" + i + ").png");
        }
        rightJogging = buildImageList(imgLocStr, images, defR);
        System.out.println(rightJogging.toString());
        imgLocStr.clear();
        images.clear();

        // Builds image strip for dashing facing right
        for (int i = 1; i <= 8; i++) {
            imgLocStr.add("dash (" + i + ").png");
        }
        rightDashing = buildImageList(imgLocStr, images, defR);
        System.out.println(rightDashing.toString());
        imgLocStr.clear();
        images.clear();

        // Builds image strip for jumping facing right
        for (int i = 1; i <= 12; i++) {
            imgLocStr.add("jump (" + i + ").png");
        }
        rightJumping = buildImageList(imgLocStr, images, defR);
        System.out.println(rightJumping.toString());
        imgLocStr.clear();
        images.clear();

        // Builds image strip for jump loop facing right
        for (int i = 1; i <= 1; i++) {
            imgLocStr.add("jumped (" + i + ").png");
        }
        rightJumped = buildImageList(imgLocStr, images, defR);
        System.out.println(rightJumped.toString());
        imgLocStr.clear();
        images.clear();

        // Builds image strip for standing facing left
        for (int i = 1; i <= 4; i++) {
            imgLocStr.add("stand (" + i + ").png");
        }
        leftStanding = buildImageList(imgLocStr, images, defL);
        System.out.println(leftStanding.toString());
        imgLocStr.clear();
        images.clear();

        // Builds image strip for jogging facing left
        for (int i = 1; i <= 20; i++) {
            imgLocStr.add("jog (" + i + ").png");
        }
        leftJogging = buildImageList(imgLocStr, images, defL);
        System.out.println(leftJogging.toString());
        imgLocStr.clear();
        images.clear();

        // Builds image strip for dashing facing left
        for (int i = 1; i <= 8; i++) {
            imgLocStr.add("dash (" + i + ").png");
        }
        leftDashing = buildImageList(imgLocStr, images, defL);
        System.out.println(leftDashing.toString());
        imgLocStr.clear();
        images.clear();

        // Builds image strip for jumping facing left
        for (int i = 1; i <= 12; i++) {
            imgLocStr.add("jump (" + i + ").png");
        }
        leftJumping = buildImageList(imgLocStr, images, defL);
        System.out.println(leftJumping.toString());
        imgLocStr.clear();
        images.clear();

        // Builds image strip for jump loop facing left
        for (int i = 1; i <= 1; i++) {
            imgLocStr.add("jumped (" + i + ").png");
        }
        leftJumped = buildImageList(imgLocStr, images, defL);
        System.out.println(leftJumped.toString());
        imgLocStr.clear();
        images.clear();
    }

    public ImageStrip buildImageList(ArrayList<String> imgLocStr, ArrayList<BufferedImage> images,
                                     String defaultFileLocation) {
        String imageFileNames = "";
        String imageFileSubstring = "";
        for (int i = 0; i < imgLocStr.size(); i++) {
            try {
                images.add(ImageIO.read(new File(defaultFileLocation + "" + imgLocStr.get(i))));
            } catch (IOException exc) {
                System.out.println("Could not find image file: " + exc.getMessage());
            }
            imageFileNames += defaultFileLocation + imgLocStr.get(i) + ", ";
        }
        for (int i = 0; i < imageFileNames.length() - 2; i++) {
            imageFileSubstring += imageFileNames.charAt(i);
        }
        return new ImageStrip(images, imageFileSubstring);
    }
}