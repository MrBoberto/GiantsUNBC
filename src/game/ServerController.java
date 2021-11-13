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
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerController extends Controller {

    private ServerSocket serverSocket;
    private Socket socket;

    private InputConnection inputConnection;
    private OutputConnection outputConnection;

    Point mouseLoc = new Point(0, 0);
    private boolean isMouseOnScreen = false;

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
            outputConnection.sendPacket(new StartPacket(otherPlayer.getPlayerNumber(), 10, 10, 0,
                    otherPlayer.getPlayerName()));
            System.out.println("Start request received and resent.");
        }
    }

    public void mouseEntered(MouseEvent e) {
        isMouseOnScreen = true;
    }

    public void mouseExited(MouseEvent e) {
        isMouseOnScreen = false;
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
        Point mouseLocRelativeToScreen = MouseInfo.getPointerInfo().getLocation();
        if (isMouseOnScreen) {
            double mouseX = mouseLocRelativeToScreen.getX() - this.getLocationOnScreen().getX();
            double mouseY = mouseLocRelativeToScreen.getY() - this.getLocationOnScreen().getY();
            mouseLoc = new Point((int) mouseX, (int) mouseY);
        }

        player.tick(mouseLoc);
        for (int j = 0; j < movingAmmo.size(); j++) {
            movingAmmo.get(j).tick();
            // Player who was hit (null if no one was hit)
            Player victim = EntityCollision.getVictim(movingAmmo.get(j));
            if (victim != null && victim.getPlayerNumber() != movingAmmo.get(j).getPlayerNumber()) {
                Projectile ammo = movingAmmo.get(j);
                if (ammo.getSERIAL() != 001) {
                    movingAmmo.remove(j);
                }
                // Player
                Player killer = victim;
                for (int k = 0; k < livingPlayers.size(); k++) {
                    if (livingPlayers.get(k).getPlayerNumber() == ammo.getPlayerNumber()) {
                        killer = livingPlayers.get(k);
                    }
                }
                victim.modifyHealth(-1 * ammo.getDamage());
                killer.addTDO(ammo.getDamage());

                if (victim.getHealth() == 0) {
                    victim.incrementDeathCount();
                    livingPlayers.remove(victim);
                    killer.incrementKillCount();
                    System.out.println(victim.getPlayerName() + " was memed by " +
                            killer.getPlayerName());
                }
            }
        }

        repaint();
    }


}