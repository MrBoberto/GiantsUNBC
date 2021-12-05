package packets;

/**
 * This file is part of a solution to
 *		CPSC300 Term Project Fall 2021
 *
 * A packet that informs the client of the location for their player to spawn, the selected map, the server player's
 * name, the angle in which they should face, and the acceptance of their StartRequest
 *
 * @author The Boyz
 * @version 1
 */

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
