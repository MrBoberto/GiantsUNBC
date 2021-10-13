package packets;

import java.io.Serializable;

public class ClientPlayPacket implements Serializable {
    private int playerNumber;
    private double x;
    private double y;
    private double angle;

    public ClientPlayPacket(int playerNumber,double x, double y, double angle) {
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
