package player;

/**
 * This file is part of a solution to
 *		CPSC300 Term Project Fall 2021
 *
 * The player which the user can control
 *
 * @author The Boyz
 * @version 1
 */

import game.Controller;
import game.World;
import map_objects.Block;
import utilities.BufferedImageLoader;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class MainPlayer extends Player {

    //Main Player movement directions
    private boolean up = false, down = false,right = false,left = false;
    private boolean upStop = false, downStop = false,rightStop = false,leftStop = false;
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
            } else {
                super.setAngle(Math.PI);
            }
            return;
        } else if (avgY == 0) {
            if (avgX == 1) {
                super.setAngle(Math.PI / 2);
            } else {
                super.setAngle(-Math.PI / 2);
            }
            return;
        }

        double acuteAngle = Math.atan((double)avgY/avgX);

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

        applyFriction();

        keepPlayersInsideOfBounds();

        updateBounds();

        updateWeaponsDelay();
    }

    private void updateWeaponsDelay() {
        if (arsenal.getPrimary().getCurrentDelay() > 0) {
            arsenal.getPrimary().setCurrentDelay(arsenal.getPrimary().getCurrentDelay() - 1);
        }
        if (arsenal.getSecondary() != null && arsenal.getSecondary().getCurrentDelay() > 0) {
            arsenal.getSecondary().setCurrentDelay(arsenal.getSecondary().getCurrentDelay() - 1);
        }
    }

    private void updateBounds() {
        boundRect = new Rectangle((int)this.x - currentImage.getImage().getWidth() / 4,
                (int)this.y - currentImage.getImage().getHeight() / 4, currentImage.getImage().getWidth() / 2,
                currentImage.getImage().getHeight() / 2);
    }

    private void keepPlayersInsideOfBounds() {
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
        }
    }

    private void applyFriction() {
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
    }

    @Override
    public void render(Graphics g) {
        super.render(g);

        Graphics2D g2d = (Graphics2D) g;

        isWalking = (up || left || down || right);
        if(isTimeForNextFrame()){
            loadImage();
            resetAnimationTimer();
        }

        if(currentImage == null) return;

        drawImages(g2d);

        drawNameAndHealthBar(g2d);
    }

    private void drawNameAndHealthBar(Graphics2D g2d) {
        g2d.setColor(playerColour);
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

    private void drawImages(Graphics2D g2d) {
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

        if (weaponSerial >= 0 && weaponSerial < 5) {
            g2d.drawImage(weaponTextures.get(weaponSerial), affTra, World.controller);
        } else if (weaponSerial == 5) {
            g2d.drawImage(currentSwordFrame.getImage(), affTra, World.controller);
        }
    }

    public void setUp(boolean up) {
        this.up = up;
    }

    public void setDown(boolean down) {
        this.down = down;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public void setLeft(boolean left) {
        this.left = left;
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
        for (String s : imgLocStr) {
            slotTextures.add(BufferedImageLoader.loadImage("/resources/GUI/arsenal_slot/" + s));
        }
    }
}
