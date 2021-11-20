package game;

import javax.swing.*;
import java.security.SecureRandom;

import static java.lang.Integer.parseInt;

public final class World {
    public static Controller controller;
    private static SecureRandom sRandom = new SecureRandom();

    public World()
    {
       int choice = Integer.parseInt(JOptionPane.showInputDialog("1 for server | 2 for client"));
        choiceOpt = choice;

        if(choice==1){
            controller = new ServerController();
            System.out.println("IM SERVER");
        } else if(choice == 2){
            controller = new ClientController();
            System.out.println("IM CLIENT");
        }
    }
    public static int getChoiceOpt() {
        return choiceOpt;
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
