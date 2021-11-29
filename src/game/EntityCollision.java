package game;

import player.Player;
import weapons.ammo.Bullet;
import weapons.ammo.Projectile;
import weapons.aoe.Explosion;
import weapons.aoe.Slash;

public class EntityCollision {
    public static int getBulletVictim(Bullet proj) {

        try{
            if (proj.getBounds().intersects(Controller.thisPlayer.getBounds())) {
                return Player.SERVER_PLAYER;
            }
            if (proj.getBounds().intersects(Controller.otherPlayer.getBounds())) {
                return Player.CLIENT_PLAYER;
            }

        } catch (NullPointerException e){
            /* empty */
        }

        return -1;
    }

    public static int getExplosionVictim(Explosion explosion) {

        try{
            if (explosion.getBounds().intersects(Controller.thisPlayer.getBounds())) {
                // If both
                if (explosion.getBounds().intersects(Controller.otherPlayer.getBounds())) {
                    return -2;
                }
                // If only this
                return Player.SERVER_PLAYER;
            }
            // If only other
            if (explosion.getBounds().intersects(Controller.otherPlayer.getBounds())) {
                return Player.CLIENT_PLAYER;
            }

        } catch (NullPointerException e){
            /* empty */
        }

        return -1;
    }

    public static int getSlashVictim(Slash slash) {

        try{
            if (slash.getBounds().intersects(Controller.thisPlayer.getBounds())) {
                if (slash.getPlayerIBelongToNumber() != Controller.thisPlayer.getPlayerNumber()) {
                    System.out.println("Hit thisPlayer");
                    return Player.SERVER_PLAYER;
                }
            }
            if (slash.getBounds().intersects(Controller.otherPlayer.getBounds())) {
                if (slash.getPlayerIBelongToNumber() != Controller.otherPlayer.getPlayerNumber()) {
                    System.out.println("Hit otherPlayer");
                    return Player.CLIENT_PLAYER;
                }
            }

        } catch (NullPointerException e){
            /* empty */
        }

        return -1;
    }
}