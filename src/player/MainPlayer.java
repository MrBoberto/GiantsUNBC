package player;

import game.ClientController;
import game.Controller;
import game.ServerController;
import game.World;
import mapObjects.Block;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

public class MainPlayer extends Player {

    //Main Player movement directions
    private boolean up = false, down = false,right = false,left = false;
    private boolean upStop = false, downStop = false,rightStop = false,leftStop = false;
    private boolean dash = false;
    protected ArrayList<BufferedImage> slotTextures;
    protected boolean button1Held = false;

    public MainPlayer(double x, double y, int playerNumber, Color color) {
        super(x, y, playerNumber, color);


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

        if (up) avgY++;
        if (right) avgX++;
        if (down) avgY--;
        if (left) avgX--;

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
//        System.out.println("player.Player 1 Acute super.getAngle(): " + Math.toDegrees(acuteAngle));

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
        } else if (dashTimer == DASH_TIMER_MAX) {
            dashTimer--;
            setVelocity(VELDASH);
        } else if (shiftIsHeld && (up || right || down || left)) {
            setVelocity(VELSNEAK);
        } else if ((up || right || down || left) && dashTimer <= 0) {
            setVelocity(VELJOG);
        } else if (shiftIsHeld) {
            setVelX(0);
        }

        //Check collisions
        checkBlockCollisions();

        // Determine distance travelled
        if((getVelX() > 0 && !rightStop) || (getVelX() < 0 && !leftStop)){
            super.setX(super.getX() + (getVelX()*speedMultiplier));

            //Increase walking distance stat
            if(getVelY() > 0 || getVelX() > 0){
                incrementWalkingDistance();
            }
        }

        if((getVelY() > 0 && !downStop) || (getVelY() < 0 && !upStop)){
            super.setY(super.getY() + (getVelY()*speedMultiplier));

            //Increase walking distance stat
            if(getVelY() > 0 || getVelX() > 0){
                incrementWalkingDistance();
            }
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
                int offset = Controller.GRID_SIZE / 4;
                int playerThickness = 8;
                int boundThickness = 1;
                //Check upper bound
                if(block.getBounds().intersects(new Rectangle(
                        (int) x - currentImage.getImage().getWidth() / 4 + offset,
                        (int) y - currentImage.getImage().getHeight() / 4 + playerThickness,
                        currentImage.getImage().getWidth() / 2 - 2*offset,
                        boundThickness))){
                    upStop = true;
                }
                //Check lower bound
                if((new Rectangle(
                        (int) x - currentImage.getImage().getWidth() / 4 + offset,
                        (int) y + currentImage.getImage().getHeight() / 4 - boundThickness - playerThickness,
                        currentImage.getImage().getWidth() / 2 - 2*offset,
                        boundThickness)).intersects(block.getBounds())){
                    downStop = true;
                }
                //Check left bound
                if((new Rectangle(
                        (int) x - currentImage.getImage().getWidth() / 4 + playerThickness,
                        (int) y - currentImage.getImage().getHeight() / 4 + offset,
                        boundThickness,
                        currentImage.getImage().getHeight() / 2- 2*offset)).intersects(block.getBounds())){
                    leftStop = true;
                }
                //Check right bound
                if((new Rectangle(
                        (int) x + currentImage.getImage().getWidth() / 4 - boundThickness - playerThickness,
                        (int) y - currentImage.getImage().getHeight() / 4 + offset,
                        boundThickness,
                        currentImage.getImage().getHeight() / 2 - 2*offset)).intersects(block.getBounds())){
                    rightStop = true;
                }
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

        //Keep player inside game area
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

        if (arsenal.getPrimary().getCurrentDelay() > 0) {
            arsenal.getPrimary().setCurrentDelay(arsenal.getPrimary().getCurrentDelay() - 1);
        }
        if (arsenal.getSecondary() != null && arsenal.getSecondary().getCurrentDelay() > 0) {
            arsenal.getSecondary().setCurrentDelay(arsenal.getSecondary().getCurrentDelay() - 1);
        }

        solidArea = new Rectangle(((int)this.x - currentImage.getImage().getWidth() / 2) + 40,
                ((int)this.y - currentImage.getImage().getHeight() / 2) + 40, currentImage.getImage().getWidth() - 85,
                currentImage.getImage().getHeight() - 85);

    }

