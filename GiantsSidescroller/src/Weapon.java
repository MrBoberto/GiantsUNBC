package GiantsSidescroller.src;

public interface Weapon {
    public void shoot(double x, double y);
    public Object getParent();
    // Measured in kg * pixels / sec
    public double getMOMENTUM();
    public double getINACCURACY();
    public int getSERIAL();
    // Returns the name of the weapon
    public String toString();
}
