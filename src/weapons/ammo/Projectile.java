package weapons.ammo;

import java.io.Serializable;

public interface Projectile extends Serializable{
     int getSERIAL();
     int getDamage();
     enum ProjectileType {ShotgunBullet, SniperRifleBullet, PistolBullet, AssaultRifleBullet, RocketLauncherBullet}
}
