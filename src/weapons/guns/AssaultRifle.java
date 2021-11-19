package weapons.guns;

import game.ServerController;
import game.World;
import packets.ClientBulletPacket;
import player.Player;
import weapons.ammo.Projectile;
//import weapons.ammo.Nato;
import weapons.ammo.ShotgunBullet;

public class AssaultRifle implements Weapon {
    private final Player playerIBelongTo;
    public static final double MOMENTUM = 0.65;
    public static final int ROUNDCOUNT = 1;
    public static final double INACCURACY = 0.075;
    public static final int MAX_DELAY = 1;
    private int currentDelay = 0;
    // Identifies type of gun
    public static final int SERIAL = 000;
    public static int DAMAGE = 8;

    public AssaultRifle(Player playerIBelongTo) {
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
                new ShotgunBullet(Player.SERVER_PLAYER, mouseX, mouseY, DAMAGE);
            }
        } else {
            for (int i = 0; i < ROUNDCOUNT; i++) {
                World.controller.getOutputConnection().sendPacket(
                        new ClientBulletPacket(
                                playerIBelongTo.getX(),
                                playerIBelongTo.getY(),
                                mouseX,
                                mouseY,
                                Projectile.ProjectileType.ShotgunBullet,
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
    public double getMOMENTUM() {
        return MOMENTUM;
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
}
