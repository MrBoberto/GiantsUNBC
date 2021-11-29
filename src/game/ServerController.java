package game;


import inventory_items.*;
import audio.SFXPlayer;
import eye_candy.DeathMark;
import mapObjects.Block;
import packets.*;
import player.Arsenal;
import player.MainPlayer;
import player.OtherPlayer;
import player.Player;
import power_ups.*;
import utilities.BufferedImageLoader;
import weapons.ammo.*;
import weapons.aoe.Explosion;
import weapons.aoe.Slash;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.awt.*;
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

        //Loading level
        level = BufferedImageLoader.loadImage("/resources/mapLayouts/Level" + MainMenu.mapSelected +".png");
        loadLevel(level);

        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(new Color(0, 0, 0));

        thisPlayer = new MainPlayer(Controller.thisX, Controller.thisY, Player.SERVER_PLAYER, Color.BLUE);
        otherPlayer = new OtherPlayer(Controller.otherX, Controller.otherY, Player.CLIENT_PLAYER, Color.RED);

        thisPlayer.setArsenal(new Arsenal(0,620, thisPlayer));
        otherPlayer.setArsenal(new Arsenal(816,620, otherPlayer));

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

        } else if (object instanceof ClientDashPacket) {
            otherPlayer.startDashTimer();
        } else if (object instanceof StartRequest packet) {

            outputConnection.sendPacket(new StartPacket(otherPlayer.getRespawnPointX(), otherPlayer.getRespawnPointY(), 0, thisPlayer.getPlayerName(), MainMenu.mapSelected));
            otherPlayer.setPlayerName(packet.getClientName());
            System.out.println("Start request received and resent.");
        } else if (object instanceof ClientBulletPacket packet) {
            switch (packet.getType()) {
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
                case RocketLauncherBullet -> new RocketLauncherBullet(
                        Player.CLIENT_PLAYER,
                        packet.getMouseXLocation(),
                        packet.getMouseYLocation(),
                        packet.getDamage()
                );
            }

            otherPlayer.incrementBulletCount();

        } else if (object instanceof ClientExplosionPacket packet) {

            new Explosion(
                    packet.getX(),
                    packet.getY(),
                    packet.getPlayerNumber()
            );
            clientWeaponAudio.setFile(-1);
            clientWeaponAudio.play();

        } else if (object instanceof ClientSlashPacket packet) {

            new Slash(
                    packet.getX(),
                    packet.getY(),
                    packet.getAngle(),
                    Player.CLIENT_PLAYER,
                    packet.getDamage()
            );
            clientWeaponAudio.setFile(-1);
            clientWeaponAudio.play();

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
                    if (bullet.getSERIAL() == 004 &&
                            EntityCollision.getBulletVictim(bullet) != bullet.getPlayerIBelongToNumber()) {
                        System.out.println("Explosive triggered");
                        explosions.add(new Explosion(bullet.x, bullet.y, bullet.getPlayerIBelongToNumber()));
                        outputConnection.sendPacket(new ServerExplosionPacket(bullet.x, bullet.y, bullet.getPlayerIBelongToNumber()));

                        // Not necessarily an explosion created by client but clientWeaponAudio was the most convenient option
                        clientWeaponAudio.setFile(-1);
                        clientWeaponAudio.play();
                    }
                } else {
                    checkVictims(bullet);
                }
            }
        }

        for (int j = 0; j < explosions.size(); j++) {
            if (explosions.get(j) != null) {
                Explosion explosion = explosions.get(j);
                if (explosion.hasDied()) {
                    explosions.remove(explosion);
                } else {
                    checkVictims(explosion);
                }
            }
        }

        for (int j = 0; j < slashes.size(); j++) {
            if (slashes.get(j) != null) {
                Slash slash = slashes.get(j);
                if (slash.hasDied()) {
                    slashes.remove(slash);
                } else {
                    checkVictims(slash);
                }
            }
        }

        for (int i = 0; i < powerUps.size(); i++) {
            if(powerUps.get(i) != null){
                checkPowerUpPickups(powerUps.get(i));
            }
        }

        for (int i = 0; i < inventoryItems.size(); i++) {
            if(inventoryItems.get(i) != null){
                checkInventoryItemPickups(inventoryItems.get(i));
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
            switch (World.getSRandom().nextInt(PowerUp.PowerUpType.values().length)) {
                case 0 -> {
                    powerUps.add(new DamageUp(x, y, 2));
                    outputConnection.sendPacket(new CreatePowerUpPacket(x, y, PowerUp.PowerUpType.DamageUp));
                }
                case 1 -> {
                    powerUps.add(new DamageDown(x, y, 0.5F));
                    outputConnection.sendPacket(new CreatePowerUpPacket(x, y, PowerUp.PowerUpType.DamageDown));
                }
                case 2 -> {
                    powerUps.add(new SpeedUp(x, y, 1.5F));
                    outputConnection.sendPacket(new CreatePowerUpPacket(x, y, PowerUp.PowerUpType.SpeedUp));
                }
                case 3->{
                    powerUps.add(new SpeedDown(x, y, 0.75F));
                    outputConnection.sendPacket(new CreatePowerUpPacket(x, y, PowerUp.PowerUpType.SpeedDown));
                }
                case 4->{
                    powerUps.add(new Ricochet(x, y, 2));
                    outputConnection.sendPacket(new CreatePowerUpPacket(x, y, PowerUp.PowerUpType.Ricochet));
                }
            }

        } else if (currentPowerUpCooldown == 0) {
            currentPowerUpCooldown = COOLDOWN_BETWEEN_POWER_UPS;
        } else {
            currentPowerUpCooldown--;
        }

        //Every COOLDOWN_BETWEEN_INVENTORY_ITEMS there is a 50% chance of an inventory item spawning.
        if(inventoryItems.size() < 7 && currentInventoryItemCooldown == 0 && World.getSRandom().nextBoolean()){
            currentInventoryItemCooldown = COOLDOWN_BETWEEN_INVENTORY_ITEMS;

            boolean loop = true;
            int x = 0;
            int y = 0;
            while(loop) {
                x = World.getSRandom().nextInt(WIDTH);
                y = World.getSRandom().nextInt(HEIGHT);
                loop = false;
                for (Block block : blocks) {
                    if ((new Rectangle(x, y, InventoryItem.INVENTORY_ITEM_DIMENSIONS.width, InventoryItem.INVENTORY_ITEM_DIMENSIONS.height)
                            .intersects(block.getBounds()))) {
                        loop = true;
                    }
                }
            }
            switch (World.getSRandom().nextInt(InventoryItem.InventoryItemType.values().length)) {
                case 0 -> {
                    inventoryItems.add(new ShotgunItem(x, y));
                    outputConnection.sendPacket(new CreateInventoryItemPacket(x, y, InventoryItem.InventoryItemType.Shotgun));
                }
                case 1 -> {
                    inventoryItems.add(new SniperRifleItem(x, y));
                    outputConnection.sendPacket(new CreateInventoryItemPacket(x, y, InventoryItem.InventoryItemType.SniperRifle));
                }
                case 2 -> {
                    inventoryItems.add(new AssaultRifleItem(x, y));
                    outputConnection.sendPacket(new CreateInventoryItemPacket(x, y, InventoryItem.InventoryItemType.AssaultRifle));
                }
                case 4->{
                    inventoryItems.add(new RocketLauncherItem(x, y));
                    outputConnection.sendPacket(new CreateInventoryItemPacket(x, y, InventoryItem.InventoryItemType.RocketLauncher));
                }
                case 5-> {
                    inventoryItems.add(new LightningSwordItem(x, y));
                    outputConnection.sendPacket(new CreateInventoryItemPacket(x, y, InventoryItem.InventoryItemType.LightningSword));
                }
            }

        } else if (currentInventoryItemCooldown == 0){
            currentInventoryItemCooldown = COOLDOWN_BETWEEN_INVENTORY_ITEMS;
        } else {
            currentInventoryItemCooldown--;
        }
    }

    private void checkPowerUpPickups(PowerUp powerUp) {
        if(powerUp.getBounds().intersects(thisPlayer.getBounds())){
            powerUp.applyPowerUp(Player.SERVER_PLAYER);
        } else if (powerUp.getBounds().intersects(otherPlayer.getBounds())){
            powerUp.applyPowerUp(Player.CLIENT_PLAYER);
        }
    }

    private void checkInventoryItemPickups(InventoryItem inventoryItem) {
        if(inventoryItem.getBounds().intersects(thisPlayer.getBounds())){
            inventoryItem.giveItem(Player.SERVER_PLAYER);
        } else if (inventoryItem.getBounds().intersects(otherPlayer.getBounds())){
            inventoryItem.giveItem(Player.CLIENT_PLAYER);
        }
    }

    private void checkVictims(Bullet bullet) {
        // Player who was hit (-1 if no one was hit)
        int victimNumber = EntityCollision.getBulletVictim(bullet);

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
            movingAmmo.remove(bullet);

            if (bullet.getSERIAL() == 004) {
                System.out.println("Rocket launcher victor: " + bullet.getPlayerIBelongToNumber());
                explosions.add(new Explosion(bullet.x, bullet.y, bullet.getPlayerIBelongToNumber()));
                outputConnection.sendPacket(new ServerExplosionPacket(bullet.x, bullet.y,
                        bullet.getPlayerIBelongToNumber()));

                // Not necessarily an explosion created by client but clientWeaponAudio was the most convenient option
                clientWeaponAudio.setFile(-1);
                clientWeaponAudio.play();
            }
    //        if (bullet.getSERIAL() != 002) {

     //       }

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

    private void checkVictims(Explosion explosion) {
        // Player who was hit (-1 if no one was hit)
        int victimNumber = EntityCollision.getExplosionVictim(explosion);

        System.out.println("victimNumber = " + victimNumber + ", killerNumber = " + explosion.getPlayerIBelongToNumber());

        // Player
        Player killer;
        Player victim;
        if (explosion.getPlayerIBelongToNumber() == Player.SERVER_PLAYER) {
            killer = thisPlayer;
            if (victimNumber == Player.CLIENT_PLAYER) {
                victim = otherPlayer;
            } else if (victimNumber == Player.SERVER_PLAYER) {
                victim = thisPlayer;
            } else {
                victim = null;
            }
        } else {
            killer = otherPlayer;
            if (victimNumber == Player.CLIENT_PLAYER) {
                victim = otherPlayer;
            } else if (victimNumber == Player.SERVER_PLAYER) {
                victim = thisPlayer;
            } else {
                victim = null;
            }
        }

        double damage =  -1 * Explosion.DAMAGE * killer.getDamageMultiplier();

        if (victim != null && explosion.isHarmful() && !victim.isInvincible()) {
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
        } else if (victimNumber == -2 && explosion.isHarmful()) {

            if (!otherPlayer.isInvincible()) {
                otherPlayer.modifyHealth(damage);
                otherPlayer.resetHealTimer();
                if (otherPlayer.getPlayerNumber() != killer.getPlayerNumber()) {
                    killer.addTDO(damage);
                    killer.incrementBulletHitCount();
                }

                if (otherPlayer.getHealth() == 0) {

                    //Handle death markers on the floor
                    new DeathMark(otherPlayer.getX(), otherPlayer.getY(), Player.CLIENT_PLAYER);
                    outputConnection.sendPacket(new EyeCandyPacket(eyeCandy.toArray(new GameObject[0])));

                    otherPlayer.incrementDeathCount();
                    otherPlayer.revive();

                    outputConnection.sendPacket(new RespawnPacket());

                    if (otherPlayer.getPlayerNumber() != killer.getPlayerNumber()) {
                        killer.incrementKillCount();
                        if(otherPlayer.getDeathCount() >= 10){
                            declareWinner(killer);
                        }
                    }

                    // System.out.println(victim.getPlayerName() + " was memed by " + killer.getPlayerName());

                }
            }
            if (!thisPlayer.isInvincible()) {
                thisPlayer.modifyHealth(damage);
                thisPlayer.resetHealTimer();
                System.out.println("I got memed");

                if (thisPlayer.getPlayerNumber() != killer.getPlayerNumber()) {
                    killer.addTDO(damage);
                    killer.incrementBulletHitCount();
                }

                if (thisPlayer.getHealth() == 0) {

                    //Handle death markers on the floor
                    new DeathMark(thisPlayer.getX(), thisPlayer.getY(), Player.SERVER_PLAYER);
                    outputConnection.sendPacket(new EyeCandyPacket(eyeCandy.toArray(new GameObject[0])));

                    thisPlayer.incrementDeathCount();
                    thisPlayer.revive();

                    if (thisPlayer.getPlayerNumber() != killer.getPlayerNumber()) {
                        killer.incrementKillCount();
                        if(thisPlayer.getDeathCount() >= 10){
                            declareWinner(killer);
                        }
                    }
                    System.out.println(thisPlayer.getPlayerName() + " was memed by " + killer.getPlayerName());
                }
            }
        }
    }

    private void checkVictims(Slash slash) {
        // Player who was hit (-1 if no one was hit)
        int victimNumber = EntityCollision.getSlashVictim(slash);

        // Player
        Player killer;
        Player victim;
        if (slash.getPlayerIBelongToNumber() == Player.SERVER_PLAYER) {
            killer = thisPlayer;
            victim = otherPlayer;

        } else {
            killer = otherPlayer;
            victim = thisPlayer;
        }

        if (victimNumber != -1 && victimNumber != slash.getPlayerIBelongToNumber() && !victim.isInvincible()) {
            //        if (bullet.getSERIAL() != 002) {

            //       }

            double damage = (-1 * Slash.DAMAGE * killer.getDamageMultiplier());
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
        isWon = true;
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