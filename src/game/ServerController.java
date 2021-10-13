package game;

import packets.ClientPlayPacket;
import packets.ClientStartPacket;
import packets.ClientStartPacketRequest;
import packets.UpdatePacket;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerController extends Controller {

    private ServerSocket serverSocket;
    private Socket socket;

    private Connection connection;

    public ServerController() {
        super();

        player.setPlayerNumber(0);
        livingPlayers.add(player);

        if(livingPlayers.indexOf(player) != player.getPlayerNumber()){
            System.out.println("Error: Player " + player.getPlayerNumber() + " not matching its index.");
        }
        try {
            serverSocket = new ServerSocket(Controller.PORT);
            socket = serverSocket.accept();
            connection = new Connection(this, socket);
        }catch (IOException e){
            e.printStackTrace();
        }

    }

    @Override
    public void packetReceived(Object object) {
        if(object instanceof ClientPlayPacket){
            ClientPlayPacket packet = (ClientPlayPacket) object;

            updateField(packet.getPlayerNumber(), packet.getX(),packet.getY());
        } else if(object instanceof ClientStartPacketRequest){
            ClientStartPacketRequest packet = (ClientStartPacketRequest) object;

            livingPlayers.add(packet.getPlayer());
            int newPlayerNumber = livingPlayers.indexOf(packet.getPlayer());
            livingPlayers.get(newPlayerNumber).setPlayerNumber(newPlayerNumber);
            connection.sendPacket(new ClientStartPacket(livingPlayers, newPlayerNumber));
        }
    }

    private void updateField(int playerNumber, double x, double y) {
        connection.sendPacket(new UpdatePacket(playerNumber, x, y));

        livingPlayers.get(playerNumber).getPos().setLocation(x,y);
    }

    @Override
    public void close() {
        try{
            connection.close();
            serverSocket.close();
        } catch (IOException e){
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
        repaint();
    }


}