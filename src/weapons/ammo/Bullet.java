package weapons.ammo;

import player.Player;

import java.io.Serializable;

public abstract class Bullet implements Projectile {
    // The precise position of the object, for use with physics
    private double x, y;
    protected int ID;
    private static int nextID = 0;
    Player playerIBelongTo;
    Type TYPE = null;


    protected Bullet(double x, double y, Player player) {
        this.x = x;
        this.y = y;
        ID = nextID++;
        playerIBelongTo = player;
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

    public Player getPlayerIBelongTo() {
        return playerIBelongTo;
    }
}
