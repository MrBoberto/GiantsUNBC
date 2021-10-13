package packets;

import player.Player;

import java.io.Serializable;
import java.util.ArrayList;

public class ClientStartPacket implements Serializable {

    ArrayList<Player> livingPlayers;
    int playerNumber;

    public ClientStartPacket(ArrayList<Player> livingPlayers, int playerNumber) {
        this.livingPlayers = livingPlayers;
        this.playerNumber = playerNumber;
    }

    public ArrayList<Player> getLivingPlayers() {
        return livingPlayers;
    }

    public int getPlayerNumber() {
        return playerNumber;
    }
}
