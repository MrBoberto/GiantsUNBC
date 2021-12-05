package packets;

/**
 * This file is part of a solution to
 *		CPSC300 Term Project Fall 2021
 *
 * A packet that informs the server of the most recent live bullet
 *
 * @author The Boyz
 * @version 1
 */

import weapons.ammo.Projectile;

import java.io.Serializable;

public record ClientBulletPacket(double playerX, double playerY, double mouseXLocation, double mouseYLocation,
                                 Projectile.ProjectileType projectileType,
                                 int damage) implements Serializable {

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
