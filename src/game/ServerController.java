package game;

import StartMenu.MainMenuTest;
import audio.SFXPlayer;
import packets.*;
import player.MainPlayer;
import player.OtherPlayer;
import player.Player;
import tile.Tiles;
import weapons.ammo.*;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Formatter;
import java.util.Locale;

public class ServerController extends Controller {

    private ServerSocket serverSocket;
    private Socket socket;
    public SFXPlayer clientWeaponAudio;

    public ServerController() {
        super();
        clientWeaponAudio = new SFXPlayer();
        new GameWindow(WIDTH,HEIGHT,"THE BOYZ", this);

        this.addKeyListener(new KeyInput());
        this.addMouseListener(new MouseInput());

        tiless = new Tiles[2];

        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(new Color(0, 0, 0));

        thisPlayer = new MainPlayer(WIDTH - 50, HEIGHT - 50, 0, Color.BLUE);
        otherPlayer = new OtherPlayer(50, 50, 0, Color.RED);

        if (MainMenuTest.playerName.equals("")) {
            thisPlayer.setPlayerName("Host");
            otherPlayer.setPlayerName("Guest");
        } else {
            thisPlayer.setPlayerName(MainMenuTest.playerName);
        }

        try {
            serverSocket = new ServerSocket(Controller.PORT);
            System.out.println("waiting for connection...");
            socket = serverSocket.accept();
            System.out.println("connection accepted");
            outputConnection = new OutputConnection(this, socket);
            System.out.println("output connection complete");
            inputConnection = new InputConnection(this, socket);
            System.out.println("input connection complete");

            //          outputConnection.setGameRunning(true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("server + client connected.");
        start();

    }

    @Override
    public void packetReceived(Object object) {
        if (object instanceof ClientUpdatePacket packet) {

            otherPlayer.setWalking(packet.isWalking());
            if (packet.isWalking()) otherPlayer.incrementWalkingDistance();

            otherPlayer.setX(packet.getX());
            otherPlayer.setY(packet.getY());
            otherPlayer.setAngle(packet.getAngle());

        } else if (object instanceof StartRequest packet) {

            outputConnection.sendPacket(new StartPacket(10, 10, 0, thisPlayer.getPlayerName()));
            otherPlayer.setPlayerName(packet.getClientName());
            System.out.println("Start request received and resent.");
        } else if (object instanceof ClientBulletPacket packet) {
            switch (packet.getType()){
                case ShotgunBullet: new ShotgunBullet(
                        Player.CLIENT_PLAYER,
                        packet.getMouseXLocation(),
                        packet.getMouseYLocation(),
                        packet.getDamage()
                );
                break;
                case SniperRifleBullet: new SniperRifleBullet(
                        Player.CLIENT_PLAYER,
                        packet.getMouseXLocation(),
                        packet.getMouseYLocation(),
                        packet.getDamage()
                );
                break;
                case PistolBullet: new PistolBullet(
                        Player.CLIENT_PLAYER,
                        packet.getMouseXLocation(),
                        packet.getMouseYLocation(),
                        packet.getDamage()
                );
                break;
                case AssaultRifleBullet: new AssaultRifleBullet(
                        Player.CLIENT_PLAYER,
                        packet.getMouseXLocation(),
                        packet.getMouseYLocation(),
                        packet.getDamage()
                );
                break;
            }

            otherPlayer.incrementBulletCount();
        } else if (object instanceof ClientSFXPacket packet) {
            clientWeaponAudio.setFile(packet.getclientSFXLocation());
            clientWeaponAudio.play();
        }
    }

    @Override
    public void close() {
        try {
            inputConnection.close();
            outputConnection.close();
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void tick(){
        super.tick();
        for (int j = 0; j < movingAmmo.size(); j++) {
            if (movingAmmo.get(j) != null) {
                Bullet bullet = movingAmmo.get(j);
                if (bullet.hasStopped()) {
                    movingAmmo.remove(bullet);

                } else {
                    // Player who was hit (-1 if no one was hit)
                    int victimNumber = EntityCollision.getVictim(bullet);

                    // Player
                    Player killer;
                    Player victim;
                    if (bullet.getPlayerIBelongToNumber() == Player.SERVER_PLAYER) {
                        killer = thisPlayer;
                        victim = otherPlayer;

                    } else {
                        killer = otherPlayer;
                        victim = thisPlayer;
                    }

                    if (victimNumber != -1 && victimNumber != bullet.getPlayerIBelongToNumber() && !victim.isInvincible()) {
                        if (bullet.getSERIAL() != 002) {
                            movingAmmo.remove(bullet);
                        }


                        killer.incrementBulletHitCount();
                        victim.modifyHealth(-1 * bullet.getDamage());
                        victim.resetHealTimer();
                        killer.addTDO(-1 * bullet.getDamage());

                        if (victim.getHealth() == 0) {
                            victim.incrementDeathCount();
                            victim.revive();
                            if(victim == otherPlayer){
                                outputConnection.sendPacket(new RespawnPacket());
                            }

                            killer.incrementKillCount();
                           // System.out.println(victim.getPlayerName() + " was memed by " + killer.getPlayerName());
                            if(victim.getDeathCount() >= 10){
                                declareWinner(killer);
                            }
                        }
                    }
                }

            }
        }
    }

    public void declareWinner(Player winner){
        int winnerNumber;
        if(winner == thisPlayer){
            winnerNumber = Player.SERVER_PLAYER;
        } else {
            winnerNumber = Player.CLIENT_PLAYER;
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





        double[][] playerInfo1 = new double[2][6];

        System.out.println("The winner is " + winner.getPlayerName());
        System.out.println("Scores: ");
        String format1 = " %10d  %10d  %10f  %10d  %10d  %10d  %10s %n";
        System.out.format("      Kills      Deaths         K/D     Bullets     Bullets     Walking    Number of%n");
        System.out.format("                                           Shot         Hit    Distance    Power-ups%n");
        System.out.format("------------------------------------------------------------------------------------%n");
        for (int i = 0; i < players.size(); i++) {
            //Save data to send to client
            Player player = players.get(i);
            playerInfo1[i][0] = player.getKillCount();
            playerInfo1[i][1] = player.getDeathCount();
            playerInfo1[i][2] = player.getKdr();
            playerInfo1[i][3] = player.getBulletCount();
            playerInfo1[i][4] = player.getBulletHitCount();
            playerInfo1[i][5] = player.getWalkingDistance();

            //Print
            System.out.format(format1,
                    player.getKillCount(),
                    player.getDeathCount(),
                    player.getKdr(),
                    player.getBulletCount(),
                    player.getBulletHitCount(),
                    player.getWalkingDistance(),
                    "???");
        }

        //Send to client
        outputConnection.sendPacket(new WinnerPacket(winnerNumber, playerInfo));

        // Kill the music
        try {
            soundtrack.stop();
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }

        stop();
    }
}