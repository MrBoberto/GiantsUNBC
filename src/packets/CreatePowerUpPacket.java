package packets;

/**
 * This file is part of a solution to
 *		CPSC300 Term Project Fall 2021
 *
 * A packet that informs a player of the creation of a PowerUp
 *
 * @author The Boyz
 * @version 1
 */

import power_ups.PowerUp;

import java.io.Serializable;

public record CreatePowerUpPacket(int x, int y, PowerUp.PowerUpType powerUpType) implements Serializable {

    public PowerUp.PowerUpType getPowerUpType() {
        return powerUpType;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
