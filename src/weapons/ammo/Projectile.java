package weapons.ammo;

import weapons.guns.Weapon;

import java.awt.*;
import java.awt.image.ImageObserver;
import java.io.Serializable;

public interface Projectile extends Serializable{
     int getSERIAL();
     int getDamage();
     enum ProjectileType {ShotgunBullet, SniperRifleBullet, PistolBullet, AssaultRifleBullet};
}
