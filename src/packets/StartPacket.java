package packets;

import java.io.Serializable;

public class StartPacket implements Serializable {

    private double x;
    private double y;
    private double angle;
    private String playerName;
    private int mapSelected;

    public StartPacket(double x, double y, double angle, String playerName, int mapSelected) {
        this.x = x;
        this.y = y;
        this.angle = angle;
        this.playerName = playerName;
        this.mapSelected = mapSelected;
    }

    public double getX() {
        return x;
    }

    public int getMapSelected() {
        return mapSelected;
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
