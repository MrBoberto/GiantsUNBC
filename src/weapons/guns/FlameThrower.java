package weapons.guns;

import player.Player;
import weapons.Weapon;
import weapons.ammo.Fireball;

import java.util.ArrayList;

import game.Thing;

public class FlameThrower implements Weapon {
    private Player parent;
    private final double MOMENTUM = 0.28;
    private final int ROUNDCOUNT = 20;
    private final double INACCURACY = 0.5;
    public final int MAX_DELAY = 0;
    private int currentDelay = 0;
    // Identifies type of gun
    private final int SERIAL = 002;
    private final double DAMAGE = 1.0;

    public FlameThrower(Player parent) {
        this.parent = parent;
    }

    /**
     * Constructs an ArrayList of shells and labels this as the bullets' parent
     * @param x The vertical line passing through the selected location
     * @param y The horizontal line passing through the selected location
     */
    @Override
    public void shoot(double x, double y) {
        ArrayList<Fireball> shell = new ArrayList<Fireball>(ROUNDCOUNT);
        for (int i = 0; i < ROUNDCOUNT; i++) {
            shell.add(new Fireball(x, y, this));
//            System.out.println("Fired fireball " + (i + 1));
        }
    }

    @Override
    public Player getParent() {
        return parent;
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
        return SERIAL + ", Flame Thrower";
    }

    @Override
    public double getDamage() {
        return this.DAMAGE * parent.getDamageMultiplier();
    }
}
