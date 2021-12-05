package packets;

/**
 * This file is part of a solution to
 *		CPSC300 Term Project Fall 2021
 *
 * A packet that informs the server to play a sound effect based upon the int passed from the client
 *
 * @author The Boyz
 * @version 1
 */

import java.io.Serializable;

public record ClientSFXPacket(int clientSFXInt) implements Serializable {}
