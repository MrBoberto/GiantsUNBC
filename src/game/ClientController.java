package game;

import packets.*;
import player.MainPlayer;
import player.OtherPlayer;
import weapons.ammo.Bullet;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class ClientController extends Controller{

    private Socket socket;

    public ClientController(){
        super();
        try {
            System.out.println("waiting for connection...");



            //socket = new Socket("142.207.59.6", Controller.PORT);
            //socket = new Socket("142.207.59.140", Controller.PORT);
            String ipAddress= JOptionPane.showInputDialog ("Please enter the server's ip address:");
            /*Scanner inputReader = new Scanner(System.in);
            System.out.println("Please enter ip address: ");
            String ipAddress = inputReader.nextLine(); //get file name
            System.out.println("You entered" + ipAddress + ".");*/

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
        if(object instanceof StartPacket packet){

            thisPlayer = new MainPlayer(packet.getX(), packet.getY(), packet.getAngle());
            otherPlayer = new OtherPlayer(WIDTH / 2, HEIGHT / 2, 0);
    //        outputConnection.setGameRunning(true);
            repaint();
        } else if(object instanceof ServerUpdatePacket packet){
            if(otherPlayer != null) {
                //otherPlayer.getPos().setLocation(packet.getX(), packet.getY());
                otherPlayer.setX(packet.getX());
                otherPlayer.setY(packet.getY());
                otherPlayer.setAngle(packet.getAngle());
            }


            repaint();
        } else if(object instanceof ServerBulletPacket packet){

            movingAmmo = new ArrayList<>(Arrays.asList(packet.getAmmo()));

//            if(movingAmmo.size() != 0) {
//                System.out.println(movingAmmo);
//            }
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
        if(thisPlayer !=null){
            thisPlayer.tick(mouseLoc);
            for (int j = 0; j < movingAmmo.size(); j++) {
                if(movingAmmo.get(j) != null)
                movingAmmo.get(j).tick();
            }
        }

        repaint();
    }
}
