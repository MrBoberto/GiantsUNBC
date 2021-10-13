package packets;

import player.Player;

import java.io.Serializable;
import java.util.ArrayList;

public class ClientStartPacket implements Serializable {

    int playerNumber;
    private double x;
    private double y;
    private double angle;

    public ClientStartPacket(int playerNumber, double x, double y, double angle) {
        this.playerNumber = playerNumber;
        this.x = x;
        this.y = y;
        this.angle = angle;
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

    public double getAngle() {
        return angle;
    }
}
