package game;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class MainMenu {

    public static JFrame wow;
    Container con;
    JPanel titleNamePanel;
    JLabel titleNameLabel;
    JPanel startButtonPanel;
    JPanel backGround ;
    public static String playerName = "";
    BufferedImage backGroundMain ;



    Font titleFont = new Font("Times New Roman",Font.PLAIN,90);
    Font button = new Font("Times New Roman",Font.PLAIN,30);
    JButton startButton, quit, name;



    public MainMenu() {
        wow = new JFrame("Your Mom");
        //wow.setSize(800,600);
        int width = wow.getWidth();
        int length = wow.getHeight();
        Dimension size = Toolkit. getDefaultToolkit(). getScreenSize();
        wow.setSize(size);

        wow.setMinimumSize(new Dimension(800, 800));

        wow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        wow.getContentPane().setBackground(Color.BLUE);
        wow.setLayout(null);
        // to make window appear on the screen
        // max size was incorrect on my multi-display monitor so I changed it - Noah
        con  = wow.getContentPane();
        System.out.println("Size" +wow.getWidth()+"width"+ wow.getHeight());
        try{

            backGroundMain = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/imageRes/textBack.png")));
        }catch (IOException e){
            e.printStackTrace();
        }

        backGround = new JPanel(){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                Graphics2D g2 = (Graphics2D)g;
                draw(g2);

            }
        };
        backGround.setBounds(0,0,wow.getWidth(),wow.getHeight());
       backGround.setBackground(Color.BLACK);

        System.out.println("Size of the frame is " +size);


        titleNamePanel = new JPanel(){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

            }
        };
        titleNamePanel.setBounds(480,100,600,150);
        titleNamePanel.setBackground(Color.black);

        titleNameLabel = new JLabel("THE BOYZ");
        titleNameLabel.setForeground(Color.RED);
        titleNameLabel.setFont(titleFont);



        startButtonPanel = new JPanel();
        startButtonPanel.setBackground(Color.BLACK);
        startButtonPanel.setBounds(700,400,100,200);

        startButton = new JButton("Start");
        startButton.setBackground(Color.black);
        startButton.setForeground(Color.RED);
        startButton.setFont(button);
        startButton.setBounds(300,400,100,400);

        startButton.addActionListener(e -> {

            System.out.println("The game has begun");
            wow.dispose();

            new World();
        });


        quit = new JButton("quit");
        quit.setBackground(Color.black);
        quit.setForeground(Color.RED);
        quit.setFont(button);
        quit.setBounds(300,400,100,400);

        quit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("The game has ended");
                JPanel quitPanel = new JPanel();
                quitPanel.setBounds(300,300,1000,1000);
                //titleNamePanel.setBounds(100,100,600,150);
                quitPanel.setBackground(Color.black);
                JLabel quitLabel = new JLabel("Thanks For Playing" );
                quitLabel.setFont(titleFont);
                quitLabel.setForeground(Color.RED);
                quitPanel.add(quitLabel);
                con.add(quitPanel);
                wow.add(titleNamePanel);
                SwingUtilities.updateComponentTreeUI(wow);
            }
        });

        name = new JButton("Name");
        name.setBackground(Color.black);
        name.setForeground(Color.RED);
        name.setFont(button);
        name.setBounds(300,400,100,300);

        name.addActionListener(e -> {
            System.out.println(" Please enter your name");
            boolean isValidPlayerName = false;
            while (!isValidPlayerName && playerName != null) {
                playerName = (JOptionPane.showInputDialog("Name for server 1 | Name for server 2"));
                if (!playerName.equals("")) {
                    isValidPlayerName = true;
                    System.out.println("playerName " +playerName);
                }
            }
            if (playerName == null) {
                playerName = "";
            }
        });


        backGround.add(titleNamePanel);
        titleNamePanel.add(titleNameLabel);
        startButtonPanel.add(startButton);
        startButtonPanel.add(name);
        startButtonPanel.add(quit);
        con.add(startButtonPanel);
        con.add(titleNamePanel);
        con.add(backGround);

        wow.setResizable(false);
        wow.setVisible(true);

    }
    public void draw(Graphics2D g2){
        g2.drawImage(backGroundMain,0,0,wow.getWidth(),wow.getHeight(),null);
    }

    public  String getPlayerName() {
        return playerName;
    }
}
