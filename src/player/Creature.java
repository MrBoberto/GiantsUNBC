package player;

import java.awt.*;
import java.awt.image.ImageObserver;

public interface Creature {
    void tick(Point MouseLoc);
    void draw(Graphics g, ImageObserver imgObs);
    Rectangle getBounds();

    double getX();
    double getY();
}
