package inventory_items;

import animation.ImageFrame;
import animation.ImageStrip;
import game.Controller;
import game.ServerController;
import game.SingleController;
import game.World;
import packets.InventoryItemPacket;
import player.Player;
import weapons.guns.LightningSword;

import java.awt.*;
import java.util.ArrayList;

import static weapons.aoe.Slash.buildImageStrip;

public class LightningSwordItem extends InventoryItem {

    //Graphics
    private static final int SECONDARY_TEXTURE_MAX_TIMER = 10;
    private int secondaryTextureTimer = 0;
    private int secondaryTextureState = 1;
    private static final int FLOAT_EFFECT_MAX_TIMER = 3;
    private int floatTimer = 0;
    private int floatState = 2;
    private boolean floatDirection = true;
    protected static ImageStrip swordAnimationStrip;
    protected final ImageFrame currentFrame;

    public LightningSwordItem(double x, double y) {
        super(x, y);

        currentFrame = swordAnimationStrip.getHead();
        texture = currentFrame.getImage();
    }

    @Override
    public void giveItem(int playerNumber) {
        int indexToRemove = Controller.inventoryItems.indexOf(this);
        if(World.controller instanceof ServerController || World.controller instanceof SingleController) {
            if(playerNumber == Player.SERVER_PLAYER) {
                if (Controller.thisPlayer.getArsenal().lacksWeapon(LightningSword.SERIAL)) {
                    Controller.inventoryItems.remove(indexToRemove);
                    Controller.thisPlayer.getArsenal().add(new LightningSword(Controller.thisPlayer));
                }
            } else {
                if (Controller.otherPlayer.getArsenal().lacksWeapon(LightningSword.SERIAL)) {
                    Controller.inventoryItems.remove(indexToRemove);
                    Controller.otherPlayer.getArsenal().add(new LightningSword(Controller.otherPlayer));
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
        InventoryItemPacket inventoryItemPacket = new InventoryItemPacket(playerNumber, indexToRemove, LightningSword.SERIAL);
        World.controller.getOutputConnection().sendPacket(inventoryItemPacket);
    }

    @Override
    public void render(Graphics g) {
        texture = currentFrame.getNext().getImage();

        g.drawImage(texture,(int)x,(int)y + floatState,INVENTORY_ITEM_DIMENSIONS.width,INVENTORY_ITEM_DIMENSIONS.height,World.controller);
    }

    public static void loadImageStrips(Controller controller) {
        ArrayList<String> imgLocStr = new ArrayList<>();
        String defLocStr;

        // Saves amount of text to be used
        if (controller instanceof ServerController || controller instanceof SingleController) {
            defLocStr = "/resources/GUI/sword/sword_blue (";
        } else {
            defLocStr = "/resources/GUI/sword/sword_red (";
        }

        // Builds image strip for standing facing right
        for (int i = 1; i <= 4; i++) {
            imgLocStr.add(i + ").png");
        }
        swordAnimationStrip = buildImageStrip(imgLocStr, defLocStr);
//        System.out.println(standing.toString());
        imgLocStr.clear();
    }
}
