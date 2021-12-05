package power_ups;

/**
 * This file is part of a solution to
 *		CPSC300 Term Project Fall 2021
 *
 * An abstract class for powers ups. Power ups spawn in zones not obstructed by blocks and have an effect
 * that is inflicted upon the player who collects it.
 *
 * @author The Boyz
 * @version 1
 */

import game.Controller;
import game.GameObject;
import player.Player;

import java.awt.*;

public abstract class PowerUp extends GameObject {

    public static final int TIME_BEFORE_DESPAWN = 15 * 60;
    protected int lifetime = 0; //in game ticks
    public static final Dimension POWER_UP_DIMENSIONS = new Dimension(Controller.GRID_SIZE*3/4, Controller.GRID_SIZE*3/4);
    public static final Dimension POWER_UP_COSMETIC_DIMENSIONS = new Dimension(Controller.GRID_SIZE /2, Controller.GRID_SIZE /2);
    public enum PowerUpType{DamageUp,DamageDown,SpeedUp,SpeedDown,Ricochet}
    protected boolean isCosmetic = false;

    protected PowerUp(double x, double y) {
        super(x, y);
    }

    @Override
    public void tick() {
        if(!isCosmetic()) {
            lifetime++;

            if (lifetime > TIME_BEFORE_DESPAWN) {
                Controller.powerUps.remove(this);
            }
        }
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle((int)x,(int)y, POWER_UP_DIMENSIONS.width,POWER_UP_DIMENSIONS.height);
    }

    public void applyPowerUp(int playerNumber){
        if(playerNumber == Player.SERVER_PLAYER){
            Controller.thisPlayer.increasePickedUpPowerUps();
        } else {
            Controller.otherPlayer.increasePickedUpPowerUps();
        }
    }
    protected abstract void updateClient(int playerNumber, int indexToRemove);

    public boolean isCosmetic() {
        return isCosmetic;
    }

    public void setCosmetic(boolean cosmetic) {
        isCosmetic = cosmetic;
    }
}
