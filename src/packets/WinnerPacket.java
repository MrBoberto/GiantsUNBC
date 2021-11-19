package packets;

import java.io.Serializable;

public class WinnerPacket implements Serializable {
    private final int winner;
    private final double[][] playerInfo;

    public WinnerPacket(int winner, double[][] playerInfo) {
        this.winner = winner;
        this.playerInfo = playerInfo;
    }

    public int getWinner() {
        return winner;
    }

    public double[][] getPlayerInfo(){
        return playerInfo;
    }
}
