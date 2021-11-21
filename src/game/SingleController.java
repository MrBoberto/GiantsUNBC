package game;

import StartMenu.MainMenuTest;
import packets.*;
import player.AIPlayer;
import player.MainPlayer;
import player.Player;
import tile.Tiles;
import weapons.ammo.*;

import java.awt.*;
import java.io.IOException;

public class SingleController extends Controller {

    public SingleController() {
        super();
        new GameWindow(WIDTH,HEIGHT,"THE BOYZ", this);

        this.addKeyListener(new KeyInput());
        this.addMouseListener(new MouseInput());

        tiless = new Tiles[2];

        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(new Color(0, 0, 0));

        thisPlayer = new MainPlayer(WIDTH - 50, HEIGHT - 50, 0, Color.BLUE);
        otherPlayer = new AIPlayer(50, 50, 0, Color.RED);

        if (MainMenuTest.playerName.equals("")) {
            thisPlayer.setPlayerName("Player");
            otherPlayer.setPlayerName("Thanos");
        } else {
            thisPlayer.setPlayerName(MainMenuTest.playerName);
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

        // AI attempts to shoot if in range
        double distance = World.pythHyp(otherPlayer.x - thisPlayer.x, otherPlayer.y - thisPlayer.y);
        if (otherPlayer.getSelectedWeapon() == 0
                && Controller.otherPlayer.getWeapons().getPrimary().getCurrentDelay() == 0) {
            if ((otherPlayer.getWeapons().getPrimary().getSPEED() / 2) *
                    (otherPlayer.getWeapons().getPrimary().getSPEED() / FRICTION) > distance) {
                Controller.otherPlayer.getWeapons().getPrimary().shoot(thisPlayer.x, thisPlayer.y);

                Controller.otherPlayer.getWeapons().getPrimary().setCurrentDelay(
                        Controller.otherPlayer.getWeapons().getPrimary().getMAX_DELAY());
            }
        } else if (otherPlayer.getSelectedWeapon() == 1
                && Controller.otherPlayer.getWeapons().getSecondary().getCurrentDelay() == 0) {
            if ((otherPlayer.getWeapons().getSecondary().getSPEED() / 2) *
                    (otherPlayer.getWeapons().getSecondary().getSPEED() / FRICTION) > distance) {
                Controller.otherPlayer.getWeapons().getSecondary().shoot(thisPlayer.x, thisPlayer.y);

                Controller.otherPlayer.getWeapons().getSecondary().setCurrentDelay(
                        Controller.otherPlayer.getWeapons().getSecondary().getMAX_DELAY());
            }
        }

        // Check bullets to see if a kill occurs
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


        double[][] playerInfo = new double[2][6];

        System.out.println("The winner is " + winner.getPlayerName());
        System.out.println("Scores: ");
        String format = " %10d  %10d  %10f  %10d  %10d  %10d  %10s %n";
        System.out.format("      Kills      Deaths         K/D     Bullets     Bullets     Walking    Number of%n");
        System.out.format("                                           Shot         Hit    Distance    Power-ups%n");
        System.out.format("------------------------------------------------------------------------------------%n");
        for (int i = 0; i < players.size(); i++) {
            //Save data to send to client
            Player player = players.get(i);
            playerInfo[i][0] = player.getKillCount();
            playerInfo[i][1] = player.getDeathCount();
            playerInfo[i][2] = player.getKdr();
            playerInfo[i][3] = player.getBulletCount();
            playerInfo[i][4] = player.getBulletHitCount();
            playerInfo[i][5] = player.getWalkingDistance();

            //Print
            System.out.format(format,
                    player.getKillCount(),
                    player.getDeathCount(),
                    player.getKdr(),
                    player.getBulletCount(),
                    player.getBulletHitCount(),
                    player.getWalkingDistance(),
                    "???");
        }

        stop();
    }
}