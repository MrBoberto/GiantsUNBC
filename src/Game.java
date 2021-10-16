public abstract class Game {
    public static final int PORT1 = 55555;
    public static final int PORT2 = 55554;

    public static int[][] playersPositions = new int[4][2];

    public static int thisPlayer;

    public Game(int thisPlayer){
        Game.thisPlayer = thisPlayer;
    }


    public abstract void close();
}
