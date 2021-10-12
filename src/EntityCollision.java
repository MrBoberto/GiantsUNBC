
import player.Creature;
import weapons.Projectile;

import java.util.*;

public class EntityCollision {
    public static boolean isCollision(Projectile proj, LinkedList<Creature> creatures) {
        for (Creature creature : creatures) {
            if (proj.getBounds().intersects(creature.getBounds())) {
                return true;
            }
        }
        return false;
    }
}