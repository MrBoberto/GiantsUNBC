package game;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;

public class Connection implements Runnable{

    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;

    private boolean running;
    private Game game;

    public Connection(Game game, Socket socket){
        this.game = game;

        try {
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        new Thread(this).start();
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
        outputStream.close();
    }
}
