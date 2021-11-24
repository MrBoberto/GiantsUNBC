package packets;

import player.Player;

import java.io.Serializable;

public class ServerUpdatePacket implements Serializable {

    private double x;
    private double y;
    double angle;
    private int[] health;
    boolean isWalking;
    int weaponSerial;
    boolean isInvincible[];

    public ServerUpdatePacket(double x, double y, double angle, int[] health, boolean isWalking, int weaponSerial, boolean[] isInvincible) {
        this.x = x;
        this.y = y;
        this.angle = angle;
        this.health = health;
        this.isWalking = isWalking;
        this.weaponSerial = weaponSerial;
        this.isInvincible = isInvincible;
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

    public int[] getHealth() {
        return health;
    }

    public boolean isWalking() {
        return isWalking;
    }

    public int getWeaponSerial() {
        return weaponSerial;
    }

    public boolean[] isInvincible() {
        return isInvincible;
    }
}
