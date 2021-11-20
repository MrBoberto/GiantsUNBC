package weapons;

import game.Thing;
import player.Player;

public interface Weapon {
     void shoot(double x, double y);
     Player getParent();
    // Measured in kg * pixels / sec
     double getMOMENTUM();
     double getINACCURACY();
     int getMAX_DELAY();
     int getCurrentDelay();
     void setCurrentDelay(int currentDelay);
     int getSERIAL();
     // Returns the name of the weapon
     String toString();
     // Returns the damage each bullet does
     double getDamage();
}
