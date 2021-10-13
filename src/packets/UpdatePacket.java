package packets;

import player.Player;

import java.io.Serializable;

public class UpdatePacket implements Serializable {

    private double[] x;
    private double[] y;
    double[] angle;

    public UpdatePacket( double[] x, double[] y, double[] angle) {
        this.x = x;
        this.y = y;
        this.angle = angle;
    }


    public double[] getX() {
        return x;
    }

    public double[] getY() {
        return y;
    }

    public double[] getAngle() {
        return angle;
    }
}
