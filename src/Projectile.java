import java.awt.*;
import java.awt.image.ImageObserver;

public interface Projectile {
    public void tick();
    public void draw(Graphics g, ImageObserver imgObs);
    public Rectangle getBounds();

    public double getX();
    public double getY();
}
