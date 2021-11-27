package game;


import audio.SFXPlayer;
import eye_candy.DeathMark;
import mapObjects.Block;
import packets.*;
import player.MainPlayer;
import player.OtherPlayer;
import player.Player;
import power_ups.DamageDown;
import power_ups.DamageUp;
import power_ups.PowerUp;
import power_ups.SpeedUp;
import utilities.BufferedImageLoader;
import weapons.ammo.*;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

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

        //Loading level
        level = BufferedImageLoader.loadImage("/resources/mapLayouts/Level" + MainMenu.mapSelected +".png");
        loadLevel(level);

        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(new Color(0, 0, 0));

        thisPlayer = new MainPlayer(Controller.thisX, Controller.thisY, 0, Color.BLUE);
        otherPlayer = new OtherPlayer(Controller.otherX, Controller.otherY, 0, Color.RED);

        if (MainMenu.playerName.equals("")) {
            thisPlayer.setPlayerName("Host");
            otherPlayer.setPlayerName("Guest");
        } else {
            thisPlayer.setPlayerName(MainMenu.playerName);
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
            otherPlayer.setWeaponSerial(packet.getWeaponSerial());

        } else if (object instanceof StartRequest packet) {

            outputConnection.sendPacket(new StartPacket(otherPlayer.getRespawnPointX(), otherPlayer.getRespawnPointY(), 0, thisPlayer.getPlayerName(), MainMenu.mapSelected));
            otherPlayer.setPlayerName(packet.getClientName());
            System.out.println("Start request received and resent.");
        } else if (object instanceof ClientBulletPacket packet) {
            switch (packet.getType()){
                case ShotgunBullet -> new ShotgunBullet(
                        Player.CLIENT_PLAYER,
                        packet.getMouseXLocation(),
                        packet.getMouseYLocation(),
                        packet.getDamage()
                );
                case SniperRifleBullet -> new SniperRifleBullet(
                        Player.CLIENT_PLAYER,
                        packet.getMouseXLocation(),
                        packet.getMouseYLocation(),
                        packet.getDamage()
                );
                case PistolBullet -> new PistolBullet(
                        Player.CLIENT_PLAYER,
                        packet.getMouseXLocation(),
                        packet.getMouseYLocation(),
                        packet.getDamage()
                );
                case AssaultRifleBullet -> new AssaultRifleBullet(
                        Player.CLIENT_PLAYER,
                        packet.getMouseXLocation(),
                        packet.getMouseYLocation(),
                        packet.getDamage()
                );
            }

            otherPlayer.incrementBulletCount();
        } else if (object instanceof ClientSFXPacket packet) {
            clientWeaponAudio.setFile(packet.getClientSFXInt());
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
                    checkVictims(bullet);
                }
            }
        }

        for (int i = 0; i < powerUps.size(); i++) {
            if(powerUps.get(i) != null){
                checkPowerUpPickups(powerUps.get(i));
            }
        }

        //Every COOLDOWN_BETWEEN_POWER_UPS there is a 50% chance of a power up spawning.
        if(powerUps.size() < 4 && currentPowerUpCooldown == 0 && World.getSRandom().nextBoolean()){
            currentPowerUpCooldown = COOLDOWN_BETWEEN_POWER_UPS;

            boolean loop = true;
            int x = 0;
            int y = 0;
            while(loop) {
                x = World.getSRandom().nextInt(WIDTH);
                y = World.getSRandom().nextInt(HEIGHT);
                loop = false;
                for (Block block : blocks) {
                    if ((new Rectangle(x, y, PowerUp.POWER_UP_DIMENSIONS.width, PowerUp.POWER_UP_DIMENSIONS.height)
                            .intersects(block.getBounds()))) {
                        loop = true;
                    }
                }
            }
            switch (World.getSRandom().nextInt(PowerUp.PowerUpType.values().length)){
                case 0:
                    powerUps.add(new DamageUp(x,y,2));
                    outputConnection.sendPacket(new CreatePowerUpPacket(x,y, PowerUp.PowerUpType.DamageUp));
                    break;
                case 1:
                    powerUps.add(new DamageDown(x,y, 0.5F));
                    outputConnection.sendPacket(new CreatePowerUpPacket(x,y, PowerUp.PowerUpType.DamageDown));
                    break;
                case 2:
                    powerUps.add(new SpeedUp(x,y,1.5F));
                    outputConnection.sendPacket(new CreatePowerUpPacket(x,y, PowerUp.PowerUpType.SpeedUp));
            }

        } else if (currentPowerUpCooldown == 0){
            currentPowerUpCooldown = COOLDOWN_BETWEEN_POWER_UPS;
        } else {
            currentPowerUpCooldown--;
        }

    }

    private void checkPowerUpPickups(PowerUp powerUp) {
        if(powerUp.getBounds().intersects(thisPlayer.getBounds())){
            powerUp.applyPowerUp(Player.SERVER_PLAYER);
        } else if (powerUp.getBounds().intersects(otherPlayer.getBounds())){
            powerUp.applyPowerUp(Player.CLIENT_PLAYER);
        }
    }

    private void checkVictims(Bullet bullet) {
        // Player who was hit (-1 if no one was hit)
        int victimNumber = EntityCollision.getVictim(bullet);
        System.out.println("CHECK");

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

            double damage = (-1 * bullet.getDamage() * killer.getDamageMultiplier());
            System.out.println(damage);
            killer.incrementBulletHitCount();
            victim.modifyHealth(damage);
            victim.resetHealTimer();
            killer.addTDO(damage);

            if (victim.getHealth() == 0) {

                //Handle death markers on the floor
                new DeathMark(victim.getX(), victim.getY(), victimNumber);
                outputConnection.sendPacket(new EyeCandyPacket(eyeCandy.toArray(new GameObject[0])));

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

    public void declareWinner(Player winner){
        int winnerNumber;
        if(winner == thisPlayer){
            winnerNumber = Player.SERVER_PLAYER;
        } else {
            winnerNumber = Player.CLIENT_PLAYER;
        }

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
        }

        renderWinner(winnerNumber, playerInfo);

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
    }
}