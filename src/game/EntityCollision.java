package game;

import player.Player;
import weapons.ammo.Bullet;
import weapons.ammo.Projectile;

public class EntityCollision {
    public static int getVictim(Bullet proj) {

            if (proj.getBounds().intersects(Controller.thisPlayer.getBounds())) {
                return Player.SERVER_PLAYER;
            }
        if (proj.getBounds().intersects(Controller.otherPlayer.getBounds())) {
            return Player.CLIENT_PLAYER;
        }

        return -1;
    }
}