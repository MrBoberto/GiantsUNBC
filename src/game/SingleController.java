package game;

import audio.SFXPlayer;
import eye_candy.DeathMark;
import inventory_items.*;
import mapObjects.Block;
import player.*;
import power_ups.*;
import utilities.BufferedImageLoader;
import weapons.ammo.*;
import weapons.aoe.Explosion;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.awt.*;
import java.io.IOException;
import java.util.List;

public class SingleController extends Controller {

    public SFXPlayer weaponAudio;

    public SingleController() {
        super();
        weaponAudio = new SFXPlayer();

        //Loading level
        level = BufferedImageLoader.loadImage("/resources/mapLayouts/Level"+ MainMenu.mapSelected +".png");
        loadLevel(level);

        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(new Color(0, 0, 0));

        thisPlayer = new MainPlayer(Controller.thisX, Controller.thisY, Player.SERVER_PLAYER, Color.BLUE);
        otherPlayer = new AIPlayer(Controller.otherX, Controller.otherY, Player.CLIENT_PLAYER, Color.RED);

        thisPlayer.setArsenal(new Arsenal(0,620, thisPlayer));
        otherPlayer.setArsenal(new Arsenal(816,620, otherPlayer));

        if (MainMenu.playerName.equals("")) {
            thisPlayer.setPlayerName("Player");
            otherPlayer.setPlayerName("Thanos");
        } else {
            thisPlayer.setPlayerName(MainMenu.playerName);
            otherPlayer.setPlayerName("Thanos");
        }

        System.out.println("Controller built.");
        start();

    }

    @Override
    public void packetReceived(Object object) {

    }

