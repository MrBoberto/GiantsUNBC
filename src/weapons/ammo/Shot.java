package weapons.ammo;

import game.World;
import weapons.Projectile;
import weapons.Weapon;
import weapons.ammo.Ammo;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;

public class Shot extends Ammo implements Projectile {

    private Rectangle boundRect;
    private final double MASS = 0.02;
    private final double DAMAGE = 10;
    private double damage = 0;
    private BufferedImage texture;
    private AffineTransform affTra;
    private Point pos;
    private double angle;
    private double velX;
    private double velY;
    private final int SERIAL = 000;


    /**
     *
     * @param x
     * @param y
     * @param aimX
     * @param aimY
     * @param playerNumber
     * @param momentum
     * @param multiplier
     * @param angle
     */
    public Shot(double x, double y, double aimX, double aimY, int playerNumber, double momentum, double multiplier, double angle) {
        super(x, y, playerNumber);

        loadImage();
        World.getWorld().getController().movingAmmo.add(this);

        this.angle = angle;
        if (angle <= -Math.PI) {
            angle += 2 * Math.PI;
        } else if (angle > Math.PI) {
            angle -= 2 * Math.PI;
        }

        damage = DAMAGE * multiplier;

//        System.out.print("angle = " + Math.toDegrees(angle) + ", momentum = " + weapon.getMOMENTUM() + ", MASS = " + MASS);
        double speed = momentum / MASS - (momentum / (MASS * 2)) * World.getWorld().getSRandom().nextDouble();

        if (angle >= Math.PI / 2 || (angle < 0 && angle >= -Math.PI / 2)) {
//            System.out.print(", Negative, speed = " + weapon.getMOMENTUM() / MASS);
            velX = World.getWorld().cosAdj(speed, angle);
            velY = World.getWorld().sinOpp(speed, angle);
        } else {
//            System.out.print(", Positive, speed = " + weapon.getMOMENTUM() / MASS);
            velX = World.getWorld().sinOpp(speed, angle);
            velY = World.getWorld().cosAdj(speed, angle);
        }

//        System.out.println(", velX = " + velX + ", velY = " + velY);

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

        if (velX == 0 && velY == 0) {
            World.getWorld().getController().movingAmmo.remove(this);
        }
    }

    public void loadImage() {
        try {
            texture = ImageIO.read(new File("resources/VFX/projectile/shot.png"));
        } catch (IOException exc) {
            System.out.println("Could not find image file: " + exc.getMessage());
        }
    }

    @Override
    public void draw(Graphics g, ImageObserver imgObs) {
        AffineTransform affTra = AffineTransform.getTranslateInstance(
                pos.x - texture.getWidth() / 2, pos.y - texture.getHeight() / 2);
        affTra.rotate(-angle, texture.getWidth() / 2,
                texture.getHeight() / 2);
        Graphics2D g2d = (Graphics2D) g;

        g2d.drawImage(texture, affTra, imgObs);

        /*
        g.setColor(new Color(50, 50, 100));
        g.drawRect(pos.x - texture.getWidth() / 2, pos.y - texture.getHeight() / 2, texture.getWidth(),
                texture.getHeight());
         */
    }

    @Override
    public Rectangle getBounds() {
        return boundRect;
    }

    @Override
    public int getSERIAL() {
        return SERIAL;
    }

    @Override
    public double getDamage() {
        return damage;
    }
}
