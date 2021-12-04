package game;


import audio.SFXPlayer;
import player.Player;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Objects;

public class GameOver {
    //public static MainMenu gameOverMenu;
    final JFrame gameOver;


    public GameOver(Player loser, Player winner, int HEIGHT, List<Player> players, int WIDTH){
        this.gameOver = new JFrame("Game over Window");
        System.out.println("This is the height is "+ HEIGHT +" and the width is "+WIDTH );


        Image img;
        try {
            img = ImageIO.read(Objects.requireNonNull(getClass().getResource("/resources/GUI/character_closeups/character_closeup_blue.png")));
            gameOver.setIconImage(img);
        } catch(Exception e) {
            System.out.println(e.getMessage());
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

        // to make window appear on the screen
        // max size was incorrect on my multi-display monitor, so I changed it - Noah
        System.out.println("Size" + gameOver.getWidth()+"width"+ gameOver.getHeight());

        JPanel mainMenuPanel = new JPanel(new GridBagLayout());

        mainMenuPanel.setOpaque(false);
        gameOver.add(mainMenuPanel);

        JButton startButton = new JButton(){
            @Override
            public void paintComponent(Graphics g){
                Graphics2D g2 = (Graphics2D) g;
                Font font = new Font("Bauhaus 93", Font.PLAIN, 30);

                Font fontResult = new Font("Apple Casual",Font.PLAIN,30);
                FontMetrics fontMetrics = g2.getFontMetrics(font);
                g2.setColor(Color.WHITE);

                g2.setFont(fontResult);
                String text =        "           Game Ends  " +
                        "Click anywhere to return to the main screen";
                g2.drawString("---------------------------------------------------------------------------------------------------",Controller.WIDTH/4 -200,30);
                g2.drawString("                            The winner is  " + winner.getPlayerName(),
                        Controller.WIDTH/2 - fontMetrics.stringWidth(text)/2,90);
                g2.drawString("                                  Scores:" + winner.getPlayerName(), Controller.WIDTH/2 - fontMetrics.stringWidth(text)/2,150);

                g2.drawString(
                        "Kills      Deaths         K/D     Bullets     Bullets     Walking    Number of",
                        Controller.WIDTH/2 - fontMetrics.stringWidth(text)/2,250);
                g2.drawString(
                        "                                               Shot         Hit     Distance     Power-ups",
                        Controller.WIDTH/2 - fontMetrics.stringWidth(text)/2,300);

                for (int i = 0; i < players.size(); i++) {
                    //Save data to send to client
                    Player player = players.get(i);

                    //Determine format
                    String format = String.format(" %10d  %10d  %10f  %10d  %10d  %10d  %10s %n",
                            player.getKillCount(),
                            player.getDeathCount(),
                            player.getKdr(),
                            player.getBulletCount(),
                            player.getBulletHitCount(),
                            player.getWalkingDistance(),
                            player.getPickedUpPowerUps());

                    if (i != 0) {
                        g2.drawString(format,
                                Controller.WIDTH/2 - fontMetrics.stringWidth(text)/2,330);
                    }
                }
                g2.drawString("---------------------------------------------------------------------------------------------------",Controller.WIDTH/4 -200,360);

                g2.drawString("                                  Scores:" + loser.getPlayerName(), Controller.WIDTH/2 - fontMetrics.stringWidth(text)/2,400);

                g2.drawString(
                        "Kills      Deaths         K/D     Bullets     Bullets     Walking    Number of",
                        Controller.WIDTH/2 - fontMetrics.stringWidth(text)/2,440);
                g2.drawString(
                        "                                               Shot         Hit     Distance     Power-ups",
                        Controller.WIDTH/2 - fontMetrics.stringWidth(text)/2,480);

                for (int i = 0; i < players.size(); i++) {
                    //Save data to send to client
                    Player player = players.get(i);

                    //Determine format
                    String format = String.format(" %10d  %10d  %10f  %10d  %10d  %10d  %10s %n",
                            player.getKillCount(),
                            player.getDeathCount(),
                            player.getKdr(),
                            player.getBulletCount(),
                            player.getBulletHitCount(),
                            player.getWalkingDistance(),
                            player.getPickedUpPowerUps());

                    if (i != 0) {
                        g2.drawString(format,
                                Controller.WIDTH/2 - fontMetrics.stringWidth(text)/2,520);
                    }
                }
                g2.drawString("---------------------------------------------------------------------------------------------------",Controller.WIDTH/4 -200,560);



                g2.setFont(font);

                g2.drawString(text, Controller.WIDTH/2 - fontMetrics.stringWidth(text)/2,640);

            }



        };

        startButton.setOpaque(false);
        startButton.setContentAreaFilled(false);
        startButton.setBorderPainted(false);
        c.anchor = GridBagConstraints.CENTER;
        c.fill = GridBagConstraints.BOTH;
        c.gridy = 0;
        c.gridx = 0;
        c.weighty = 1.0;
        c.weightx = 1.0;
        mainMenuPanel.add(startButton, c);

        startButton.addActionListener(f -> {
            mainMenuPanel.remove(startButton);
            gameOver.dispose();
            new MainMenu();


        });

        gameOver.setVisible(true);

        // Things get wild


        System.out.println("I am in the game over Window");

        // gameOverMenu = new MainMenu();
    }


}
