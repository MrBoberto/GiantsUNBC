package packets;

import java.io.Serializable;

public class DeathCountPacket implements Serializable {
    int serverDeaths;
    int clientDeaths;

    public DeathCountPacket(int serverDeaths, int clientDeaths) {
        this.serverDeaths = serverDeaths;
        this.clientDeaths = clientDeaths;
    }

    public int getServerDeaths() {
        return serverDeaths;
    }

    public int getClientDeaths() {
        return clientDeaths;
    }
}
