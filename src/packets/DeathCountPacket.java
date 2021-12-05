package packets;

/**
 * This file is part of a solution to
 *		CPSC300 Term Project Fall 2021
 *
 * A packet that informs a player of how many deaths have occurred
 *
 * @author The Boyz
 * @version 1
 */

import java.io.Serializable;

public record DeathCountPacket(int serverDeaths, int clientDeaths) implements Serializable {}
