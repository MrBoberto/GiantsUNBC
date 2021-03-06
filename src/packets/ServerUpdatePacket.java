package packets;

/**
 * This file is part of a solution to
 *		CPSC300 Term Project Fall 2021
 *
 * A packet that informs the client of the server's position, angle they are facing, whether they are walking, and
 * the type of their current weapon
 *
 * @author The Boyz
 * @version 1
 */

import java.io.Serializable;

public record ServerUpdatePacket(double x, double y, double angle, int[] health, boolean isWalking, int weaponSerial,
                                 boolean[] isInvincible) implements Serializable {}
