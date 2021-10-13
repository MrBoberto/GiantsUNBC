package game;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

public class Connection implements Runnable{
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;

    private boolean running;
    private Controller controller;

    public Connection(Controller controller, Socket socket){
        this.controller = controller;

        try {
            outputStream = new ObjectOutputStream(socket.getOutputStream());
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
                controller.packetReceived(object);
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
        outputStream.close();
    }

    public void sendPacket(Object object){
        try {
            outputStream.reset();
            outputStream.writeObject(object);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
