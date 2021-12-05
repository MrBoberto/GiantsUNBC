package player;

/**
 * This file is part of a solution to
 *		CPSC300 Term Project Fall 2021
 *
 * The computer-programmed user "Thanos" who does his best to kill the player in singleplayer mode
 *
 * @author The Boyz
 * @version 1
 */

import game.Controller;
import game.SingleController;
import game.World;
import inventory_items.InventoryItem;
import mapObjects.Block;
import power_ups.PowerUp;
import weapons.guns.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class AIPlayer extends OtherPlayer {
    public static final double DESIRED_OFFSET = 40;
    //Main Player movement directions
    private boolean up = false, down = false,right = false,left = false;
    private boolean upStop = false, downStop = false,rightStop = false,leftStop = false;
    private boolean attack = true;
    private static final double shortRangeBound = 500;   // Distance determining whether to use short or long-range weapon
    private static final int fear = 50;              // Max health advantage the player can have for the AI to want to attack them
    private int randAngleDuration = 0;
    private int randAngleDurationMax = 20;
    private int tryToGetCollectTimer = 0;
    Point target;                       // It may be a player, power up, or weapon; depends on Thanos' judgement
    private Point closestInventoryItem;
    private Point closestPowerUp;
    private int targetType = 3;
    private static String myDialogue = "";
    private static int dialogueCount = 0;


    public AIPlayer(double x, double y, int playerNumber, Color color) {
        super(x, y, playerNumber, color);
        playerColour = new Color(200, 0, 255);
        target = new Point(0, 0);

        // Graphics-related

        loadImageStrips();
        currentImage = standing.getHead();

        setDialogue("Reality can be whatever I want.");
    }


    /**
     * Determines the angle in which the player is facing
     */
    public void setAngle() {
//        ("player.Player 1 Prev super.getAngle(): " + Math.toDegrees(super.getAngle()));
        int avgX = 0;
        int avgY = 0;

        if (randAngleDuration > 0) randAngleDuration--;

        // If there is anything in Thanos' path
        if (rightStop || downStop || leftStop || upStop || randAngleDuration > 0) {
            if (leftStop && randAngleDuration - 1 < randAngleDurationMax) {
                angle = Math.PI * World.sRandom.nextDouble();
                randAngleDurationMax = 5 + World.sRandom.nextInt(10);
                randAngleDuration = randAngleDurationMax;
            }
            if (upStop && randAngleDuration - 1 < randAngleDurationMax) {
                angle = Math.PI / 2 + Math.PI * World.sRandom.nextDouble();
                randAngleDurationMax = 5 + World.sRandom.nextInt(10);
                randAngleDuration = randAngleDurationMax;
            }
            if (rightStop && randAngleDuration - 1 < randAngleDurationMax) {
                angle = Math.PI + Math.PI * World.sRandom.nextDouble();
                randAngleDurationMax = 5 + World.sRandom.nextInt(10);
                randAngleDuration = randAngleDurationMax;
            }
            if (downStop && randAngleDuration - 1 < randAngleDurationMax) {
                angle = 3 * Math.PI / 2 + Math.PI * World.sRandom.nextDouble();
                randAngleDurationMax = 5 + World.sRandom.nextInt(10);
                randAngleDuration = randAngleDurationMax;
            }

            // Ensure angle is in bounds
            for (int i = 0; i < 2; i++) {
                if (angle <= -Math.PI) {
                    angle += 2 * Math.PI;
                } else if (angle > Math.PI) {
                    angle -= 2 * Math.PI;
                }
            }
        }
        // If nothing is in Thanos' path
        else {
            down = false;
            up = false;
            right = false;
            left = false;

            if (this.x - 15 < target.x && this.x + 15 > target.x && this.y - 15 < target.y && this.y + 15 > target.y && targetType > 0) {
                tryToGetCollectTimer = 0;
                closestInventoryItem = null;
                closestPowerUp = null;
            }

            // If it is time to select a new target
            if ((tryToGetCollectTimer < 1)) {
                for (InventoryItem anInventoryItem: SingleController.getInventoryItems()) {
                    if (closestInventoryItem == null) {
                        closestInventoryItem = new Point((int) anInventoryItem.getX(), (int) anInventoryItem.getY());
                    } else {
                        if (World.pythHyp(x - closestInventoryItem.x, y - closestInventoryItem.y)
                                < World.pythHyp(x - anInventoryItem.getX(), y - anInventoryItem.getY())) {
                            closestInventoryItem = new Point((int) anInventoryItem.getX(), (int) anInventoryItem.getY());
                        }
                    }
                }
                if (closestInventoryItem == null) closestInventoryItem = new Point((int) Controller.thisPlayer.getX(), (int) Controller.thisPlayer.getY());

                for (PowerUp aPowerUp: SingleController.getPowerUps()) {
                    if (closestPowerUp == null) {
                        closestPowerUp = new Point((int) aPowerUp.getX(), (int) aPowerUp.getY());
                    } else {
                        if (World.pythHyp(x - closestPowerUp.x, y - closestPowerUp.y)
                                < World.pythHyp(x - aPowerUp.getX(), y - aPowerUp.getY())) {
                            closestPowerUp = new Point((int) aPowerUp.getX(), (int) aPowerUp.getY());
                        }
                    }
                }
                if (closestPowerUp == null) closestPowerUp = new Point((int) Controller.thisPlayer.getX(), (int) Controller.thisPlayer.getY());

                Point tempTarget = new Point((int) Controller.thisPlayer.getX(), (int) Controller.thisPlayer.getY());
                if (World.pythHyp(closestInventoryItem.x - x, closestInventoryItem.y - y) * 2
                        < World.pythHyp(closestPowerUp.x - x, closestPowerUp.y - y)) {
                    if (World.pythHyp(tempTarget.x, tempTarget.y) * 2
                            > World.pythHyp(closestInventoryItem.x - x, closestInventoryItem.y - y)) {
                        tempTarget = new Point(closestInventoryItem.x, closestInventoryItem.y);
                        targetType = 1;
                    } else {
                        targetType = 0;
                    }
                } else {
                    if (World.pythHyp(tempTarget.x, tempTarget.y) * 2
                            > World.pythHyp(closestPowerUp.x - x, closestPowerUp.y - y)) {
                        tempTarget = new Point(closestPowerUp.x, closestPowerUp.y);
                        targetType = 2;
                    } else {
                        targetType = 0;
                    }
                }
                target = tempTarget;
                tryToGetCollectTimer = (int) World.pythHyp(tempTarget.x - x, tempTarget.y - y) / 3 - 1;
            } else {
                tryToGetCollectTimer--;
                if (targetType == 0) {
                    target = new Point((int) Controller.thisPlayer.getX(), (int) Controller.thisPlayer.getY());
                }
            }

            if (Controller.thisPlayer.getHealth() <= health + fear || targetType > 0) {
                if (!attack) {
                    setDialogue("You should have gone for the head.");
                    dialogueCount = myDialogue.length() * 5;
                }
                attack = true;
                // Distance the AI wants to be from the player when attacking
                if (target.y < y - DESIRED_OFFSET) {
                    avgY++;
                    up = true;
                }
                if (target.x > x + DESIRED_OFFSET) {
                    avgX++;
                    right = true;
                }
                if (target.y > y + DESIRED_OFFSET) {
                    avgY--;
                    down = true;
                }
                if (target.x < x - DESIRED_OFFSET) {
                    avgX--;
                    left = true;
                }
            } else {
                if (attack) {
                    setDialogue("Your optimism is misplaced, Asgardian.");
                    dialogueCount = myDialogue.length() * 5;
                }
                attack = false;
                if (target.y < y) {
                    avgY--;
                    down = true;
                }
                if (target.x > x) {
                    avgX--;
                    left = true;
                }
                if (target.y > y) {
                    avgY++;
                    up = true;
                }
                if (target.x < x) {
                    avgX++;
                    right = true;
                }
            }

            if (avgX == 0) {
                if (avgY == 1) {
                    super.setAngle(0);
                } else {
                    super.setAngle(Math.PI);
                }
            } else if (avgY == 0) {
                if (avgX == 1) {
                    super.setAngle(Math.PI / 2);
                } else {
                    super.setAngle(-Math.PI / 2);
                }
            } else if (avgX == 1) {
                if (avgY == 1) {
                    super.setAngle(Math.PI / 4);
                } else {
                    super.setAngle(3 * Math.PI / 4);
                }
            } else {
                if (avgY == 1) {
                    super.setAngle(-Math.PI / 4);
                } else {
                    super.setAngle(-3 * Math.PI / 4);
                }
            }
        }
    }

    /**
     * Determines horizontal and vertical velocities
     * @param speed The net speed at which the player should move
     */
    public void setVelocity(double speed) {
        if (super.getAngle() == 0) {
            setVelY(-speed);
            setVelX(0);
        } else if (super.getAngle() == Math.PI / 2) {
            setVelX(speed);
            setVelY(0);
        } else if (super.getAngle() == Math.PI) {
            setVelY(speed);
            setVelX(0);
        } else if (super.getAngle() == -Math.PI / 2) {
            setVelX(-speed);
            setVelY(0);
        } else {
            setVelX(speed * Math.cos(super.getAngle()));
            setVelY(-speed * Math.sin(super.getAngle()));
//            ("player.Player 1 Current super.getAngle(): " + super.getAngle());
            if ((super.getAngle() > (Math.PI / 2) && super.getAngle() < Math.PI)
                    || (super.getAngle() < 0 && super.getAngle() > -Math.PI / 2)) {
                setVelX(getVelX() * -1);
                setVelY(getVelY() * -1);
            }
        }
    }

    /**
     * Determines what speed to move at based on the controls being used and them translates the player
     * based on the horizontal and vertical velocities
     */
    public void move() {
        // Determine velocities
        if (dashTimer == DASH_TIMER_MAX) {
            dashTimer--;
            setVelocity(DASH_VELOCITY);
        } else if ((up || right || down || left) && dashTimer <= 0) {
            setVelocity(JOG_VELOCITY);
        }

        //Check collisions
        checkBlockCollisions();

        // Determine distance travelled
        if((getVelX() > 0 && !rightStop) || (getVelX() < 0 && !leftStop)){
            super.setX(super.getX() + getVelX());

            //Increase walking distance stat
            if(getVelY() > 0 || getVelX() > 0){
                incrementWalkingDistance();
            }
        }

        if((getVelY() > 0 && !downStop) || (getVelY() < 0 && !upStop)){
            super.setY(super.getY() + getVelY());

            //Increase walking distance stat
            if(getVelY() > 0 || getVelX() > 0){
                incrementWalkingDistance();
            }
        }
    }

    /**
     * Translates the player using move(), applies friction, prevents the player from leaving the window,
     * updates the player's hitbox, and shoots weapons
     */
    @Override
    public void tick() {
        super.tick();
        setAngle();
        if (dialogueCount > 0) dialogueCount--;

        // Determine whether to use a longer or shorter ranged weapon
        if (Controller.thisPlayer.getX() < x - shortRangeBound || Controller.thisPlayer.getY() < y - shortRangeBound
                || Controller.thisPlayer.getX() > x + shortRangeBound || Controller.thisPlayer.getY() > y + shortRangeBound) {
            if (selectedWeapon == 0 && arsenal.getSecondary() != null
                    && (arsenal.getPrimary().getSPEED() < arsenal.getSecondary().getSPEED()
                    || arsenal.getSecondary().getSERIAL() == RocketLauncher.SERIAL)) {
                    selectedWeapon = 1;
            } else if (selectedWeapon == 1
                    && (arsenal.getPrimary().getSPEED() > arsenal.getSecondary().getSPEED()
                    || arsenal.getPrimary().getSERIAL() == RocketLauncher.SERIAL)) {
                    selectedWeapon = 0;
            }
        } else if (arsenal.getSecondary() != null) {
            if (selectedWeapon == 1 && (arsenal.getPrimary().getSPEED() < arsenal.getSecondary().getSPEED()
                    || arsenal.getPrimary().getSERIAL() == LightningSword.SERIAL)) {
                selectedWeapon = 0;
            } else if (selectedWeapon == 0 && (arsenal.getPrimary().getSPEED() > arsenal.getSecondary().getSPEED()
                    || arsenal.getSecondary().getSERIAL() == LightningSword.SERIAL)) {
                selectedWeapon = 1;
            }
        }

        if (dashTimer <= 0) {
            startDashTimer();
        }

        if (dashTimer < DASH_TIMER_MAX && dashTimer > 0) {
            dashTimer--;
        }

        move();

        // Apply vertical friction
        if (getVelY() > Controller.FRICTION) {
            setVelY(getVelY() - Controller.FRICTION);
        } else if (getVelY() < -Controller.FRICTION) {
            setVelY(getVelY() + Controller.FRICTION);
        } else if (getVelY() != 0) {
            setVelY(0);
        }
        // Apply horizontal friction
        if (getVelX() > Controller.FRICTION) {
            setVelX(getVelX() - Controller.FRICTION);
        } else if (getVelX() < -Controller.FRICTION) {
            setVelX(getVelX() + Controller.FRICTION);
        } else if (getVelX() != 0) {
            setVelX(0);
        }

        //images
        if (super.getX() <= currentImage.getImage().getWidth() / 2.0) {
            super.setX(currentImage.getImage().getWidth() / 2.0);
        } else if (super.getX() >= Controller.WIDTH - currentImage.getImage().getWidth() / 2.0) {
            super.setX(Controller.WIDTH - currentImage.getImage().getWidth() / 2.0);
        }

        if (super.getY() <= currentImage.getImage().getHeight() / 2.0) {
            super.setY(currentImage.getImage().getHeight() / 2.0);
            setVelY(1);
        } else if (super.getY() >= Controller.HEIGHT - currentImage.getImage().getHeight() / 2.0) {
            super.setY(Controller.HEIGHT - currentImage.getImage().getHeight() / 2.0);
            setVelY(0);
        }

        boundRect = new Rectangle((int)this.x - currentImage.getImage().getWidth() / 4,
                (int)this.y - currentImage.getImage().getHeight() / 4, currentImage.getImage().getWidth() / 2,
                currentImage.getImage().getHeight() / 2);


        if (arsenal.getPrimary().getCurrentDelay() > 0) {
            arsenal.getPrimary().setCurrentDelay(arsenal.getPrimary().getCurrentDelay() - 1);
        }
        if (arsenal.getSecondary() != null && arsenal.getSecondary().getCurrentDelay() > 0) {
            arsenal.getSecondary().setCurrentDelay(arsenal.getSecondary().getCurrentDelay() - 1);
        }
    }

    private void checkBlockCollisions(){
        upStop = false;
        downStop = false;
        leftStop = false;
        rightStop = false;
        for (int i = 0; i < Controller.blocks.size(); i++) {
            Block block = Controller.blocks.get(i);

            if(getBounds() != null){
                int offset = 3;
                //Check upper bound
                if(block.getBounds().intersects(new Rectangle(
                        (int) x - currentImage.getImage().getWidth() / 4 + offset,
                        (int) y - currentImage.getImage().getHeight() / 4 - offset,
                        currentImage.getImage().getWidth() / 2 - offset,
                        1))){
                    upStop = true;
                }
                if((new Rectangle(
                        (int) x - currentImage.getImage().getWidth() / 4 + offset,
                        (int) y + currentImage.getImage().getHeight() / 4 + offset,
                        currentImage.getImage().getWidth() / 2 - offset,
                        1)).intersects(block.getBounds())){
                    downStop = true;
                }
                if((new Rectangle(
                        (int) x - currentImage.getImage().getWidth() / 4 - offset,
                        (int) y - currentImage.getImage().getHeight() / 4 + offset,
                        1,
                        currentImage.getImage().getHeight() / 2- offset)).intersects(block.getBounds())){
                    leftStop = true;
                }
                if((new Rectangle(
                        (int) x + currentImage.getImage().getWidth() / 4 + offset,
                        (int) y - currentImage.getImage().getHeight() / 4 + offset,
                        1,
                        currentImage.getImage().getHeight() / 2 - offset)).intersects(block.getBounds())){
                    rightStop = true;
                }
            }
        }
    }

    @Override
    public void render(Graphics g) {
        super.render(g);

        isWalking = (up || left || down || right);
        loadImage();

        if(currentImage == null) return;
        // Sets up the axis of rotation
        AffineTransform affTra = AffineTransform.getTranslateInstance(
                x - currentImage.getImage().getWidth() / 2.0,
                y - currentImage.getImage().getHeight() / 2.0);
        // Rotates the frame
        affTra.rotate(super.getAngle(), currentImage.getImage().getWidth() / 2.0,
                currentImage.getImage().getHeight() / 2.0);
        Graphics2D g2d = (Graphics2D) g;

        // Draws the rotated image
        if(skipFrame) {
            skipFrame = false;
        } else {
            g2d.drawImage(currentImage.getImage(), affTra, World.controller);
        }

        if (weaponSerial >= 0 && weaponSerial < 5) {
            g2d.drawImage(weaponTextures.get(weaponSerial), affTra, World.controller);
        } else if (weaponSerial == 5) {
            g2d.drawImage(currentSwordFrame.getImage(), affTra, World.controller);
        }

        if (dialogueCount > 0) {
            g2d.setColor(Color.WHITE);
            Font font1 = new Font("Times New Roman", Font.PLAIN, 10);
            g2d.setFont(font1);
            FontMetrics stringSize1 = g2d.getFontMetrics(font1);

            g2d.fillRect((int) x - (stringSize1.stringWidth(myDialogue)) / 2 - 3,
                    (int) y - 50 - currentImage.getImage().getHeight() / 4,
                    stringSize1.stringWidth(myDialogue) + 6, 20);
            g2d.setColor(Color.BLACK);
            g2d.drawRect((int) x - (stringSize1.stringWidth(myDialogue)) / 2 - 3,
                    (int) y - 50 - currentImage.getImage().getHeight() / 4,
                    stringSize1.stringWidth(myDialogue) + 6, 20);
            g2d.setColor(Color.WHITE);
            g2d.fillPolygon(new int[] {(int) x - 2, (int) x, (int) x + 2}, new int[] { (int) y - 60, (int) y - 50, (int) y - 60}, 3);
            g2d.setColor(Color.BLACK);
            g2d.drawLine((int) x - 2, (int) y - 40, (int) x, (int) y - 50);
            g2d.drawLine((int) x + 2, (int) y - 40, (int) x, (int) y - 50);

            g2d.drawString(myDialogue, (int) x - (stringSize1.stringWidth(myDialogue)) / 2,
                    (int) y - 40 - currentImage.getImage().getHeight() / 4);
        }

        // Draws the player's hitbox
        g.setColor(playerColour);

        Font font2 = new Font("Arial", Font.BOLD, 15);
        g2d.setFont(font2);
        FontMetrics stringSize = g2d.getFontMetrics(font2);

        g2d.fillRect((int) x - currentImage.getImage().getWidth() / 4,
                (int) y - currentImage.getImage().getHeight() / 4 - 5,
                (currentImage.getImage().getWidth() * health / 200),
                5);

        g2d.drawString(playerName, (int) x - (stringSize.stringWidth(playerName)) / 2,
                (int) y - 10 - currentImage.getImage().getHeight() / 4);
    }

    /**
     * Loads the image files into the image strips based upon their names
     */
    @Override
    public void loadImageStrips() {
        ArrayList<String> imgLocStr = new ArrayList<>();
        String defLocStr;

        // Saves amount of text to be used
        defLocStr = "/resources/Textures/PLAYER_THANOS/";

        // Builds image strip for standing facing right
        for (int i = 1; i <= 4; i++) {
            imgLocStr.add("stand (" + i + ").png");
        }
        standing = buildImageStrip(imgLocStr, defLocStr);
//        (standing.toString());
        imgLocStr.clear();

        // Builds image strip for jogging
        for (int i = 1; i <= 20; i++) {
            imgLocStr.add("jog (" + i + ").png");
        }
        jogging = buildImageStrip(imgLocStr, defLocStr);
//        (jogging.toString());
        imgLocStr.clear();

        // Builds image strip for dashing
        for (int i = 1; i <= 8; i++) {
            imgLocStr.add("dash (" + i + ").png");
        }
        dashing = buildImageStrip(imgLocStr, defLocStr);
//        (dashing.toString());
        imgLocStr.clear();

        defLocStr = "/resources/Textures/WEAPONS/";

        // Builds image strip for jumping
        for (int i = 1; i <= 7; i++) {
            imgLocStr.add("sword_thanos (" + i + ").png");
        }
        leftwardSwordTextures = buildImageStrip(imgLocStr, defLocStr);
//        (jumping.toString());
        imgLocStr.clear();

        // Builds image strip for jumping
        for (int i = 8; i <= 14; i++) {
            imgLocStr.add("sword_thanos (" + i + ").png");
        }
        rightwardSwordTextures = buildImageStrip(imgLocStr, defLocStr);
//        (jumping.toString());
        imgLocStr.clear();

        for (int i = 0; i <= 4; i++) {
            imgLocStr.add("weapon (" + i + ").png");
        }
        weaponTextures = new ArrayList<>();
        // Load weapon textures
        for (String s : imgLocStr) {
            try {
                weaponTextures.add(ImageIO.read(Objects.requireNonNull(getClass().getResource("/resources/Textures/WEAPONS/" + s))));
            } catch (IOException ignored) {
            }
        }
    }

    public static void setDialogue(String dialogue) {
        myDialogue = dialogue;
        dialogueCount = myDialogue.length() * 5;
    }
}
