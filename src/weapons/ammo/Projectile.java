package weapons.ammo;

/**
 * This file is part of a solution to
 *		CPSC300 Term Project Fall 2021
 *
 * An interface of a projectile that can inflict damage upon a player
 *
 * @author The Boyz
 * @version 1
 */

import java.io.Serializable;

public interface Projectile extends Serializable{
     int getSERIAL();
     int getDamage();
     enum ProjectileType {ShotgunBullet, SniperRifleBullet, PistolBullet, AssaultRifleBullet, RocketLauncherBullet}
}
