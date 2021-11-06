package weapons.ammo;

import weapons.guns.Weapon;

import java.awt.*;
import java.awt.image.ImageObserver;
import java.io.Serializable;

public interface Projectile extends Serializable{
     void tick();
     void draw(Graphics g, ImageObserver imgObs);
     Rectangle getBounds();

     double getX();
     double getY();
     double getAngle();
     int getSERIAL();
     int getID();
     enum Type{ShotgunBullet}
}
