package game;

/**
 * This file is part of a solution to
 *		CPSC300 Term Project Fall 2021
 *
 * In a multiplayer matchup, receives packets from the other player
 *
 * @author The Boyz
 * @version 1
 */

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.SocketException;

public class InputConnection implements Runnable {

    private ObjectInputStream inputStream;

    private boolean running;
    private final Controller controller;

    public InputConnection(Controller controller, Socket socket) {
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
        while (running) {
            try {
                Object object = inputStream.readObject();
                controller.packetReceived(object);
            } catch (EOFException | SocketException e) {
                running = false;
            } catch (IOException | ClassNotFoundException e) {
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
