package weapons.ammo;

import game.Controller;
import game.GameObject;
import player.MainPlayer;

import java.awt.*;

public abstract class Bullet extends GameObject implements Projectile {
    // The precise position of the object, for use with physics
    protected int ID;
    private static int nextID = 0;
    protected int playerIBelongToNumber;
    protected ProjectileType ProjectileTYPE = null;
    protected int damage;


    protected Bullet(double x, double y, double angle) {
        super(x, y, angle);
        ID = nextID++;
        Controller.movingAmmo.add(this);
    }

    @Override
    public void tick() {
        x += velX;
        y += velY;
        if(x == 0 || y == 0){
            return;
        }
        // pos.setLocation(super.getX(), super.getY());

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
    }


    public int getID(){
        return ID;
    }

    public boolean hasStopped() {
        return (velX == 0 && velY == 0);
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
}
