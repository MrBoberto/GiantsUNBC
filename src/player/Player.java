package player;

import animation.ImageFrame;
import animation.ImageStrip;
import game.Thing;
import game.World;
import weapons.guns.FlameThrower;
import weapons.guns.Shotgun;
import weapons.guns.SniperRifle;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Player extends Thing implements Creature {
    // The texture of the player being used in the current frame
    private ImageFrame currentImage;
    // The integer representation of the player's current position
    private Point pos;
    private double velX = 0;
    private double velY = 0;
    private Rectangle boundRect;

    // Preset velocities of player actions
    public final double VELJUMP = -12;
    public final double VELSNEAK = 2.2;
    public final double VELJOG = 5.2;
    public final double VELDASH = 19;
    public final double VELSUPERDASH = 30;

    // Determines what the player did last frame to help determine what animation to play.
    private int lastAction = 1;
    public final int LANDINGTIMERMAX = 27;
    public final int SUPERDASHTIMERMAX = 67;
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
    private boolean button1Held = false;
    private boolean isFalling = true;
    private boolean isJumping = false;
    private boolean isSneaking = false;

    // Prevents dash from being held
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
        boundRect = new Rectangle(pos.x - currentImage.getImage().getWidth() / 2,
                pos.y - currentImage.getImage().getHeight() / 2, currentImage.getImage().getWidth(),
                currentImage.getImage().getHeight());

        weapons.add(new Shotgun(this));
        weapons.add(new FlameThrower(this));
        weapons.add(new SniperRifle(this));
    }

    /**
     * Determines which animation is being played and which frame should currently be played
     */
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

    /**
     * Gets the current frame from loadImage() and rotates it based on the player's angle
     * @param g
     * @param imgObs
     */
    public void draw(Graphics g, ImageObserver imgObs) {
        loadImage();

        // Sets up the axis of rotation
        AffineTransform affTra = AffineTransform.getTranslateInstance(
                pos.x - currentImage.getImage().getWidth() / 2,
                pos.y - currentImage.getImage().getHeight() / 2);
        // Rotates the frame
        affTra.rotate(super.getAngle(), currentImage.getImage().getWidth() / 2,
                currentImage.getImage().getHeight() / 2);
        Graphics2D g2d = (Graphics2D) g;

        // Draws the rotated image
        g2d.drawImage(currentImage.getImage(), affTra, imgObs);

        // Draws the player's hitbox
        g.setColor(new Color(0, 0, 200));
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
            // Will eventually be removed
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
            // Will eventually be removed
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
                button1Held = true;
            }
        }
    }

    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            button1Held = false;
        }
    }

    public void mouseClicked(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON3) {
            if (mouseInside) {
                // Switch between primary and secondary
                if (selectedWeapon < 1) {
                    if (selectedWeapon == 0 && weapons.getSecondary() != null) {
                        selectedWeapon ++;
                    }
                } else {
                    if (selectedWeapon == 1 && weapons.getSecondary() != null) {
                        selectedWeapon = 0;
                    }
                }
            }
        }
    }

    public void mouseEntered(MouseEvent e) {
        mouseInside = true;
    }

    public void mouseExited(MouseEvent e) {
        mouseInside = false;
    }

    /**
     * Determines the angle in which the player is facing
     */
    public void setAngle() {
        System.out.println("player.Player 1 Prev super.getAngle(): " + Math.toDegrees(super.getAngle()));
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
        System.out.println("player.Player 1 Acute super.getAngle(): " + Math.toDegrees(acuteAngle));

        if (avgY < 0) {
            acuteAngle += Math.PI;
        }

        super.setAngle(acuteAngle);
    }

    /**
     * Determines horizontal and vertical velocities
     * @param speed The net speed at which the player should move
     */
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
            System.out.println("player.Player 1 Current super.getAngle(): " + super.getAngle());
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

    /**
     * Determines what speed to move at based on the controls being used and them translates the player
     * based on the horizontal and vertical velocities
     */
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

    /**
     * Translates the player using move(), applies friction, prevents the player from leaving the window,
     * updates the player's hitbox, and shoots weapons
     * @param mouseLoc
     */
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

        if (weapons.getPrimary().getCurrentDelay() > 0) {
            weapons.getPrimary().setCurrentDelay(weapons.getPrimary().getCurrentDelay() - 1);
        }
        if (weapons.getSecondary().getCurrentDelay() > 0) {
            weapons.getSecondary().setCurrentDelay(weapons.getSecondary().getCurrentDelay() - 1);
        }


        // Shoot at the selected point
        if (button1Held) {
            if (selectedWeapon == 0 && weapons.getPrimary().getCurrentDelay() == 0) {
                weapons.getPrimary().shoot(mouseLoc.x, mouseLoc.y);
                weapons.getPrimary().setCurrentDelay(weapons.getPrimary().getMAX_DELAY());
            } else if (selectedWeapon == 1 && weapons.getPrimary().getCurrentDelay() == 0) {
                weapons.getSecondary().shoot(mouseLoc.x, mouseLoc.y);
                weapons.getSecondary().setCurrentDelay(weapons.getSecondary().getMAX_DELAY());
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

    /**
     * Loads the image files into the image strips based upon their names
     */
    public void loadImageStrips() {
        ArrayList<String> imgLocStr = new ArrayList<String>();

       // Saves amount of text to be used
        String defLocStr = "resources/Textures/PLAYER_ONE/";

        // Builds image strip for standing facing right
        for (int i = 1; i <= 4; i++) {
            imgLocStr.add("stand (" + i + ").png");
        }
        standing = buildImageStrip(imgLocStr, defLocStr);
        System.out.println(standing.toString());
        imgLocStr.clear();

        // Builds image strip for jogging
        for (int i = 1; i <= 20; i++) {
            imgLocStr.add("jog (" + i + ").png");
        }
        jogging = buildImageStrip(imgLocStr, defLocStr);
        System.out.println(jogging.toString());
        imgLocStr.clear();

        // Builds image strip for dashing
        for (int i = 1; i <= 8; i++) {
            imgLocStr.add("dash (" + i + ").png");
        }
        dashing = buildImageStrip(imgLocStr, defLocStr);
        System.out.println(dashing.toString());
        imgLocStr.clear();

        // Builds image strip for jumping
        for (int i = 1; i <= 8; i++) {
            imgLocStr.add("jump (" + i + ").png");
        }
        jumping = buildImageStrip(imgLocStr, defLocStr);
        System.out.println(jumping.toString());
        imgLocStr.clear();
    }

    /**
     * Builds the animation.ImageStrip for a specific animation
     * @param imgLocStr All file names to be loaded into the animation.ImageStrip for animation
     * @param defaultFileLocation The file path of the images
     * @return An animation.ImageStrip for animation
     */
    public ImageStrip buildImageStrip(ArrayList<String> imgLocStr, String defaultFileLocation) {
        // The ArrayList of image files to be put into the animation.ImageStrip
        ArrayList<BufferedImage> images = new ArrayList<>();
        // Used to track images that are loaded
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
        // Used for the toString() method of this animation.ImageStrip
        for (int i = 0; i < imageFileNames.length() - 2; i++) {
            imageFileSubstring += imageFileNames.charAt(i);
        }
        return new ImageStrip(images, imageFileSubstring);
    }
}