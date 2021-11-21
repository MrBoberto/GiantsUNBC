package packets;

public class ClientSFXPacket {
    String clientSFXLocation;

    public ClientSFXPacket(String clientSFXLocation) {
        this.clientSFXLocation = clientSFXLocation;
    }

    public String getclientSFXLocation() {
        return clientSFXLocation;
    }
}
