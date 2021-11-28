package game;

import audio.SFXPlayer;
import packets.*;
import player.MainPlayer;
import player.OtherPlayer;
import player.Player;
import power_ups.DamageDown;
import power_ups.DamageUp;
import power_ups.SpeedDown;
import power_ups.SpeedUp;
import weapons.aoe.Explosion;
import utilities.BufferedImageLoader;

import java.awt.*;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;

public class ClientControllerAutomatic extends Controller {

    private Socket socket;
    private Socket socketActual;

    private int shotgunAudioCount = 10;
    private String correctIp; //makes java happy, doesn't need to be initialzed in theory

    public SFXPlayer serverWeaponAudio;

    public ClientControllerAutomatic() throws UnknownHostException {
        super();

        serverWeaponAudio = new SFXPlayer();

        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(new Color(0, 0, 0));

        thisPlayer = new MainPlayer(Controller.otherX, Controller.otherY, 0, Color.RED);
        otherPlayer = new OtherPlayer(Controller.thisX, Controller.thisY, 0, Color.BLUE);


        InetAddress correctAddress =InetAddress.getLocalHost(); //to make java happy, should not need to be initailzed
        try {
            Enumeration<NetworkInterface> Interfaces = NetworkInterface.getNetworkInterfaces();
            boolean firstAddress = false;
            while(Interfaces.hasMoreElements())
            {
                NetworkInterface Interface = Interfaces.nextElement();
                Enumeration<InetAddress> Addresses = Interface.getInetAddresses();
                while(Addresses.hasMoreElements())
                {
                    InetAddress Address = Addresses.nextElement();
                    if (!Address.getHostAddress().contains("f")&&!Address.getHostAddress().contains(":")&&!Address.getHostAddress().contains("127.0.0.1")&&!firstAddress)
                    {
                        System.out.println(Address.getHostAddress() + " is on the network");
                        firstAddress = true;
                        correctAddress =Address;
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        final byte[] ip;
        try {
            ip = correctAddress.getAddress();
        } catch (Exception e) {
            return;     // exit method, otherwise "ip might not have been initialized"
        }

        if (MainMenu.playerName.equals("")) {
            thisPlayer.setPlayerName("Guest");
            otherPlayer.setPlayerName("Host");
        } else {
            thisPlayer.setPlayerName(MainMenu.playerName);
        }
        try {
            System.out.println("waiting for connection...");
            //String ipAddress = MainMenu.ipaddress;

            for(int i=1;i<=254;i++) {
                final int j = i;  // i as non-final variable cannot be referenced from inner class
                new Thread(new Runnable() {   // new thread for parallel execution
                    public void run() {
                        try {
                            ip[3] = (byte)j;
                            InetAddress address = InetAddress.getByAddress(ip);
                            String output = address.toString().substring(1);
                            try{
                                socket = new Socket(output, game.Controller.PORT);
                                System.out.println("FOUND SERVER");
                                System.out.println(output + " is this the server");
                                correctIp = output;
                                socket.close();
                            }catch (Exception e) {//e.printStackTrace();}
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();     // dont forget to start the thread
            }

            socketActual = new Socket(correctIp, Controller.PORT);
            System.out.println("connection accepted");

            outputConnection = new OutputConnection(this, socketActual);
            outputConnection.sendPacket(new StartRequest(thisPlayer.getPlayerName()));
            inputConnection = new InputConnection(this, socketActual);

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("server + client connected.");

        start();
    }

    @Override
    public void packetReceived(Object object) {
        if (object instanceof StartPacket packet) {
            //Loading level
            level = BufferedImageLoader.loadImage("/resources/mapLayouts/Level" + packet.getMapSelected() + ".png");
            loadLevel(level);


            otherPlayer.setPlayerName(packet.getPlayerName());
            thisPlayer.setRespawnPointX(packet.getX());
            thisPlayer.setRespawnPointY(packet.getY());
            thisPlayer.setX(packet.getX());
            thisPlayer.setY(packet.getY());


        } else if (object instanceof ServerUpdatePacket packet) {
            if (otherPlayer != null) {

                otherPlayer.setWalking(packet.isWalking());
                //otherPlayer.getPos().setLocation(packet.getX(), packet.getY());
                otherPlayer.setX(packet.getX());
                otherPlayer.setY(packet.getY());
                otherPlayer.setAngle(packet.getAngle());
                otherPlayer.setHealth(packet.getHealth()[0]);
                thisPlayer.setHealth(packet.getHealth()[1]);
                otherPlayer.setWeaponSerial(packet.getWeaponSerial());
                otherPlayer.setInvincible(packet.isInvincible()[0]);
                thisPlayer.setInvincible(packet.isInvincible()[1]);
            }
        } else if (object instanceof ServerBulletPacket packet) {

            movingAmmo = new ArrayList<>(Arrays.asList(packet.getAmmo()));

        } else if(object instanceof ServerExplosionPacket packet){

            explosions.add(new Explosion(packet.getX(), packet.getY(), packet.getPlayerNumber()));
            serverWeaponAudio.setFile(-1);
            serverWeaponAudio.play();

        } else if (object instanceof ServerSFXPacket packet) {

            serverWeaponAudio.setFile(packet.getServerSFXInt());
            serverWeaponAudio.play();

        } else if (object instanceof RespawnPacket) {

            thisPlayer.revive();

        } else if (object instanceof EyeCandyPacket packet) {
            eyeCandy = new ArrayList<>(Arrays.asList(packet.getEyeCandy()));
        } else if (object instanceof PowerUpEffectPacket packet) {
            powerUps.remove(packet.getIndexToRemove());
            switch (packet.getPlayerToBeAffected()) {
                case Player.SERVER_PLAYER:
                    otherPlayer.setDamageMultiplier(packet.getDamageMultiplier(), packet.getTime());
                    otherPlayer.setSpeedMultiplier(packet.getSpeedMultiplier(), packet.getTime());
                    /* here goes other property changes */
                    break;
                case Player.CLIENT_PLAYER:
                    thisPlayer.setDamageMultiplier(packet.getDamageMultiplier(), packet.getTime());
                    thisPlayer.setSpeedMultiplier(packet.getSpeedMultiplier(), packet.getTime());
                    break;
            }
        } else if (object instanceof CreatePowerUpPacket packet) {
            //Use default properties since server is the one that controls effects and collisions.
            switch (packet.getPowerUpType()){
                case DamageUp -> powerUps.add(new DamageUp(packet.getX(),packet.getY(),Player.DEFAULT_DAMAGE_MULTIPLIER));
                case DamageDown -> powerUps.add(new DamageDown(packet.getX(),packet.getY(),Player.DEFAULT_DAMAGE_MULTIPLIER));
                case SpeedUp -> powerUps.add(new SpeedUp(packet.getX(),packet.getY(),Player.DEFAULT_SPEED_MULTIPLIER));
                case SpeedDown -> powerUps.add(new SpeedDown(packet.getX(),packet.getY(),Player.DEFAULT_SPEED_MULTIPLIER));
            }
        }
        else if (object instanceof WinnerPacket packet) {

            isWon = true;
            Player winner;
            if (packet.getWinner() == Player.SERVER_PLAYER) {
                winner = otherPlayer;
            } else {
                winner = thisPlayer;
            }

            renderWinner(packet.getWinner(), packet.getPlayerInfo());

            System.out.println("The winner is " + winner.getPlayerName());
            System.out.println("Scores: ");
            String format = " %10d  %10d  %10f  %10d  %10d  %10d  %10s %n";
            System.out.format("      Kills      Deaths         K/D     Bullets     Bullets     Walking    Number of%n");
            System.out.format("                                           Shot         Hit    Distance    Power-ups%n");
            System.out.format("------------------------------------------------------------------------------------%n");
            for (int i = 0; i < players.size(); i++) {

                //Print
                System.out.format(format,
                        (int) packet.getPlayerInfo()[i][0],
                        (int) packet.getPlayerInfo()[i][1],
                        packet.getPlayerInfo()[i][2],
                        (int) packet.getPlayerInfo()[i][3],
                        (int) packet.getPlayerInfo()[i][4],
                        (int) packet.getPlayerInfo()[i][5],
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
