package packets;

/**
 * This file is part of a solution to
 *		CPSC300 Term Project Fall 2021
 *
 * A packet that informs a player of the current shadows in the game
 *
 * @author The Boyz
 * @version 1
 */

import game.GameObject;

import java.io.Serializable;

public record EyeCandyPacket(GameObject[] eyeCandy) implements Serializable {}
