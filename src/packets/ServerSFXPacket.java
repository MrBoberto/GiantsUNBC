package packets;

/**
 * This file is part of a solution to
 *		CPSC300 Term Project Fall 2021
 *
 * A packet that informs the client to play a sound effect based upon the int passed from the server
 *
 * @author The Boyz
 * @version 1
 */

import java.io.Serializable;

public record ServerSFXPacket(int serverSFXInt) implements Serializable {}
