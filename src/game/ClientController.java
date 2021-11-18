package game;

import StartMenu.MainMenuTest;
import packets.*;
import player.MainPlayer;
import player.OtherPlayer;
import weapons.ammo.Bullet;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class ClientController extends Controller{

    private Socket socket;

    public ClientController(){
        super();
        thisPlayer = new MainPlayer(WIDTH, HEIGHT, 0, Color.RED);
        otherPlayer = new OtherPlayer(50, 50, 0, Color.BLUE);

        if (MainMenuTest.playerName.equals("")) {
            thisPlayer.setPlayerName("Guest");
            otherPlayer.setPlayerName("Host");
        } else {
            thisPlayer.setPlayerName(MainMenuTest.playerName);
        }
        try {
            System.out.println("waiting for connection...");



            //socket = new Socket("142.207.59.6", Controller.PORT);
            //socket = new Socket("142.207.59.140", Controller.PORT);
            String ipAddress = JOptionPane.showInputDialog ("Please enter the server's ip address:");
            /*Scanner inputReader = new Scanner(System.in);
            System.out.println("Please enter ip address: ");
            String ipAddress = inputReader.nextLine(); //get file name
            System.out.println("You entered" + ipAddress + ".");*/

            socket = new Socket(ipAddress, Controller.PORT);
            System.out.println("connection accepted");

            outputConnection = new OutputConnection(this, socket);
            outputConnection.sendPacket(new StartRequest(thisPlayer.getPlayerName()));
            inputConnection = new InputConnection(this, socket);

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("server + client connected.");

        start();
    }

    @Override
    public void packetReceived(Object object) {
        if(object instanceof StartPacket packet){

            otherPlayer.setPlayerName(packet.getPlayerName());
    //        outputConnection.setGameRunning(true);

        } else if(object instanceof ServerUpdatePacket packet){
            if(otherPlayer != null) {

                otherPlayer.setWalking(otherPlayer.getX() != packet.getX() || otherPlayer.getY() != packet.getY());
                //otherPlayer.getPos().setLocation(packet.getX(), packet.getY());
                otherPlayer.setX(packet.getX());
                otherPlayer.setY(packet.getY());
                otherPlayer.setAngle(packet.getAngle());


            }


        } else if(object instanceof ServerBulletPacket packet){

            movingAmmo = new ArrayList<>(Arrays.asList(packet.getAmmo()));

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
}
