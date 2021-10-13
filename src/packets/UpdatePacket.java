package packets;

import player.Player;

import java.io.Serializable;

public class UpdatePacket implements Serializable {

    private int playerNumber;
    private double x;
    private double y;

    public UpdatePacket(int playerNumber, double x, double y) {
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
