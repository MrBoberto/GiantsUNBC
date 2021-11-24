package weapons.guns;

import game.ServerController;
import game.World;
import packets.ClientBulletPacket;
import game.World;
import player.Player;
import weapons.ammo.Projectile;
//import weapons.ammo.Nato;
import weapons.ammo.ShotgunBullet;

public class Shotgun implements Weapon {
    private Player playerIBelongTo;
    public static final double MOMENTUM = 0.85;
    public static final int ROUNDCOUNT = 10;
    public static final double INACCURACY = 0.1;
    public static final int MAX_DELAY = 40;
    private int currentDelay = 0;
    // Identifies type of gun
    public static final int SERIAL = 000;
    public static int DAMAGE = 10;
    public static final Type TYPE = Type.Shotgun;

    public Shotgun(Player playerIBelongTo) {
        this.playerIBelongTo = playerIBelongTo;
    }

    /**
     * Constructs an ArrayList of shells and labels this as the bullets' parent
     * @param aimX The vertical line passing through the selected location
     * @param aimY The horizontal line passing through the selected location
     */
    //TODO: MERGEEEEEEEE
    @Override
    public void shoot(double mouseX, double mouseY) {
        if (World.controller instanceof ServerController) {
            new ShotgunBullet(Player.SERVER_PLAYER, mouseX, mouseY, DAMAGE);
        } else {
            World.controller.getOutputConnection().sendPacket(new ClientBulletPacket(playerIBelongTo.getX(), playerIBelongTo.getY(), mouseX, mouseY, Projectile.Type.ShotgunBullet, DAMAGE));
        }
    }
//            public void shoot(double aimX, double aimY) {
//        ArrayList<Shot> shell = new ArrayList<Shot>(ROUNDCOUNT);
//        for (int i = 0; i < ROUNDCOUNT; i++) {
//            shell.add(new Shot(parent.getX(), parent.getY(), aimX, aimY, parent.getPlayerNumber(),
//                    MOMENTUM, parent.getDamageMultiplier(),
//                    World.getWorld().atan(aimX - getParent().getX(),
//                            aimY - getParent().getY(), 0) - INACCURACY / 2
//                            + getINACCURACY() * World.getWorld().getSRandom().nextDouble()));
////            System.out.println("Fired shot " + (i + 1));
//        }
//    }

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
        return SERIAL + ", Shotgun";
    }

    @Override
    public double getDamage() {
        return DAMAGE * playerIBelongTo.getDamageMultiplier();
    }
}
