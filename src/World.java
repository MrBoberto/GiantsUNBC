import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;

public class World {
    private static World theWorld;
    private Controller theController;
    private SecureRandom sRandom = new SecureRandom();

    private World()
    {
        theController = new Controller();
    }

    /**
     * Ensures there can only be one world
     * @return
     */
    public static World getWorld() {
        if (theWorld == null)
        {
            theWorld = new World();
        }
        return theWorld;
    }

    public Controller getController() {
        return this.theController;
    }

    /**
     * Returns an angle based on the opposite and adjacent side lengths. If this fails, returns the given angle.
     * @param x
     * @param y
     * @param angle
     * @return
     */
    public double atan(double x, double y, double angle) {
        if (x == 0 && y == 0) {
            return angle;
        } else if (x == 0) {
            if (y > 0) {
                return 0;
            } else {
                return Math.PI;
            }
        } else if (y == 0) {
            if (x > 0) {
                return Math.PI / 2;
            } else {
                return 3 * Math.PI / 2;
            }
        } else if (x > 0 && y < 0) {
            return (Math.atan(-y / x) + Math.PI / 2);
        } else if (x < 0 && y < 0) {
            return (Math.atan(-x / -y) - Math.PI);
        } else if (x < 0 && y > 0) {
            return (Math.atan(y / -x) - Math.PI / 2);
        } else {
            return (Math.atan(x / y));
        }
    }

    public double cosAdj(double hyp, double angle) {
        if (angle == Math.PI || angle == Math.PI / 2 || angle == 0 || angle == -Math.PI / 2) {
            return hyp;
        } else if (angle > Math.PI / 2) {
            return hyp * Math.cos(angle - Math.PI / 2);
        } else if (angle > 0) {
            return hyp * Math.cos(angle);
        } else if (angle > -Math.PI / 2) {
            return -hyp * Math.cos(angle + Math.PI / 2);
        } else {
            return -hyp * Math.cos(angle + Math.PI);
        }
    }

    public double sinOpp(double hyp, double angle) {
        if (angle == Math.PI || angle == Math.PI / 2 || angle == 0 || angle == -Math.PI / 2) {
            return 0;
        } else if (angle > Math.PI / 2) {
            return -hyp * Math.sin(angle - Math.PI / 2);
        } else if (angle > 0) {
            return hyp * Math.sin(angle);
        } else if (angle > -Math.PI / 2) {
            return hyp * Math.sin(angle + Math.PI / 2);
        } else {
            return -hyp * Math.sin(angle + Math.PI);
        }
    }

    public SecureRandom getSRandom() {
        return sRandom;
    }
}
