package packets;

import java.io.Serializable;

public record DeathCountPacket(int serverDeaths, int clientDeaths) implements Serializable {

    public int getServerDeaths() {
        return serverDeaths;
    }

    public int getClientDeaths() {
        return clientDeaths;
    }
}
