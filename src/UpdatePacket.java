import java.io.Serializable;

public class UpdatePacket implements Serializable {

    private int x;
    private int y;
    private int currentPlayer;

    public UpdatePacket(int x, int y, int currentPlayer) {
        this.x = x;
        this.y = y;
        this.currentPlayer = currentPlayer;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getCurrentPlayer() {
        return currentPlayer;
    }
}
