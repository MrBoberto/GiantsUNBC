package game;

import javax.swing.*;
import java.security.SecureRandom;

public class World {
    private static World theWorld;
    private Controller controller;
    private String name;
    private SecureRandom sRandom = new SecureRandom();

    private World()
    {
        boolean isValidPlayerName = false;
        while (!isValidPlayerName) {
            name = (JOptionPane.showInputDialog("Enter your username")) + "";
            if (!name.equals("")) {
                System.out.println("Your username is: " + name);
                isValidPlayerName = true;
            }
        }

        int choice = Integer.parseInt(JOptionPane.showInputDialog("1 for server | 2 for client"));

        if(choice==1){
            controller = new ServerController();
            System.out.println("IM SERVER");
        } else if(choice == 2){
            controller = new ClientController();
            System.out.println("IM CLIENT");
        }
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
        return this.controller;
    }

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

    public String getName() {
        return name;
    }
}
