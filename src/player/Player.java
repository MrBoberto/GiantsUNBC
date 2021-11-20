package player;

import StartMenu.MainMenuTest;
import animation.ImageFrame;
import animation.ImageStrip;
import game.*;
import weapons.guns.AssaultRifle;
import weapons.guns.Pistol;
import weapons.guns.Shotgun;
import weapons.guns.SniperRifle;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


public abstract class Player extends GameObject {
    // Can be 0 = primary or 1 = secondary
    public static final int PRIMARY_WEAPON = 0, SECONDARY_WEAPON = 1;
    // The integer representation of the player's current position
//    protected Point pos;
    public static final int SERVER_PLAYER = 0, CLIENT_PLAYER = 1;
    // Preset velocities of player actions
    protected final double VELJUMP = -12;
    protected final double VELSNEAK = 2.2;
    protected final double VELJOG = 5.2;
    protected final double VELDASH = 19;
    protected final double VELSUPERDASH = 30;
    protected final int LANDINGTIMERMAX = 27;
    protected final int SUPERDASHTIMERMAX = 67;
    public MainMenuTest mainMenuTest;
    // The texture of the player being used in the current frame
    protected ImageFrame currentImage;
    protected Rectangle boundRect;
    protected int health = 100;
    protected int killCount = 0;
    protected int deathCount = 0;
    protected double kdr = -1;
    protected long tdo = 0;
    protected double damageMultiplier = 1;
    protected int playerNumber;
    protected String playerName;
    protected Color playerColour;
    // Determines what the player did last frame to help determine what animation to play.
    protected int lastAction = 1;
    protected int landingTimer = LANDINGTIMERMAX;
    protected int superDashTimer = SUPERDASHTIMERMAX;
    protected int dashTimer = 0;
    protected int jumpTimer = 0;
    // Animation strips for player. Action number is next to each.
    protected boolean imageLoaded = false;
    protected ImageStrip standing;   // 1
    protected ImageStrip jogging;    // 2
    protected ImageStrip crouching;  // 3
    protected ImageStrip sneaking;   // 4
    protected ImageStrip jumping;    // 5
    protected ImageStrip jumped;     // 5 Animation loop at end of jump
    protected ImageStrip dashing;    // 6
    protected ImageStrip landing;    // 7
    protected boolean shiftIsHeld = false;
    protected boolean spaceIsHeld = false;
    protected boolean ctrlIsHeld = false;
    protected boolean tIsHeld = false;
    protected boolean mouseInside = true;
    protected boolean button1Held = false;
    protected boolean isFalling = true;
    protected boolean isJumping = false;
    protected boolean isSneaking = false;
    protected boolean isWalking = false;
    // Prevents dash from being held
    protected boolean canDash = true;

    protected Arsenal weapons = new Arsenal();
    protected int selectedWeapon = 0;

    //Respawn point
    protected double respawnPointX = 0;
    protected double respawnPointY = 0;

    //Stats
    protected long bulletsShot = 0;
    protected long bulletsHit = 0;
    protected long walkingDistance = 0;

    //Collisions
    public Rectangle solidArea;
    public boolean collisionOn =false;

    //Invincibility
    protected int invincibilityTimer = 0;
    public static final int RESPAWN_INVINCIBILITY_TIME = 180;

    //Animation timers
    protected int animationTimer = 0;
    public static final int ANIMATION_DELAY = 1;

