package packets;

import java.io.Serializable;

public record ClientSlashPacket(double x, double y, double angle, boolean isLeft, int damage) implements Serializable {

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
