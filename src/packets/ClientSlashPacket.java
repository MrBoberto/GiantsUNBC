package packets;

/**
 * This file is part of a solution to
 *		CPSC300 Term Project Fall 2021
 *
 * A packet that informs the server of a slash performed by the client
 *
 * @author The Boyz
 * @version 1
 */

import java.io.Serializable;

public record ClientSlashPacket(double x, double y, double angle, boolean isLeft, int damage) implements Serializable {}
