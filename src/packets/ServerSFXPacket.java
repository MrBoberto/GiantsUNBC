package packets;

import java.io.Serializable;

public record ServerSFXPacket(int serverSFXInt) implements Serializable {

    public int getServerSFXInt() {
        return serverSFXInt;
    }
}
