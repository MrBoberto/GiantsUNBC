package game;

import StartMenu.MainMenuTest;
import packets.ClientBulletPacket;
import packets.ClientUpdatePacket;
import packets.StartPacket;
import packets.StartRequest;
import player.MainPlayer;
import player.OtherPlayer;
import player.Player;
import weapons.ammo.*;

import java.awt.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerController extends Controller {

    private ServerSocket serverSocket;
    private Socket socket;

    public ServerController() {
        super();
        thisPlayer = new MainPlayer(WIDTH, HEIGHT, 0, Color.BLUE);
        otherPlayer = new OtherPlayer(50, 50, 0, Color.RED);

        if (MainMenuTest.playerName.equals("")) {
            thisPlayer.setPlayerName("Host");
            otherPlayer.setPlayerName("Guest");
        } else {
            thisPlayer.setPlayerName(MainMenuTest.playerName);
        }

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
                case PistolBullet -> new PistolBullet(
                        Player.CLIENT_PLAYER,
                        packet.getMouseXLocation(),
                        packet.getMouseYLocation(),
                        packet.getDamage()
                );
                case AssaultRifleBullet -> new AssaultRifleBullet(
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
        for (int j = 0; j < movingAmmo.size(); j++) {
            if (movingAmmo.get(j) != null) {
                Bullet bullet = movingAmmo.get(j);
                if (bullet.hasStopped()) {
                    movingAmmo.remove(bullet);

                } else {
                    // Player who was hit (-1 if no one was hit)
                    int victimNumber = EntityCollision.getVictim(bullet);
                    if (victimNumber != -1 && victimNumber != bullet.getPlayerIBelongToNumber()) {
                        if (bullet.getSERIAL() != 002) {
                            movingAmmo.remove(bullet);
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
    }
}