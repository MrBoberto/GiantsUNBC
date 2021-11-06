package game;

import packets.ClientUpdatePacket;
import packets.StartPacket;
import packets.StartRequest;
import packets.ServerUpdatePacket;
import player.MainPlayer;
import player.OtherPlayer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class ClientController extends Controller{

    private Socket socket;
    private InputConnection inputConnection;
    private OutputConnection outputConnection;
    public ClientController(){
        super();
        try {
            System.out.println("waiting for connection...");
            //Scanner inputReader = new Scanner(System.in);

            socket = new Socket("142.207.59.6", Controller.PORT);
            socket = new Socket("142.207.59.140", Controller.PORT);
            String ipAddress= JOptionPane.showInputDialog ("Please ip address:");
            //System.out.println("You entered" + ipAddress + ".");
            //System.out.println("Please enter ip address: ");
            //String ipAddress = inputReader.nextLine(); //get file name

            socket = new Socket(ipAddress, Controller.PORT);
            System.out.println("connection accepted");

            outputConnection = new OutputConnection(this, socket);
            outputConnection.sendPacket(new StartRequest());
            inputConnection = new InputConnection(this, socket);

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("server + client connected.");
    }

    @Override
    public void packetReceived(Object object) {
        if(object instanceof StartPacket){

            StartPacket packet = (StartPacket) object;
            player = new MainPlayer(packet.getPlayerNumber(),packet.getX(), packet.getY(), packet.getAngle());

            for (int i = 0; i < packet.getPlayerNumber(); i++) {
                livingPlayers.add(i,new OtherPlayer(i,WIDTH / 2, HEIGHT / 2, 0));
            }
            livingPlayers.add(player);
    //        outputConnection.setGameRunning(true);
            repaint();



        } else if(object instanceof ServerUpdatePacket ){
            ServerUpdatePacket packet = (ServerUpdatePacket) object;
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
            inputConnection.close();
            outputConnection.close();
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
        }

        repaint();
    }
}
