package weapons.guns;

import game.ServerController;
import game.SingleController;
import game.World;
import packets.ClientBulletPacket;
import player.Player;
import weapons.ammo.AssaultRifleBullet;
import weapons.ammo.Projectile;
import weapons.ammo.ShotgunBullet;
import weapons.ammo.SniperRifleBullet;

import java.util.ArrayList;

public class SniperRifle implements Weapon {
    private Player playerIBelongTo;
    public static final double SPEED = 40;
    public static final int ROUNDCOUNT = 1;
    public static final double INACCURACY = 0;
    public static final int MAX_DELAY = 200;
    private int currentDelay = 0;
    // Identifies type of gun
    private static final int SERIAL = 001;
    public static final int DAMAGE = 100;

    public SniperRifle(Player playerIBelongTo) {
        this.playerIBelongTo = playerIBelongTo;
    }

    /**
     * Constructs an ArrayList of shells and labels this as the bullets' parent
     * @param mouseX The vertical line passing through the selected location
     * @param mouseY The horizontal line passing through the selected location
     */
    @Override
    public void shoot(double mouseX, double mouseY) {
        if (World.controller instanceof ServerController) {
            // new ShotgunBullet(Player.SERVER_PLAYER, mouseX, mouseY, DAMAGE);
            for (int i = 0; i < ROUNDCOUNT; i++) {
                new SniperRifleBullet(Player.SERVER_PLAYER, mouseX, mouseY, DAMAGE);
            }
        } else if (World.controller instanceof SingleController) {
            // new ShotgunBullet(Player.SERVER_PLAYER, mouseX, mouseY, DAMAGE);
            if (playerIBelongTo.getPlayerNumber() == 0) {
                for (int i = 0; i < ROUNDCOUNT; i++) {
                    new SniperRifleBullet(Player.SERVER_PLAYER, mouseX, mouseY, DAMAGE);
                }
            } else {
                for (int i = 0; i < ROUNDCOUNT; i++) {
                    new SniperRifleBullet(Player.CLIENT_PLAYER, mouseX, mouseY, DAMAGE);
                }
            }
        } else {
            for (int i = 0; i < ROUNDCOUNT; i++) {
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
        return SERIAL + ", Sniper Rifle";
    }

    @Override
    public double getDamage() {
        return DAMAGE * playerIBelongTo.getDamageMultiplier();
    }
}
