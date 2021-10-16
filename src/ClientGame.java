import java.io.IOException;
import java.net.Socket;

public class ClientGame extends Game{

    Socket socket;
    InputConnection inputConnection;

    public ClientGame(int playerNumber){
        super(playerNumber);

        try {
            if(thisPlayer == 0){
                socket = new Socket("localhost", Game.PORT2);
            } else {
                socket = new Socket("localhost", Game.PORT1);
            }

            inputConnection = new InputConnection(this, socket);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void packetReceived(Object object) {
        if(object instanceof UpdatePacket packet){
            playersPositions[packet.getCurrentPlayer()][0]=packet.getX();
            playersPositions[packet.getCurrentPlayer()][1]= packet.getY();
        }
    }

    @Override
    public void close() {
        try {
            inputConnection.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
