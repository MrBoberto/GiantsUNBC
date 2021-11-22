package packets;

import java.io.Serializable;

public class ClientSFXPacket implements Serializable {
    String clientSFXLocation;

    public ClientSFXPacket(String clientSFXLocation) {
        this.clientSFXLocation = clientSFXLocation;
    }

    public String getClientSFXLocation() {
        return clientSFXLocation;
    }
}
