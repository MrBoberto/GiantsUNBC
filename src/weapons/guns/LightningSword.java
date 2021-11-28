package weapons.guns;

import audio.SFXPlayer;
import game.*;
import packets.ClientBulletPacket;
import packets.ClientSFXPacket;
import packets.ClientSlashPacket;
import packets.ServerSFXPacket;
import player.Player;
import weapons.ammo.Projectile;
import weapons.ammo.ShotgunBullet;
import weapons.aoe.Slash;

public class LightningSword implements Weapon {
    private final Player playerIBelongTo;
    public static final double SPEED = 0;
    public static final int ROUNDCOUNT = 5;
    public static final double INACCURACY = 0.1;
    public static final int MAX_DELAY = 4;
    private int currentDelay = 0;
    // Identifies type of gun
    public static final int SERIAL = 005;
    public static int DAMAGE = 100;
    public static int HALF_LENGTH = 60;         // The length of half of one side
    public SFXPlayer audio;

    public LightningSword(Player playerIBelongTo) {
        this.playerIBelongTo = playerIBelongTo;

        try
        {
            audio = new SFXPlayer();
        }
        catch (Exception ex)
        {
            System.out.println("Error with playing shotgun sound.");
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

        double angle = 0;
        double x = 0;
        double y = 0;

        angle = World.atan(
                mouseX - playerIBelongTo.getX(),
                mouseY - playerIBelongTo.getY(),
                0
        );

        if (angle <= -3 * Math.PI / 4 && angle > -Math.PI) {
            y = playerIBelongTo.getY() - HALF_LENGTH;
            x = playerIBelongTo.getX() - HALF_LENGTH * Math.tan(angle);
        } else if (angle <= -Math.PI / 2) {
            x = playerIBelongTo.getX() - HALF_LENGTH;
            y = playerIBelongTo.getY() - HALF_LENGTH * Math.tan(Math.PI / 2 - angle);
        } else if (angle <= -Math.PI / 4) {
            x = playerIBelongTo.getX() - HALF_LENGTH;
            y = playerIBelongTo.getY() + HALF_LENGTH * Math.tan(angle);
        } else if (angle <= 0) {
            y = playerIBelongTo.getY() + HALF_LENGTH;
            x = playerIBelongTo.getX() - HALF_LENGTH * Math.tan(Math.PI / 2 - angle);
        } else if (angle <= Math.PI / 4) {
            y = playerIBelongTo.getY() + HALF_LENGTH;
            x = playerIBelongTo.getX() + HALF_LENGTH * Math.tan(angle);
        } else if (angle <= Math.PI / 2) {
            x = playerIBelongTo.getX() + HALF_LENGTH;
            y = playerIBelongTo.getY() - HALF_LENGTH * Math.tan(angle);
        } else if (angle <= 3 * Math.PI / 4) {
            x = playerIBelongTo.getX() + HALF_LENGTH;
            y = playerIBelongTo.getY() + HALF_LENGTH * Math.tan(angle);
        } else {
            y = playerIBelongTo.getY() - HALF_LENGTH;
            x = playerIBelongTo.getX() + HALF_LENGTH * Math.tan(angle);
        }

        if (World.controller instanceof ServerController) {
            World.controller.getOutputConnection().sendPacket(new ServerSFXPacket(SERIAL));
            // new ShotgunBullet(Player.SERVER_PLAYER, mouseX, mouseY, DAMAGE);
            new Slash(x, y, angle, Player.SERVER_PLAYER, DAMAGE);
        } else if (World.controller instanceof SingleController) {
            // new ShotgunBullet(Player.SERVER_PLAYER, mouseX, mouseY, DAMAGE);
            if (playerIBelongTo.getPlayerNumber() == 0) {
                new Slash(x, y, angle, Player.SERVER_PLAYER, DAMAGE);
            } else {
                new Slash(x, y, angle, Player.CLIENT_PLAYER, DAMAGE);
            }
        } else {
            World.controller.getOutputConnection().sendPacket(new ClientSFXPacket(SERIAL));
            for (int i = 0; i < ROUNDCOUNT; i++) {
                World.controller.getOutputConnection().sendPacket(
                        new ClientSlashPacket(x, y, angle, DAMAGE)
                );
            }
        }
    }

    @Override
    public Player getPlayerIBelongTo() {
        return playerIBelongTo;
    }

    @Override
    public double getSPEED() {
        return SPEED;
    }

    @Override
    public double getINACCURACY() {
        return INACCURACY;
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
    public double getDamage() {
        return DAMAGE * playerIBelongTo.getDamageMultiplier();
    }

    @Override
    public void playAudio() {
        try {
            audio.setFile(SERIAL);
            audio.play();
        } catch(Exception e) {
            System.out.println(e.getCause());
        }
    }
}
