package packets;

import java.io.Serializable;

public class StartPacket implements Serializable {

    private double x;
    private double y;
    private double angle;
    private String playerName;

    public StartPacket(double x, double y, double angle, String playerName) {
        this.x = x;
        this.y = y;
        this.angle = angle;
        this.playerName = playerName;
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

    public String getPlayerName() {
        return playerName;
    }
}
