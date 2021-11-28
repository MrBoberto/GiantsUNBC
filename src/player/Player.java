package player;

import game.MainMenu;
import animation.ImageFrame;
import animation.ImageStrip;
import game.*;
import weapons.guns.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;


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

    public MainMenu mainMenu;
    // The texture of the player being used in the current frame
    protected ImageFrame currentImage;
    protected Rectangle boundRect;
    protected int health = 100;
    protected int healTimer = 120;
    protected final int HEALTIMERMAX = 120;
    protected int killCount = 0;
    protected int deathCount = 0;
    protected double kdr = -1;
    protected double tdo = 0;

    // Power Ups variables

    public static final float DEFAULT_DAMAGE_MULTIPLIER = 1;
    protected float damageMultiplier = DEFAULT_DAMAGE_MULTIPLIER;
    protected int damageMultiplierTimer = 0;

    public static final float DEFAULT_SPEED_MULTIPLIER = 1;
    protected float speedMultiplier = DEFAULT_SPEED_MULTIPLIER;
    protected int speedMultiplierTimer = 0;

    public static final int DEFAULT_NUMBER_OF_BOUNCES = 1;
    protected int numberOfBulletBounces = DEFAULT_NUMBER_OF_BOUNCES;
    protected int ricochetTimer = 0;

    //Player characteristics
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
    protected ArrayList<BufferedImage> weaponTextures;
    protected boolean shiftIsHeld = false;
    protected boolean spaceIsHeld = false;
    protected boolean ctrlIsHeld = false;
    protected boolean tIsHeld = false;
    protected boolean mouseInside = true;
    protected boolean isFalling = true;
    protected boolean isJumping = false;
    protected boolean isSneaking = false;
    protected boolean isWalking = false;
    // Prevents dash from being held
    protected boolean canDash = true;

    protected Arsenal weapons = new Arsenal();
    protected int selectedWeapon = 0;
    protected int weaponSerial = -1;

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
    protected int invincibilityGraphicTimer = 0;
    public static final int INVINCIBILITY_GRAPHIC_TIME = 15;
    protected boolean skipFrame = false;
    protected boolean isInvincible = false;

    //Animation timers
    protected int animationTimer = 0;
    public static final int ANIMATION_DELAY = 1;


    public Player(double x, double y, double angle, Color playerColour) {
        super(x, y, angle);

        respawnPointX = x;
        respawnPointY = y;

        System.out.println(x + " " + y);

        this.playerColour = playerColour;


        Controller.players.add(this);

        weapons.add(new Pistol(this));
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
            kdr = (float)killCount / (float)deathCount;
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
        double kills = killCount;
        double deaths = deathCount;
        kdr = kills / deaths;
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
        } else if (getVelY() != 0 && (jumpTimer > 0 || lastAction == 5 || lastAction == -5) && jumping != null) {
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

    public void resetHealTimer() {
        healTimer = HEALTIMERMAX;
    }

    /**
     * Increase total damage output of the player
     *
     * @param tdoMod Damage value to add
     */
    public void addTDO(double tdoMod) {
        if (this.tdo > 1.6 * Math.pow(10, 308) || this.tdo < -1.6 * Math.pow(10, 308)) {
            System.out.println("ERROR: TDO overflow");
        } else {
            this.tdo += tdoMod;
        }
    }

    public double getDamageMultiplier() {
        return damageMultiplier;
    }

    public void setDamageMultiplier(float damageMultiplier, int time) {
        if(damageMultiplier != -1) {
            this.damageMultiplier = damageMultiplier;
            damageMultiplierTimer = time;
        }

    }

    /**
     * Loads the image files into the image strips based upon their names
     */
    public void loadImageStrips() {
        ArrayList<String> imgLocStr = new ArrayList<>();
        String defLocStr;
        ;
        // Saves amount of text to be used
        if (playerColour == Color.BLUE) {
                defLocStr = "/resources/Textures/PLAYER_ONE/";
            } else {
                defLocStr = "/resources/Textures/PLAYER_TWO/";
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

        for (int i = 0; i <= 4; i++) {
            imgLocStr.add("weapon (" + i + ").png");
        }
        weaponTextures = new ArrayList<>();
        // Load weapon textures
        for (int i = 0; i < imgLocStr.size(); i++) {
            try {
                weaponTextures.add(ImageIO.read(Objects.requireNonNull(getClass().getResource("/resources/Textures/WEAPONS/" + imgLocStr.get(i)))));
            } catch (IOException exc) {
                System.out.println("Could not find image file: " + exc.getMessage());
            }
        }
        imageLoaded = true;
    }

    @Override
    public void tick(){
        if (healTimer > 0) {
            healTimer--;
        } else if (healTimer == 0 && health < 100) {
            health++;
            healTimer += 4;
        }
        if (invincibilityTimer > 0) {
            invincibilityTimer--;
        }
        animationTimer++;

        if (selectedWeapon == 0) {
            weaponSerial = weapons.getPrimary().getSERIAL();
        } else {
            weaponSerial = weapons.getSecondary().getSERIAL();
        }

        //Increase timer in powerUps if present.
        if(damageMultiplierTimer > 0){
            damageMultiplierTimer--;
        } else {
            damageMultiplier = DEFAULT_DAMAGE_MULTIPLIER;
        }

        if(speedMultiplierTimer > 0){
            speedMultiplierTimer--;
        } else {
            speedMultiplier = DEFAULT_SPEED_MULTIPLIER;
        }

        if(ricochetTimer > 0){
            ricochetTimer--;
        }
    }

    @Override
    public void render(Graphics g){
        Graphics2D g2 = (Graphics2D) g;
        //Render shadow
        g2.setColor(new Color(0, 0, 0, 64));
        g2.rotate(+Math.PI * 5/4, x,y);
        g2.fillOval(
                (int) x - Controller.GRID_SIZE / 4,
                (int) y - Controller.GRID_SIZE / 4,
                Controller.GRID_SIZE / 2,
                Controller.GRID_SIZE* 7/8);
        g2.rotate(-Math.PI * 5/4 , x,y);

        if(isInvincible()) {
            invincibilityGraphicTimer ++;
            if(invincibilityGraphicTimer > INVINCIBILITY_GRAPHIC_TIME){
                skipFrame = true;
                if(invincibilityGraphicTimer > 2*INVINCIBILITY_GRAPHIC_TIME) {
                    invincibilityGraphicTimer = 0;
                }
            }
        }
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
                images.add(ImageIO.read(getClass().getResource(defaultFileLocation + "" + imgLocStr.get(i))));
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

    public void revive(){
        health = 100;
        x = respawnPointX;
        y = respawnPointY;
        invincibilityTimer = RESPAWN_INVINCIBILITY_TIME;
    }

    public int getSelectedWeapon() {
        return selectedWeapon;
    }

    public void setSelectedWeapon(int selectedWeapon) {
        this.selectedWeapon = selectedWeapon;
    }

    public int getWeaponSerial() {
        return weaponSerial;
    }

    public void setWeaponSerial(int weaponSerial) {
        this.weaponSerial = weaponSerial;
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
        if(World.controller instanceof ServerController || World.controller instanceof SingleController) {
            return invincibilityTimer > 0;
        } else {
            return isInvincible;
        }
    }

    //Client use only
    public void setInvincible(boolean invincible) {
        isInvincible = invincible;
    }

    public void setInvincible(int time){
        invincibilityTimer = time;
    }

    public boolean isTimeForNextFrame(){
       return animationTimer >= ANIMATION_DELAY;
    }

    public void resetAnimationTimer(){
        animationTimer = 0;
    }

    public double getRespawnPointX() {
        return respawnPointX;
    }

    public double getRespawnPointY() {
        return respawnPointY;
    }

    public void setRespawnPointX(double respawnPointX) {
        this.respawnPointX = respawnPointX;
    }

    public void setRespawnPointY(double respawnPointY) {
        this.respawnPointY = respawnPointY;
    }

    public void setSpeedMultiplier(float speedMultiplier, int time){
        if(speedMultiplier != -1) {
            this.speedMultiplier = speedMultiplier;
            speedMultiplierTimer = time;
        }
    }

    public boolean isRicochetEnabled(){
        return ricochetTimer > 0;
    }

    public void setRicochet(int bounces, int ricochetTimer) {
        this.ricochetTimer = ricochetTimer;
        this.numberOfBulletBounces = bounces;
    }

    public int getNumberOfBulletBounces() {
        return numberOfBulletBounces;
    }
}