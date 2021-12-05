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
import mapObjects.Block;
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

    @Override
    public void tick() {
        boxCooldown--;
        if (texture != null && getSERIAL() != 01 && getSERIAL() != 04) {
            boundRect = new Rectangle((int) x - texture.getWidth() / 2,
                    (int) y - texture.getHeight() / 2, texture.getWidth(),
                    texture.getHeight());
        } else if (texture != null) {
            boundRect = new Rectangle((int) x - texture.getWidth() / 4,
                    (int) y - texture.getHeight() / 4, texture.getWidth() / 2,
                    texture.getHeight() / 2);
        }

            boolean bounced = checkBlockCollision();
        if(bounced){
            x -= getVelX();
            y -= getVelY();
        }


        if (x == 0 || y == 0) {
            return;
        }

        if (World.pythHyp(getVelX(), getVelY()) >= Controller.FRICTION) {
            double newSpeed = World.pythHyp(getVelX(), getVelY()) - Controller.FRICTION;

            if (angle >= Math.PI / 2 || (angle < 0 && angle >= -Math.PI / 2)) {
//            System.out.print(", Negative, speed = " + weapon.getMOMENTUM() / MASS);
                setVelX(World.cosAdj(newSpeed, angle));
                setVelY(World.sinOpp(newSpeed, angle));
            } else {
//            System.out.print(", Positive, speed = " + weapon.getMOMENTUM() / MASS);
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
                        /////////////////
                        //Check upperBounds
                        int offset = 5;
                        up = (new Rectangle(xx, yy-offset, 1, 1)).intersects(block.getBounds())
                                && (angle > 1/2.0 * PI|| angle < -1/2.0 * PI) && getVelY()<0;
                        down = ((new Rectangle(xx, yy+offset, 1, 1)).intersects(block.getBounds())
                                && (angle < 1/2.0 * PI|| angle > -1/2.0 * PI) && getVelY()>0);
                        right =((new Rectangle(xx+offset, yy, 1, 1)).intersects(block.getBounds())
                                && (angle < PI || angle > -0 * PI) && getVelX()>0);
                        left  = ((new Rectangle(xx-offset, yy, 1, 1)).intersects(block.getBounds())
                                && (angle > -1 * PI|| angle < 0 * PI) && getVelX()<0);

                        if(up && !down && !right && !left){
                            angle = Math.PI - angle;
                            bouncesLeft--;
                            return true;
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
//                        if ((new Rectangle(xx, yy, 2, 2)).intersects(block.getBounds()) && i != boxCooldown){
//                            boxCooldown = i;
                            //Check next left pixel
//                            double distanceFromPlayerLeft = pow(xx - 10 - playerX, 2) + pow(yy - playerY, 2);
//                            if((new Rectangle(xx - 10, yy, 1, 1)).intersects(block.getBounds())
//                                    && (left==0 || left > sqrt(distanceFromPlayerLeft))){
//                                left = sqrt(distanceFromPlayerLeft);
//                            }
//
//                            double distanceFromPlayerRight = pow(xx + 10 - playerX, 2) + pow(yy - playerY, 2);
//                            if((new Rectangle(xx + 10, yy, 1, 1)).intersects(block.getBounds())
//                                    && (right==0 || right >  sqrt(distanceFromPlayerRight))){
//                                right =  sqrt(distanceFromPlayerRight);
//                            }
//
//                            double distanceFromPlayerUp = pow(xx - playerX, 2) + pow(yy - playerY - 10, 2);
//                            if((new Rectangle(xx, yy -10, 1, 1)).intersects(block.getBounds())
//                                    && (up==0 || up > sqrt(distanceFromPlayerUp))){
//                                up = sqrt(distanceFromPlayerUp);
//                            }
//
//                            double distanceFromPlayerDown = pow(xx - playerX, 2) + pow(yy - playerY + 10, 2);
//                            if((new Rectangle(xx, yy +10, 1, 1)).intersects(block.getBounds())
//                                    && (down==0 || down >  sqrt(distanceFromPlayerDown))){
//                                down = sqrt(distanceFromPlayerDown);
//                            }

//                            double distanceFromPlayerLeft = pow(xx - 2 - playerX, 2) + pow(yy - playerY, 2);
//                            if( (left==0 || left > sqrt(distanceFromPlayerLeft))){
//                                left = sqrt(distanceFromPlayerLeft);
//                            }
//
//                            double distanceFromPlayerRight = pow(xx + 2 - playerX, 2) + pow(yy - playerY, 2);
//                            if(( (right==0 || right >  sqrt(distanceFromPlayerRight)))){
//                                right =  sqrt(distanceFromPlayerRight);
//                            }
//
//                            double distanceFromPlayerUp = pow(xx - playerX, 2) + pow(yy - playerY - 2, 2);
//                            if( (up==0 || up > sqrt(distanceFromPlayerUp))){
//                                up = sqrt(distanceFromPlayerUp);
//                            }
//
//                            double distanceFromPlayerDown = pow(xx - playerX, 2) + pow(yy - playerY + 2, 2);
//                            if((down==0 || down >  sqrt(distanceFromPlayerDown))){
//                                down = sqrt(distanceFromPlayerDown);
//                            }
   //                     }
                        //////////////////
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

//            double w = 0.5 * (getBounds().getWidth() + block.getBounds().getWidth());
//            double h = 0.5 * (getBounds().getHeight() + block.getBounds().getHeight());
//            double dx = x - block.getX() + block.getBounds().getWidth()/2;
//            double dy = y - block.getY() + block.getBounds().getHeight()/2;
//
//            if (abs(dx) <= w && abs(dy) <= h)
//            {
//                /* collision! */
//                double wy = w * dy;
//                double hx = h * dx;
//
//                if (wy > hx) {
//                    if (wy > -hx) {
//                        /* collision at the top */
//                    angle = Math.PI - angle;
//                    bouncesLeft--;
//                    }else {
//                        /* on the left */
//                        angle = - angle;
//                        bouncesLeft--;
//                    }
//                }else{
//                if (wy > -hx) {
//                    /* on the right */
//                    angle = - angle;
//                    bouncesLeft--;
//                }else{
//                        /* at the bottom */
//                    angle = Math.PI - angle;
//                    bouncesLeft--;
//                    }}
//            int xx = (int) x;
//            int yy = (int) y;
//            int left = -1;
//            int right = -1;
//            int up = -1;
//            int down = -1;
//
//            int x1 = 0;
//            int y1 = 0;
//            int x2 = 0;
//            int y2 = 0;

//
//            out: while (xx != (int) (x + getVelX())) {
//                while (yy != (int) (y + getVelY())) {
//                    if (bouncesLeft > 0 && texture != null) {
//                        if ((new Rectangle(
//                                xx,
//                                yy - 2,
//                                1,
//                                1
//                        )).intersects(block.getBounds())
//                                && !(new Rectangle(
//                                        xx,
//                                        yy,
//                                        1,
//                                        1
//                                )).intersects(block.getBounds()) && getVelY() < 0) {
//
//                            if ((up == -1) || up < xx * yy) {
//                                up = xx * yy ;
//                                x1 = xx;
//
//                            }
//
//                        }
//                        if ((new Rectangle(
//                                xx,
//                                yy + 1,
//                                1,
//                                1
//                        )).intersects(block.getBounds())&& !(new Rectangle(
//                                xx,
//                                yy,
//                                1,
//                                1
//                        )).intersects(block.getBounds()) && getVelY() > 0) {
//
//
//                            if ((down == -1) || down > xx * yy) {
//                                down = xx * yy  ;
//                                x2 = xx;
//
//                            }
//                        }
//
//
//                    }
//                    if (yy < (int) (y + getVelY())) {
//                        yy++;
//                    } else {
//                        yy--;
//                    }
//                }
//                if (xx < (int) (x + getVelX())) {
//                    xx++;
//                } else {
//                    xx--;
//                }
//            }

//             while (yy != (int) (y + getVelY())) {
//                while (xx != (int) (x + getVelX())) {
//                    if (bouncesLeft > 0 && texture != null) {
//                        if ((new Rectangle(
//                                xx -2,
//                                yy,
//                                2,
//                                2
//                        )).intersects(block.getBounds()) && getVelX() < 0) {
//
//                            System.out.println(left);
//                            if ((left == -1) || left < xx * yy) {
//                                y1 = yy ;
//
//                                left = xx * yy ;
//                            }
//
//                        }
//                        if ((new Rectangle(
//                                xx+1,
//                                yy,
//                                1,
//                                1
//                        )).intersects(block.getBounds())&& !(new Rectangle(
//                                xx,
//                                yy,
//                                1,
//                                1
//                        )).intersects(block.getBounds()) && getVelX() > 0) {
//
//
//                            if ((right == -1) || right > xx * yy) {
//                                y2 = yy  ;
//                                right = xx * yy;
//
//                            }
//                        }
//
//
//                    }
//
//                    if (xx < (int) (x + getVelX())) {
//                        xx++;
//                    } else {
//                        xx--;
//                    }
//                }
//                 if (yy < (int) (y + getVelY())) {
//                     yy++;
//                 } else {
//                     yy--;
//                 }
//            }
//
//            if (!(right == -1 && left == -1 && up == -1 && down == -1)) {
//
//                if ( (Math.abs(up)  < Math.abs(left) || left == -1) && ((Math.abs(up) < Math.abs(right)  || right == -1) && up !=-1) && getVelY()<0) {
//                    angle = Math.PI - angle;
//                    bouncesLeft--;
//
//
//                } else if (((  (Math.abs(down) < Math.abs(left) || left == -1) && (Math.abs(down) < Math.abs(right)  || right == -1)&& down !=-1)) && getVelY()>0){
//                    angle = Math.PI - angle;
//                    bouncesLeft--;
//
//                }
//                else if (((Math.abs(left) < Math.abs(up) || up == -1) && (Math.abs(left) < Math.abs(down) || down == -1)&& left !=-1) && getVelX()<0) {
//                    angle = -angle;
//                    bouncesLeft--;
//
//                }else if ( ( (Math.abs(right)  < Math.abs(up) || up == -1) && (Math.abs(right) < Math.abs(down) || down == -1)&& right !=-1) && getVelX()>0) {
//                    angle = -angle;
//                    bouncesLeft--;
//
//                }
//                System.out.println(up + " " + down + " " + left + " "+ right);
              //  bounced = true;
//            }

        for(Block block: Controller.blocks){
            if (getBounds() != null && getBounds().intersects(block.getBounds()) && bouncesLeft <= 0) {

                setVelX(0);
                setVelY(0);
                return false;
            }
        }
        return false;
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
