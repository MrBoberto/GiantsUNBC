package game;

import packets.ClientUpdatePacket;
import packets.ServerUpdatePacket;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class OutputConnection implements Runnable {
    private ObjectOutputStream outputStream;

    private boolean running;
    private boolean gameRunning = true;
    private Controller controller;

    public OutputConnection(Controller controller, Socket socket) {
        this.controller = controller;

        try {
            outputStream = new ObjectOutputStream(socket.getOutputStream());

        } catch (IOException e) {
            e.printStackTrace();
        }

        new Thread(this).start();
    }

    @Override
    public void run() {
        running = true;
        while (running) {


            // Send current state of game to clients.
            if (gameRunning) {
                if (controller instanceof ServerController) {
                    double[] x = new double[Controller.livingPlayers.size()];
                    double[] y = new double[Controller.livingPlayers.size()];
                    double[] angle = new double[Controller.livingPlayers.size()];

                    for (int i = 0; i < Controller.livingPlayers.size(); i++) {
                        x[i] = Controller.livingPlayers.get(i).getX();
                        y[i] = Controller.livingPlayers.get(i).getY();
                        angle[i] = Controller.livingPlayers.get(i).getAngle();


                    }

                    sendPacket(new ServerUpdatePacket(x, y, angle));
                } else {
                    if(controller.getPlayer() != null){
                        sendPacket(new ClientUpdatePacket(
                                controller.getPlayer().getPlayerNumber(),
                                controller.getPlayer().getX(),
                                controller.getPlayer().getY(),
                                controller.getPlayer().getAngle()));
                    }

                }

            }
            try {
                Thread.sleep(10);
            } catch(InterruptedException e) {
                e.printStackTrace();
            }

        }
        System.out.println("OUTPUT ERROR");
    }

    public void close() throws IOException {
        running = false;
        outputStream.close();
    }

    public void sendPacket(Object object) {
        try {
            outputStream.reset();
            outputStream.writeObject(object);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isGameRunning() {
        return gameRunning;
    }

    public void setGameRunning(boolean gameRunning) {
        this.gameRunning = gameRunning;
    }
}
