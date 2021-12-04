package packets;

import java.io.Serializable;

public record StartRequest(String clientName) implements Serializable {

    public String getClientName() {
        return clientName;
    }
}
