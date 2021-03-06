package inventory_items;

/**
 * This file is part of a solution to
 *		CPSC300 Term Project Fall 2021
 *
 * A rocket launcher that spawns in zones not obstructed by blocks and can be picked up and used by a player
 *
 * @author The Boyz
 * @version 1
 */

import game.Controller;
import game.ServerController;
import game.SingleController;
import game.World;
import packets.InventoryItemPacket;
import player.Player;
import utilities.BufferedImageLoader;
import weapons.guns.RocketLauncher;

import java.awt.*;

public class RocketLauncherItem extends InventoryItem {

    //Graphics
    private static final int SECONDARY_TEXTURE_MAX_TIMER = 10;
    private int secondaryTextureTimer = 0;
    private int secondaryTextureState = 1;
    private static final int FLOAT_EFFECT_MAX_TIMER = 3;
    private int floatTimer = 0;
    private int floatState = 2;
    private boolean floatDirection = true;

    public RocketLauncherItem(double x, double y) {
        super(x, y);

        texture = BufferedImageLoader.loadImage("/resources/GUI/arsenal_slot/arsenal (" + 4 + ").png");
    }

    @Override
    public void giveItem(int playerNumber) {
        int indexToRemove = Controller.inventoryItems.indexOf(this);
        if(World.controller instanceof ServerController || World.controller instanceof SingleController) {
            if(playerNumber == Player.SERVER_PLAYER) {
                if (Controller.thisPlayer.getArsenal().lacksWeapon(4)) {
                    Controller.inventoryItems.remove(indexToRemove);
                    Controller.thisPlayer.getArsenal().add(new RocketLauncher(Controller.thisPlayer));
                }
            } else {
                if (Controller.otherPlayer.getArsenal().lacksWeapon(4)) {
                    Controller.inventoryItems.remove(indexToRemove);
                    Controller.otherPlayer.getArsenal().add(new RocketLauncher(Controller.otherPlayer));
                }
            }
        }

        if (World.controller instanceof SingleController) return;
        updateClient(playerNumber, indexToRemove);
    }

    @Override
    public void tick() {
        super.tick();

        if(secondaryTextureTimer > SECONDARY_TEXTURE_MAX_TIMER){
            secondaryTextureTimer = 0;
            if(secondaryTextureState == -1){
                secondaryTextureState = 1;
            } else {
                secondaryTextureState--;
            }
        }
        if(floatTimer > FLOAT_EFFECT_MAX_TIMER){
            floatTimer = 0;
            if(floatState == 2 || floatState == -2) {
                floatDirection = !floatDirection;
            }
            if(floatDirection){
                floatState++;
            } else {
                floatState--;
            }
        }

        floatTimer++;
        secondaryTextureTimer++;
    }

    @Override
    protected void updateClient(int playerNumber, int indexToRemove) {
        InventoryItemPacket inventoryItemPacket = new InventoryItemPacket(playerNumber, indexToRemove, RocketLauncher.SERIAL);
        World.controller.getOutputConnection().sendPacket(inventoryItemPacket);
    }

    @Override
    public void render(Graphics g) {
        g.drawImage(texture,(int)x,(int)y + floatState,INVENTORY_ITEM_DIMENSIONS.width,INVENTORY_ITEM_DIMENSIONS.height,World.controller);
    }
}
