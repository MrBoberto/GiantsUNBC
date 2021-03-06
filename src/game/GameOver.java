package game;

/**
 * This file is part of a solution to
 * CPSC300 Term Project Fall 2021
 * <p>
 * The window that displays the scoreboard for both players at the end of a match
 *
 * @author The Boyz
 * @version 1
 */

import audio.SFXPlayer;
import player.Player;
import utilities.BufferedImageLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

public class GameOver {
    final JFrame gameOver;

    public GameOver(Player loser, Player winner, List<Player> players) {
        this.gameOver = new JFrame("Game over Window");

        Image img;
        try {
            img = BufferedImageLoader.loadImage("/resources/GUI/character_closeups/character_closeup_blue.png");
            gameOver.setIconImage(img);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Dimension size = new Dimension(Controller.WIDTH, Controller.HEIGHT);
        gameOver.setSize(size);
        gameOver.setPreferredSize(size);
        gameOver.setMaximumSize(size);
        gameOver.setMinimumSize(size);
        gameOver.setResizable(false);
        gameOver.setLocationRelativeTo(null);
        gameOver.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        gameOver.getContentPane().setBackground(Color.BLACK);
        GridBagConstraints c = new GridBagConstraints();

        SFXPlayer sfxPlayer = new SFXPlayer();
        sfxPlayer.setFile(-2);

        JPanel mainMenuPanel = new JPanel(new GridBagLayout());

        mainMenuPanel.setOpaque(false);
        gameOver.add(mainMenuPanel);

        JButton screen = createScreenButton(loser, winner, players, mainMenuPanel);

        c.anchor = GridBagConstraints.CENTER;
        c.fill = GridBagConstraints.BOTH;
        c.gridy = 0;
        c.gridx = 0;
        c.weighty = 1.0;
        c.weightx = 1.0;
        mainMenuPanel.add(screen, c);

        gameOver.setVisible(true);
    }

    private JButton createScreenButton(Player loser, Player winner, List<Player> players, JPanel mainMenuPanel) {
        JButton button = new JButton() {
            @Override
            public void paintComponent(Graphics g) {
                Image image = null;
                try {
                    image = BufferedImageLoader.loadImage("/resources/Textures/BG/stone_background.png");

                } catch (Exception e) {
                    e.printStackTrace();
                }

                Graphics2D g2 = (Graphics2D) g;
                g2.drawImage(image, 0, 0, gameOver.getWidth(), gameOver.getHeight(), null);
                Font font = new Font("Bauhaus 93", Font.PLAIN, 30);
                Font MainFont = new Font("Bauhaus 93", Font.PLAIN, 50);

                Font fontResult = new Font("Apple Casual", Font.PLAIN, 30);
                FontMetrics fontMetrics = g2.getFontMetrics(font);
                g2.setColor(Color.WHITE);

                g2.setFont(MainFont);
                String text = "         Game Ends  " +
                        "Hit enter to return to the main screen";
                g2.drawString("                   Score Board:", Controller.WIDTH / 2 - fontMetrics.stringWidth(text) / 2, 70);


                g2.setFont(fontResult);
                g2.drawString("------------------------------------------------------------------------------------------------------------------------------------------------------------------------", Controller.WIDTH / 4 - 600, 30);
                g2.drawString("                           The winner is  " + winner.getPlayerName(),
                        Controller.WIDTH / 2 - fontMetrics.stringWidth(text) / 2, 130);
                g2.drawString("                                 Scores:" + winner.getPlayerName(), Controller.WIDTH / 2 - fontMetrics.stringWidth(text) / 2, 170);

                g2.drawString(
                        " Kills      Deaths         K/D     Bullets     Bullets     Walking    Number of",
                        Controller.WIDTH / 2 - fontMetrics.stringWidth(text) / 2 - 50, 250);
                g2.drawString(
                        "                                             Shot         Hit     Distance     Power-ups",
                        Controller.WIDTH / 2 - fontMetrics.stringWidth(text) / 2 - 50, 300);

                for (int i = 0; i < players.size(); i++) {
                    //Save data to send to client


                    //Determine format
                    String format = String.format("%10d  %10d  %10f  %10d  %10d  %10d  %10s %n",
                            winner.getKillCount(),
                            winner.getDeathCount(),
                            winner.getKdr(),
                            winner.getBulletCount(),
                            winner.getBulletHitCount(),
                            winner.getWalkingDistance(),
                            winner.getPickedUpPowerUps());

                    if (i != 0) {
                        g2.drawString(format,
                                Controller.WIDTH / 2 - fontMetrics.stringWidth(text) / 2 - 60, 330);
                    }
                }
                //g2.drawString(" -------------------------------------------------------------------------------------------------------------------------------------------", Controller.WIDTH / 4 - 200, 360);
                g2.drawString("------------------------------------------------------------------------------------------------------------------------------------------------------------------------", Controller.WIDTH / 4 - 600, 360);

                g2.drawString("                                Scores:" + loser.getPlayerName(), Controller.WIDTH / 2 - fontMetrics.stringWidth(text) / 2 - 50, 400);

                g2.drawString(
                        " Kills      Deaths         K/D     Bullets     Bullets     Walking    Number of",
                        Controller.WIDTH / 2 - fontMetrics.stringWidth(text) / 2 - 50, 440);
                g2.drawString(
                        "                                             Shot         Hit     Distance     Power-ups",
                        Controller.WIDTH / 2 - fontMetrics.stringWidth(text) / 2 - 50, 480);

                for (int i = 0; i < players.size(); i++) {
                    //Save data to send to client
//

                    //Determine format
                    String format = String.format("%10d  %10d  %10f  %10d  %10d  %10d  %10s %n",
                            loser.getKillCount(),
                            loser.getDeathCount(),
                            loser.getKdr(),
                            loser.getBulletCount(),
                            loser.getBulletHitCount(),
                            loser.getWalkingDistance(),
                            loser.getPickedUpPowerUps());

                    if (i != 0) {
                        g2.drawString(format,
                                Controller.WIDTH / 2 - fontMetrics.stringWidth(text) / 2 - 60, 520);
                    }
                }
                //g2.drawString(" -----------------------------------------------------------------------------------------------------------------------------------------", Controller.WIDTH / 4 - 200, 560);
                g2.drawString("------------------------------------------------------------------------------------------------------------------------------------------------------------------------", Controller.WIDTH / 4 - 600, 560);


                g2.setFont(font);

                g2.drawString(text, Controller.WIDTH / 2 - fontMetrics.stringWidth(text) / 2, 640);

            }


        };
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);


        button.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    mainMenuPanel.remove(button);
                    gameOver.dispose();
                    new MainMenu();
                }
            }
        });
        return button;
    }


}
