package packets;

import weapons.ammo.Projectile;

import java.io.Serializable;

public class ServerExplosionPacket implements Serializable {
    double x;
    double y;
    int playerNumber;

    public ServerExplosionPacket(double x, double y, int playerNumber) {
        System.out.println("Client Explosion Packet created.");
        this.x = x;
        this.y = y;
        this.playerNumber = playerNumber;
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
}
