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
import weapons.aoe.Slash;
import weapons.guns.LightningSword;
import weapons.guns.RocketLauncher;
import weapons.guns.Weapon;

import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("ForLoopReplaceableByForEach")
public class SingleController extends Controller {

    public final SFXPlayer weaponAudio;

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
        } else {
            thisPlayer.setPlayerName(MainMenu.playerName);
        }
        otherPlayer.setPlayerName("Thanos");
        start();

    }

    @Override
    public void packetReceived(Object object) {

    }

    @Override
    public void close() {
        try {
            if (World.controller instanceof SingleController) return;
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
                    if (bullet.getSERIAL() == 4 &&
                            EntityCollision.getBulletVictim(bullet) != bullet.getPlayerIBelongToNumber()) {
                        //("Rocket stopped moving!");
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

        for (int i = 0; i < explosions.size(); i++) {
            if (explosions.get(i) != null) {
                checkVictims(explosions.get(i));
            }
        }

        for (int i = 0; i < slashes.size(); i++) {
            if (slashes.get(i) != null) {
                checkVictims(slashes.get(i));
            }
        }

        for (int i = 0; i < powerUps.size(); i++) {
            if (powerUps.get(i) != null) {
                checkPowerUpPickups(powerUps.get(i));
            }
        }

        for (int i = 0; i < inventoryItems.size(); i++) {
            if (inventoryItems.get(i) != null) {
                checkInventoryItemPickups(inventoryItems.get(i));
            }
        }

        //Every COOLDOWN_BETWEEN_POWER_UPS there is a 50% chance of a power up spawning.
        if(powerUps.size() < 4 && currentPowerUpCooldown == 0 && World.sRandom.nextBoolean()){
            currentPowerUpCooldown = COOLDOWN_BETWEEN_POWER_UPS;

            boolean loop = true;
            int x = 0;
            int y = 0;
            while(loop) {
                x = World.sRandom.nextInt(WIDTH);
                y = World.sRandom.nextInt(HEIGHT);
                loop = false;
                for (Block block : blocks) {
                    if ((new Rectangle(x, y, PowerUp.POWER_UP_DIMENSIONS.width, PowerUp.POWER_UP_DIMENSIONS.height)
                            .intersects(block.getBounds()))) {
                        loop = true;
                    }
                }
            }
            switch (World.sRandom.nextInt(PowerUp.PowerUpType.values().length)) {
                case 0 -> powerUps.add(new DamageUp(x, y, 2));
                case 1 -> powerUps.add(new DamageDown(x, y, 0.5F));
                case 2 -> powerUps.add(new SpeedUp(x, y, 1.5F));
                case 3-> powerUps.add(new SpeedDown(x, y, 0.25F));
                case 4-> powerUps.add(new Ricochet(x, y, 2));
            }

        } else if (currentPowerUpCooldown == 0) {
            currentPowerUpCooldown = COOLDOWN_BETWEEN_POWER_UPS;
        } else {
            currentPowerUpCooldown--;
        }

        //Every COOLDOWN_BETWEEN_INVENTORY_ITEMS there is a 50% chance of an inventory item spawning.
        if(inventoryItems.size() < 5 && currentInventoryItemCooldown == 0 && World.sRandom.nextBoolean()){
            currentInventoryItemCooldown = COOLDOWN_BETWEEN_INVENTORY_ITEMS;

            boolean loop = true;
            int x = 0;
            int y = 0;
            while(loop) {
                x = World.sRandom.nextInt(WIDTH);
                y = World.sRandom.nextInt(HEIGHT);
                loop = false;
                for (Block block : blocks) {
                    if ((new Rectangle(x, y, InventoryItem.INVENTORY_ITEM_DIMENSIONS.width, InventoryItem.INVENTORY_ITEM_DIMENSIONS.height)
                            .intersects(block.getBounds()))) {
                        loop = true;
                    }
                }
            }
            switch (World.sRandom.nextInt(InventoryItem.InventoryItemType.values().length)) {
                case 0 -> inventoryItems.add(new ShotgunItem(x, y));
                case 1 -> inventoryItems.add(new SniperRifleItem(x, y));
                case 2 -> inventoryItems.add(new AssaultRifleItem(x, y));
                case 4-> inventoryItems.add(new RocketLauncherItem(x, y));
                case 5-> inventoryItems.add(new LightningSwordItem(x, y));
            }

        } else if (currentInventoryItemCooldown == 0){
            currentInventoryItemCooldown = COOLDOWN_BETWEEN_INVENTORY_ITEMS;
        } else {
            currentInventoryItemCooldown--;
        }

        // AI attempts to shoot if in range
        double distance = World.pythHyp(otherPlayer.x - thisPlayer.x, otherPlayer.y - thisPlayer.y);
        Weapon primary = otherPlayer.getArsenal().getPrimary();            // Graphics version of this is unreliable
        Weapon secondary = otherPlayer.getArsenal().getSecondary();         // Graphics version of this is unreliable
        if (otherPlayer.getSelectedWeapon() == 0
                && primary.getCurrentDelay() == 0) {
            if ((primary.getSPEED() / 2) *
                    (primary.getSPEED() / FRICTION) > distance
                    || primary.getSERIAL() == LightningSword.SERIAL || primary.getSERIAL() == RocketLauncher.SERIAL) {
                primary.shoot(thisPlayer.x, thisPlayer.y);
                primary.playAudio();

                primary.setCurrentDelay(
                        primary.getMAX_DELAY());
            }
        } else if (otherPlayer.getSelectedWeapon() == 1
                && secondary != null
                && secondary.getCurrentDelay() == 0) {
            if ((secondary.getSPEED() / 2) *
                    (secondary.getSPEED() / FRICTION) > distance
                    || secondary.getSERIAL() == LightningSword.SERIAL || secondary.getSERIAL() == RocketLauncher.SERIAL) {
                secondary.shoot(thisPlayer.x, thisPlayer.y);
                secondary.playAudio();

                secondary.setCurrentDelay(
                        secondary.getMAX_DELAY());
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
            if (bullet.getSERIAL() == 4) {
                movingAmmo.remove(bullet);
                bullet.setVelX(0);
                bullet.setVelY(0);
                //("Rocket launcher victor: " + bullet.getPlayerIBelongToNumber() + "; Rocket launcher loser: " + victimNumber);
                explosions.add(new Explosion(bullet.x, bullet.y, bullet.getPlayerIBelongToNumber()));

                weaponAudio.setFile(-1);
                weaponAudio.play();
            } else if (bullet.getSERIAL() != 2) {
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

                killer.incrementKillCount();
                if (killer instanceof AIPlayer) {
                    AIPlayer.setDialogue("I am inevitable.");
                }
                if(victim.getDeathCount() >= PLAYER_LIVES){
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
        } else {
            killer = otherPlayer;
        }
        if (victimNumber == Player.CLIENT_PLAYER) {
            victim = otherPlayer;
        } else if (victimNumber == Player.SERVER_PLAYER) {
            victim = thisPlayer;
        } else {
            victim = null;
        }

        if (victimNumber >= 0 && explosion.isHarmful() && !Objects.requireNonNull(victim).isInvincible()) {

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
                if(victim.getDeathCount() >= PLAYER_LIVES){
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
                        if(otherPlayer.getDeathCount() >=PLAYER_LIVES){
                            declareWinner(killer);
                        }
                    }
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
                        if(thisPlayer.getDeathCount() >=PLAYER_LIVES){
                            declareWinner(killer);
                        }
                    }
                    // (victim.getPlayerName() + " was memed by " + killer.getPlayerName());
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

            double damage = (-1 * Slash.DAMAGE * killer.getDamageMultiplier());
            killer.incrementBulletHitCount();
            victim.modifyHealth(damage);
            victim.resetHealTimer();
            killer.addTDO(damage);

            if (victim.getHealth() == 0) {

                //Handle death markers on the floor
                new DeathMark(victim.getX(), victim.getY(), victimNumber);

                victim.incrementDeathCount();
                victim.revive();

                killer.incrementKillCount();
                // (victim.getPlayerName() + " was memed by " + killer.getPlayerName());
                if(victim.getDeathCount() >= PLAYER_LIVES){
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
        renderWinner(winnerNumber);
        // Kill the music
        soundtrack.stop();
        isRunning = false;
    }

    public static List<InventoryItem> getInventoryItems() {
        return inventoryItems;
    }

    public static List<PowerUp> getPowerUps() {
        return powerUps;
    }

    public void renderWinner(int winnerNumber) {
        gameWindow.frame.dispose();
        try
        {
            soundtrack.stop();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        Player winner;
        Player loser;
        if (winnerNumber == Player.SERVER_PLAYER) {
            winner = thisPlayer;
            loser = otherPlayer;
        } else {
            winner = otherPlayer;
            loser = thisPlayer;
        }


        isRunning = false;
        gameWindow.frame.dispose();
        World.setGameOver(new GameOver(loser,winner, players));

    }
}