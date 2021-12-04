package player;

import animation.ImageFrame;
import animation.ImageStrip;
import game.*;
import power_ups.PowerUp;
import packets.ClientDashPacket;
import packets.ServerDashPacket;
import utilities.BufferedImageLoader;

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
    protected final double JOG_VELOCITY = 5.2;
    protected final double DASH_VELOCITY = 16;

    // The texture of the player being used in the current frame
    protected ImageFrame currentImage;
    protected Rectangle boundRect;
    protected int health = 100;
    protected int healTimer = 120;
    protected final int HEAL_TIMER_MAX = 120;
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
    protected final int playerNumber;
    protected String playerName;
    protected Color playerColour;

    // Determines what the player did last frame to help determine what animation to play.
    protected int lastAction = 1;
    protected int dashTimer = 0;
    protected int dashAnimationTimer = 0;
    public final int DASH_TIMER_MAX = 20;
    protected ImageStrip standing;   // 1
    protected ImageStrip jogging;    // 2
    protected ImageStrip dashing;    // 6
    protected ArrayList<BufferedImage> weaponTextures;
    protected ImageStrip leftwardSwordTextures;
    protected ImageStrip rightwardSwordTextures;
    protected ImageFrame currentSwordFrame;
    protected boolean isSwordLeft;
    protected int swordTextureCount = 0;
    protected final int SWORD_TEXTURE_MAX = 3;
    protected int swordAnimationCount = 0;
    protected final int SWORD_ANIMATION_MAX = 3;
    protected boolean isWalking = false;

    //Arsenals
    protected Arsenal arsenal;
    protected int selectedWeapon = 0;
    protected int weaponSerial = -1;

    //Respawn point
    protected double respawnPointX;
    protected double respawnPointY;

    //Stats
    protected long bulletsShot = 0;
    protected long bulletsHit = 0;
    protected long walkingDistance = 0;
    protected long pickedUpPowerUps = 0;

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


    public Player(double x, double y, int playerNumber, Color playerColour) {
        super(x, y);

        this.playerNumber = playerNumber;

        respawnPointX = x;
        respawnPointY = y;

        System.out.println(x + " " + y);

        this.playerColour = playerColour;


        Controller.players.add(this);
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
        if (deathCount == 0) {
            return killCount;
        }
        else {
            return (double) killCount / (double) deathCount;
        }
    }

    /**
     * Determines which animation is being played and which frame should currently be played
     */
    protected void loadImage() {
        if (dashAnimationTimer > 0) {
            if (lastAction == 6) {
                currentImage = dashing.getNext(currentImage);
            } else {
                currentImage = dashing.getHead();
            }
            lastAction = 6;
            // Don't continue jump animation even if in midair
            dashAnimationTimer--;
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

        if (currentSwordFrame == null || swordTextureCount == 0 && swordAnimationCount <= 0) {
            if (isSwordLeft) {
                currentSwordFrame = rightwardSwordTextures.getHead();
            } else {
                currentSwordFrame = leftwardSwordTextures.getHead();
            }
            swordTextureCount++;
        } else if (weaponSerial == 5 && swordAnimationCount == SWORD_ANIMATION_MAX) {
            swordAnimationCount--;
            if (isSwordLeft) {
                currentSwordFrame = rightwardSwordTextures.getHead().getNext().getNext().getNext().getNext();
            } else {
                currentSwordFrame = leftwardSwordTextures.getHead().getNext().getNext().getNext().getNext();
            }
            swordTextureCount = SWORD_TEXTURE_MAX + 1;
        } else if (weaponSerial == 5 && swordAnimationCount > 0) {
            swordAnimationCount--;
            currentSwordFrame = currentSwordFrame.getNext();
        } else if (weaponSerial == 5) {
            if (swordTextureCount >= SWORD_TEXTURE_MAX) {
                if (isSwordLeft) {
                    currentSwordFrame = leftwardSwordTextures.getHead();
                } else {
                    currentSwordFrame = rightwardSwordTextures.getHead();
                }
                swordTextureCount = 0;
            } else {
                currentSwordFrame = currentSwordFrame.getNext();
            }
            swordTextureCount++;
        }
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
        healTimer = HEAL_TIMER_MAX;
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

        imgLocStr.clear();

        for (int i = 0; i <= 4; i++) {
            imgLocStr.add("weapon (" + i + ").png");
        }
        weaponTextures = new ArrayList<>();
        // Load weapon textures
        for (String s : imgLocStr) {
            try {
                weaponTextures.add(ImageIO.read(Objects.requireNonNull(getClass().getResource("/resources/Textures/WEAPONS/" + s))));
            } catch (IOException exc) {
                System.out.println("Could not find image file: " + exc.getMessage());
            }
        }
        imgLocStr.clear();

        if (playerNumber == 0) {
            defLocStr = "/resources/Textures/WEAPONS/sword_blue (";
        } else {
            defLocStr = "/resources/Textures/WEAPONS/sword_red (";
        }

        // Builds image strip for jumping
        for (int i = 1; i <= 7; i++) {
            imgLocStr.add(i + ").png");
        }
        leftwardSwordTextures = buildImageStrip(imgLocStr, defLocStr);
//        System.out.println(jumping.toString());
        imgLocStr.clear();

        // Builds image strip for jumping
        for (int i = 8; i <= 14; i++) {
            imgLocStr.add(i + ").png");
        }
        rightwardSwordTextures = buildImageStrip(imgLocStr, defLocStr);
//        System.out.println(jumping.toString());
        imgLocStr.clear();

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
            weaponSerial = arsenal.getPrimary().getSERIAL();
        } else {
            weaponSerial = arsenal.getSecondary().getSERIAL();
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
        g2.rotate(Math.PI * 5/4, x,y);
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
        StringBuilder imageFileNames = new StringBuilder();
        StringBuilder imageFileSubstring = new StringBuilder();
        for (String s : imgLocStr) {
            images.add(BufferedImageLoader.loadImage(defaultFileLocation + "" + s));
            imageFileNames.append(defaultFileLocation).append(s).append(", ");
        }
        // Used for the toString() method of this animation.ImageStrip
        for (int i = 0; i < imageFileNames.length() - 2; i++) {
            imageFileSubstring.append(imageFileNames.charAt(i));
        }
        return new ImageStrip(images, imageFileSubstring.toString());
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

    public Arsenal getArsenal() {
        return arsenal;
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

    public void setRespawnPoint(double respawnPointX, double respawnPointY) {
        this.respawnPointX = respawnPointX;
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

        if(ricochetTimer != -1) {
            this.ricochetTimer = ricochetTimer;
            this.numberOfBulletBounces = bounces;
        }
    }

    public float getSpeedMultiplier() {
        return speedMultiplier;
    }

    public boolean isDamageUp(){
        return getDamageMultiplier() > DEFAULT_DAMAGE_MULTIPLIER;
    }

    public boolean isDamageDown(){
        return getDamageMultiplier() < DEFAULT_DAMAGE_MULTIPLIER;
    }

    public boolean isSpeedUp(){
        return getSpeedMultiplier() > DEFAULT_SPEED_MULTIPLIER;
    }

    public boolean isSpeedDown(){
        return getSpeedMultiplier() < DEFAULT_SPEED_MULTIPLIER;
    }

    public int getNumberOfBulletBounces() {
        return numberOfBulletBounces;
    }

    public void startDashTimer() {
        if (dashTimer <= 1) {
            dashTimer = DASH_TIMER_MAX;
            dashAnimationTimer = dashing.getAnimationLength();

            if (World.controller instanceof ServerController && playerNumber == 0) {
                World.controller.getOutputConnection().sendPacket(new ServerDashPacket());
            } else if (World.controller instanceof ClientController && playerNumber == 1) {
                World.controller.getOutputConnection().sendPacket(new ClientDashPacket());
            }
        }
    }

    public void setArsenal(Arsenal arsenal) {
        this.arsenal = arsenal;
    }

    public PowerUp.PowerUpType[] getPowerUps(){
        ArrayList<PowerUp.PowerUpType> powerUps = new ArrayList<>();

        if(isDamageUp()){
            powerUps.add(PowerUp.PowerUpType.DamageUp);
        }
        if(isDamageDown()){
            powerUps.add(PowerUp.PowerUpType.DamageDown);
        }
        if(isSpeedUp()){
            powerUps.add(PowerUp.PowerUpType.SpeedUp);
        }
        if(isSpeedDown()){
            powerUps.add(PowerUp.PowerUpType.SpeedDown);
        }
        if(isRicochetEnabled()){
            powerUps.add(PowerUp.PowerUpType.Ricochet);
        }
        return powerUps.toArray(new PowerUp.PowerUpType[0]);
    }

    public boolean isSwordLeft() {
        return isSwordLeft;
    }

    public void setSwordLeft(boolean swordLeft) {
        isSwordLeft = swordLeft;
        swordAnimationCount = SWORD_ANIMATION_MAX;
    }

    public void setDeathCount(int deathCount){
        this.deathCount = deathCount;
    }

    public long getPickedUpPowerUps() {
        return pickedUpPowerUps;
    }

    public void increasePickedUpPowerUps() {
        this.pickedUpPowerUps++;
    }

    public void setKillCount(int killCount) {
        this.killCount = killCount;
    }

    public void setBulletsShot(long bulletsShot) {
        this.bulletsShot = bulletsShot;
    }

    public void setBulletsHit(long bulletsHit) {
        this.bulletsHit = bulletsHit;
    }

    public void setWalkingDistance(long walkingDistance) {
        this.walkingDistance = walkingDistance;
    }

    public void setPickedUpPowerUps(long pickedUpPowerUps) {
        this.pickedUpPowerUps = pickedUpPowerUps;
    }
}