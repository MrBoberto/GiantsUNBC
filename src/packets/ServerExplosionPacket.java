package packets;

import java.io.Serializable;

public record ServerExplosionPacket(double x, double y, int playerNumber) implements Serializable {
    public ServerExplosionPacket {
        System.out.println("Client Explosion Packet created.");
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
