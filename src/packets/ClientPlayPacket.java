package packets;

import java.io.Serializable;

public class ClientPlayPacket implements Serializable {
    private int playerNumber;
    private double x;
    private double y;

    public ClientPlayPacket(int playerNumber,double x, double y) {
        this.playerNumber = playerNumber;
        this.x = x;
        this.y = y;
    }

    public int getPlayerNumber() {
        return playerNumber;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}
