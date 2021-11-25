package weapons.ammo;

import game.Controller;
import game.GameObject;
import game.World;
import mapObjects.Block;
import player.MainPlayer;

import java.awt.*;

public abstract class Bullet extends GameObject implements Projectile {
    // The precise position of the object, for use with physics
    protected int ID;
    private static int nextID = 0;
    protected int playerIBelongToNumber;
    protected ProjectileType ProjectileTYPE = null;
    protected int damage;
    Rectangle boundRect;


    protected Bullet(double x, double y, double angle) {
        super(x, y, angle);
        ID = nextID++;
        Controller.movingAmmo.add(this);
    }

    @Override
    public void tick() {
        x += getVelX();
        y += getVelY();
        if(x == 0 || y == 0){
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

        if(texture != null && getSERIAL() != 01 && getSERIAL() != 04){
            boundRect = new Rectangle((int)x - texture.getWidth() / 2,
                    (int)y- texture.getHeight() / 2, texture.getWidth(),
                    texture.getHeight());
        } else if (texture != null) {
            boundRect = new Rectangle((int)x - texture.getWidth() / 4,
                    (int)y- texture.getHeight() / 4, texture.getWidth() / 2,
                    texture.getHeight() / 2);
        }

        checkBlockCollision();
    }

    protected void checkBlockCollision(){
        for (int i = 0; i < Controller.blocks.size(); i++) {
            Block block = Controller.blocks.get(i);

            if(getBounds() != null && getBounds().intersects(block.getBounds())){
                setVelX(0);
                setVelY(0);
            }
        }
    }


    public int getID(){
        return ID;
    }

    public boolean hasStopped() {
        return (getVelX() == 0 && getVelY() == 0);
    }

    public int getPlayerIBelongToNumber() {
        return playerIBelongToNumber;
    }

    public ProjectileType getTYPE() {
        return ProjectileTYPE;
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
