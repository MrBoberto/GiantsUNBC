package packets;

import java.io.Serializable;

public class ServerSFXPacket implements Serializable {
    int serverSFXInt;

    public ServerSFXPacket(int serverSFXInt) {
        this.serverSFXInt = serverSFXInt;
    }

    public int getServerSFXInt() {
        return serverSFXInt;
    }
}
