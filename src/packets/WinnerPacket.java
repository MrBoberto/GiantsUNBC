package packets;

import java.io.Serializable;

public record WinnerPacket(int winner, double[][] playerInfo) implements Serializable {

    public int getWinner() {
        return winner;
    }

    public double[][] getPlayerInfo() {
        return playerInfo;
    }
}
