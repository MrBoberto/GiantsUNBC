package packets;

/**
 * This file is part of a solution to
 *		CPSC300 Term Project Fall 2021
 *
 * A packet that informs the server of the client's name and that they are available to start a game
 *
 * @author The Boyz
 * @version 1
 */

import java.io.Serializable;

public record StartRequest(String clientName) implements Serializable {

    public String getClientName() {
        return clientName;
    }
}
