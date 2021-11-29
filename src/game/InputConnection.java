package game;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

public class InputConnection implements Runnable{

    private ObjectInputStream inputStream;

    private boolean running;
    private Controller controller;

    public InputConnection(Controller controller, Socket socket){
        this.controller = controller;

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
        System.out.println("hello from input thread");
        while(running){
            try {
                Object object = inputStream.readObject();
                controller.packetReceived(object);
            } catch (EOFException | SocketException e){
                running = false;
            }

            catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }

        }
        System.out.println("ERROR");
    }

    public void close() throws IOException {
        running = false;
        inputStream.close();
    }


}
