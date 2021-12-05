package weapons.guns;

/**
 * This file is part of a solution to
 *		CPSC300 Term Project Fall 2021
 *
 * A weapon owned by a player that can be used to inflict damage upon their opponent
 *
 * @author The Boyz
 * @version 1
 */

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
