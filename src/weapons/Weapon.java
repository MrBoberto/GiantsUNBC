package weapons;

import game.Thing;

public interface Weapon {
     void shoot(double x, double y);
     Thing getParent();
    // Measured in kg * pixels / sec
     double getMOMENTUM();
     double getINACCURACY();
     int getMAX_DELAY();
     int getCurrentDelay();
     void setCurrentDelay(int currentDelay);
     int getSERIAL();
    // Returns the name of the weapon
     String toString();
}
