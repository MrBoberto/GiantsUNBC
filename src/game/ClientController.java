package game;

/**
 * This file is part of a solution to
 *		CPSC300 Term Project Fall 2021
 *
 * The controller from the client perspective
 *
 * @author The Boyz
 * @version 1
 */

import audio.SFXPlayer;
import inventory_items.*;
import packets.*;
import player.Arsenal;
import player.MainPlayer;
import player.OtherPlayer;
import player.Player;
import power_ups.*;
import utilities.BufferedImageLoader;
import weapons.aoe.Explosion;
import weapons.aoe.Slash;
import weapons.guns.*;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;


public class ClientController extends Controller {

    // Multiplayer
    private Socket socket;
    private String correctIp;

    // Audio
    public final SFXPlayer serverWeaponAudio;

    public ClientController() throws UnknownHostException {
        super();

        serverWeaponAudio = new SFXPlayer();
        setBackground(new Color(0, 0, 0));

        initializePlayers();

        if (establishConnection()) return;     // exit method, otherwise "ip might not have been initialized"

        start();
    }

    private boolean establishConnection() {
        //Get ip
        InetAddress correctAddress = Main.getAddress();
        final byte[] ip;
        try {
            ip = correctAddress.getAddress();
        } catch (Exception e) {
            return true;
        }
        String ipAddress = JOptionPane.showInputDialog("Please enter the server's ip address, or leave blank to search automatically:");
        try {
            while (socket == null) {
                try {
                    if (ipAddress.equals("")) {
                        for (int i = 1; i <= 254; i++) {
                            final int j = i;  // i as non-final variable cannot be referenced from inner class
                            // new thread for parallel execution
                            new Thread(() -> {
                                try {
                                    ip[3] = (byte) j;
                                    InetAddress address = InetAddress.getByAddress(ip);
                                    String output = address.toString().substring(1);
                                    try {
                                        socket = new Socket(output, Controller.PORT);
                                        System.out.println("FOUND SERVER");
                                        System.out.println(output + " is this the server");
                                        correctIp = output;
                                        //socket.close(); //needed if 2 connections tried
                                    } catch (Exception e) {//e.printStackTrace();}
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }).start();     // don't forget to start the thread
                        }
                    } else try {
                        socket = new Socket(ipAddress, Controller.PORT);
                    } catch (Exception ignored) {
                    }
                } catch (Exception e) {
                    System.out.println("USER CLICKED CANCEL");
                    gameWindow.getFrame().dispose();
                    new MainMenu();
                    break;
                }
                TimeUnit.SECONDS.sleep(1);
                if (socket == null){
                    ipAddress = JOptionPane.showInputDialog("Could not find ip/invalid address please enter server's ip: ");
                }
            }

            System.out.println("The client:" + correctAddress.getHostAddress() + "\nThe server" + correctIp);
            if (correctAddress.getHostAddress().equals(correctIp)) {
                System.out.println("THE SERVER AND CLIENT ARE ON THE SAME COMPUTER");
            }
            System.out.println("waiting for connection...");
            System.out.println("connection accepted");

            outputConnection = new OutputConnection(this, socket);
            System.out.println("output connection complete");
            outputConnection.sendPacket(new StartRequest(thisPlayer.getPlayerName()));
            inputConnection = new InputConnection(this, socket);
            System.out.println("input connection complete");

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("server + client connected.");
        return false;
    }

    private void initializePlayers() {
        thisPlayer = new MainPlayer(Controller.otherX, Controller.otherY, Player.CLIENT_PLAYER, Color.RED);
        otherPlayer = new OtherPlayer(Controller.thisX, Controller.thisY, Player.SERVER_PLAYER, Color.BLUE);

        otherPlayer.setArsenal(new Arsenal(0, 620, otherPlayer));
        thisPlayer.setArsenal(new Arsenal(816, 620, thisPlayer));

        if (MainMenu.playerName.equals("")) {
            thisPlayer.setPlayerName("Guest");
        } else {
            thisPlayer.setPlayerName(MainMenu.playerName);
        }
    }

    @Override
    public void packetReceived(Object object) {
        if (object instanceof StartPacket packet) {

            loadStartPacket(packet);

        } else if (object instanceof ServerUpdatePacket packet) {

            if (otherPlayer != null) {
                loadServerUpdatePacket(packet);
            }

        } else if (object instanceof ServerBulletPacket packet) {

            loadServerBulletPacket(packet);

        } else if (object instanceof ServerExplosionPacket packet) {

            loadServerExplosionPacket(packet);

        } else if (object instanceof DeathCountPacket packet) {

            loadDeathCountPacket(packet);

        } else if (object instanceof ClientSlashPacket packet) {

            loadClientSlashPacket(packet);

        } else if (object instanceof ServerSFXPacket packet) {

            loadServerSFXPacket(packet);

        } else if (object instanceof RespawnPacket) {

            thisPlayer.revive();

        } else if (object instanceof EyeCandyPacket packet) {

            loadEyeCandyPacket(packet);

        } else if (object instanceof ServerDashPacket) {

            otherPlayer.startDashTimer();

        } else if (object instanceof PowerUpEffectPacket packet) {

            loadPowerUpEffectPacket(packet);

        } else if (object instanceof CreatePowerUpPacket packet) {

            loadCreatePowerUpPacket(packet);

        } else if (object instanceof InventoryItemPacket packet) {

            loadInventoryItemPacket(packet);

        } else if (object instanceof CreateInventoryItemPacket packet) {

            loadCreateInventoryItemPacket(packet);

        } else if (object instanceof WinnerPacket packet) {

            loadWinnerPacket(packet);

        } else if (object instanceof ArsenalPacket packet) {
            otherPlayer.getArsenal().setInventory(packet.primary(), packet.secondary(), packet.selected(), packet.inventory());
        }
    }

    private void loadWinnerPacket(WinnerPacket packet) {
        isWon = true;

        thisPlayer.setKillCount((int) packet.playerInfo()[1][0]);
        thisPlayer.setDeathCount((int) packet.playerInfo()[1][1]);
        thisPlayer.setBulletsShot((int) packet.playerInfo()[1][3]);
        thisPlayer.setBulletsHit((int) packet.playerInfo()[1][4]);
        thisPlayer.setWalkingDistance((int) packet.playerInfo()[1][5]);
        thisPlayer.setPickedUpPowerUps((int) packet.playerInfo()[1][6]);

        otherPlayer.setKillCount((int) packet.playerInfo()[0][0]);
        otherPlayer.setDeathCount((int) packet.playerInfo()[0][1]);
        otherPlayer.setBulletsShot((int) packet.playerInfo()[0][3]);
        otherPlayer.setBulletsHit((int) packet.playerInfo()[0][4]);
        otherPlayer.setWalkingDistance((int) packet.playerInfo()[0][5]);
        otherPlayer.setPickedUpPowerUps((int) packet.playerInfo()[0][6]);


        renderWinner(packet.getWinner());
        isRunning = false;
    }

    private void loadCreateInventoryItemPacket(CreateInventoryItemPacket packet) {
        switch (packet.getPowerUpType()) {
            case Shotgun -> inventoryItems.add(new ShotgunItem(packet.getX(), packet.getY()));
            case SniperRifle -> inventoryItems.add(new SniperRifleItem(packet.getX(), packet.getY()));
            case Pistol -> inventoryItems.add(new PistolItem(packet.getX(), packet.getY()));
            case AssaultRifle -> inventoryItems.add(new AssaultRifleItem(packet.getX(), packet.getY()));
            case RocketLauncher -> inventoryItems.add(new RocketLauncherItem(packet.getX(), packet.getY()));
            case LightningSword -> inventoryItems.add(new LightningSwordItem(packet.getX(), packet.getY()));
        }
    }

    private void loadInventoryItemPacket(InventoryItemPacket packet) {
        if (packet.getIndexToRemove() >= 0) {
            switch (packet.getPlayerToBeAffected()) {
                case Player.SERVER_PLAYER:
                    switch (packet.getSerial()) {
                        case 0:
                            if (Controller.otherPlayer.getArsenal().lacksWeapon(0)) {
                                Controller.otherPlayer.getArsenal().add(new Shotgun(Controller.otherPlayer));
                                inventoryItems.remove(packet.getIndexToRemove());
                            }
                            break;
                        case 1:
                            if (Controller.otherPlayer.getArsenal().lacksWeapon(1)) {
                                Controller.otherPlayer.getArsenal().add(new SniperRifle(Controller.otherPlayer));
                                inventoryItems.remove(packet.getIndexToRemove());
                            }
                            break;
                        case 2:
                            if (Controller.otherPlayer.getArsenal().lacksWeapon(2)) {
                                Controller.otherPlayer.getArsenal().add(new Pistol(Controller.otherPlayer));
                                inventoryItems.remove(packet.getIndexToRemove());
                            }
                            break;
                        case 3:
                            if (Controller.otherPlayer.getArsenal().lacksWeapon(3)) {
                                Controller.otherPlayer.getArsenal().add(new AssaultRifle(Controller.otherPlayer));
                                inventoryItems.remove(packet.getIndexToRemove());
                            }
                            break;
                        case 4:
                            if (Controller.otherPlayer.getArsenal().lacksWeapon(4)) {
                                Controller.otherPlayer.getArsenal().add(new RocketLauncher(Controller.otherPlayer));
                                inventoryItems.remove(packet.getIndexToRemove());
                            }
                            break;
                        case 5:
                            if (Controller.otherPlayer.getArsenal().lacksWeapon(5)) {
                                Controller.otherPlayer.getArsenal().add(new LightningSword(Controller.otherPlayer));
                                inventoryItems.remove(packet.getIndexToRemove());
                            }
                            break;
                    }
                    break;
                case Player.CLIENT_PLAYER:
                    switch (packet.getSerial()) {
                        case 0:
                            if (Controller.thisPlayer.getArsenal().lacksWeapon(0)) {
                                Controller.thisPlayer.getArsenal().add(new Shotgun(Controller.thisPlayer));
                                inventoryItems.remove(packet.getIndexToRemove());
                            }
                            break;
                        case 1:
                            if (Controller.thisPlayer.getArsenal().lacksWeapon(1)) {
                                Controller.thisPlayer.getArsenal().add(new SniperRifle(Controller.thisPlayer));
                                inventoryItems.remove(packet.getIndexToRemove());
                            }
                            break;
                        case 2:
                            if (Controller.thisPlayer.getArsenal().lacksWeapon(2)) {
                                Controller.thisPlayer.getArsenal().add(new Pistol(Controller.thisPlayer));
                                inventoryItems.remove(packet.getIndexToRemove());
                            }
                            break;
                        case 3:
                            if (Controller.thisPlayer.getArsenal().lacksWeapon(3)) {
                                Controller.thisPlayer.getArsenal().add(new AssaultRifle(Controller.thisPlayer));
                                inventoryItems.remove(packet.getIndexToRemove());
                            }
                            break;
                        case 4:
                            if (Controller.thisPlayer.getArsenal().lacksWeapon(4)) {
                                Controller.thisPlayer.getArsenal().add(new RocketLauncher(Controller.thisPlayer));
                                inventoryItems.remove(packet.getIndexToRemove());
                            }
                            break;
                        case 5:
                            if (Controller.thisPlayer.getArsenal().lacksWeapon(5)) {
                                Controller.thisPlayer.getArsenal().add(new LightningSword(Controller.thisPlayer));
                                inventoryItems.remove(packet.getIndexToRemove());
                            }
                            break;
                    }
                    break;
            }
        }
    }

    private void loadCreatePowerUpPacket(CreatePowerUpPacket packet) {
        //Use default properties since server is the one that controls effects and collisions.
        switch (packet.getPowerUpType()) {
            case DamageUp -> powerUps.add(new DamageUp(packet.getX(), packet.getY(), Player.DEFAULT_DAMAGE_MULTIPLIER));
            case DamageDown -> powerUps.add(new DamageDown(packet.getX(), packet.getY(), Player.DEFAULT_DAMAGE_MULTIPLIER));
            case SpeedUp -> powerUps.add(new SpeedUp(packet.getX(), packet.getY(), Player.DEFAULT_SPEED_MULTIPLIER));
            case SpeedDown -> powerUps.add(new SpeedDown(packet.getX(), packet.getY(), Player.DEFAULT_SPEED_MULTIPLIER));
            case Ricochet -> powerUps.add(new Ricochet(packet.getX(), packet.getY(), Player.DEFAULT_NUMBER_OF_BOUNCES));
        }
    }

    private void loadPowerUpEffectPacket(PowerUpEffectPacket packet) {
        powerUps.remove(packet.getIndexToRemove());
        switch (packet.getPlayerToBeAffected()) {
            case Player.SERVER_PLAYER -> {
                if (packet.getDamageMultiplier() != -1) {
                    otherPlayer.setDamageMultiplier(packet.getDamageMultiplier(), packet.getTime());
                }

                if (packet.getSpeedMultiplier() != -1) {
                    otherPlayer.setSpeedMultiplier(packet.getSpeedMultiplier(), packet.getTime());
                }

                if (packet.getRicochetBounces() != -1) {
                    otherPlayer.setRicochet(packet.getRicochetBounces(), packet.getTime());

                }
            }
            /* here goes other property changes */
            case Player.CLIENT_PLAYER -> {
                if (packet.getDamageMultiplier() != -1) {
                    thisPlayer.setDamageMultiplier(packet.getDamageMultiplier(), packet.getTime());
                }

                if (packet.getSpeedMultiplier() != -1) {
                    thisPlayer.setSpeedMultiplier(packet.getSpeedMultiplier(), packet.getTime());
                }

                if (packet.getRicochetBounces() != -1) {
                    thisPlayer.setRicochet(packet.getRicochetBounces(), packet.getTime());
                    System.out.println(packet.getRicochetBounces());
                }
            }
        }
    }

    private void loadEyeCandyPacket(EyeCandyPacket packet) {
        eyeCandy = new ArrayList<>(Arrays.asList(packet.getEyeCandy()));
    }

    private void loadServerSFXPacket(ServerSFXPacket packet) {
        serverWeaponAudio.setFile(packet.getServerSFXInt());
        serverWeaponAudio.play();
    }

    private void loadClientSlashPacket(ClientSlashPacket packet) {
        new Slash(
                packet.getX(),
                packet.getY(),
                packet.getAngle(),
                packet.isLeft(),
                Player.SERVER_PLAYER
        );
        otherPlayer.setSwordLeft(!packet.isLeft());
        serverWeaponAudio.setFile(5);
        serverWeaponAudio.play();
    }

    private void loadDeathCountPacket(DeathCountPacket packet) {
        thisPlayer.setDeathCount(packet.getClientDeaths());
        otherPlayer.setDeathCount(packet.getServerDeaths());
    }

    private void loadServerExplosionPacket(ServerExplosionPacket packet) {
        explosions.add(new Explosion(packet.getX(), packet.getY(), packet.getPlayerNumber()));
        serverWeaponAudio.setFile(-1);
        serverWeaponAudio.play();
    }

    private void loadServerBulletPacket(ServerBulletPacket packet) {
        movingAmmo = new ArrayList<>(Arrays.asList(packet.getAmmo()));
    }

    private void loadServerUpdatePacket(ServerUpdatePacket packet) {
        otherPlayer.setWalking(packet.isWalking());
        otherPlayer.setX(packet.getX());
        otherPlayer.setY(packet.getY());
        otherPlayer.setAngle(packet.getAngle());
        otherPlayer.setHealth(packet.getHealth()[0]);
        otherPlayer.setWeaponSerial(packet.getWeaponSerial());
        otherPlayer.setInvincible(packet.isInvincible()[0]);

        thisPlayer.setHealth(packet.getHealth()[1]);
        thisPlayer.setInvincible(packet.isInvincible()[1]);
    }

    private void loadStartPacket(StartPacket packet) {
        if (packet.getPlayerName() == null || packet.getPlayerName().equals("")) {
            otherPlayer.setPlayerName("Host");
        } else {
            otherPlayer.setPlayerName(packet.getPlayerName());
        }

        loadLevel(BufferedImageLoader.loadImage("/resources/mapLayouts/Level" + packet.getMapSelected() + ".png"));

        thisPlayer.setRespawnPoint(packet.getX(), packet.getY());
        thisPlayer.setX(packet.getX());
        thisPlayer.setY(packet.getY());
    }

    public void renderWinner(int winnerNumber) {
        gameWindow.frame.dispose();
        try {
            soundtrack.stop();
        } catch (Exception ex) {
            System.out.println("Error with stopping sound.");
            ex.printStackTrace();
        }

        System.out.println("renderWinner");


        Player winner;
        Player loser;
        if (winnerNumber == Player.CLIENT_PLAYER) {
            winner = thisPlayer;
            loser = otherPlayer;
        } else {
            winner = otherPlayer;
            loser = thisPlayer;
        }


        isRunning = false;
        gameWindow.frame.dispose();
        World.setGameOver(new GameOver(loser, winner, players));

    }

    @Override
    public void close() {
        try {
            outputConnection.close();

        } catch (IOException ignored) {
            /* empty */
        }

        try {
            inputConnection.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
