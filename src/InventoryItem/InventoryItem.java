package InventoryItem;

import game.Controller;
import game.GameObject;

import java.awt.*;

public abstract class InventoryItem extends GameObject {

    public static final int TIME_BEFORE_DESPAWN = 10 * 60; // = 10 seconds
    protected int lifetime = 0; //in game ticks
    public static final Dimension INVENTORY_ITEM_DIMENSIONS = new Dimension(Controller.GRID_SIZE*3/4, Controller.GRID_SIZE*3/4);
    public enum InventoryItemType{Shotgun,SniperRifle,Pistol,AssaultRifle,RocketLauncher}

    protected InventoryItem(double x, double y) {
        super(x, y);
    }

    @Override
    public void tick() {
        lifetime++;

        if(lifetime > TIME_BEFORE_DESPAWN){
            Controller.powerUps.remove(this);
        }
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle((int)x,(int)y, INVENTORY_ITEM_DIMENSIONS.width,INVENTORY_ITEM_DIMENSIONS.height);
    }

    public abstract void applyPowerUp(int playerNumber);
    protected abstract void updateClient(int playerNumber, int indexToRemove);

}
