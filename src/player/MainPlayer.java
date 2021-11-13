package player;

import game.World;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class MainPlayer extends Player {

    public MainPlayer(double x, double y, double angle) {
        super(x, y, angle);
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
        if (key == KeyEvent.VK_1 || key == KeyEvent.VK_NUMPAD1) {
            if (selectedWeapon == 0) {
                weapons.setPrimary(1);
            }
            else {
                weapons.setSecondary(1);
            }
            System.out.println(weapons);
        }
        if (key == KeyEvent.VK_2 || key == KeyEvent.VK_NUMPAD2) {
            if (selectedWeapon == 0) {
                weapons.setPrimary(2);
            }
            else {
                weapons.setSecondary(2);
            }
            System.out.println(weapons);
        }
        if (key == KeyEvent.VK_3 || key == KeyEvent.VK_NUMPAD3) {
            if (selectedWeapon == 0) {
                weapons.setPrimary(3);
            }
            else {
                weapons.setSecondary(3);
            }
            System.out.println(weapons);
        }
        if (key == KeyEvent.VK_4 || key == KeyEvent.VK_NUMPAD4) {
            if (selectedWeapon == 0) {
                weapons.setPrimary(4);
            }
            else {
                weapons.setSecondary(4);
            }
            System.out.println(weapons);
        }
        if (key == KeyEvent.VK_5 || key == KeyEvent.VK_NUMPAD5) {
            if (selectedWeapon == 0) {
                weapons.setPrimary(5);
            }
            else {
                weapons.setSecondary(5);
            }
            System.out.println(weapons);
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
//        System.out.println("player.Player 1 Prev super.getAngle(): " + Math.toDegrees(super.getAngle()));
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
 //       pos.setLocation(super.getX(), super.getY());
    }

    /**
     * Translates the player using move(), applies friction, prevents the player from leaving the window,
     * updates the player's hitbox, and shoots weapons
     * @param mouseLoc
     */
    public void tick(Point mouseLoc) {
        move();

        // Apply vertical friction
        if (velY > World.controller.FRICTION) {
            velY -= World.controller.FRICTION;
        } else if (velY < -World.controller.FRICTION) {
            velY += World.controller.FRICTION;
        } else if (velY != 0) {
            velY = 0;
        }
        // Apply horizontal friction
        if (velX > World.controller.FRICTION) {
            velX -= World.controller.FRICTION;
        } else if (velX < -World.controller.FRICTION) {
            velX += World.controller.FRICTION;
        } else if (velX != 0) {
            velX = 0;
        }

        //images
        if (super.getX() <= currentImage.getImage().getWidth() / 2) {
            super.setX(currentImage.getImage().getWidth() / 2);
        } else if (super.getX() >= World.controller.WIDTH - currentImage.getImage().getWidth() / 2) {
            super.setX(World.controller.WIDTH - currentImage.getImage().getWidth() / 2);
        }

        if (super.getY() <= currentImage.getImage().getHeight() / 2) {
            super.setY(currentImage.getImage().getHeight() / 2);
            velY = 1;
        } else if (super.getY() >= World.controller.HEIGHT - currentImage.getImage().getHeight() / 2) {
            super.setY(World.controller.HEIGHT - currentImage.getImage().getHeight() / 2);
            velY = 0;
            isFalling = false;
        }
        boundRect = new Rectangle((int)x - currentImage.getImage().getWidth() / 2,
                (int)y - currentImage.getImage().getHeight() / 2, currentImage.getImage().getWidth(),
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
}
