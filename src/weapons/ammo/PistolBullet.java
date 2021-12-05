package weapons.ammo;

import game.*;
import player.Player;
import utilities.BufferedImageLoader;
import weapons.guns.Pistol;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class PistolBullet extends Bullet {

    private static final int SERIAL = 2;
    private static final double INACCURACY = 0.01;


    public PistolBullet(int player, double aimX, double aimY, int damage) {
        super(player);

        // If initialized to 0, sometimes bullet is deleted before constructor finishes.
        setVelX(-1);
        setVelY(-1);

        ProjectileTYPE = ProjectileType.PistolBullet;

        if((playerIBelongToNumber == Player.SERVER_PLAYER && World.controller instanceof ServerController)
                || (playerIBelongToNumber == Player.CLIENT_PLAYER && World.controller instanceof ClientController)
                || (playerIBelongToNumber == Player.SERVER_PLAYER && World.controller instanceof SingleController)) {

            x = Controller.thisPlayer.getX();
            y = Controller.thisPlayer.getY();

            angle = World.atan(
                    aimX - Controller.thisPlayer.getX(),
                    aimY - Controller.thisPlayer.getY(),
                    0
            ) - INACCURACY / 2 + INACCURACY * World.sRandom.nextDouble();

        } else if((playerIBelongToNumber == Player.SERVER_PLAYER && World.controller instanceof ClientController)
                || (playerIBelongToNumber == Player.CLIENT_PLAYER && World.controller instanceof ServerController)
                || (playerIBelongToNumber == Player.CLIENT_PLAYER && World.controller instanceof SingleController)){

            x = Controller.otherPlayer.getX();
            y = Controller.otherPlayer.getY();

            angle = World.atan(
                    aimX - Controller.otherPlayer.getX(),
                    aimY - Controller.otherPlayer.getY(),
                    0
            ) - INACCURACY / 2 + INACCURACY * World.sRandom.nextDouble();

        }

        if (angle <= -Math.PI) {
            angle += 2 * Math.PI;
        } else if (angle > Math.PI) {
            angle -= 2 * Math.PI;
        }

        this.damage = damage;
        loadImage();


//        System.out.print("angle = " + Math.toDegrees(angle) + ", momentum = " + weapon.getMOMENTUM() + ", MASS = " + MASS);
        double speed = Pistol.SPEED - (Pistol.SPEED / 10) * World.sRandom.nextDouble();

        if (angle >= Math.PI / 2 || (angle < 0 && angle >= -Math.PI / 2)) {
//            System.out.print(", Negative, speed = " + weapon.getMOMENTUM() / MASS);
            setVelX(World.cosAdj(speed, angle));
            setVelY(World.sinOpp(speed, angle));
        } else {
//            System.out.print(", Positive, speed = " + weapon.getMOMENTUM() / MASS);
            setVelX(World.sinOpp(speed, angle));
            setVelY(World.cosAdj(speed, angle));
        }

    }



    public void loadImage() {
        texture = BufferedImageLoader.loadImage("/resources/VFX/projectile/shot.png");
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
}
