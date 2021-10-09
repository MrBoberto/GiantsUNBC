package GiantsSidescroller.src;
import java.util.*;

public class EntityCollision {
    public static boolean isCollision(Projectile proj, LinkedList<Creature> creatures) {
        for (int i = 0; i < creatures.size(); i++) {
            if (proj.getBounds().intersects(creatures.get(i).getBounds())) {
                return true;
            }
        }
        return false;
    }
}