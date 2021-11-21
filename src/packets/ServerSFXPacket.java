package packets;

public class ServerSFXPacket {
    String serverSFXLocation;

    public ServerSFXPacket(String serverSFXLocation) {
        this.serverSFXLocation = serverSFXLocation;
    }

    public String getServerSFXLocation() {
        return serverSFXLocation;
    }
}
