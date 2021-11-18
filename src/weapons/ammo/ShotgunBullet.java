package weapons.ammo;

import game.ClientController;
import game.Controller;
import game.ServerController;
import game.World;
import player.Player;
import weapons.guns.Shotgun;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.*;
import java.io.*;

public class ShotgunBullet extends Bullet {

    private final double MASS = 0.02;
    private final int SERIAL = 000;

    public ShotgunBullet(int player, double aimX, double aimY, int damage) {
        super(0,0,0);
        playerIBelongToNumber = player;
        ProjectileTYPE = ProjectileType.ShotgunBullet;

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
    }



    public void loadImage() {
        try {
            texture = ImageIO.read(new File("resources/VFX/projectile/shot.png"));
        } catch (IOException exc) {
            System.out.println("Could not find image file: " + exc.getMessage());
        }
    }

    @Override
    public void render(Graphics g) {

        if(texture == null){
            loadImage();
        }
        if(texture != null && (x != 0 && y!=0)){
            AffineTransform affTra = AffineTransform.getTranslateInstance(
                    x - texture.getWidth() / 2.0, y - texture.getHeight() / 2.0);
            affTra.rotate(angle, texture.getWidth() / 2.0,
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



}
