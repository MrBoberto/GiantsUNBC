package packets;

import weapons.ammo.Projectile;
import weapons.guns.Weapon;

import java.io.Serializable;

public class ClientBulletPacket implements Serializable {
    double playerX;
    double playerY;
    double mouseXLocation;
    double mouseYLocation;
    Projectile.Type type;

    public ClientBulletPacket(double playerX, double playerY, double mouseXLocation, double mouseYLocation, Projectile.Type type) {
        this.playerX = playerX;
        this.playerY = playerY;
        this.mouseXLocation = mouseXLocation;
        this.mouseYLocation = mouseYLocation;
        this.type = type;
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

    public Projectile.Type getType() {
        return type;
    }
}