    @Override
    public void close() {
        try {
            inputConnection.close();
            outputConnection.close();
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
                        //System.out.println("Rocket stopped moving!");
                        movingAmmo.remove(bullet);
                        bullet.setVelX(0);
                        bullet.setVelY(0);
                        explosions.add(new Explosion(bullet.x, bullet.y, bullet.getPlayerIBelongToNumber()));

                        weaponAudio.setFile(-1);
                        weaponAudio.play();
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
                }
                case 1 -> {
                    powerUps.add(new DamageDown(x, y, 0.5F));
                }
                case 2 -> {
                    powerUps.add(new SpeedUp(x, y, 1.5F));
                }
                case 3->{
                    powerUps.add(new SpeedDown(x, y, 0.75F));
                }
                case 4->{
                    powerUps.add(new Ricochet(x, y, 2));
                }
            }

        } else if (currentPowerUpCooldown == 0) {
            currentPowerUpCooldown = COOLDOWN_BETWEEN_POWER_UPS;
        } else {
            currentPowerUpCooldown--;
        }

        //Every COOLDOWN_BETWEEN_INVENTORY_ITEMS there is a 50% chance of an inventory item spawning.
        if(inventoryItems.size() < 5 && currentInventoryItemCooldown == 0 && World.getSRandom().nextBoolean()){
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
                }
                case 1 -> {
                    inventoryItems.add(new SniperRifleItem(x, y));
                }
                case 2 -> {
                    inventoryItems.add(new AssaultRifleItem(x, y));
                }
                case 4->{
                    inventoryItems.add(new RocketLauncherItem(x, y));
                }
            }

        } else if (currentInventoryItemCooldown == 0){
            currentInventoryItemCooldown = COOLDOWN_BETWEEN_INVENTORY_ITEMS;
        } else {
            currentInventoryItemCooldown--;
        }

        // AI attempts to shoot if in range
        double distance = World.pythHyp(otherPlayer.x - thisPlayer.x, otherPlayer.y - thisPlayer.y);
        if (otherPlayer.getSelectedWeapon() == 0
                && Controller.otherPlayer.getArsenal().getPrimary().getCurrentDelay() == 0) {
            if ((otherPlayer.getArsenal().getPrimary().getSPEED() / 2) *
                    (otherPlayer.getArsenal().getPrimary().getSPEED() / FRICTION) > distance) {
                Controller.otherPlayer.getArsenal().getPrimary().shoot(thisPlayer.x, thisPlayer.y);
                Controller.otherPlayer.getArsenal().getPrimary().playAudio();

                Controller.otherPlayer.getArsenal().getPrimary().setCurrentDelay(
                        Controller.otherPlayer.getArsenal().getPrimary().getMAX_DELAY());
            }
        } else if (otherPlayer.getSelectedWeapon() == 1
                && Controller.otherPlayer.getArsenal().getSecondary() != null
                && Controller.otherPlayer.getArsenal().getSecondary().getCurrentDelay() == 0) {
            if ((otherPlayer.getArsenal().getSecondary().getSPEED() / 2) *
                    (otherPlayer.getArsenal().getSecondary().getSPEED() / FRICTION) > distance) {
                Controller.otherPlayer.getArsenal().getSecondary().shoot(thisPlayer.x, thisPlayer.y);
                Controller.otherPlayer.getArsenal().getSecondary().playAudio();

                Controller.otherPlayer.getArsenal().getSecondary().setCurrentDelay(
                        Controller.otherPlayer.getArsenal().getSecondary().getMAX_DELAY());
            }
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
            if (bullet.getSERIAL() == 004) {
                movingAmmo.remove(bullet);
                bullet.setVelX(0);
                bullet.setVelY(0);
                //System.out.println("Rocket launcher victor: " + bullet.getPlayerIBelongToNumber() + "; Rocket launcher loser: " + victimNumber);
                explosions.add(new Explosion(bullet.x, bullet.y, bullet.getPlayerIBelongToNumber()));

                weaponAudio.setFile(-1);
                weaponAudio.play();
            } else if (bullet.getSERIAL() != 002) {
                movingAmmo.remove(bullet);
            }

            killer.incrementBulletHitCount();
            victim.modifyHealth(-1 * bullet.getDamage());
            victim.resetHealTimer();
            killer.addTDO(-1 * bullet.getDamage());

            if (victim.getHealth() == 0) {

                //Handle death markers on the floor
                new DeathMark(victim.getX(), victim.getY(), victimNumber);

                victim.incrementDeathCount();
                victim.revive();

                System.out.println("Individual kill: " + victim.getPlayerName() + " was memed by "
                        + killer.getPlayerName() + "'s " + bullet.getSERIAL());

                killer.incrementKillCount();
                if (killer instanceof AIPlayer) {
                    AIPlayer.setDialogue("I am inevitable.");
                }
                if(victim.getDeathCount() >= 10){
                    declareWinner(killer);
                }
            }
        }
    }

    private void checkVictims(Explosion explosion) {
        // Player who was hit (-1 if no one was hit)
        int victimNumber = EntityCollision.getExplosionVictim(explosion);

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

        if (victimNumber >= 0 && explosion.isHarmful() && !victim.isInvincible()) {

            killer.incrementBulletHitCount();
            victim.modifyHealth(-1 * Explosion.DAMAGE);
            victim.resetHealTimer();
            killer.addTDO(-1 * Explosion.DAMAGE);

            if (victim.getHealth() == 0) {

                //Handle death markers on the floor
                new DeathMark(victim.getX(), victim.getY(), victimNumber);

                victim.incrementDeathCount();
                victim.revive();

                killer.incrementKillCount();
                //System.out.println("Individual kill: " + victim.getPlayerName() + " was memed by " + killer.getPlayerName() + " using the " + RocketLauncherBullet.getIteration() + "th explosion.");
                // System.out.println(victim.getPlayerName() + " was memed by " + killer.getPlayerName());
                if(victim.getDeathCount() >= 10){
                    declareWinner(killer);
                }
            }
        } else if (victimNumber == -2 && explosion.isHarmful()) {

            if (!otherPlayer.isInvincible()) {
                otherPlayer.modifyHealth(-1 * Explosion.DAMAGE);
                otherPlayer.resetHealTimer();
                if (otherPlayer.getPlayerNumber() != killer.getPlayerNumber()) {
                    killer.addTDO(-1 * Explosion.DAMAGE);
                    killer.incrementBulletHitCount();
                }

                if (otherPlayer.getHealth() == 0) {

                    //Handle death markers on the floor
                    new DeathMark(otherPlayer.getX(), otherPlayer.getY(), Player.CLIENT_PLAYER);

                    otherPlayer.incrementDeathCount();
                    otherPlayer.revive();

                    if (otherPlayer.getPlayerNumber() != killer.getPlayerNumber()) {
                        killer.incrementKillCount();
                        if(otherPlayer.getDeathCount() >= 10){
                            declareWinner(killer);
                        }
                    }

                    System.out.println("Double kill: " + thisPlayer.getPlayerName() + " was memed by " + killer.getPlayerName());

                }
            }
            if (!thisPlayer.isInvincible()) {
                thisPlayer.modifyHealth(-1 * Explosion.DAMAGE);
                thisPlayer.resetHealTimer();

                if (thisPlayer.getPlayerNumber() != killer.getPlayerNumber()) {
                    killer.addTDO(-1 * Explosion.DAMAGE);
                    killer.incrementBulletHitCount();
                }

                if (thisPlayer.getHealth() == 0) {

                    //Handle death markers on the floor
                    new DeathMark(thisPlayer.getX(), thisPlayer.getY(), Player.SERVER_PLAYER);

                    thisPlayer.incrementDeathCount();
                    thisPlayer.revive();

                    if (thisPlayer.getPlayerNumber() != killer.getPlayerNumber()) {
                        killer.incrementKillCount();
                        if(thisPlayer.getDeathCount() >= 10){
                            declareWinner(killer);
                        }
                    }
                    // System.out.println(victim.getPlayerName() + " was memed by " + killer.getPlayerName());
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

        isRunning = false;
    }

    public static List<InventoryItem> getInventoryItems() {
        return inventoryItems;
    }

    public static List<PowerUp> getPowerUps() {
        return powerUps;
    }
}