package packets;

import weapons.ammo.Projectile;

import java.io.Serializable;

public class ClientBulletPacket implements Serializable {
    double playerX;
    double playerY;
    double mouseXLocation;
    double mouseYLocation;
    Projectile.ProjectileType projectileType;
    int damage;

    public ClientBulletPacket(double playerX, double playerY, double mouseXLocation, double mouseYLocation,
                              Projectile.ProjectileType projectileType, int damage) {
        this.playerX = playerX;
        this.playerY = playerY;
        this.mouseXLocation = mouseXLocation;
        this.mouseYLocation = mouseYLocation;
        this.projectileType = projectileType;
        this.damage = damage;
    }

    public double getPlayerX() {
        return playerX;
    }

    public double getPlayerY() {
        return playerY;
    }

    public double getMouseXLocation() {
        return mouseXLocation;
    }

    public double getMouseYLocation() {
        return mouseYLocation;
    }

    public Projectile.ProjectileType getType() {
        return projectileType;
    }

    public int getDamage() {
        return damage;
    }
}
