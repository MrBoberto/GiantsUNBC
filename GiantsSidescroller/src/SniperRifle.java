package GiantsSidescroller.src;

import java.util.ArrayList;

public class SniperRifle implements Weapon {
    private Object parent;
    private final double MOMENTUM = 15;
    private final int ROUNDCOUNT = 1;
    private final double INACCURACY = 0;
    private final int DELAY = 400;
    // Identifies type of gun
    private final int SERIAL = 001;

    public SniperRifle(Object parent) {
        this.parent = parent;
    }

    /**
     * Constructs an ArrayList of shells and labels this as the bullets' parent
     * @param x The vertical line passing through the selected location
     * @param y The horizontal line passing through the selected location
     */
    @Override
    public void shoot(double x, double y) {
        ArrayList<Nato> shell = new ArrayList<Nato>(ROUNDCOUNT);
        for (int i = 0; i < ROUNDCOUNT; i++) {
            shell.add(new Nato(x, y, this));
            System.out.println("Fired nato " + (i + 1));
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
        return SERIAL + ", Sniper Rifle";
    }
}