    public Player(double x, double y, double angle, Color playerColour) {
        super(x, y, angle);

        respawnPointX = x;
        respawnPointY = y;
        this.playerColour = playerColour;


        Controller.players.add(this);

        weapons.add(new AssaultRifle(this));
        weapons.add(new Pistol(this));
        weapons.add(new SniperRifle(this));
        weapons.add(new Shotgun(this));


        //Animation handlers
//        animationTimer.

    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public int getPlayerNumber() {
        return playerNumber;
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
    protected void loadImage() {

        if (dashTimer > 0 && dashing != null) {
            if (lastAction == 6) {
                currentImage = dashing.getNext(currentImage);
            } else {
                currentImage = dashing.getHead();
            }
            lastAction = 6;
            // Don't continue jump animation even if in midair
            jumpTimer = 0;
            dashTimer--;
        } else if (velY != 0 && (jumpTimer > 0 || lastAction == 5 || lastAction == -5) && jumping != null) {
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
        } else if (isWalking && jogging != null ) {
            if (lastAction == 2) {
                currentImage = jogging.getNext(currentImage);
            } else {
                currentImage = jogging.getHead();
            }
            lastAction = 2;
        } else if (standing != null ) {
            if (lastAction == 1) {
                currentImage = standing.getNext(currentImage);
            } else {
                currentImage = standing.getHead();
            }
            lastAction = 1;
        }
    }



    public void collisionArea(Graphics g){

        g.setColor(Color.black);
        g.drawRect(((int)this.x - currentImage.getImage().getWidth() / 2) +40,
                ((int)this.y - currentImage.getImage().getHeight() / 2) +40, currentImage.getImage().getWidth()-85,
                currentImage.getImage().getHeight()-85);

        collisionOn = false;
        double entityLeftWorldX = super.getX() + solidArea.x;
        double entityRightWorldX = super.getX() + solidArea.x + solidArea.width;
        double entityToptWorldY = super.getY() + solidArea.y;
        double entityBottomWorldY = super.getY() + solidArea.y + solidArea.height;

        double playerLeftCol = entityLeftWorldX/50;
        double playerRightCol = entityRightWorldX/50;
        double plaerTopRow = entityToptWorldY/50;
        double playerBottomRow = entityBottomWorldY/50;

        int tileNum1, tileNum2;

        //switch ()

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

    public void setHealth(int health) {
        this.health = health;
    }

    public int getHealth() {
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
     *
     * @param tdoMod Damage value to add
     */
    public void addTDO(long tdoMod) {
        if (this.tdo > 1.6 * (10 ^ 308) || this.tdo < -1.6 * (10 ^ 308)) {
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

    /**
     * Loads the image files into the image strips based upon their names
     */
    public void loadImageStrips() {
        ArrayList<String> imgLocStr = new ArrayList<>();
        String defLocStr = "resources/Textures/PLAYER_ONE/";
        ;
        // Saves amount of text to be used
        if (playerColour == Color.BLUE) {
                defLocStr = "resources/Textures/PLAYER_ONE/";
            } else {
                defLocStr = "resources/Textures/PLAYER_TWO/";
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

        imageLoaded = true;
    }

    @Override
    public void tick(){
        invincibilityTimer--;
        animationTimer++;
    }

    @Override
    public Rectangle getBounds() {
        return boundRect;
    }

    /**
     * Builds the animation.ImageStrip for a specific animation
     *
     * @param imgLocStr           All file names to be loaded into the animation.ImageStrip for animation
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

    public int getSelectedWeapon() {
        return selectedWeapon;
    }

    public void setSelectedWeapon(int selectedWeapon) {
        this.selectedWeapon = selectedWeapon;
    }

    public Arsenal getWeapons() {
        return weapons;
    }

    public boolean isWalking() {
        return isWalking;
    }

    public void setWalking(boolean walking) {
        isWalking = walking;
    }

    public void revive(){
        health = 100;
        x = respawnPointX;
        y = respawnPointY;
        invincibilityTimer = RESPAWN_INVINCIBILITY_TIME;
    }

    public void incrementBulletCount(){
        bulletsShot++;
    }

    public long getBulletCount(){
        return bulletsShot;
    }

    public void incrementBulletHitCount(){
        bulletsHit++;
    }
    public long getBulletHitCount(){
        return bulletsHit;
    }

    public long getWalkingDistance() {
        return walkingDistance;
    }

    public void incrementWalkingDistance() {
        walkingDistance++;
    }

    public boolean isInvincible(){
        return invincibilityTimer > 0;
    }

    public boolean isTimeForNextFrame(){
       return animationTimer >= ANIMATION_DELAY;
    }

    public void resetAnimationTimer(){
        animationTimer = 0;
    }
}