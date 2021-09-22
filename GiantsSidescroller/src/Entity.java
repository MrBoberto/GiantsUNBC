package GiantsSidescroller.src;

import java.awt.*;

public interface Entity {
    public void tick();
    public void render(Graphics g);
    public Rectangle getBounds();

    public double getX();
    public double getY();
}
