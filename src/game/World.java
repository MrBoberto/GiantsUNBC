package game;

/**
 * This file is part of a solution to
 *		CPSC300 Term Project Fall 2021
 *
 * This class initializes the controller based on what game mode the player is using. It also contains some
 * trigonometric functions to be used by other classes to help with calculating directions of movement
 *
 * @author The Boyz
 * @version 1
 */

import java.net.UnknownHostException;
import java.security.SecureRandom;

public class World {
    public static Controller controller;
    public static final SecureRandom sRandom = new SecureRandom();
    protected static GameWindow gameWindow;
    protected static GameOver gameOver;                       // Should be null except at the end

    public static void world(int choice) {

        if(choice==1){
            controller = new ServerController();
        } else if(choice == 2){
            controller = new ClientController();
        } else if (choice == 3) {
            controller = new SingleController();
        }
    }

    public static double pythHyp(double a, double b) {
        return Math.sqrt((Math.pow(a, 2)) + (Math.pow(b, 2)));
    }

    public static GameWindow getGameWindow() {
        return gameWindow;
    }

    public static void setGameWindow(GameWindow gameWindow) {
        World.gameWindow = gameWindow;
    }

    public static GameOver getGameOver() {
        return gameOver;
    }

    public static void setGameOver(GameOver gameOver) {
        World.gameOver = gameOver;
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
}
