package power_ups;

import game.Controller;
import game.GameObject;

import java.awt.*;

public abstract class PowerUp extends GameObject {

    public static final int TIME_BEFORE_DESPAWN = 10 * 60; // = 10 seconds
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

    public abstract void applyPowerUp(int playerNumber);
    protected abstract void updateClient(int playerNumber, int indexToRemove);

    public boolean isCosmetic() {
        return isCosmetic;
    }

    public void setCosmetic(boolean cosmetic) {
        isCosmetic = cosmetic;
    }
}
