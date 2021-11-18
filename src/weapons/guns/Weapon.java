package weapons.guns;

import player.Player;

import java.io.Serializable;

public interface Weapon extends Serializable {

    void shoot(double mouseX, double mouseY);

    Player getPlayerIBelongTo();

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
