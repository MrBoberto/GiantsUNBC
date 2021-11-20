package weapons.ammo;

import weapons.Weapon;

public class Ammo {
    // The precise position of the object, for use with physics
    private double x, y;
    private Weapon weapon;

    public Ammo(double x, double y, Weapon weapon) {
        this.x = x;
        this.y = y;
        this.weapon = weapon;
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

    public Weapon getWeapon() {
        return weapon;
    }

    public void setWeapon(Weapon weapon) {
        this.weapon = weapon;
    }
}
