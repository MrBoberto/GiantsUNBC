package packets;

import java.io.Serializable;

public record ClientUpdatePacket(double x, double y, double angle, boolean isWalking,
                                 int weaponSerial) implements Serializable {

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getAngle() {
        return angle;
    }

    public int getWeaponSerial() {
        return weaponSerial;
    }
}
