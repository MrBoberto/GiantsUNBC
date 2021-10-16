import java.util.ArrayList;

public class Shotgun implements Weapon {
    private Object parent;
    private final double MOMENTUM = 0.85;
    private final int ROUNDCOUNT = 10;
    private final double INACCURACY = 0.1;
    public final int MAX_DELAY = 40;
    private int currentDelay = 0;
    // Identifies type of gun
    private final int SERIAL = 000;

    public Shotgun(Object parent) {
        this.parent = parent;
    }

    /**
     * Constructs an ArrayList of shells and labels this as the bullets' parent
     * @param x The vertical line passing through the selected location
     * @param y The horizontal line passing through the selected location
     */
    @Override
    public void shoot(double x, double y) {
        ArrayList<Shot> shell = new ArrayList<Shot>(ROUNDCOUNT);
        for (int i = 0; i < ROUNDCOUNT; i++) {
            shell.add(new Shot(x, y, this));
            System.out.println("Fired shot " + (i + 1));
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
}
