package GiantsSidescroller.src;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
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
    public final double VELJUMP = -15.5;
    public final double VELSNEAK = 3;
    public final double VELJOG = 7;
    public final double VELDASH = 25;
    public final double VELSUPERDASH = 40;

    // Determines what the player did last frame to help determine what animation to play.
    private int lastAction = 1;
    public final int LANDINGTIMERMAX = 20;
    public final int SUPERDASHTIMERMAX = 50;
    private int landingTimer = LANDINGTIMERMAX;
    private int superDashTimer = SUPERDASHTIMERMAX;
    private int dashTimer = 0;
    private int jumpTimer = 0;

    // Animation strips for player. Action number is next to each.
    private ImageStrip standing;   // 1
    private ImageStrip jogging;    // 2
    private ImageStrip crouching;  // 3
    private ImageStrip sneaking;   // 4
    private ImageStrip jumping;    // 5
    private ImageStrip jumped;     // 5 Animation loop at end of jump
    private ImageStrip dashing;    // 6
    private ImageStrip landing;    // 7
    
    // Coins in the player's inventory
    private int pocketMoney = 0;
    private boolean wIsHeld = false;
    private boolean dIsHeld = false;
    private boolean sIsHeld = false;
    private boolean aIsHeld = false;
    private boolean shiftIsHeld = false;
    private boolean spaceIsHeld = false;
    private boolean ctrlIsHeld = false;
    private boolean tIsHeld = false;
    private boolean mouseInside = true;
    private boolean mouseHeld = false;
    private boolean isFalling = true;
    private boolean isJumping = false;
    private boolean isSneaking = false;

    // Prevents dash from being held
    private boolean canDash = true;
    private Arsenal weapons = new Arsenal();
    // Can be 0 = primary or 1 = secondary
    private int selectedWeapon = 0;

    public Player(double x, double y, double angle) {
        super(x, y, angle);

        loadImageStrips();
        currentImage = standing.getHead();

        pos = new Point((int) super.getX(), (int) super.getY());
        pocketMoney = 0;
        boundRect = new Rectangle(pos.x - 60, pos.y - 60, 120, 120);

        weapons.add(new Shotgun(this));
    }

    private void loadImage() {
        if (dashTimer > 0) {
            if (lastAction == 6) {
                currentImage = dashing.getNext(currentImage);
            } else {
                currentImage = dashing.getHead();
            }
            lastAction = 6;
            // Don't continue jump animation even if in midair
            jumpTimer = 0;
            dashTimer--;
        } else if (velY != 0 && (jumpTimer > 0 || lastAction == 5 || lastAction == -5)) {
            if (jumpTimer == jumping.getLength()) {
                currentImage = jumping.getHead();
                jumpTimer--;
            } else if (lastAction == 5 && jumpTimer > 0) {
                currentImage = jumping.getNext(currentImage);
                jumpTimer--;
            } else {
                currentImage = jumped.getHead();
            }
            lastAction = 5;
        } else if(wIsHeld || dIsHeld || sIsHeld || aIsHeld) {
            if (lastAction == 2) {
                currentImage = jogging.getNext(currentImage);
            } else {
                currentImage = jogging.getHead();
            }
            lastAction = 2;
        } else {
            if (lastAction == 1) {
                currentImage = standing.getNext(currentImage);
            } else {
                currentImage = standing.getHead();
            }
            lastAction = 1;
        }
    }

    public void draw(Graphics g, ImageObserver imgObs) {
        loadImage();

        AffineTransform affTra = AffineTransform.getTranslateInstance(
                pos.x - currentImage.getImage().getWidth() / 2, pos.y - currentImage.getImage().getHeight() / 2);
        affTra.rotate(super.getAngle(), currentImage.getImage().getWidth() / 2,
                currentImage.getImage().getHeight() / 2);
        Graphics2D g2d = (Graphics2D) g;

        g2d.drawImage(currentImage.getImage(), affTra, imgObs);

        /*
        g.drawImage(
                currentImage.getImage(),
                pos.x- 60,
                pos.y - 60,
                imgObs
        );
        */

        g.setColor(new Color(0, 100, 0));
        g.drawRect(pos.x - currentImage.getImage().getWidth() / 2,
                pos.y - currentImage.getImage().getHeight() / 2, currentImage.getImage().getWidth(),
                currentImage.getImage().getHeight());
    }

    public void keyPressed(KeyEvent e) {
        // Determine key code so it can be compared to a key recognized by humans
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_W) {
            wIsHeld = true;
        }
        if (key == KeyEvent.VK_D) {
            dIsHeld = true;
        }
        if (key == KeyEvent.VK_S) {
            sIsHeld = true;
        }
        if (key == KeyEvent.VK_A) {
            aIsHeld = true;
        }
        if (key == KeyEvent.VK_SHIFT) {
            shiftIsHeld = true;
            isSneaking = true;
        }
        if (key == KeyEvent.VK_SPACE) {
            spaceIsHeld = true;
            isJumping = true;
        }
        if (key == KeyEvent.VK_CONTROL) {
            ctrlIsHeld = true;
        }
        if (key == KeyEvent.VK_T) {
            tIsHeld = true;
        }

        setAngle();
    }

    public void keyReleased(KeyEvent e) {
        // Determine key code so that it can be compared to a key recognized by humans
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
        if (key == KeyEvent.VK_SHIFT) {
            shiftIsHeld = false;
            isSneaking = false;
        }
        if (key == KeyEvent.VK_SPACE) {
            spaceIsHeld = false;
        }
        if (key == KeyEvent.VK_CONTROL) {
            ctrlIsHeld = false;
            canDash = true;
        }
        if (key == KeyEvent.VK_T) {
            tIsHeld = false;
            superDashTimer = SUPERDASHTIMERMAX;
        }

        setAngle();
    }

    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            if (mouseInside) {
                mouseHeld = true;
            }
        }
    }

    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            mouseHeld = false;
        }
    }

    public void mouseEntered(MouseEvent e) {
        mouseInside = true;
    }

    public void mouseExited(MouseEvent e) {
        mouseInside = false;
    }

    public void setAngle() {
        System.out.println("Player 1 Prev super.getAngle(): " + Math.toDegrees(super.getAngle()));
        int avgX = 0;
        int avgY = 0;

        if (wIsHeld) {
            avgY++;
        }
        if (dIsHeld) {
            avgX++;
        }
        if (sIsHeld) {
            avgY--;
        }
        if (aIsHeld) {
            avgX--;
        }
        if (avgX == 0 && avgY == 0) {
            return;
        } else if (avgX == 0) {
            if (avgY == 1) {
                super.setAngle(0);
                return;
            } else {
                super.setAngle(Math.PI);
                return;
            }
        } else if (avgY == 0) {
            if (avgX == 1) {
                super.setAngle(Math.PI / 2);
                return;
            } else {
                super.setAngle(-Math.PI / 2);
                return;
            }
        }

        double acuteAngle = Math.atan(avgY/avgX);
        System.out.println("Player 1 Acute super.getAngle(): " + Math.toDegrees(acuteAngle));

        if (avgY < 0) {
            acuteAngle += Math.PI;
        }

        super.setAngle(acuteAngle);
    }

    public void setVelocity(double speed) {
        if (super.getAngle() == 0) {
            velY = -speed;
            velX = 0;
        } else if (super.getAngle() == Math.PI / 2) {
            velX = speed;
            velY = 0;
        } else if (super.getAngle() == Math.PI) {
            velY = speed;
            velX = 0;
        } else if (super.getAngle() == -Math.PI / 2) {
            velX = -speed;
            velY = 0;
        } else {
            velX = speed * Math.cos(super.getAngle());
            velY = -speed * Math.sin(super.getAngle());
            System.out.println("Player 1 Current super.getAngle(): " + super.getAngle());
            if ((super.getAngle() > (Math.PI / 2) && super.getAngle() < Math.PI)
                    || (super.getAngle() < 0 && super.getAngle() > -Math.PI / 2)) {
                velX *= -1;
                velY *= -1;
            }
        }
    }

    public double getVelocity() {
        return Math.sqrt(Math.pow(velX, 2) + Math.pow(velY, 2)) * Math.cos(super.getAngle());
    }

    public void move() {
        // Determine velocities
        if (tIsHeld) {
            if (superDashTimer <= 0) {
                setVelocity(VELSUPERDASH);
            } else {
                isJumping = false;
                superDashTimer -= 1;
                if (wIsHeld || dIsHeld || sIsHeld || aIsHeld) {
                    setVelocity(VELSNEAK / 2);
                } else {
                    velX = 0;
                }
            }
        } else if (ctrlIsHeld && canDash && getVelocity() <= VELJOG) {
            setVelocity(VELDASH);
            dashTimer = dashing.getLength();
        } else if (shiftIsHeld && (wIsHeld || dIsHeld || sIsHeld || aIsHeld)) {
            setVelocity(VELSNEAK);
        } else if ((wIsHeld || dIsHeld || sIsHeld || aIsHeld) && !shiftIsHeld && getVelocity() < VELJOG) {
            setVelocity(VELJOG);
        } else if (shiftIsHeld) {
            velX = 0;
        }

        // Determine distance travelled
        super.setX(super.getX() + velX);
        super.setY(super.getY() + velY);
        pos.setLocation(super.getX(), super.getY());
    }

    public void tick(Point mouseLoc) {
        move();

        // Apply vertical friction
        if (velY > World.getWorld().getController().FRICTION) {
            velY -= World.getWorld().getController().FRICTION;
        } else if (velY < -World.getWorld().getController().FRICTION) {
            velY += World.getWorld().getController().FRICTION;
        } else if (velY != 0) {
            velY = 0;
        }
        // Apply horizontal friction
        if (velX > World.getWorld().getController().FRICTION) {
            velX -= World.getWorld().getController().FRICTION;
        } else if (velX < -World.getWorld().getController().FRICTION) {
            velX += World.getWorld().getController().FRICTION;
        } else if (velX != 0) {
            velX = 0;
        }

        if (super.getX() <= currentImage.getImage().getWidth() / 2) {
            super.setX(currentImage.getImage().getWidth() / 2);
        } else if (super.getX() >= World.getWorld().getController().WIDTH - currentImage.getImage().getWidth() / 2) {
            super.setX(World.getWorld().getController().WIDTH - currentImage.getImage().getWidth() / 2);
        }

        if (super.getY() <= currentImage.getImage().getHeight() / 2) {
            super.setY(currentImage.getImage().getHeight() / 2);
            velY = 1;
        } else if (super.getY() >= World.getWorld().getController().HEIGHT - currentImage.getImage().getHeight() / 2) {
            super.setY(World.getWorld().getController().HEIGHT - currentImage.getImage().getHeight() / 2);
            velY = 0;
            isFalling = false;
        }
        boundRect = new Rectangle(pos.x - currentImage.getImage().getWidth() / 2,
                pos.y - currentImage.getImage().getHeight() / 2, currentImage.getImage().getWidth(),
                currentImage.getImage().getHeight());

        if (mouseHeld) {
            if (selectedWeapon == 0) {
                weapons.getPrimary().shoot(mouseLoc.x, mouseLoc.y);
            }
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

    public Rectangle getBounds() {
        return boundRect;
    }

    public void loadImageStrips() {
        ArrayList<String> imgLocStr = new ArrayList<String>();
        ArrayList<BufferedImage> images = new ArrayList<BufferedImage>();

        // Saves amount of text to be used
        String defLocStr = "GiantsSidescroller/src/images/player1/";

        // Builds image strip for standing facing right
        for (int i = 1; i <= 4; i++) {
            imgLocStr.add("stand (" + i + ").png");
        }
        standing = buildImageList(imgLocStr, images, defLocStr);
        System.out.println(standing.toString());
        imgLocStr.clear();
        images.clear();

        // Builds image strip for jogging
        for (int i = 1; i <= 20; i++) {
            imgLocStr.add("jog (" + i + ").png");
        }
        jogging = buildImageList(imgLocStr, images, defLocStr);
        System.out.println(jogging.toString());
        imgLocStr.clear();
        images.clear();

        // Builds image strip for dashing
        for (int i = 1; i <= 8; i++) {
            imgLocStr.add("dash (" + i + ").png");
        }
        dashing = buildImageList(imgLocStr, images, defLocStr);
        System.out.println(dashing.toString());
        imgLocStr.clear();
        images.clear();

        // Builds image strip for jumping
        for (int i = 1; i <= 8; i++) {
            imgLocStr.add("jump (" + i + ").png");
        }
        jumping = buildImageList(imgLocStr, images, defLocStr);
        System.out.println(jumping.toString());
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