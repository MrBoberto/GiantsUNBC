package GiantsSidescroller.src;

public class Ammo {
    // The precise position of the object, for use with physics
    private double x, y;
    private Weapon weapon;

    /**
     * Takes a target location and a weapon, then sets the velocity of the projectile
     * to launch toward that target location from the player
     * @param x The x position of the projectile
     * @param y The y position of the projectile
     * @param weapon The weapon from which the ammo is being fired
     */
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
