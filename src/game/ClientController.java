package game;

import audio.SFXPlayer;
import packets.*;
import player.MainPlayer;
import player.OtherPlayer;
import player.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

public class ClientController extends Controller{

    private Socket socket;

    private int shotgunAudioCount = 10;
    public SFXPlayer serverWeaponAudio;

    public ClientController(){
        super();
        new GameWindow(WIDTH,HEIGHT,"THE BOYZ", this);

        this.addKeyListener(new KeyInput());
        this.addMouseListener(new MouseInput());
        serverWeaponAudio = new SFXPlayer();

        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(new Color(0, 0, 0));

        thisPlayer = new MainPlayer(Controller.otherX, Controller.otherY, 0, Color.RED);
        otherPlayer = new OtherPlayer(Controller.thisX, Controller.thisY, 0, Color.BLUE);

        if (MainMenu.playerName.equals("")) {
            thisPlayer.setPlayerName("Guest");
            otherPlayer.setPlayerName("Host");
        } else {
            thisPlayer.setPlayerName(MainMenu.playerName);
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


        } else if(object instanceof ServerUpdatePacket packet){
            if(otherPlayer != null) {

                otherPlayer.setWalking(packet.isWalking());
                //otherPlayer.getPos().setLocation(packet.getX(), packet.getY());
                otherPlayer.setX(packet.getX());
                otherPlayer.setY(packet.getY());
                otherPlayer.setAngle(packet.getAngle());
                otherPlayer.setHealth(packet.getHealth()[0]);
                thisPlayer.setHealth(packet.getHealth()[1]);
                otherPlayer.setWeaponSerial(packet.getWeaponSerial());
            }
        } else if(object instanceof ServerBulletPacket packet){

            movingAmmo = new ArrayList<>(Arrays.asList(packet.getAmmo()));

        } else if (object instanceof ServerSFXPacket packet) {

            serverWeaponAudio.setFile(packet.getServerSFXInt());
            serverWeaponAudio.play();

        } else if(object instanceof RespawnPacket){

            thisPlayer.revive();

        } else if (object instanceof EyeCandyPacket packet){
            eyeCandy = new ArrayList<>(Arrays.asList(packet.getEyeCandy())) ;

            System.out.println(eyeCandy);
        } else if(object instanceof WinnerPacket packet){


            Player winner;
            if(packet.getWinner() == Player.SERVER_PLAYER){
                winner = otherPlayer;
            } else {
                winner = thisPlayer;
            }

            BufferStrategy bs = this.getBufferStrategy();
            if(bs == null){
                this.createBufferStrategy(3);
                return;
            }
            Graphics2D g2D = (Graphics2D) bs.getDrawGraphics();

            g2D.setColor(Color.BLACK);
            Font font = new Font("Arial", Font.BOLD, 25);
            g2D.setFont(font);
            FontMetrics stringSize = g2D.getFontMetrics(font);

            g2D.drawString("The winner is " + winner.getPlayerName(), WIDTH / 2, HEIGHT / 10);
            g2D.drawString("Scores:" + winner.getPlayerName(), WIDTH / 2, HEIGHT / 5);
            g2D.drawString("The winner is " + winner.getPlayerName(), WIDTH / 2, 3 * HEIGHT / 10);
            g2D.drawString(
                    "      Kills      Deaths         K/D     Bullets     Bullets     Walking    Number of",
                    WIDTH / 2, 2 * HEIGHT / 5);
            g2D.drawString(
                    "                                           Shot         Hit    Distance    Power-ups",
                    WIDTH / 2, HEIGHT / 2);

            double[][] playerInfo = new double[2][6];

            for (int i = 0; i < players.size(); i++) {
                //Save data to send to client
                Player player = players.get(i);
                playerInfo[i][0] = player.getKillCount();
                playerInfo[i][1] = player.getDeathCount();
                playerInfo[i][2] = player.getKdr();
                playerInfo[i][3] = player.getBulletCount();
                playerInfo[i][4] = player.getBulletHitCount();
                playerInfo[i][5] = player.getWalkingDistance();

                //Determine format
                String format = String.format(" %10d  %10d  %10f  %10d  %10d  %10d  %10s %n",
                        player.getKillCount(),
                        player.getDeathCount(),
                        player.getKdr(),
                        player.getBulletCount(),
                        player.getBulletHitCount(),
                        player.getWalkingDistance(),
                        "???");

                if (i == 0) {
                    g2D.drawString(format,
                            WIDTH / 2, 3 * HEIGHT / 5);
                } else {
                    g2D.drawString(format,
                            WIDTH / 2, 7 * HEIGHT / 10);
                }
            }

            System.out.println("The winner is " + winner.getPlayerName());
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
