package game;


import packets.*;
import player.MainPlayer;
import player.OtherPlayer;
import player.Player;
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

public class AIController extends Controller{

    private Socket socket;
    private GameOver gameOver;

    public AIController() {
        super();
        thisPlayer = new MainPlayer(Controller.otherX, Controller.otherY, 0, Color.RED);
        otherPlayer = new OtherPlayer(Controller.thisX, Controller.thisY, 0, Color.BLUE);

        thisPlayer.setPlayerName("CPU");

        System.out.println("CPU connected.");

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
                otherPlayer.setHealth(packet.getHealth()[0]);
                thisPlayer.setHealth(packet.getHealth()[1]);
            }
        } else if(object instanceof ServerBulletPacket packet){

            movingAmmo = new ArrayList<>(Arrays.asList(packet.getAmmo()));

        } else if(object instanceof RespawnPacket){

            thisPlayer.revive();

        } else if(object instanceof WinnerPacket packet){


            Player winner;
            if(packet.getWinner() == Player.SERVER_PLAYER){
                winner = otherPlayer;
            } else {
                winner = thisPlayer;
            }
            System.out.println("The winner is " +winner.getPlayerName());
            System.out.println("Scores: ");
            String format = " %10d  %10d  %10f  %10d  %10d  %10d  %10s %n";
            System.out.format("      Kills      Deaths         K/D     Bullets     Bullets     Walking    Number of%n");
            System.out.format("                                           Shot         Hit    Distance    Power-ups%n");
            System.out.format("------------------------------------------------------------------------------------%n");
            for (int i = 0; i < players.size(); i++) {

                //Print
                System.out.format(format,
                        (int)packet.getPlayerInfo()[i][0],
                        (int)packet.getPlayerInfo()[i][1],
                        packet.getPlayerInfo()[i][2],
                        (int)packet.getPlayerInfo()[i][3],
                        (int)packet.getPlayerInfo()[i][4],
                        (int)packet.getPlayerInfo()[i][5],
                        "???");
            }



            stop();
            gameWindow.frame.dispose();
        }
    }

    @Override
    public void tick() {
        super.tick();

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
