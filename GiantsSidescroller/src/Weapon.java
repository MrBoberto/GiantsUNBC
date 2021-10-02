package GiantsSidescroller.src;

public interface Weapon {
    public void shoot(double x, double y);
    public Object getParent();
    // Measured in kg * pixels / sec
    public double getMOMENTUM();
    public int getSERIAL();
}
