package packets;

/**
 * This file is part of a solution to
 *		CPSC300 Term Project Fall 2021
 *
 * A packet that informs the client of all current live ammunition
 *
 * @author The Boyz
 * @version 1
 */

import weapons.ammo.Bullet;

import java.io.Serializable;

public record ServerBulletPacket(Bullet[] bullets) implements Serializable {

    public Bullet[] getAmmo() {
        return bullets;
    }
}
