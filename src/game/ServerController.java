package game;

import packets.ClientBulletPacket;
import packets.ClientUpdatePacket;
import packets.StartPacket;
import packets.StartRequest;
import player.MainPlayer;
import player.Player;
import weapons.ammo.Bullet;
import weapons.ammo.Projectile;
import weapons.ammo.ShotgunBullet;
import weapons.ammo.SniperRifleBullet;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerController extends Controller {

    private ServerSocket serverSocket;
    private Socket socket;

    public ServerController() {
        super();

        thisPlayer = new MainPlayer(WIDTH / 2, HEIGHT / 2, 0);

        try {
            serverSocket = new ServerSocket(Controller.PORT);
            System.out.println("waiting for connection...");
            socket = serverSocket.accept();
            System.out.println("connection accepted");
            outputConnection = new OutputConnection(this, socket);
            System.out.println("output connection complete");
            inputConnection = new InputConnection(this, socket);
            System.out.println("input connection complete");

            //          outputConnection.setGameRunning(true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("server + client connected.");
        start();

    }

    @Override
    public void packetReceived(Object object) {
        if (object instanceof ClientUpdatePacket packet) {

            otherPlayer.setWalking(otherPlayer.getX() != packet.getX() || otherPlayer.getY() != packet.getY());

            otherPlayer.setX(packet.getX());
            otherPlayer.setY(packet.getY());
            otherPlayer.setAngle(packet.getAngle());



            repaint();
        } else if (object instanceof StartRequest packet) {

            outputConnection.sendPacket(new StartPacket(10, 10, 0, thisPlayer.getPlayerName()));
            otherPlayer.setPlayerName(packet.getClientName());
            System.out.println("Start request received and resent.");
        } else if (object instanceof ClientBulletPacket packet) {

            switch (packet.getType()){
                case ShotgunBullet -> new ShotgunBullet(
                        Player.CLIENT_PLAYER,
                        packet.getMouseXLocation(),
                        packet.getMouseYLocation(),
                        packet.getDamage()
                        );
                case SniperRifleBullet -> new SniperRifleBullet(
                        Player.CLIENT_PLAYER,
                        packet.getMouseXLocation(),
                        packet.getMouseYLocation(),
                        packet.getDamage()
                        );
            }
        }
    }

    @Override
    public void close() {
        try {
            inputConnection.close();
            outputConnection.close();
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void tick(){
        super.tick();
        for (int j = 0; j < gameObjects.size(); j++) {

            if (gameObjects.get(j) != null && gameObjects.get(j) instanceof Bullet bullet) {
                if (bullet.hasStopped()) {
                    gameObjects.set(j, null);
                    gameObjects.remove(null);
                } else {
                    // Player who was hit (-1 if no one was hit)
                    int victimNumber = EntityCollision.getVictim(bullet);
                    if (victimNumber != -1 && victimNumber != bullet.getPlayerIBelongToNumber()) {
                        if (bullet.getSERIAL() != 002) {
                            gameObjects.remove(j);
                        }
                        // Player
                        Player killer;
                        Player victim;
                        if (bullet.getPlayerIBelongToNumber() == Player.SERVER_PLAYER) {
                            killer = thisPlayer;
                            victim = otherPlayer;
                        } else {
                            killer = otherPlayer;
                            victim = thisPlayer;
                        }

                        victim.modifyHealth(-1 * bullet.getDamage());
                        killer.addTDO(-1 * bullet.getDamage());

                        if (victim.getHealth() == 0) {
                            victim.incrementDeathCount();
                            killer.incrementKillCount();
                            System.out.println(victim.getPlayerName() + " was memed by " +
                                    killer.getPlayerName());
                        }
                    }
                }

            }
        }
        repaint();
    }
}