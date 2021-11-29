package game;

import packets.ServerBulletPacket;
import packets.ClientUpdatePacket;
import packets.ServerExplosionPacket;
import packets.ServerUpdatePacket;
import weapons.ammo.Bullet;
import weapons.aoe.Explosion;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
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

                    int[] serverHealths = {Controller.thisPlayer.getHealth(), Controller.otherPlayer.getHealth()};

                    sendPacket(new ServerUpdatePacket(
                            Controller.thisPlayer.getX(),
                            Controller.thisPlayer.getY(),
                            Controller.thisPlayer.getAngle(),
                            serverHealths,
                            Controller.thisPlayer.isWalking(),
                            Controller.thisPlayer.getWeaponSerial(),
                            new boolean[] {Controller.thisPlayer.isInvincible(), Controller.otherPlayer.isInvincible() }
                    ));

                    sendPacket(new ServerBulletPacket(Controller.movingAmmo.toArray(new Bullet[0])));
                } else {
                    if (controller.getPlayer() != null) {
                        sendPacket(new ClientUpdatePacket(
                                controller.getPlayer().getX(),
                                controller.getPlayer().getY(),
                                controller.getPlayer().getAngle(),
                                controller.getPlayer().isWalking(),
                                controller.getPlayer().getWeaponSerial()));
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
        } catch (SocketException e){
            World.controller.getGameWindow().frame.dispose();
            running = false;
            new MainMenu();
        }catch (IOException e) {
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
