package packets;

import player.Player;

import java.io.Serializable;

public class StartRequest implements Serializable {

    private String clientName;

    public StartRequest(String clientName) {
        this.clientName = clientName;
    }

    public String getClientName() {
        return clientName;
    }
}
