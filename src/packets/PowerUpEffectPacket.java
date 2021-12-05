package packets;

/**
 * This file is part of a solution to
 *		CPSC300 Term Project Fall 2021
 *
 * A packet that informs a player of an applied PowerUp effect
 *
 * @author The Boyz
 * @version 1
 */

import java.io.Serializable;

public class PowerUpEffectPacket implements Serializable {
    private final int playerToBeAffected;
    private final int indexToRemove;
    private final int time;

    //Possible changes. -1 == no change.
    private float damageMultiplier = -1;
    private float speedMultiplier = -1;
    private int ricochetBounces = -1;

    public PowerUpEffectPacket(int playerToBeAffected, int indexToRemove, int time) {
        this.playerToBeAffected = playerToBeAffected;
        this.indexToRemove = indexToRemove;
        this.time = time;
    }

    public int getTime() {
        return time;
    }

    public void setRicochetBounces(int ricochetBounces) {
        this.ricochetBounces = ricochetBounces;
    }

    public int getRicochetBounces() {
        return ricochetBounces;
    }

    public void setDamageMultiplier(float damageMultiplier) {
        this.damageMultiplier = damageMultiplier;
    }

    public void setSpeedMultiplier(float speedMultiplier){
        this.speedMultiplier = speedMultiplier;
    }

    public float getSpeedMultiplier() {
        return speedMultiplier;
    }

    public int getPlayerToBeAffected() {
        return playerToBeAffected;
    }

    public float getDamageMultiplier() {
        return damageMultiplier;
    }

    public int getIndexToRemove() {
        return indexToRemove;
    }
}
