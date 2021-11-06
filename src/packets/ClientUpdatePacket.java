package packets;

import java.io.Serializable;

public class ClientUpdatePacket implements Serializable {

    private double x;
    private double y;
    private double angle;

    public ClientUpdatePacket(double x, double y, double angle) {
        this.x = x;
        this.y = y;
        this.angle = angle;
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
}
