package packets;

import player.Player;

import java.io.Serializable;

public class ClientStartPacketRequest implements Serializable {

    private Player player;

    public ClientStartPacketRequest(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }
}
