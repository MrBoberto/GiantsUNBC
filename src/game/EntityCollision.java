package game;

import game.Controller;
import player.Player;
import weapons.Projectile;

import java.util.*;

public class EntityCollision {
    public static Player getVictim(Projectile proj) {
        for (int i = 0; i < Controller.livingPlayers.size(); i++) {
            if (proj.getBounds().intersects(Controller.livingPlayers.get(i).getBounds())) {
                return Controller.livingPlayers.get(i);
            }
        }
        return null;
    }
}