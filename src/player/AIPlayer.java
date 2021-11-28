package player;

import game.Controller;
import game.World;
import mapObjects.Block;
import weapons.guns.AssaultRifle;
import weapons.guns.Pistol;
import weapons.guns.Shotgun;
import weapons.guns.SniperRifle;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class AIPlayer extends OtherPlayer {
    //Main Player movement directions
    private boolean up = false, down = false,right = false,left = false;
    private boolean upStop = false, downStop = false,rightStop = false,leftStop = false;
    private boolean attac = true;
    private double desiredOffset = 40;  // Distance the AI wants to be from the player when attacking
    private double shortRangeBound = 500;   // Distance determining whether to use short or long-range weapon
    private int fear = 50;              // Max health advantage the player can have for the ai to want to attack them
    private int randAngleDuration = 0;
    private int randAngleDurationMax = 20;


    public AIPlayer(double x, double y, double angle, Color color) {
        super(x, y, angle, color);
        playerColour = new Color(200, 0, 255);
        weapons.clear();
        weapons.add(new SniperRifle(this));
        weapons.add(new AssaultRifle(this));
        weapons.add(new Pistol(this));
        weapons.add(new Shotgun(this));

        playerNumber = 1;
        // Graphics-related

        loadImageStrips();
        currentImage = standing.getHead();
    }


    /**
     * Determines the angle in which the player is facing
     */
    public void setAngle() {
//        System.out.println("player.Player 1 Prev super.getAngle(): " + Math.toDegrees(super.getAngle()));
        int avgX = 0;
        int avgY = 0;

        if (randAngleDuration > 0) randAngleDuration--;

        // If there is anything in Thanos' path
        if (rightStop || downStop || leftStop || upStop || randAngleDuration > 0) {
            if (leftStop && randAngleDuration - 1 < randAngleDurationMax) {
                angle = Math.PI * World.getSRandom().nextDouble();
                randAngleDurationMax = 5 + World.getSRandom().nextInt(10);
                randAngleDuration = randAngleDurationMax;
            }
            if (upStop && randAngleDuration - 1 < randAngleDurationMax) {
                angle = Math.PI / 2 + Math.PI * World.getSRandom().nextDouble();
                randAngleDurationMax = 5 + World.getSRandom().nextInt(10);
                randAngleDuration = randAngleDurationMax;
            }
            if (rightStop && randAngleDuration - 1 < randAngleDurationMax) {
                angle = Math.PI + Math.PI * World.getSRandom().nextDouble();
                randAngleDurationMax = 5 + World.getSRandom().nextInt(10);
                randAngleDuration = randAngleDurationMax;
            }
            if (downStop && randAngleDuration - 1 < randAngleDurationMax) {
                angle = 3 * Math.PI / 2 + Math.PI * World.getSRandom().nextDouble();
                randAngleDurationMax = 5 + World.getSRandom().nextInt(10);
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

            if (Controller.thisPlayer.getHealth() <= health + fear) {
                if (!attac) {
                    System.out.println(playerName + ": You should have gone for the head.");
                }
                attac = true;
                if (Controller.thisPlayer.getY() < y - desiredOffset) {
                    avgY++;
                    up = true;
                }
                if (Controller.thisPlayer.getX() > x + desiredOffset) {
                    avgX++;
                    right = true;
                }
                if (Controller.thisPlayer.getY() > y + desiredOffset) {
                    avgY--;
                    down = true;
                }
                if (Controller.thisPlayer.getX() < x - desiredOffset) {
                    avgX--;
                    left = true;
                }
            } else {
                if (attac) {
                    System.out.println(playerName + ": Your optimism is misplaced, Asgardian.");
                }
                attac = false;
                if (Controller.thisPlayer.getY() < y) {
                    avgY--;
                    down = true;
                }
                if (Controller.thisPlayer.getX() > x) {
                    avgX--;
                    left = true;
                }
                if (Controller.thisPlayer.getY() > y) {
                    avgY++;
                    up = true;
                }
                if (Controller.thisPlayer.getY() < x) {
                    avgX++;
                    right = true;
                }
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
            } else if (avgX == 1) {
                if (avgY == 1) {
                    super.setAngle(Math.PI / 4);
                    return;
                } else {
                    super.setAngle(3 * Math.PI / 4);
                    return;
                }
            } else if (avgX == -1){
                if (avgY == 1) {
                    super.setAngle(-Math.PI / 4);
                    return;
                } else {
                    super.setAngle(-3 * Math.PI / 4);
                    return;
                }
            }

            double acuteAngle = Math.atan(avgY/avgX);
//        System.out.println("player.Player 1 Acute super.getAngle(): " + Math.toDegrees(acuteAngle));

            if (avgY < 0) {
                acuteAngle += Math.PI;
            }

            super.setAngle(acuteAngle);
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
//            System.out.println("player.Player 1 Current super.getAngle(): " + super.getAngle());
            if ((super.getAngle() > (Math.PI / 2) && super.getAngle() < Math.PI)
                    || (super.getAngle() < 0 && super.getAngle() > -Math.PI / 2)) {
                setVelX(getVelX() * -1);
                setVelY(getVelY() * -1);
            }
        }
    }

    public double getVelocity() {
        return Math.sqrt(Math.pow(getVelX(), 2) + Math.pow(getVelY(), 2)) * Math.cos(super.getAngle());
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
                if (up || right || down || left) {
                    setVelocity(VELSNEAK / 2);
                } else {
                    setVelX(0);
                }
            }
        } else if (ctrlIsHeld && canDash && getVelocity() <= VELJOG) {
            setVelocity(VELDASH);
            dashTimer = dashing.getLength();
        } else if (shiftIsHeld && (up || right || down || left)) {
            setVelocity(VELSNEAK);
        } else if ((up || right || down || left) && !shiftIsHeld && getVelocity() < VELJOG) {
            setVelocity(VELJOG);
        } else if (shiftIsHeld) {
            setVelX(0);
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

        // Determine whether to use shorter or longer ranged weapon
        if (Controller.thisPlayer.getX() < x - shortRangeBound || Controller.thisPlayer.getY() < y - shortRangeBound
                || Controller.thisPlayer.getX() > x + shortRangeBound || Controller.thisPlayer.getY() > y + shortRangeBound) {
        if (selectedWeapon == 0 && weapons.getSecondary() != null
            && weapons.getPrimary().getSPEED() < weapons.getSecondary().getSPEED()) {
                selectedWeapon = 1;
            } else if (selectedWeapon == 1 && weapons.getPrimary().getSPEED() > weapons.getSecondary().getSPEED()) {
                selectedWeapon = 0;
            }
        } else if (weapons.getSecondary() != null) {
            if (selectedWeapon == 1 && weapons.getPrimary().getSPEED() < weapons.getSecondary().getSPEED()) {
                selectedWeapon = 0;
            } else if (selectedWeapon == 0 && weapons.getPrimary().getSPEED() > weapons.getSecondary().getSPEED()) {
                selectedWeapon = 1;
            }
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
            isFalling = false;
        }

        boundRect = new Rectangle((int)this.x - currentImage.getImage().getWidth() / 4,
                (int)this.y - currentImage.getImage().getHeight() / 4, currentImage.getImage().getWidth() / 2,
                currentImage.getImage().getHeight() / 2);

        collisionOn = false;
//        double entityLeftWorldX = super.getX() + solidArea.x;
//        double entityRightWorldX = super.getX() + solidArea.x + solidArea.width;
//        double entityTopWorldX = super.getY() + solidArea.y;
//        double entityBottomWorldY = super.getY() + solidArea.y + solidArea.height;


        if (weapons.getPrimary().getCurrentDelay() > 0) {
            weapons.getPrimary().setCurrentDelay(weapons.getPrimary().getCurrentDelay() - 1);
        }
        if (weapons.getSecondary() != null && weapons.getSecondary().getCurrentDelay() > 0) {
            weapons.getSecondary().setCurrentDelay(weapons.getSecondary().getCurrentDelay() - 1);
        }

        solidArea = new Rectangle(((int)this.x - currentImage.getImage().getWidth() / 2) + 40,
                ((int)this.y - currentImage.getImage().getHeight() / 2) + 40, currentImage.getImage().getWidth() - 85,
                currentImage.getImage().getHeight() - 85);
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

    /**
     * Gets the current frame from loadImage() and rotates it based on the player's angle
     *
     * @param g
     */
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

        if (weaponSerial == -1 || weaponTextures.get(weaponSerial) == null) return;
        g2d.drawImage(weaponTextures.get(weaponSerial), affTra, World.controller);

        // Draws the player's hitbox
        g.setColor(playerColour);

        Font font = new Font("Arial", Font.BOLD, 15);
        g2d.setFont(font);
        FontMetrics stringSize = g2d.getFontMetrics(font);

        g2d.fillRect((int) x - currentImage.getImage().getWidth() / 4,
                (int) y - currentImage.getImage().getHeight() / 4 - 5,
                (currentImage.getImage().getWidth() * health / 200),
                5);

        g2d.drawString(playerName, (int) x - (stringSize.stringWidth(playerName)) / 2,
                (int) y - 10 - currentImage.getImage().getHeight() / 4);
    }

    @Override
    /**
     * Loads the image files into the image strips based upon their names
     */
    public void loadImageStrips() {
        ArrayList<String> imgLocStr = new ArrayList<>();
        String defLocStr;
        ;
        // Saves amount of text to be used
        defLocStr = "/resources/Textures/PLAYER_THANOS/";

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
}
