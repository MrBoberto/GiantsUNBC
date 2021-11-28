package game;

import javax.swing.*;
import java.net.UnknownHostException;
import java.security.SecureRandom;

import static java.lang.Integer.parseInt;

public final class World {
    public static Controller controller;
    public static SecureRandom sRandom = new SecureRandom();

    public World(int choice) throws UnknownHostException {

        if(choice==1){
            controller = new ServerControllerAutomatic();
            System.out.println("IM SERVER");
        } else if(choice == 2){
            controller = new ClientControllerAutomatic();
            System.out.println("IM CLIENT");
        } else if (choice == 3) {
            controller = new SingleController();
            System.out.println("Loading singleplayer mode");
        }
    }

    public static double pythHyp(double a, double b) {
        return Math.sqrt((Math.pow(a, 2)) + (Math.pow(b, 2)));
    }

    public static double pythSide(double c, double a) {
        return Math.sqrt((Math.pow(c, 2)) - (Math.pow(a, 2)));
    }


    public static double atan(double x, double y, double angle) {
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

    public static double cosAdj(double hyp, double angle) {
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

    public static double sinOpp(double hyp, double angle) {
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

    public static SecureRandom getSRandom() {
        return sRandom;
    }
}
