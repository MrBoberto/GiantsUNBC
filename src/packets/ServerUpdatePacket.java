package packets;

import player.Player;

import java.io.Serializable;

public class ServerUpdatePacket implements Serializable {

    private double x;
    private double y;
    double angle;
    private int[] health = new int[2];

    public ServerUpdatePacket(double x, double y, double angle, int[] health) {
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

    public int[] getHealth() {
        return health;
    }
}
