package power_ups;

import game.Controller;
import game.ServerController;
import game.SingleController;
import game.World;
import packets.PowerUpEffectPacket;
import player.Player;
import utilities.BufferedImageLoader;

import java.awt.*;

public class Ricochet extends PowerUp{

    public static final int EFFECT_TIME = 60 * 14; // in ticks
    private final int bounces;


    //Graphics
    private static final int FLOAT_EFFECT_MAX_TIMER = 3;
    private int floatTimer = 0;
    private int floatState = 2;
    private boolean floatDirection = true;

    public Ricochet(double x, double y, int bounces) {
        super(x, y);

        this.bounces = bounces;

        texture = BufferedImageLoader.loadImage("/resources/Textures/power_ups/ricochet_sprite.png");

    }

    @Override
    public void applyPowerUp(int playerNumber) {
        int indexToRemove = Controller.powerUps.indexOf(this);
        Controller.powerUps.remove(indexToRemove);
        if(World.controller instanceof ServerController || World.controller instanceof SingleController){
            if(playerNumber == Player.SERVER_PLAYER){
                Controller.thisPlayer.setRicochet(bounces, EFFECT_TIME);
            } else {
                Controller.otherPlayer.setRicochet(bounces, EFFECT_TIME);
            }
        }

        if (World.controller instanceof SingleController) return;
        updateClient(playerNumber, indexToRemove);
    }

    @Override
    public void tick() {
        super.tick();

        if(!isCosmetic()) {
            if (floatTimer > FLOAT_EFFECT_MAX_TIMER) {
                floatTimer = 0;
                if (floatState == 2 || floatState == -2) {
                    floatDirection = !floatDirection;
                }
                if (floatDirection) {
                    floatState++;
                } else {
                    floatState--;
                }
            }

            floatTimer++;
        }
    }

    @Override
    protected void updateClient(int playerNumber, int indexToRemove) {
        PowerUpEffectPacket powerUpEffectPacket = new PowerUpEffectPacket(playerNumber, indexToRemove, EFFECT_TIME);
        powerUpEffectPacket.setRicochetBounces(bounces);
        World.controller.getOutputConnection().sendPacket(powerUpEffectPacket);
    }

    @Override
    public void render(Graphics g) {
        Dimension currentDimension;
        if(isCosmetic()){
            currentDimension = POWER_UP_COSMETIC_DIMENSIONS;
        } else {
            currentDimension = POWER_UP_DIMENSIONS;
        }
        g.drawImage(texture,(int)x,(int)y + floatState,currentDimension.width,currentDimension.height,World.controller);

    }
}
