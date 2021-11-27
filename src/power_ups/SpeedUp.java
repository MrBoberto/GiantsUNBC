package power_ups;

import game.Controller;
import game.ServerController;
import game.SingleController;
import game.World;
import packets.PowerUpEffectPacket;
import player.Player;
import utilities.BufferedImageLoader;

import java.awt.*;
import java.awt.image.BufferedImage;

public class SpeedUp extends PowerUp{

    public static final int EFFECT_TIME = 480; // in game ticks (= 8 seconds)
    private final float multiplier;
    private final BufferedImage secondary_texture;

    //Graphics
    private final int SECONDARY_TEXTURE_MAX_TIMER = 10;
    private int secondaryTextureTimer = 0;
    private int secondaryTextureState = 1;
    private final int FLOAT_EFFECT_MAX_TIMER = 3;
    private int floatTimer = 0;
    private int floatState = 2;
    private boolean floatDirection = true;

    public SpeedUp(double x, double y, float multiplier) {
        super(x, y);

        this.multiplier = multiplier;

        texture = BufferedImageLoader.loadImage("/resources/Textures/power_ups/SPEED_sprite.png");
        secondary_texture = BufferedImageLoader.loadImage("/resources/Textures/power_ups/up_arrow_orange_sprite.png");
    }

    @Override
    public void applyPowerUp(int playerNumber) {
        int indexToRemove = Controller.powerUps.indexOf(this);
        Controller.powerUps.remove(indexToRemove);
        if(World.controller instanceof ServerController || World.controller instanceof SingleController){
            if(playerNumber == Player.SERVER_PLAYER){
                Controller.thisPlayer.setSpeedMultiplier(multiplier, EFFECT_TIME);
            } else {
                Controller.otherPlayer.setSpeedMultiplier(multiplier, EFFECT_TIME);
            }
        }

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
        PowerUpEffectPacket powerUpEffectPacket = new PowerUpEffectPacket(playerNumber, indexToRemove, EFFECT_TIME);
        powerUpEffectPacket.setSpeedMultiplier(multiplier);
        World.controller.getOutputConnection().sendPacket(powerUpEffectPacket);
    }

    @Override
    public void render(Graphics g) {
        g.drawImage(texture,(int)x,(int)y + floatState,POWER_UP_DIMENSIONS.width,POWER_UP_DIMENSIONS.height,World.controller);
        g.drawImage(secondary_texture,(int)x,(int)y + (secondaryTextureState*2)+ floatState,POWER_UP_DIMENSIONS.width,POWER_UP_DIMENSIONS.height,World.controller);

    }
}
