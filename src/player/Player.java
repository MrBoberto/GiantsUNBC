package player;

import StartMenu.MainMenuTest;
import animation.ImageFrame;
import animation.ImageStrip;
import game.ServerController;
import game.Thing;
import game.World;
import weapons.guns.Shotgun;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

public abstract class Player extends Thing implements Creature {
    // The texture of the player being used in the current frame
    protected ImageFrame currentImage;
    // The integer representation of the player's current position
//    protected Point pos;
    protected double velX = 0;
    protected double velY = 0;
    protected Rectangle boundRect;
    protected double health = 100;
    protected int killCount = 0;
    protected int deathCount = 0;
    protected double kdr = -1;
    protected double tdo = 0;
    protected double damageMultiplier = 1;


    protected int playerNumber;
    protected String playerName;
    protected Color playerColour;

    // Preset velocities of player actions
    protected final double VELJUMP = -12;
    protected final double VELSNEAK = 2.2;
    protected final double VELJOG = 5.2;
    protected final double VELDASH = 19;
    protected final double VELSUPERDASH = 30;

    // Determines what the player did last frame to help determine what animation to play.
    protected int lastAction = 1;
    protected final int LANDINGTIMERMAX = 27;
    protected final int SUPERDASHTIMERMAX = 67;
    protected int landingTimer = LANDINGTIMERMAX;
    protected int superDashTimer = SUPERDASHTIMERMAX;
    protected int dashTimer = 0;
    protected int jumpTimer = 0;

    // Animation strips for player. Action number is next to each.
    protected ImageStrip standing;   // 1
    protected ImageStrip jogging;    // 2
    protected ImageStrip crouching;  // 3
    protected ImageStrip sneaking;   // 4
    protected ImageStrip jumping;    // 5
    protected ImageStrip jumped;     // 5 Animation loop at end of jump
    protected ImageStrip dashing;    // 6
    protected ImageStrip landing;    // 7

    protected boolean wIsHeld = false;
    protected boolean dIsHeld = false;
    protected boolean sIsHeld = false;
    protected boolean aIsHeld = false;
    protected boolean shiftIsHeld = false;
    protected boolean spaceIsHeld = false;
    protected boolean ctrlIsHeld = false;
    protected boolean tIsHeld = false;
    protected boolean mouseInside = true;
    protected boolean button1Held = false;
    protected boolean isFalling = true;
    protected boolean isJumping = false;
    protected boolean isSneaking = false;

    // Prevents dash from being held
    // Prevents dash from being held
    protected boolean canDash = true;
    protected Arsenal weapons = new Arsenal();
    // Can be 0 = primary or 1 = secondary
    protected int selectedWeapon = 0;

    public static final int SERVER_PLAYER = 0, CLIENT_PLAYER = 1;

    public Player(double x, double y, double angle) {
        super(x, y, angle);

        loadImageStrips();
        this.playerNumber = playerNumber;
        if (MainMenuTest.playerName.equals("")) {
            if (playerNumber == 0) {
                playerName = "Host";
            } else {
                playerName = "Guest";
            }
        } else {
            playerName = MainMenuTest.playerName;
        }

        // Graphics-related
        setColour();
        loadImageStrips(playerNumber);
        currentImage = standing.getHead();
       // pos = new Point((int) super.getX(), (int) super.getY());
      //  pocketMoney = 0;
        boundRect = new Rectangle((int)this.x - currentImage.getImage().getWidth() / 2,
                (int)this.y - currentImage.getImage().getHeight() / 2, currentImage.getImage().getWidth(),
                currentImage.getImage().getHeight());

        weapons.add(new Shotgun(this));
        weapons.add(new Shotgun(this));
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public int getKillCount() {
        return killCount;
    }

    /**
     * Increases killCount by 1 and updates kdr if there are more than 0 deaths
     */
    public void incrementKillCount() {
        this.killCount++;
        // kdr stays at -1 as long as there are no deaths so that it is easy to identify
        if (deathCount != 0) {
            kdr = killCount / deathCount;
        } else {
            kdr = -1;
        }
    }

    public int getDeathCount() {
        return deathCount;
    }

    /**
     * Increases deathCount by 1 and updates kdr
     */
    public void incrementDeathCount() {
        this.deathCount++;
        kdr = killCount / deathCount;
    }

    public double getKdr() {
        return kdr;
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
                x - currentImage.getImage().getWidth() / 2,
                y - currentImage.getImage().getHeight() / 2);
        // Rotates the frame
        affTra.rotate(super.getAngle(), currentImage.getImage().getWidth() / 2,
                currentImage.getImage().getHeight() / 2);
        Graphics2D g2d = (Graphics2D) g;

        // Draws the rotated image
        g2d.drawImage(currentImage.getImage(), affTra, imgObs);

        // Draws the player's hitbox
        g.setColor(playerColour);
        g.drawRect((int)x - currentImage.getImage().getWidth() / 2,
                (int)y - currentImage.getImage().getHeight() / 2, currentImage.getImage().getWidth(),
                currentImage.getImage().getHeight());

        Font font = new Font("Arial", Font.BOLD, 20);
        FontMetrics stringSize = g2d.getFontMetrics(font);
        g2d.drawString(playerName, pos.x - (stringSize.stringWidth(playerName)) / 2,
                pos.y - currentImage.getImage().getHeight() / 2);
    }

//    public Point getPos() {
//        return pos;
//    }

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