    /**
     * Gets the current frame from loadImage() and rotates it based on the player's angle
     *
     * @param g
     */
    @Override
    public void render(Graphics g) {
        super.render(g);

        Graphics2D g2d = (Graphics2D) g;


        // displayInventory(g2d);

        isWalking = (up || left || down || right);
        if(isTimeForNextFrame()){
            loadImage();
            resetAnimationTimer();
        }

        if(currentImage == null) return;
        // Sets up the axis of rotation
        AffineTransform affTra = AffineTransform.getTranslateInstance(
                x - currentImage.getImage().getWidth() / 2.0,
                y - currentImage.getImage().getHeight() / 2.0);
        // Rotates the frame
        affTra.rotate(super.getAngle(), currentImage.getImage().getWidth() / 2.0,
                currentImage.getImage().getHeight() / 2.0);

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

    private void displayInventory(Graphics2D g2d) {
        if (selectedWeapon == 0) {
            // If primary is selected, this is primary slot
            AffineTransform affTraPP = AffineTransform.getTranslateInstance(Controller.WIDTH - 240, 0);
            // If primary is selected, this is secondary slot
            AffineTransform affTraPS = AffineTransform.getTranslateInstance(Controller.WIDTH - 90, 30);
            affTraPS.scale(0.5, 0.5);
            if (arsenal.getPrimary() != null) {
                g2d.drawImage(slotTextures.get(arsenal.getPrimary().getSERIAL() + 1), affTraPP, World.controller);
                if (arsenal.getSecondary() != null) {
                    g2d.drawImage(slotTextures.get(arsenal.getSecondary().getSERIAL() + 1), affTraPS, World.controller);
                    AffineTransform affTraI = AffineTransform.getTranslateInstance((Controller.WIDTH) - (arsenal.size() * 70), 120);
                    affTraI.scale(0.5, 0.5);
                    int j = 240;
                    for (int i = 0; i < arsenal.size(); i++) {
                        affTraI.translate(i * j, 0);
                        g2d.drawImage(slotTextures.get(arsenal.get(i).getSERIAL() + 1), affTraI, World.controller);
                        j /= 2;
                    }
                } else {
                    // The empty texture is a placeholder in case a background is made for inventory slots
                    g2d.drawImage(slotTextures.get(0), affTraPS, World.controller);
                }
            } else {
                // The empty texture is a placeholder in case a background is made for inventory slots
                g2d.drawImage(slotTextures.get(0), affTraPP, World.controller);
            }
        } else {
            // If secondary is selected, this is primary slot
            AffineTransform affTraSP = AffineTransform.getTranslateInstance(Controller.WIDTH - 210, 30);
            // If secondary is selected, this is secondary slot
            AffineTransform affTraSS = AffineTransform.getTranslateInstance(Controller.WIDTH - 120, 0);
            affTraSP.scale(0.5, 0.5);
            if (arsenal.getPrimary() != null) {
                g2d.drawImage(slotTextures.get(arsenal.getPrimary().getSERIAL() + 1), affTraSP, World.controller);
                if (arsenal.getSecondary() != null) {
                    g2d.drawImage(slotTextures.get(arsenal.getSecondary().getSERIAL() + 1), affTraSS, World.controller);
                    AffineTransform affTraI = AffineTransform.getTranslateInstance((Controller.WIDTH) - (arsenal.size() * 70), 120);
                    affTraI.scale(0.5, 0.5);
                    int j = 240;
                    for (int i = 0; i < arsenal.size(); i++) {
                        affTraI.translate(i * j, 0);
                        g2d.drawImage(slotTextures.get(arsenal.get(i).getSERIAL() + 1), affTraI, World.controller);
                        j /= 2;
                    }
                } else {
                    // The empty texture is a placeholder in case a background is made for inventory slots
                    g2d.drawImage(slotTextures.get(0), affTraSS, World.controller);
                }
            } else {
                // The empty texture is a placeholder in case a background is made for inventory slots
                g2d.drawImage(slotTextures.get(0), affTraSP, World.controller);
            }
        }
    }

    public boolean isUp() {
        return up;
    }

    public void setUp(boolean up) {
        this.up = up;
    }

    public boolean isDown() {
        return down;
    }

    public void setDown(boolean down) {
        this.down = down;
    }

    public boolean isRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public boolean isLeft() {
        return left;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public boolean isShiftIsHeld() {
        return shiftIsHeld;
    }

    public void setShiftIsHeld(boolean shiftIsHeld) {
        this.shiftIsHeld = shiftIsHeld;
    }

    public boolean isSpaceIsHeld() {
        return spaceIsHeld;
    }

    public void setSpaceIsHeld(boolean spaceIsHeld) {
        this.spaceIsHeld = spaceIsHeld;
    }

    public boolean isCtrlIsHeld() {
        return ctrlIsHeld;
    }

    public void setCtrlIsHeld(boolean ctrlIsHeld) {
        this.ctrlIsHeld = ctrlIsHeld;
    }

    public boolean istIsHeld() {
        return tIsHeld;
    }

    public void settIsHeld(boolean tIsHeld) {
        this.tIsHeld = tIsHeld;
    }

    public boolean isMouseInside() {
        return mouseInside;
    }

    public void setMouseInside(boolean mouseInside) {
        this.mouseInside = mouseInside;
    }

    public boolean isButton1Held() {
        return button1Held;
    }

    public void setButton1Held(boolean button1Held) {
        this.button1Held = button1Held;
    }

    @Override
    public void loadImageStrips() {
        super.loadImageStrips();

        ArrayList<String> imgLocStr = new ArrayList<>();

        for (int i = -1; i <= 4; i++) {
            imgLocStr.add("arsenal (" + i + ").png");
        }
        slotTextures = new ArrayList<>();
        // Load weapon textures
        for (int i = 0; i < imgLocStr.size(); i++) {
            try {
                slotTextures.add(ImageIO.read(getClass().getResource("/resources/GUI/arsenal_slot/" + imgLocStr.get(i))));
            } catch (IOException exc) {
                System.out.println("Could not find image file: " + exc.getMessage());
            }
        }
        imageLoaded = true;
    }
}
