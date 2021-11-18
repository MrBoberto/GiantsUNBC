package weapons.ammo;

import game.ClientController;
import game.Controller;
import game.ServerController;
import game.World;
import player.Player;
import weapons.guns.Shotgun;
import weapons.guns.Weapon;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.*;
import java.io.*;
import java.util.ArrayList;

public class ShotgunBullet extends Bullet {

    private Rectangle boundRect;
    private final double MASS = 0.02;
    transient private BufferedImage texture;
    //private Point pos;
    private double angle;
    private double velX;
    private double velY;
    private final int SERIAL = 000;

    public ShotgunBullet(int player, double aimX, double aimY, int damage) {
        super();
        playerIBelongTo = player;
        if((playerIBelongTo == Player.SERVER_PLAYER && World.controller instanceof ServerController)
                || (playerIBelongTo == Player.CLIENT_PLAYER && World.controller instanceof ClientController)){
            setX(Controller.thisPlayer.getX());
            setY(Controller.thisPlayer.getY());

            angle = World.atan(aimX - Controller.thisPlayer.getX(),
                    aimY - Controller.thisPlayer.getY(), 0);
        } else if((playerIBelongTo == Player.SERVER_PLAYER && World.controller instanceof ClientController)
                || (playerIBelongTo == Player.CLIENT_PLAYER && World.controller instanceof ServerController)){
            setX(Controller.otherPlayer.getX());
            setY(Controller.otherPlayer.getY());
            angle = World.atan(aimX - Controller.otherPlayer.getX(),
                    aimY - Controller.otherPlayer.getY(), 0);
        }
        TYPE = Type.ShotgunBullet;
        this.damage = damage;
        loadImage();
        Controller.movingAmmo.add(this);




//        System.out.print("angle = " + Math.toDegrees(angle) + ", momentum = " + weapon.getMOMENTUM() + ", MASS = " + MASS);
        double speed = Shotgun.MOMENTUM / MASS - 10* World.getSRandom().nextDouble();

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
       // pos.setLocation(super.getX(), super.getY());

        // Apply vertical friction
        if (velY > World.controller.FRICTION) {
            velY -= World.controller.FRICTION;
        } else if (velY < -World.controller.FRICTION) {
            velY += World.controller.FRICTION;
        } else if (velY != 0) {
            velY = 0;
        }
        // Apply horizontal friction
        if (velX > World.controller.FRICTION) {
            velX -= World.controller.FRICTION;
        } else if (velX < -World.controller.FRICTION) {
            velX += World.controller.FRICTION;
        } else if (velX != 0) {
            velX = 0;
        }
        if(texture!= null){
            boundRect = new Rectangle((int)x - texture.getWidth() / 2,
                    (int)y- texture.getHeight() / 2, texture.getWidth(),
                    texture.getHeight());
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
        if(texture == null){
            loadImage();
        }
        if(texture != null && (x != 0 && y!=0)){
            AffineTransform affTra = AffineTransform.getTranslateInstance(
                    x - texture.getWidth() / 2, y - texture.getHeight() / 2);
            affTra.rotate(angle, texture.getWidth() / 2,
                    texture.getHeight() / 2);
            Graphics2D g2d = (Graphics2D) g;

            g2d.drawImage(texture, affTra, imgObs);

        /*
        g.setColor(new Color(50, 50, 100));
        g.drawRect(pos.x - texture.getWidth() / 2, pos.y - texture.getHeight() / 2, texture.getWidth(),
                texture.getHeight());
         */
        }

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
    public double getAngle() {
        return angle;
    }
    @Override
    public int getID() {
        return ID;
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
