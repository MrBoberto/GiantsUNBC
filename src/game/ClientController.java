package game;

import packets.ClientPlayPacket;
import packets.ClientStartPacket;
import packets.ClientStartPacketRequest;
import packets.UpdatePacket;
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
            connection.sendPacket(new ClientStartPacketRequest(player));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void packetReceived(Object object) {
        if(object instanceof ClientStartPacket){
            ClientStartPacket packet = (ClientStartPacket) object;

            livingPlayers = packet.getLivingPlayers();
            player.setPlayerNumber(packet.getPlayerNumber());
            livingPlayers.set(player.getPlayerNumber(), player);
        } else if(object instanceof  UpdatePacket){
            UpdatePacket packet = (UpdatePacket) object;

            livingPlayers.get(packet.getPlayerNumber()).getPos().setLocation(packet.getX(),packet.getY());
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
        player.tick(mouseLoc);
        for (int j = 0; j < movingAmmo.size(); j++) {
            movingAmmo.get(j).tick();
        }

        connection.sendPacket(new ClientPlayPacket(player.getPlayerNumber(),player.getPos().getX(),player.getPos().getY()));
        repaint();
    }
}
