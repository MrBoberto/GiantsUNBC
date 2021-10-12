package weapons;

import java.awt.*;
import java.awt.image.ImageObserver;

public interface Projectile {
     void tick();
     void draw(Graphics g, ImageObserver imgObs);
     Rectangle getBounds();

     double getX();
     double getY();
}
