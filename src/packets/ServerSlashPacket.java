package packets;

import weapons.ammo.Projectile;

import java.io.Serializable;

public class ServerSlashPacket implements Serializable {
    double x;
    double y;
    double angle;
    int damage;

    public ServerSlashPacket(double x, double y, double angle, int damage) {
        this.x = x;
        this.y = y;
        this.angle = angle;
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

    public int getDamage() {
        return damage;
    }
}
