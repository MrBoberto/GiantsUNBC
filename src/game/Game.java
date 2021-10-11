package game;

import gui.GameWindow;
import gui.Window;

import javax.swing.*;

public abstract class Game {

    public static final int PORT = 55555;

    public static final int WIDTH = 600, HEIGHT = 600;
    public static final int FIELD_WIDTH = WIDTH / 3, FIELD_HEIGHT = HEIGHT / 3;

    public static final int NOBODY = 0, PLAYER_ONE = 1, PLAYER_TWO = 2;
    protected int[][] fields;

    private Window window;
    protected GameWindow gameWindow;

    protected int currentPlayer;
    protected int thisPlayer;

    public Game(int thisPlayer) {
        this.thisPlayer = thisPlayer;
        window = new Window(this, "TicTacToe", WIDTH, HEIGHT);
        gameWindow = new GameWindow(this);
        fields = new int[3][3];
        window.add(gameWindow);
        window.setVisible(true);
        currentPlayer = Game.PLAYER_ONE;
    }

    protected void showWinner(int winner) {

        if (winner == Game.NOBODY) {
            JOptionPane.showMessageDialog(null, "TIE!");

        } else {
            JOptionPane.showMessageDialog(null, "The player " + winner + " has won the game!");
        }
    }

    protected boolean isMyTurn() {
        return thisPlayer == currentPlayer;
    }

    public abstract void inputReceived(int x, int y);

    public abstract void packetReceived(Object object);

    public abstract void close();

    public int[][] getFields() {
        return fields;
    }
}
