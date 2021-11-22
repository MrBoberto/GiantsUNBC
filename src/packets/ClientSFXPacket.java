package packets;

import java.io.Serializable;

public class ClientSFXPacket implements Serializable {
    int clientSFXInt;

    public ClientSFXPacket(int clientSFXInt) {
        this.clientSFXInt = clientSFXInt;
    }

    public int getClientSFXInt() {
        return clientSFXInt;
    }
}
