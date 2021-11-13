package game;

import packets.ClientBulletPacket;
import packets.ClientUpdatePacket;
import packets.StartPacket;
import packets.StartRequest;
import player.MainPlayer;
import player.OtherPlayer;
import player.Player;
import weapons.ammo.Bullet;
import weapons.ammo.Projectile;
import weapons.ammo.ShotgunBullet;
import weapons.guns.Weapon;

import java.awt.*;
import java.awt.event.ActionEvent;
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

    }

    @Override
    public void packetReceived(Object object) {
        if (object instanceof ClientUpdatePacket packet) {
            //otherPlayer.getPos().setLocation(packet.getX(), packet.getY());
            otherPlayer.setX(packet.getX());
            otherPlayer.setY(packet.getY());
            otherPlayer.setAngle(packet.getAngle());

            repaint();
        } else if (object instanceof StartRequest packet) {

            outputConnection.sendPacket(new StartPacket( 10, 10, 0, thisPlayer.getPlayerName()));
            otherPlayer.setPlayerName(packet.getClientName());
            System.out.println("Start request received and resent.");
        } else if (object instanceof ClientBulletPacket packet){
            if(packet.getType() == Projectile.Type.ShotgunBullet){
                movingAmmo.add(new ShotgunBullet(Player.CLIENT_PLAYER, packet.getMouseXLocation(), packet.getMouseYLocation(), packet.getDamage()));
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
    public void actionPerformed(ActionEvent e) {
        Point mouseLoc = MouseInfo.getPointerInfo().getLocation();
        thisPlayer.tick(mouseLoc);
        for (int j = 0; j < movingAmmo.size(); j++) {
            if(movingAmmo.get(j) != null) {

                if (movingAmmo.get(j).hasStopped()) {
                    movingAmmo.set(j, null);
                    movingAmmo.remove(null);
                } else {
                    movingAmmo.get(j).tick();
                    // Player who was hit (-1 if no one was hit)
                    int victimNumber = EntityCollision.getVictim(movingAmmo.get(j));
                    if (victimNumber != -1 && victimNumber != movingAmmo.get(j).getPlayerIBelongTo()) {
                        Projectile bullet = movingAmmo.get(j);
                        if (bullet.getSERIAL() != 002) {
                            movingAmmo.remove(j);
                        }
                        // Player
                        Player killer;
                        Player victim;
                        if(movingAmmo.get(j).getPlayerIBelongTo() == Player.SERVER_PLAYER){
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
//        if(movingAmmo.size() != 0) {
//            System.out.println(movingAmmo);
//        }

        repaint();
    }


}