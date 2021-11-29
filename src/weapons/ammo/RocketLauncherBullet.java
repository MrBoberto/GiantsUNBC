package weapons.ammo;

import game.*;
import mapObjects.Block;
import player.Player;
import weapons.guns.RocketLauncher;
import weapons.guns.SniperRifle;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.*;
import java.io.*;

public class RocketLauncherBullet extends Bullet {

    private final int SERIAL = 004;
    //private static int iteration = 0;

    public RocketLauncherBullet(int player, double aimX, double aimY, int damage) {
        super(0,0,0,player);
        ProjectileTYPE = ProjectileType.RocketLauncherBullet;

        //iteration++;
        //System.out.println("Rocket " + iteration + "Created.");

        // If initialized to 0, sometimes bullet is deleted before constructor finishes.
        setVelX(-1);
        setVelY(-1);

        if((playerIBelongToNumber == Player.SERVER_PLAYER && World.controller instanceof ServerController)
                || (playerIBelongToNumber == Player.CLIENT_PLAYER && World.controller instanceof ClientController)
                || (playerIBelongToNumber == Player.SERVER_PLAYER && World.controller instanceof SingleController)) {
            x = Controller.thisPlayer.getX();
            y = Controller.thisPlayer.getY();

            angle = World.atan(
                    aimX - Controller.thisPlayer.getX(),
                    aimY - Controller.thisPlayer.getY(),
                    0
            );
        } else if((playerIBelongToNumber == Player.SERVER_PLAYER && World.controller instanceof ClientController)
                || (playerIBelongToNumber == Player.CLIENT_PLAYER && World.controller instanceof ServerController)
                || (playerIBelongToNumber == Player.CLIENT_PLAYER && World.controller instanceof SingleController)){
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

        if (angle <= -Math.PI) {
            angle += 2 * Math.PI;
        } else if (angle > Math.PI) {
            angle -= 2 * Math.PI;
        }

//        System.out.print("angle = " + Math.toDegrees(angle) + ", momentum = " + weapon.getMOMENTUM() + ", MASS = " + MASS);
        double speed = RocketLauncher.SPEED;

        if (angle >= Math.PI / 2 || (angle < 0 && angle >= -Math.PI / 2)) {
//            System.out.print(", Negative, speed = " + weapon.getMOMENTUM() / MASS);
            setVelX(World.cosAdj(speed, angle));
            setVelY(World.sinOpp(speed, angle));
        } else {
//            System.out.print(", Positive, speed = " + weapon.getMOMENTUM() / MASS);
            setVelX(World.sinOpp(speed, angle));
            setVelY(World.cosAdj(speed, angle));
        }


        //System.out.println(iteration + " velX = " + getVelX() + ", velY = " + getVelY());
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
            texture = ImageIO.read(getClass().getResource("/resources/VFX/projectile/nato.png"));
        } catch (IOException exc) {
            System.out.println("Could not find image file: " + exc.getMessage());
        }
    }

    @Override
    public double getAngle() {
        return angle;
    }

    @Override
    public int getSERIAL() {
        return SERIAL;
    }

    // Rockets do not slow down
    @Override
    public void tick() {
        x += getVelX();
        y += getVelY();
        if(x == 0 || y == 0){
            return;
        }

        if (texture != null) {
            boundRect = new Rectangle((int)x - 1,
                    (int)y-1, 2,
                    2);
        }

        checkBlockCollision();
    }
}
