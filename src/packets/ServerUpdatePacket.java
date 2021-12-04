package packets;

import java.io.Serializable;

public record ServerUpdatePacket(double x, double y, double angle, int[] health, boolean isWalking, int weaponSerial,
                                 boolean[] isInvincible) implements Serializable {


    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getAngle() {
        return angle;
    }

    public int[] getHealth() {
        return health;
    }

    public int getWeaponSerial() {
        return weaponSerial;
    }
}
