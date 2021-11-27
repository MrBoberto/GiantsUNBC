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

public class DamageUp extends PowerUp{

    public static final int EFFECT_TIME = 480; // in game ticks (= 8 seconds)
    private final float multiplier;
    private final BufferedImage secondary_texture;

    public DamageUp(double x, double y, float multiplier) {
        super(x, y);

        this.multiplier = multiplier;

        texture = BufferedImageLoader.loadImage("/resources/Textures/power_ups/DMG_sprite.png");
        secondary_texture = BufferedImageLoader.loadImage("/resources/Textures/power_ups/up_arrow_orange_sprite.png");
    }

    @Override
    public void applyPowerUp(int playerNumber) {
        int indexToRemove = Controller.powerUps.indexOf(this);
        Controller.powerUps.remove(indexToRemove);
        if(World.controller instanceof ServerController || World.controller instanceof SingleController){
            if(playerNumber == Player.SERVER_PLAYER){
                Controller.thisPlayer.setDamageMultiplier(multiplier);
            } else {
                Controller.otherPlayer.setDamageMultiplier(multiplier);
            }
        }

        updateClient(playerNumber, indexToRemove);
    }

    @Override
    protected void updateClient(int playerNumber, int indexToRemove) {
        PowerUpEffectPacket powerUpEffectPacket = new PowerUpEffectPacket(playerNumber, indexToRemove);
        powerUpEffectPacket.setDamageMultiplier(multiplier);
        World.controller.getOutputConnection().sendPacket(powerUpEffectPacket);
    }

    @Override
    public void render(Graphics g) {
        g.drawImage(texture,(int)x,(int)y,POWER_UP_DIMENSIONS.width,POWER_UP_DIMENSIONS.height,World.controller);
        g.drawImage(secondary_texture,(int)x,(int)y,POWER_UP_DIMENSIONS.width,POWER_UP_DIMENSIONS.height,World.controller);
    }
}
