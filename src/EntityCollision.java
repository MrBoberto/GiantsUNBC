
import game.Controller;
import player.Creature;
import weapons.Projectile;

import java.util.*;

public class EntityCollision {
    public static boolean isCollision(Projectile proj) {
        for (int i = 0; i < Controller.livingPlayers.size(); i++) {
            if (proj.getBounds().intersects(Controller.livingPlayers.get(i).getBounds())) {
                return true;
            }
        }
        return false;
    }
}