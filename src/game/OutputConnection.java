package game;

import packets.ServerBulletPacket;
import packets.ClientUpdatePacket;
import packets.ServerUpdatePacket;
import weapons.ammo.Bullet;
import weapons.ammo.Projectile;
import weapons.ammo.ShotgunBullet;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Arrays;

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
                    //TODO: UPDATE player 2 movement to have lag?
//                    double[] x = new double[2];
//                    double[] y = new double[Controller.PLAYER_COUNT];
//                    double[] angle = new double[Controller.PLAYER_COUNT];


//                    x[0] = Controller.thisPlayer.getX();
//                    y[0] = Controller.thisPlayer.getY();
//                    angle[0] = Controller.thisPlayer.getAngle();

//                    x[1] = Controller.otherPlayer.getX();
//                    y[1] = Controller.otherPlayer.getY();
//                    angle[1] = Controller.otherPlayer.getAngle();

                    int[] serverHealths = {Controller.thisPlayer.getHealth(), Controller.otherPlayer.getHealth()};

                    sendPacket(new ServerUpdatePacket(Controller.thisPlayer.getX(), Controller.thisPlayer.getY(),
                            Controller.thisPlayer.getAngle(), serverHealths));
//
//                    double x[] = new double[Controller.movingAmmo.size()];
//                    double y[] = new double[Controller.movingAmmo.size()];
//                    int ID[] = new int[Controller.movingAmmo.size()];
//                    int playerIBelongTo[] = new int[Controller.movingAmmo.size()];
//                    Projectile.Type[] types = new Projectile.Type[Controller.movingAmmo.size()];
//                    for (int i = 0; i < Controller.movingAmmo.size(); i++) {
//                        x[i] = Controller.movingAmmo.get(i).getX();
//                        y[i] = Controller.movingAmmo.get(i).getY();
//                        ID[i] = Controller.movingAmmo.get(i).getID();
//                        playerIBelongTo[i] = Controller.movingAmmo.get(i).getPlayerIBelongTo();
//                        types[i] = Controller.movingAmmo.get(i).getTYPE();
//                    }
                    sendPacket(new ServerBulletPacket(Controller.movingAmmo.toArray(new Bullet[0])));
                } else {
                    if (controller.getPlayer() != null) {
                        sendPacket(new ClientUpdatePacket(
                                controller.getPlayer().getX(),
                                controller.getPlayer().getY(),
                                controller.getPlayer().getAngle(),
                                controller.getPlayer().getHealth()));
                    }

                }

            }
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        System.out.println("OUTPUT ERROR");
    }

    public void close() throws IOException {
        running = false;
        outputStream.close();
    }

    public synchronized void sendPacket(Object object) {
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
