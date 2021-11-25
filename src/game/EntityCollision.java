package game;

import player.Player;
import weapons.ammo.Bullet;
import weapons.ammo.Projectile;
import weapons.aoe.Explosion;

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
}