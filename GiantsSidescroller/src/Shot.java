package GiantsSidescroller.src;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;

public class Shot extends Ammo implements Projectile {

    private Rectangle boundRect;
    private final double MASS = 0.02;
    private BufferedImage texture;
    private AffineTransform affTra;
    private Point pos;
    private double angle;
    private double velX;
    private double velY;

    public Shot(double aimX, double aimY, Weapon weapon) {
        super(weapon.getParent().getX(), weapon.getParent().getY(), weapon);

        loadImage();

        angle = World.getWorld().atan(aimX - super.getWeapon().getParent().getX(),
                aimY - super.getWeapon().getParent().getY(), 0) + World.getWorld().sRandom.nextInt(7);

        if (angle >= Math.PI / 2 || (angle < 0 && angle >= -Math.PI / 2)) {
            velX = World.getWorld().cosAdj((weapon.getMomentum() / MASS), angle);
            velY = World.getWorld().sinOpp((weapon.getMomentum() / MASS), angle);
        } else {
            velX = World.getWorld().sinOpp((weapon.getMomentum() / MASS), angle);
            velY = World.getWorld().cosAdj((weapon.getMomentum() / MASS), angle);
        }

        pos = new Point((int) super.getX(), (int) super.getY());
        boundRect = new Rectangle(pos.x - texture.getWidth() / 2, pos.y - texture.getHeight() / 2,
                texture.getWidth(), texture.getHeight());
    }

    @Override
    public void tick() {
        super.setX(super.getX() + velX);
        super.setY(super.getY() + velY);
        pos.setLocation(super.getX(), super.getY());

        // Apply vertical friction
        if (velY > World.getWorld().getController().FRICTION) {
            velY -= World.getWorld().getController().FRICTION;
        } else if (velY < -World.getWorld().getController().FRICTION) {
            velY += World.getWorld().getController().FRICTION;
        } else if (velY != 0) {
            velY = 0;
        }
        // Apply horizontal friction
        if (velX > World.getWorld().getController().FRICTION) {
            velX -= World.getWorld().getController().FRICTION;
        } else if (velX < -World.getWorld().getController().FRICTION) {
            velX += World.getWorld().getController().FRICTION;
        } else if (velX != 0) {
            velX = 0;
        }

        boundRect = new Rectangle(pos.x - texture.getWidth() / 2,
                pos.y - texture.getHeight() / 2, texture.getWidth(),
                texture.getHeight());
    }

    public void loadImage() {
        try {
            texture = ImageIO.read(new File("GiantsSidescroller/src/images/projectile/shot.png"));
        } catch (IOException exc) {
            System.out.println("Could not find image file: " + exc.getMessage());
        }
    }

    @Override
    public void draw(Graphics g, ImageObserver imgObs) {
        loadImage();

        AffineTransform affTra = AffineTransform.getTranslateInstance(
                pos.x - texture.getWidth() / 2, pos.y - texture.getHeight() / 2);
        affTra.rotate(angle, texture.getWidth() / 2,
                texture.getHeight() / 2);
        Graphics2D g2d = (Graphics2D) g;

        g2d.drawImage(texture, affTra, imgObs);

        g.setColor(new Color(50, 50, 100));
        g.drawRect(pos.x - texture.getWidth() / 2, pos.y - texture.getHeight() / 2, texture.getWidth(),
                texture.getHeight());
    }

    @Override
    public Rectangle getBounds() {
        return boundRect;
    }
}
