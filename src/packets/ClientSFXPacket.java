package packets;

import java.io.Serializable;

public record ClientSFXPacket(int clientSFXInt) implements Serializable {

    public int getClientSFXInt() {
        return clientSFXInt;
    }
}
