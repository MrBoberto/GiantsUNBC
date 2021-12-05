package weapons.guns;

import audio.SFXPlayer;
import game.*;
import packets.ClientSFXPacket;
import packets.ClientSlashPacket;
import player.Player;
import weapons.aoe.Slash;

public class LightningSword implements Weapon {
    private final Player playerIBelongTo;
    public static final double SPEED = 0;
    public static final int ROUND_COUNT = 5;
    public static final int MAX_DELAY = 4;
    private int currentDelay = 0;
    // Identifies type of gun
    public static final int SERIAL = 5;
    public static final WeaponType WEAPON_TYPE = WeaponType.LightningSword;
    public static final int DAMAGE = 55;
    public static final int HALF_LENGTH = 60;         // The length of half of one side
    public SFXPlayer audio;

    public LightningSword(Player playerIBelongTo) {
        this.playerIBelongTo = playerIBelongTo;

        try
        {
            audio = new SFXPlayer();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    /**
     * Constructs an ArrayList of shells and labels this as the bullets' parent
     * @param mouseX The vertical line passing through the selected location
     * @param mouseY The horizontal line passing through the selected location
     */
    @Override
    public void shoot(double mouseX, double mouseY) {

        double angle;
        double x;
        double y;

        angle = World.atan(
                mouseX - playerIBelongTo.getX(),
                mouseY - playerIBelongTo.getY(),
                0
        );

        if (angle <= -3 * Math.PI / 4 && angle > -Math.PI) {
            y = playerIBelongTo.getY() - HALF_LENGTH;
            x = playerIBelongTo.getX() - HALF_LENGTH * Math.tan(angle);
        }
        else if (angle <= -Math.PI / 2) {
            x = playerIBelongTo.getX() - HALF_LENGTH;
            y = playerIBelongTo.getY() - HALF_LENGTH * Math.tan(Math.PI / 2 - angle);
        }
        else if (angle <= -Math.PI / 4) {
            x = playerIBelongTo.getX() - HALF_LENGTH;
            y = playerIBelongTo.getY() + HALF_LENGTH * Math.tan(angle + Math.PI / 2);
        }
        else if (angle <= 0) {
            y = playerIBelongTo.getY() + HALF_LENGTH;
            x = playerIBelongTo.getX() + HALF_LENGTH * Math.tan(angle);
        }
        else if (angle <= Math.PI / 4) {
            y = playerIBelongTo.getY() + HALF_LENGTH;
            x = playerIBelongTo.getX() + HALF_LENGTH * Math.tan(angle);
        }
        else if (angle <= Math.PI / 2) {
            x = playerIBelongTo.getX() + HALF_LENGTH;
            y = playerIBelongTo.getY() + HALF_LENGTH * Math.tan(Math.PI / 2 - angle);
        }
        else if (angle <= 3 * Math.PI / 4) {
            x = playerIBelongTo.getX() + HALF_LENGTH;
            y = playerIBelongTo.getY() - HALF_LENGTH * Math.tan(angle - Math.PI / 2);
        }
        else {
            y = playerIBelongTo.getY() - HALF_LENGTH;
            x = playerIBelongTo.getX() + HALF_LENGTH * Math.tan(-angle);
        }

        if (World.controller instanceof ServerController) {
            // new ShotgunBullet(Player.SERVER_PLAYER, mouseX, mouseY, DAMAGE);
            new Slash(x, y, angle, playerIBelongTo.isSwordLeft(), Player.SERVER_PLAYER);
            World.controller.getOutputConnection().sendPacket(new ClientSFXPacket(SERIAL));
            for (int i = 0; i < ROUND_COUNT; i++) {
                World.controller.getOutputConnection().sendPacket(
                        new ClientSlashPacket(x, y, angle, playerIBelongTo.isSwordLeft(), DAMAGE)
                );
            }
        } else if (World.controller instanceof SingleController) {
            // new ShotgunBullet(Player.SERVER_PLAYER, mouseX, mouseY, DAMAGE);
            if (playerIBelongTo.getPlayerNumber() == 0) {
                new Slash(x, y, angle, playerIBelongTo.isSwordLeft(), Player.SERVER_PLAYER);
            } else {
                new Slash(x, y, angle, playerIBelongTo.isSwordLeft(), Player.CLIENT_PLAYER);
            }
        } else {
            new Slash(x, y, angle, playerIBelongTo.isSwordLeft(), playerIBelongTo.getPlayerNumber());
            for (int i = 0; i < ROUND_COUNT; i++) {
                World.controller.getOutputConnection().sendPacket(
                        new ClientSlashPacket(x, y, angle, playerIBelongTo.isSwordLeft(), DAMAGE)
                );
            }
        }
        playerIBelongTo.setSwordLeft(!playerIBelongTo.isSwordLeft());
    }

    @Override
    public double getSPEED() {
        return SPEED;
    }

    @Override
    public int getMAX_DELAY() {
        return MAX_DELAY;
    }

    @Override
    public int getCurrentDelay() {
        return currentDelay;
    }

    @Override
    public void setCurrentDelay(int currentDelay) {
        this.currentDelay = currentDelay;
    }

    @Override
    public int getSERIAL() {
        return SERIAL;
    }

    @Override
    public String toString() {
        return SERIAL + ", Shotgun";
    }

    @Override
    public void playAudio() {
        try {
            audio.setFile(SERIAL);
            audio.play();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public WeaponType getWeaponType() {
        return WEAPON_TYPE;
    }
}
