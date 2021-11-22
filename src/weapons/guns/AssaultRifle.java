package weapons.guns;

import audio.SFXPlayer;
import game.ServerController;
import game.SingleController;
import game.World;
import packets.ClientBulletPacket;
import packets.ClientSFXPacket;
import packets.ServerSFXPacket;
import player.Player;
import weapons.ammo.AssaultRifleBullet;
import weapons.ammo.Projectile;

public class AssaultRifle implements Weapon {
    private final Player playerIBelongTo;
    public static final double SPEED = 32.5;
    public static final int ROUNDCOUNT = 1;
    public static final double INACCURACY = 0.075;
    public static final int MAX_DELAY = 0;
    private int currentDelay = 0;
    // Identifies type of gun
    public static final int SERIAL = 003;
    public static int DAMAGE = 8;
    public static String audioLocation = "resources/SFX/Assault Rifle.wav";
    public SFXPlayer audio;

    public AssaultRifle(Player playerIBelongTo) {
        this.playerIBelongTo = playerIBelongTo;
        try
        {
            audio = new SFXPlayer();
        }
        catch (Exception ex)
        {
            System.out.println("Error with playing assault rifle sound.");
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
        if (World.controller instanceof ServerController) {
            World.controller.getOutputConnection().sendPacket(new ServerSFXPacket(audioLocation));
            // new ShotgunBullet(Player.SERVER_PLAYER, mouseX, mouseY, DAMAGE);
            for (int i = 0; i < ROUNDCOUNT; i++) {
                new AssaultRifleBullet(Player.SERVER_PLAYER, mouseX, mouseY, DAMAGE);
            }
        } else if (World.controller instanceof SingleController) {
            // new ShotgunBullet(Player.SERVER_PLAYER, mouseX, mouseY, DAMAGE);
            if (playerIBelongTo.getPlayerNumber() == 0) {
                for (int i = 0; i < ROUNDCOUNT; i++) {
                    new AssaultRifleBullet(Player.SERVER_PLAYER, mouseX, mouseY, DAMAGE);
                }
            } else {
                for (int i = 0; i < ROUNDCOUNT; i++) {
                    new AssaultRifleBullet(Player.CLIENT_PLAYER, mouseX, mouseY, DAMAGE);
                }
            }
        } else {
            World.controller.getOutputConnection().sendPacket(new ClientSFXPacket(audioLocation));
            for (int i = 0; i < ROUNDCOUNT; i++) {
                World.controller.getOutputConnection().sendPacket(
                        new ClientBulletPacket(
                                playerIBelongTo.getX(),
                                playerIBelongTo.getY(),
                                mouseX,
                                mouseY,
                                Projectile.ProjectileType.AssaultRifleBullet,
                                DAMAGE
                        )
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
        return SERIAL + ", Assault Rifle";
    }

    @Override
    public double getDamage() {
        return DAMAGE * playerIBelongTo.getDamageMultiplier();
    }

    @Override
    public void playAudio() {
        try {
            audio.setFile(audioLocation);
            audio.play();
        } catch(Exception e) {
            System.out.println(e.getCause());
        }
    }
}
