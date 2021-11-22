package packets;

import java.io.Serializable;

public class ServerSFXPacket implements Serializable {
    String serverSFXLocation;

    public ServerSFXPacket(String serverSFXLocation) {
        this.serverSFXLocation = serverSFXLocation;
    }

    public String getServerSFXLocation() {
        return serverSFXLocation;
    }
}
