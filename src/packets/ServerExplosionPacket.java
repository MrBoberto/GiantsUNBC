package packets;

/**
 * This file is part of a solution to
 *		CPSC300 Term Project Fall 2021
 *
 * A packet that informs the client of the most recent explosion
 *
 * @author The Boyz
 * @version 1
 */

import java.io.Serializable;

public record ServerExplosionPacket(double x, double y, int playerNumber) implements Serializable {}
