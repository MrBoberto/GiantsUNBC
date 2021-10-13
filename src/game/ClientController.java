package game;

import packets.ClientPlayPacket;
import packets.ClientStartPacket;
import packets.ClientStartPacketRequest;
import packets.UpdatePacket;
import player.MainPlayer;
import player.OtherPlayer;
import player.Player;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.Socket;

public class ClientController extends Controller{

    private Socket socket;
    private Connection connection;
    public ClientController(){
        super();
        try {
            socket = new Socket("localhost", Controller.PORT);
            connection = new Connection(this, socket);
            connection.sendPacket(new ClientStartPacketRequest());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void packetReceived(Object object) {
        if(object instanceof ClientStartPacket){
            ClientStartPacket packet = (ClientStartPacket) object;

            player = new MainPlayer(packet.getPlayerNumber(),packet.getX(), packet.getY(), packet.getAngle());

            for (int i = 0; i < packet.getPlayerNumber(); i++) {
                livingPlayers.add(i,new OtherPlayer(i,WIDTH / 2, HEIGHT / 2, 0));
            }
            livingPlayers.add(player);
            repaint();



        } else if(object instanceof  UpdatePacket){
            UpdatePacket packet = (UpdatePacket) object;

            for (int i = 0; i < livingPlayers.size(); i++) {

                if(i != player.getPlayerNumber()){
                    livingPlayers.get(i).getPos().setLocation(packet.getX()[i],packet.getY()[i]);
                    livingPlayers.get(i).setAngle(packet.getAngle()[i]);
                }
            }


            repaint();
        }
    }

    @Override
    public void close() {
        try {
            connection.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Point mouseLoc = MouseInfo.getPointerInfo().getLocation();
        if(player!=null){
            player.tick(mouseLoc);
            for (int j = 0; j < movingAmmo.size(); j++) {
                movingAmmo.get(j).tick();
            }

            connection.sendPacket(new ClientPlayPacket(player.getPlayerNumber(),player.getX(),player.getY(), player.getAngle()));
        }

        repaint();
    }
}
