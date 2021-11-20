package packets;

import java.io.Serializable;

public class ClientUpdatePacket implements Serializable {

    private double x;
    private double y;
    private double angle;
    private int health;

    public ClientUpdatePacket(double x, double y, double angle, int health) {
        this.x = x;
        this.y = y;
        this.angle = angle;
        this.health = health;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getAngle() {
        return angle;
    }

    public int getHealth() {
        return health;
    }
}
