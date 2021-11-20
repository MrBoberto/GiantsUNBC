package packets;

import java.io.Serializable;

public class ClientUpdatePacket implements Serializable {

    private double x;
    private double y;
    private double angle;
    boolean isWalking;

    public ClientUpdatePacket(double x, double y, double angle, boolean isWalking) {
        this.x = x;
        this.y = y;
        this.angle = angle;
        this.isWalking = isWalking;
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


    public boolean isWalking() {
        return isWalking;
    }
}