    public double getHealth() {
        return health;
    }

    public void modifyHealth(double healthMod) {
        this.health += healthMod;
        if (health < 0) {
            health = 0;
        }
    }

    /**
     * Increase total damage output of the player
     * @param tdoMod Damage value to add
     */
    public void addTDO(double tdoMod) {
        if (this.tdo > 1.6*(10^308) || this.tdo < -1.6*(10^308)) {
            System.out.println("ERROR: TDO overflow");
        } else {
            this.tdo += tdoMod;
        }
    }

    public double getDamageMultiplier() {
        return damageMultiplier;
    }

    public void setDamageMultiplier(int damageMultiplier) {
        this.damageMultiplier = damageMultiplier;
    }

    public void setColour() {
        if (playerNumber == 0) {
            playerColour = new Color(0, 0, 200);
        } else {
            playerColour = new Color(200, 0, 0);
        }
    }

    /**
     * Loads the image files into the image strips based upon their names
     */
    public void loadImageStrips() {
        ArrayList<String> imgLocStr = new ArrayList<>();
        String defLocStr = "resources/Textures/PLAYER_ONE/";;
       // Saves amount of text to be used
        if(World.controller instanceof ServerController){
            if (this instanceof MainPlayer) {
                defLocStr = "resources/Textures/PLAYER_ONE/";
            } else if (this instanceof OtherPlayer) {
                defLocStr = "resources/Textures/PLAYER_TWO/";
            }
        } else {
            if (this instanceof OtherPlayer) {
                defLocStr = "resources/Textures/PLAYER_ONE/";
            } else if (this instanceof MainPlayer) {
                defLocStr = "resources/Textures/PLAYER_TWO/";
            }
        }



        // Builds image strip for standing facing right
        for (int i = 1; i <= 4; i++) {
            imgLocStr.add("stand (" + i + ").png");
        }
        standing = buildImageStrip(imgLocStr, defLocStr);
//        System.out.println(standing.toString());
        imgLocStr.clear();

        // Builds image strip for jogging
        for (int i = 1; i <= 20; i++) {
            imgLocStr.add("jog (" + i + ").png");
        }
        jogging = buildImageStrip(imgLocStr, defLocStr);
//        System.out.println(jogging.toString());
        imgLocStr.clear();

        // Builds image strip for dashing
        for (int i = 1; i <= 8; i++) {
            imgLocStr.add("dash (" + i + ").png");
        }
        dashing = buildImageStrip(imgLocStr, defLocStr);
//        System.out.println(dashing.toString());
        imgLocStr.clear();

        // Builds image strip for jumping
        for (int i = 1; i <= 8; i++) {
            imgLocStr.add("jump (" + i + ").png");
        }
        jumping = buildImageStrip(imgLocStr, defLocStr);
//        System.out.println(jumping.toString());
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