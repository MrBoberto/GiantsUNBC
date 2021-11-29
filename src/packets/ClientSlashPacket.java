package packets;

import weapons.ammo.Projectile;

import java.io.Serializable;

public class ClientSlashPacket implements Serializable {
    double x;
    double y;
    double angle;
    boolean isLeft;
    int damage;

    public ClientSlashPacket(double x, double y, double angle, boolean isLeft, int damage) {
        this.x = x;
        this.y = y;
        this.angle = angle;
        this.isLeft = isLeft;
        this.damage = damage;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getAngle() {
        return angle;
    }

    public boolean isLeft() {
        return isLeft;
    }

    public int getDamage() {
        return damage;
    }
}
