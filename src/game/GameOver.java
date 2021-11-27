package game;

import audio.AudioPlayer;
import audio.SFXPlayer;
import player.Player;
import utilities.BufferedImageLoader;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Objects;

public class GameOver {
    //public static MainMenu gameOverMenu;
    JFrame gameOver;

    public GameOver(Player winner, int HEIGHT, Graphics2D g2D, List<Player> players, int WIDTH, FontMetrics stringSize){
        this.gameOver = new JFrame("Game over Window");


        Image img;
        try {
            img = ImageIO.read(Objects.requireNonNull(getClass().getResource("/resources/GUI/icon/icon.png")));
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

        gameOver.getContentPane().setBackground(Color.BLUE);
        GridBagConstraints c = new GridBagConstraints();

        SFXPlayer sfxPlayer = new SFXPlayer();
        sfxPlayer.setFile(-2);

        // to make window appear on the screen
        // max size was incorrect on my multi-display monitor so I changed it - Noah
        System.out.println("Size" + gameOver.getWidth()+"width"+ gameOver.getHeight());

        BufferedImage backgroundImage = BufferedImageLoader.loadImage("/resources/imageRes/textBack.png");

        JPanel mainMenuPanel = new JPanel(new GridBagLayout()){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                Graphics2D g2 = (Graphics2D)g;
                g2.drawImage(backgroundImage,0,0, gameOver.getWidth(), gameOver.getHeight(),null);

            }};

        mainMenuPanel.setOpaque(false);
        gameOver.add(mainMenuPanel);

        JButton startButton = new JButton(){
            @Override
            public void paintComponent(Graphics g){
                Graphics2D g2 = (Graphics2D) g;
                Font font = new Font("Bauhaus 93", Font.PLAIN, 30);
                FontMetrics fontMetrics = g2.getFontMetrics(font);
                g2.setColor(Color.WHITE);
                g2.setFont(font);
                String text = "Game Ends click here to Play Again";
                g2.drawString(text, Controller.WIDTH/2 - fontMetrics.stringWidth(text)/2,Controller.HEIGHT * 3/4);
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

           // JPanel bottomPanel = buttonsPanel(mainMenuPanel);

            c.fill = GridBagConstraints.CENTER;
            c.weighty = 0.2;
            c.weightx = 1.0;
            c.insets = new Insets(5,5,10,10);
            //mainMenuPanel.add(bottomPanel, c);
            mainMenuPanel.validate();
            mainMenuPanel.repaint();
        });

        gameOver.setVisible(true);

        try
        {
            AudioPlayer soundtrack = new AudioPlayer("/resources/Music/The_Number_J.wav");
            soundtrack.play();
        }
        catch (Exception ex)
        {
            System.out.println("Error with playing sound.");
            ex.printStackTrace();

        }



        System.out.println("I am in the gameover Window");

        // gameOverMenu = new MainMenu();
    }




    public void printGame(Player winner, int HEIGHT, Graphics2D g2D, List<Player> players, int WIDTH, FontMetrics stringSize) {
        g2D.drawString("The winner is " + winner.getPlayerName(),
                WIDTH / 2 - (stringSize.stringWidth("The winner is ")), HEIGHT / 10);
        g2D.drawString("Scores:" + winner.getPlayerName(), WIDTH / 2 - (stringSize.stringWidth("Scores:")),
                HEIGHT / 5);
        g2D.drawString("The winner is " + winner.getPlayerName(),
                WIDTH / 2 - (stringSize.stringWidth("The winner is ")), 3 * HEIGHT / 10);
        g2D.drawString(
                "      Kills      Deaths         K/D     Bullets     Bullets     Walking    Number of",
                WIDTH / 2 - (stringSize.stringWidth(
                        "      Kills      Deaths         K/D     Bullets     Bullets     Walking    Number of")),
                2 * HEIGHT / 5);
        g2D.drawString(
                "                                           Shot         Hit    Distance    Power-ups",
                WIDTH / 2 - (stringSize.stringWidth(
                        "                                           Shot         Hit    Distance    Power-ups")), HEIGHT / 2);

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
                    "???");

            if (i == 0) {
                g2D.drawString(format,
                        WIDTH / 2 - (stringSize.stringWidth(format)), 3 * HEIGHT / 5);
            } else {
                g2D.drawString(format,
                        WIDTH / 2 - (stringSize.stringWidth(format)), 7 * HEIGHT / 10);
            }
        }
    }

}
