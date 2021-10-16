import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

public class InputConnection implements Runnable {

    private ObjectInputStream inputStream;

    private boolean running;
    private ClientGame game;

    public InputConnection(ClientGame game, Socket socket){
        this.game = game;

        try {
            inputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        new Thread(this).start();
    }

    @Override
    public void run() {
        running = true;
        while(running){
            try {
                Object object = inputStream.readObject();
                game.packetReceived(object);
            } catch (EOFException | SocketException e){
                running = false;
            }

            catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }

        }
    }

    public void close() throws IOException {
        running = false;

        inputStream.close();
    }
}
