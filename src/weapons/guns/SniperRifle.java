package weapons.guns;

import audio.SFXPlayer;
import game.ServerController;
import game.SingleController;
import game.World;
import packets.ClientBulletPacket;
import packets.ClientSFXPacket;
import packets.ServerSFXPacket;
import player.Player;
import weapons.ammo.Projectile;
import weapons.ammo.SniperRifleBullet;

public class SniperRifle implements Weapon {
    private final Player playerIBelongTo;
    public static final double SPEED = 30;
    public static final int ROUND_COUNT = 1;
    public static final int MAX_DELAY = 200;
    private int currentDelay = 0;
    // Identifies type of gun
    public static final int SERIAL = 1;
    public static final WeaponType WEAPON_TYPE = WeaponType.SniperRifle;
    public static final int DAMAGE = 100;
    public SFXPlayer audio;

    public SniperRifle(Player playerIBelongTo) {
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
        if (World.controller instanceof ServerController) {
            World.controller.getOutputConnection().sendPacket(new ServerSFXPacket(SERIAL));
            // new ShotgunBullet(Player.SERVER_PLAYER, mouseX, mouseY, DAMAGE);
            for (int i = 0; i < ROUND_COUNT; i++) {
                new SniperRifleBullet(Player.SERVER_PLAYER, mouseX, mouseY, DAMAGE);
            }
        } else if (World.controller instanceof SingleController) {
            // new ShotgunBullet(Player.SERVER_PLAYER, mouseX, mouseY, DAMAGE);
            if (playerIBelongTo.getPlayerNumber() == 0) {
                for (int i = 0; i < ROUND_COUNT; i++) {
                    new SniperRifleBullet(Player.SERVER_PLAYER, mouseX, mouseY, DAMAGE);
                }
            } else {
                for (int i = 0; i < ROUND_COUNT; i++) {
                    new SniperRifleBullet(Player.CLIENT_PLAYER, mouseX, mouseY, DAMAGE);
                }
            }
        } else {
            World.controller.getOutputConnection().sendPacket(new ClientSFXPacket(SERIAL));
            for (int i = 0; i < ROUND_COUNT; i++) {
                World.controller.getOutputConnection().sendPacket(
                        new ClientBulletPacket(
                                playerIBelongTo.getX(),
                                playerIBelongTo.getY(),
                                mouseX,
                                mouseY,
                                Projectile.ProjectileType.SniperRifleBullet,
                                DAMAGE
                        )
                );
            }
        }
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
        return SERIAL + ", Sniper Rifle";
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
