package packets;

/**
 * This file is part of a solution to
 *		CPSC300 Term Project Fall 2021
 *
 * A packet that informs the server of the client's position, angle they are facing, whether they are walking, and
 * the type of their current weapon
 *
 * @author The Boyz
 * @version 1
 */

import java.io.Serializable;

public record ClientUpdatePacket(double x, double y, double angle, boolean isWalking,
                                 int weaponSerial) implements Serializable {

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getAngle() {
        return angle;
    }

    public int getWeaponSerial() {
        return weaponSerial;
    }
}
