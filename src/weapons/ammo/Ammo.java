package weapons.ammo;

import weapons.Weapon;

public class Ammo {
    // The precise position of the object, for use with physics
    private double x, y;
    private final int PLAYERNUMBER;

    public Ammo(double x, double y, int playerNumber) {
        this.x = x;
        this.y = y;
        this.PLAYERNUMBER = playerNumber;
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

    public int getPlayerNumber() {
        return PLAYERNUMBER;
    }
}
