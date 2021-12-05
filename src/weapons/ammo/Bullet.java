package weapons.ammo;

/**
 * This file is part of a solution to
 *		CPSC300 Term Project Fall 2021
 *
 * A bullet spawned from a player's gun that can inflict damage upon the opponent
 *
 * @author The Boyz
 * @version 1
 */

import game.*;
import map_objects.Block;
import player.Player;

import java.awt.*;

import static java.lang.Math.*;

public abstract class Bullet extends GameObject implements Projectile {
    // The precise position of the object, for use with physics
    protected final int ID;
    private static int nextID = 0;
    protected final int playerIBelongToNumber;
    Rectangle boundRect;

    int bouncesLeft = 0;
    int boxCooldown = 0;
    final int lastBox = 0;

    protected ProjectileType ProjectileTYPE = null;
    protected int damage;

    protected Bullet(int playerIBelongToNumber) {
        super(0, 0, 0);
        this.playerIBelongToNumber = playerIBelongToNumber;
        ID = nextID++;
        getBouncesLeft();
        Controller.movingAmmo.add(this);
    }

    @Override
    public void tick() {
        //Ricochet check
        boxCooldown--;

        updateBounds();

        boolean bounced = handleRicochetPowerUp();

        if (x == 0 || y == 0) {
            return;
        }

        handleVelocity(bounced);
    }

    private void updateBounds() {
        if (texture != null && getSERIAL() != 1 && getSERIAL() != 4) {
            boundRect = new Rectangle((int) x - texture.getWidth() / 2,
                    (int) y - texture.getHeight() / 2, texture.getWidth(),
                    texture.getHeight());
        } else if (texture != null) {
            boundRect = new Rectangle((int) x - texture.getWidth() / 4,
                    (int) y - texture.getHeight() / 4, texture.getWidth() / 2,
                    texture.getHeight() / 2);
        }
    }

    private void handleVelocity(boolean bounced) {
        if (World.pythHyp(getVelX(), getVelY()) >= Controller.FRICTION) {
            double newSpeed = World.pythHyp(getVelX(), getVelY()) - Controller.FRICTION;

            if (angle >= Math.PI / 2 || (angle < 0 && angle >= -Math.PI / 2)) {
                setVelX(World.cosAdj(newSpeed, angle));
                setVelY(World.sinOpp(newSpeed, angle));
            } else {
                setVelX(World.sinOpp(newSpeed, angle));
                setVelY(World.cosAdj(newSpeed, angle));
            }
        } else {
            setVelX(0);
            setVelY(0);
        }

        if(!bounced) {
            x += getVelX();
            y += getVelY();
        }
    }

    private boolean handleRicochetPowerUp() {
        boolean bounced = checkBlockCollision();
        if(bounced){
            x -= getVelX();
            y -= getVelY();
        }
        return bounced;
    }

    protected boolean checkBlockCollision() {
        if(texture == null ) return false;

        if(bouncesLeft > 0){

            for (int i = 0; i < Controller.blocks.size(); i++) {
                Block block = Controller.blocks.get(i);

                if(boxCooldown > 0 && lastBox == i){
                    continue;
                }

                boolean up, down, left, right;
                int xx = (int) x;
                int yy = (int) y;
                while (xx != (int) (x + getVelX())) {
                    while (yy != (int) (y + getVelY())) {
                        int offset = 5;
                        up = (new Rectangle(xx, yy-offset, 1, 1)).intersects(block.getBounds())
                                && (angle > 1/2.0 * PI|| angle < -1/2.0 * PI) && getVelY()<0;
                        down = ((new Rectangle(xx, yy+offset, 1, 1)).intersects(block.getBounds())
                                && (angle < 1/2.0 * PI|| angle > -1/2.0 * PI) && getVelY()>0);
                        right =((new Rectangle(xx+offset, yy, 1, 1)).intersects(block.getBounds())
                                && (angle < PI || angle > -0 * PI) && getVelX()>0);
                        left  = ((new Rectangle(xx-offset, yy, 1, 1)).intersects(block.getBounds())
                                && (angle > -1 * PI|| angle < 0 * PI) && getVelX()<0);

                        //up
                        if(up && !down && !right && !left){
                            angle = Math.PI - angle;
                            bouncesLeft--;
                            return true;
                            //down
                        } else if(down && !right && !left){
                            angle = Math.PI - angle;
                            bouncesLeft--;
                            return true;
                            //right
                        }else if(right && !left){
                            angle =  - angle;
                            bouncesLeft--;
                            return true;
                            //left
                        }else if(left){
                            angle = - angle;
                            bouncesLeft--;
                            return true;
                        }
                        if (yy < (int) (y + getVelY())) {
                            yy++;
                        } else {
                            yy--;
                        }
                    }
                    if (xx < (int) (x + getVelX())) {
                        xx++;
                    } else {
                        xx--;
                    }
                }


            }
        }

        for(Block block: Controller.blocks){
            if (getBounds() != null && getBounds().intersects(block.getBounds()) && bouncesLeft <= 0) {

                setVelX(0);
                setVelY(0);
                return false;
            }
        }
        return false;
    }

    private void getBouncesLeft() {
        if ((((playerIBelongToNumber == Player.SERVER_PLAYER && World.controller instanceof ServerController)
                || (playerIBelongToNumber == Player.SERVER_PLAYER && World.controller instanceof SingleController)
                || (playerIBelongToNumber == Player.CLIENT_PLAYER && World.controller instanceof ClientController))
                && Controller.thisPlayer.isRicochetEnabled())){
            bouncesLeft = Controller.thisPlayer.getNumberOfBulletBounces();

        } else if (((playerIBelongToNumber == Player.CLIENT_PLAYER && World.controller instanceof ServerController)
                || (playerIBelongToNumber == Player.CLIENT_PLAYER && World.controller instanceof SingleController)
                || (playerIBelongToNumber == Player.SERVER_PLAYER && World.controller instanceof ClientController))
                && Controller.otherPlayer.isRicochetEnabled()) {
            bouncesLeft = Controller.otherPlayer.getNumberOfBulletBounces();
        }
    }


    public boolean hasStopped() {
        return (getVelX() == 0 && getVelY() == 0);
    }

    public int getPlayerIBelongToNumber() {
        return playerIBelongToNumber;
    }

    @Override
    public String toString() {
        return "Bullet{" +
                "x=" + x +
                ", y=" + y +
                ", ID=" + ID +
                ", playerIBelongTo=" + playerIBelongToNumber +
                ", TYPE=" + ProjectileTYPE +
                '}';
    }

    public int getDamage() {
        return damage;
    }

    @Override
    public Rectangle getBounds() {
        return boundRect;
    }
}
