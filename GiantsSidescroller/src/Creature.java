package GiantsSidescroller.src;

import java.awt.*;
import java.awt.image.ImageObserver;

public interface Creature {
    public void tick(Point MouseLoc);
    public void draw(Graphics g, ImageObserver imgObs);
    public Rectangle getBounds();

    public double getX();
    public double getY();
}
