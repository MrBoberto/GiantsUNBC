package weapons.ammo;

import game.ClientController;
import game.Controller;
import game.ServerController;
import game.World;
import player.Player;
import weapons.guns.SniperRifle;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.*;
import java.io.*;

public class SniperRifleBullet extends Bullet {

    private Rectangle boundRect;
    private final double MASS = 0.2;

    private double angle;
    private double velX;
    private double velY;
    private final int SERIAL = 001;

    public SniperRifleBullet(int player, double aimX, double aimY, int damage) {
        super(0,0,0);

        playerIBelongToNumber = player;
        if((playerIBelongToNumber == Player.SERVER_PLAYER && World.controller instanceof ServerController)
                || (playerIBelongToNumber == Player.CLIENT_PLAYER && World.controller instanceof ClientController)){
            x = Controller.thisPlayer.getX();
            y = Controller.thisPlayer.getY();

            angle = World.atan(
                    aimX - Controller.thisPlayer.getX(),
                    aimY - Controller.thisPlayer.getY(),
                    0
            );
        } else if((playerIBelongToNumber == Player.SERVER_PLAYER && World.controller instanceof ClientController)
                || (playerIBelongToNumber == Player.CLIENT_PLAYER && World.controller instanceof ServerController)){
            x = Controller.otherPlayer.getX();
            y = Controller.otherPlayer.getY();

            angle = World.atan(
                    aimX - Controller.otherPlayer.getX(),
                    aimY - Controller.otherPlayer.getY(),
                    0
            );
        }

        this.damage = damage;
        loadImage();
        Controller.movingAmmo.add(this);

        if (angle <= -Math.PI) {
            angle += 2 * Math.PI;
        } else if (angle > Math.PI) {
            angle -= 2 * Math.PI;
        }


//        System.out.print("angle = " + Math.toDegrees(angle) + ", momentum = " + weapon.getMOMENTUM() + ", MASS = " + MASS);
        double speed = SniperRifle.MOMENTUM / MASS;

        if (angle >= Math.PI / 2 || (angle < 0 && angle >= -Math.PI / 2)) {
//            System.out.print(", Negative, speed = " + weapon.getMOMENTUM() / MASS);
            velX = World.cosAdj(speed, angle);
            velY = World.sinOpp(speed, angle);
        } else {
//            System.out.print(", Positive, speed = " + weapon.getMOMENTUM() / MASS);
            velX = World.sinOpp(speed, angle);
            velY = World.cosAdj(speed, angle);
        }

//        System.out.println(", velX = " + velX + ", velY = " + velY);

        //pos = new Point((int) super.getX(), (int) super.getY());
        boundRect = new Rectangle((int)x - texture.getWidth() / 2, (int)y - texture.getHeight() / 2,
                texture.getWidth(), texture.getHeight());
    }

    @Override
    public void tick() {
        super.setX(super.getX() + velX);
        super.setY(super.getY() + velY);

        if(x == 0 || y == 0){
            return;
        }

        //pos.setLocation(super.getX(), super.getY());

        // Apply vertical friction
        if (velY > Controller.FRICTION) {
            velY -= Controller.FRICTION;
        } else if (velY < -Controller.FRICTION) {
            velY += Controller.FRICTION;
        } else if (velY != 0) {
            velY = 0;
        }
        // Apply horizontal friction
        if (velX > Controller.FRICTION) {
            velX -= Controller.FRICTION;
        } else if (velX < -Controller.FRICTION) {
            velX += Controller.FRICTION;
        } else if (velX != 0) {
            velX = 0;
        }

    }

    @Override
    public void render(Graphics g) {
        if(texture == null){
            loadImage();
        }
        if(texture != null && (x != 0 && y!=0)) {
            AffineTransform affTra = AffineTransform.getTranslateInstance(
                    x - texture.getWidth() / 2.0, y - texture.getHeight() / 2.0);
            affTra.rotate(-angle, texture.getWidth() / 2.0,
                    texture.getHeight() / 2.0);
            Graphics2D g2d = (Graphics2D) g;

            g2d.drawImage(texture, affTra, World.controller);

        /*
        g.setColor(new Color(50, 50, 100));
        g.drawRect(pos.x - texture.getWidth() / 2, pos.y - texture.getHeight() / 2, texture.getWidth(),
                texture.getHeight());
         */
        }
    }

    public void loadImage() {
        try {
            texture = ImageIO.read(new File("resources/VFX/projectile/nato.png"));
        } catch (IOException exc) {
            System.out.println("Could not find image file: " + exc.getMessage());
        }
    }

    @Override
    public Rectangle getBounds() {
        return boundRect;
    }

    @Override
    public double getAngle() {
        return angle;
    }

    @Override
    public int getSERIAL() {
        return SERIAL;
    }

    @Override
    public boolean hasStopped() {
        return (velX == 0 && velY == 0);
    }

    @Serial
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeInt(1); // how many images are serialized?

        ImageIO.write(texture, "png", out); // png is lossless
    }

    @Serial
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        texture = ImageIO.read(in);
    }
}
