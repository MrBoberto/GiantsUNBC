package weapons.guns;

import java.io.Serializable;

public interface Weapon extends Serializable {

    enum WeaponType{Shotgun, SniperRifle, Pistol, AssaultRifle, RocketLauncher, LightningSword}

    void shoot(double mouseX, double mouseY);

    // Measured in kg * pixels / sec
    double getSPEED();

    int getMAX_DELAY();

    int getCurrentDelay();

    void setCurrentDelay(int currentDelay);

    int getSERIAL();

    // Returns the name of the weapon
    String toString();

    void playAudio();

    WeaponType getWeaponType();
}
