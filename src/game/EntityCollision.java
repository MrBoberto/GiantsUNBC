package game;

import player.Player;
import weapons.ammo.Bullet;
import weapons.aoe.Explosion;
import weapons.aoe.Slash;

public class EntityCollision {
    public static int getBulletVictim(Bullet bullet) {

        try {
            if (bullet.getBounds().intersects(Controller.thisPlayer.getBounds())) {
                return Player.SERVER_PLAYER;
            }
            if (bullet.getBounds().intersects(Controller.otherPlayer.getBounds())) {
                return Player.CLIENT_PLAYER;
            }

        } catch (NullPointerException e) {
            /* empty */
        }

        return -1;
    }

    public static int getExplosionVictim(Explosion explosion) {

        try {
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

        } catch (NullPointerException e) {
            /* empty */
        }

        return -1;
    }

    public static int getSlashVictim(Slash slash) {

        try {
            if (slash.getBounds().intersects(Controller.thisPlayer.getBounds())) {
                if (slash.getPlayerIBelongToNumber() != Controller.thisPlayer.getPlayerNumber()) {
                    return Player.SERVER_PLAYER;
                }
            }
            if (slash.getBounds().intersects(Controller.otherPlayer.getBounds())) {
                if (slash.getPlayerIBelongToNumber() != Controller.otherPlayer.getPlayerNumber()) {
                    return Player.CLIENT_PLAYER;
                }
            }

        } catch (NullPointerException e) {
            /* empty */
        }

        return -1;
    }
}