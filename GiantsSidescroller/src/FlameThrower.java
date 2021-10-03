package GiantsSidescroller.src;

import java.util.ArrayList;

public class FlameThrower implements Weapon {
    private Object parent;
    private final double MOMENTUM = 0.4;
    private final int ROUNDCOUNT = 20;
    private final double INACCURACY = 0.4;
    private final int DELAY = 0;
    // Identifies type of gun
    private final int SERIAL = 002;

    public FlameThrower(Object parent) {
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
            System.out.println("Fired fireball " + (i + 1));
        }
    }

    @Override
    public Object getParent() {
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
    public int getSERIAL() {
        return SERIAL;
    }

    @Override
    public String toString() {
        return SERIAL + ", Flame Thrower";
    }
}
