package player;

import StartMenu.MainMenuTest;
import game.ClientController;
import game.Controller;
import game.ServerController;
import game.World;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;

public class MainPlayer extends Player {

    //Main Player movement directions
    private boolean up = false, down = false,right = false,left = false;

    public MainPlayer(double x, double y, double angle, Color color) {
        super(x, y, angle, color);

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
//            System.out.println("player.Player 1 Current super.getAngle(): " + super.getAngle());
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
                if (up || right || down || left) {
                    setVelocity(VELSNEAK / 2);
                } else {
                    velX = 0;
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
            velX = 0;
        }

        //Increase walking distance stat
        if(velY > 0 || velX > 0){
            incrementWalkingDistance();
        }

        // Determine distance travelled
        super.setX(super.getX() + velX);
        super.setY(super.getY() + velY);
    }

    /**
     * Translates the player using move(), applies friction, prevents the player from leaving the window,
     * updates the player's hitbox, and shoots weapons
     */
    @Override
    public void tick() {
        setAngle();
        move();

        // Apply vertical friction
        if (velY > Controller.FRICTION) {
            velY -= Controller.FRICTION;
        } else if (velY < -Controller.FRICTION) {
            velY += Controller.FRICTION;
        } else if (velY != 0) {
            velY = 0;
        }
        // Apply horizontal friction
        if (velX > Controller.FRICTION) {
            velX -= Controller.FRICTION;
        } else if (velX < -Controller.FRICTION) {
            velX += Controller.FRICTION;
        } else if (velX != 0) {
            velX = 0;
        }

        //images
        if (super.getX() <= currentImage.getImage().getWidth() / 2.0) {
            super.setX(currentImage.getImage().getWidth() / 2.0);
        } else if (super.getX() >= Controller.WIDTH - currentImage.getImage().getWidth() / 2.0) {
            super.setX(Controller.WIDTH - currentImage.getImage().getWidth() / 2.0);
        }

        if (super.getY() <= currentImage.getImage().getHeight() / 2.0) {
            super.setY(currentImage.getImage().getHeight() / 2.0);
            velY = 1;
        } else if (super.getY() >= Controller.HEIGHT - currentImage.getImage().getHeight() / 2.0) {
            super.setY(Controller.HEIGHT - currentImage.getImage().getHeight() / 2.0);
            velY = 0;
            isFalling = false;
        }

        boundRect = new Rectangle((int)this.x - currentImage.getImage().getWidth() / 4,
                (int)this.y - currentImage.getImage().getHeight() / 4, currentImage.getImage().getWidth() / 2,
                currentImage.getImage().getHeight() / 2);

        if (weapons.getPrimary().getCurrentDelay() > 0) {
            weapons.getPrimary().setCurrentDelay(weapons.getPrimary().getCurrentDelay() - 1);
        }
        if (weapons.getSecondary().getCurrentDelay() > 0) {
            weapons.getSecondary().setCurrentDelay(weapons.getSecondary().getCurrentDelay() - 1);
        }
    }

    /**
     * Gets the current frame from loadImage() and rotates it based on the player's angle
     *
     * @param g
     */
    @Override
    public void render(Graphics g) {
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
        g2d.drawImage(currentImage.getImage(), affTra, World.controller);

        // Draws the player's hitbox
        g.setColor(playerColour);
        g.drawRect((int) x - currentImage.getImage().getWidth() / 4,
                (int) y - currentImage.getImage().getHeight() / 4, currentImage.getImage().getWidth() / 2,
                currentImage.getImage().getHeight() / 2);

        Font font = new Font("Arial", Font.BOLD, 20);
        FontMetrics stringSize = g2d.getFontMetrics(font);

        g2d.fillRect((int) x - currentImage.getImage().getWidth() / 4,
                (int) y - currentImage.getImage().getHeight() / 4 - 5,
                (currentImage.getImage().getWidth() * health / 200),
                5);

        g2d.drawString(playerName, (int) x - (stringSize.stringWidth(playerName)) / 4,
                (int) y - 5 - currentImage.getImage().getHeight() / 4);
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


}
