package packets;

/**
 * This file is part of a solution to
 *		CPSC300 Term Project Fall 2021
 *
 * A packet that informs the client of who won and of the game statistics
 *
 * @author The Boyz
 * @version 1
 */

import java.io.Serializable;

public record WinnerPacket(int winner, double[][] playerInfo) implements Serializable {

    public int getWinner() {
        return winner;
    }

    public double[][] getPlayerInfo() {
        return playerInfo;
    }
}
