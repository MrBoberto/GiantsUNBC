package game;

import packets.ClientUpdatePacket;
import packets.StartPacket;
import packets.StartRequest;
import player.MainPlayer;
import player.OtherPlayer;
import player.Player;
import weapons.Projectile;
import weapons.ammo.Ammo;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerController extends Controller {

    private ServerSocket serverSocket;
    private Socket socket;

    private InputConnection inputConnection;
    private OutputConnection outputConnection;

    public ServerController() {
        super();

        player = new MainPlayer(0, WIDTH / 2, HEIGHT / 2, 0);
        livingPlayers.add(player);


        if (livingPlayers.indexOf(player) != player.getPlayerNumber()) {
            System.out.println("Error: Player " + player.getPlayerNumber() + " not matching its index.");
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

    }

    @Override
    public void packetReceived(Object object) {
        if (object instanceof ClientUpdatePacket) {
            ClientUpdatePacket packet = (ClientUpdatePacket) object;
            livingPlayers.get(packet.getPlayerNumber()).getPos().setLocation(packet.getX(), packet.getY());
            livingPlayers.get(packet.getPlayerNumber()).setAngle(packet.getAngle());

            System.out.println("Received update packet");
            repaint();
        } else if (object instanceof StartRequest) {

            OtherPlayer otherPlayer = new OtherPlayer(livingPlayers.size(), 10, 10, 0);
            livingPlayers.add(otherPlayer);
            outputConnection.sendPacket(new StartPacket(otherPlayer.getPlayerNumber(), 10, 10, 0));
            System.out.println("Start request received and resent.");
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
    public void actionPerformed(ActionEvent e) {
        Point mouseLoc = MouseInfo.getPointerInfo().getLocation();
        player.tick(mouseLoc);
        for (int j = 0; j < movingAmmo.size(); j++) {
            movingAmmo.get(j).tick();
            // Player who was hit (null if no one was hit)
            Player victim = EntityCollision.getVictim(movingAmmo.get(j));
            if (victim != null && victim != movingAmmo.get(j).getWeapon().getParent()) {
                Projectile ammo = movingAmmo.get(j);
                if (ammo.getSERIAL() != 002) {
                    movingAmmo.remove(j);
                }
                // Player
                Player killer = ammo.getWeapon().getParent();
                victim.modifyHealth(-1 * ammo.getWeapon().getDamage());
                killer.addTDO(-1 * ammo.getWeapon().getDamage());

                if (victim.getHealth() == 0) {
                    victim.incrementDeathCount();
                    livingPlayers.remove(victim);
                    killer.incrementKillCount();
                    System.out.println(victim.getPlayerNumber() + " was memed by " +
                            killer.getPlayerNumber());
                }
            }
        }

        repaint();
    }


}