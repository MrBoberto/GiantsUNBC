package weapons.ammo;

import player.Player;

import java.io.Serializable;

public abstract class Bullet implements Projectile {
    // The precise position of the object, for use with physics
    protected double x, y;
    protected int ID;
    private static int nextID = 0;
    int playerIBelongTo;
    Type TYPE = null;


    protected Bullet() {
        ID = nextID++;

    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public int getID(){
        return ID;
    }

    public abstract boolean hasStopped();

    public int getPlayerIBelongTo() {
        return playerIBelongTo;
    }

    public Type getTYPE() {
        return TYPE;
    }

    @Override
    public String toString() {
        return "Bullet{" +
                "x=" + x +
                ", y=" + y +
                ", ID=" + ID +
                ", playerIBelongTo=" + playerIBelongTo +
                ", TYPE=" + TYPE +
                '}';
    }
}
