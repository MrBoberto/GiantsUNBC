import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

public class OutputConnection implements Runnable{

    private ObjectOutputStream outputStream;

    private ServerGame game;
    private Object object;

    public OutputConnection(ServerGame game, Socket socket){
        this.game = game;

        try {
            outputStream = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendPacket(Object object){

        this.object = object;
        new Thread(this).start();
    }

    @Override
    public void run() {
        try {
            outputStream.reset();
            outputStream.writeObject(object);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() throws IOException {
        outputStream.close();
    }
}
