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
        if (object instanceof ClientUpdatePacket) {
            ClientUpdatePacket packet = (ClientUpdatePacket) object;
            otherPlayer.getPos().setLocation(packet.getX(), packet.getY());
            otherPlayer.setAngle(packet.getAngle());

            repaint();
        } else if (object instanceof StartRequest) {

            outputConnection.sendPacket(new StartPacket( 10, 10, 0));
            System.out.println("Start request received and resent.");
        } else if (object instanceof ClientBulletPacket packet){
            if(packet.getType() == Projectile.Type.ShotgunBullet){
                movingAmmo.add(new ShotgunBullet(otherPlayer, packet.getPlayerX(), packet.getPlayerY()));
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
            movingAmmo.get(j).tick();
            // Player who was hit (null if no one was hit)
            //TODO: Player hitbox.
//            Player victim = EntityCollision.getVictim(movingAmmo.get(j));
//            if (victim != null && victim != movingAmmo.get(j).getPlayerIBelongTo()) {
//                Bullet ammo = movingAmmo.get(j);
//                if (ammo.getSERIAL() != 002) {
//                    movingAmmo.remove(j);
//                }

                //TODO: damage modifiers are built into bullet? Into player??
//                victim.modifyHealth(-1 * ammo..getDamage());
//                if (victim.getHealth() == 0) {
//                    livingPlayers.remove(victim);
//                    Player killer = ammo.getWeapon().getParent();
//                    killer.incrementKillCount();
//                    System.out.println(victim.getPlayerNumber() + " was memed by " +
////                            killer.getPlayerNumber());
////                }
//            }
        }

        repaint();
    }
}