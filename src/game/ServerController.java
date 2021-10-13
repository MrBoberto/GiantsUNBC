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
import java.net.ServerSocket;
import java.net.Socket;

public class ServerController extends Controller {

    private ServerSocket serverSocket;
    private Socket socket;

    private Connection connection;

    public ServerController() {
        super();

        player = new MainPlayer(0,WIDTH / 2, HEIGHT / 2, 0);
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
        if(object instanceof ClientPlayPacket packet){
            livingPlayers.get(packet.getPlayerNumber()).getPos().setLocation(packet.getX(), packet.getY());
            livingPlayers.get(packet.getPlayerNumber()).setAngle(packet.getAngle());
            updateField();
        } else if(object instanceof ClientStartPacketRequest){

            OtherPlayer otherPlayer = new OtherPlayer(livingPlayers.size(),10,10,0);
            livingPlayers.add(otherPlayer);
            connection.sendPacket(new ClientStartPacket(otherPlayer.getPlayerNumber(),10,10,0));
        }
    }

    private void updateField() {
         double[] x = new double[livingPlayers.size()];
         double[] y = new double[livingPlayers.size()];
        double[] angle = new double[livingPlayers.size()];
        for (int i = 0; i < livingPlayers.size(); i++) {
                x[i]=livingPlayers.get(i).getX();
            y[i]=livingPlayers.get(i).getY();
            angle[i]=livingPlayers.get(i).getAngle();

        }
        connection.sendPacket(new UpdatePacket(x, y, angle));
        repaint();
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