package packets;

import java.io.Serializable;

public record StartPacket(double x, double y, double angle, String playerName,
                          int mapSelected) implements Serializable {

    public double getX() {
        return x;
    }

    public int getMapSelected() {
        return mapSelected;
    }

    public double getY() {
        return y;
    }

    public String getPlayerName() {
        return playerName;
    }
}
