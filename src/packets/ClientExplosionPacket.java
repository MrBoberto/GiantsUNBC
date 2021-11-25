package packets;

import weapons.ammo.Projectile;

import java.io.Serializable;

public class ClientExplosionPacket implements Serializable {
    double x;
    double y;
    int playerNumber;
    int damage;

    public ClientExplosionPacket(double x, double y, int playerNumber, int damage) {
        System.out.println("Client Explosion Packet created.");
        this.x = x;
        this.y = y;
        this.playerNumber = playerNumber;
        this.damage = damage;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public int getPlayerNumber() {
        return playerNumber;
    }

    public int getDamage() {
        return damage;
    }
}
