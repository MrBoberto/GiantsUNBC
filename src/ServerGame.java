import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerGame extends Game{
    private ServerSocket serverSocket;
    private Socket socket;
    private OutputConnection outputConnection;
    private Expo expo;

    public static final int WIDTH = 600, HEIGHT = 600;

    public ServerGame(int playerNumber){
        super(playerNumber);

        try {
            if(playerNumber == 0){
                serverSocket = new ServerSocket(Game.PORT1);
            } else {
                serverSocket = new ServerSocket(Game.PORT2);

            }



            socket = serverSocket.accept();


            outputConnection = new OutputConnection(this, socket);

            Window window = new Window(this, WIDTH,HEIGHT);
            this.expo = new Expo(this);
            window.add(expo);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void inputReceived(int x, int y) {

        outputConnection.sendPacket(new UpdatePacket(x,y,thisPlayer));
    }



    @Override
    public void close() {
        try{
            outputConnection.close();
            serverSocket.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
