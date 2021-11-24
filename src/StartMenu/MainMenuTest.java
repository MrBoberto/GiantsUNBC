package StartMenu;

import tile.TileManager;
import tile.Tiles;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class MainMenuTest {

    public static JFrame wow;
    Container con;
    JPanel titleNamePanel;
    JLabel titleNameLabel;
    JPanel startButtonPanel;
    JPanel backGround ;
    public static String playerName = "";
    BufferedImage backGroundMain ;

    ButtonListener buttonListener = new ButtonListener();
    TileManager tileManager = new TileManager(this);


    Font titleFont = new Font("Times New Roman",Font.PLAIN,90);
    Font button = new Font("Times New Roman",Font.PLAIN,30);
    JButton startButton, quit, name;
    public static void main(String[] args) {
        new MainMenuTest();//comments

    }

    public ButtonListener getButtonListener() {
        return buttonListener;
    }

    public MainMenuTest(){
        wow = new JFrame("Your Mom");
        //wow.setSize(800,600);
        int width = wow.getWidth();
        int length = wow.getHeight();
        wow.setExtendedState(JFrame.MAXIMIZED_BOTH);

        wow.setMinimumSize(new Dimension(800, 800));

        wow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        wow.getContentPane().setBackground(Color.BLUE);
        wow.setLayout(null);
        // to make window appear on the screen
        con  = wow.getContentPane();
        Dimension d= wow.getMaximumSize();
        wow.setSize(d.width, d.height);
        System.out.println("Size" +wow.getWidth()+"width"+ d.height);
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


        titleNamePanel = new JPanel(){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);


                    Graphics2D g2 = (Graphics2D)g;
                    tileManager.draw(g2);
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

        //System.out.println(buttonListener.getNumber());
        startButton.addActionListener(buttonListener);


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

        name.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean isValidPlayerName = false;

                // While there is no valid input and user has not pressed cancel
                while (!isValidPlayerName && playerName != null) {
                    playerName = (JOptionPane.showInputDialog("Name for server 1 | Name for server 2"));
                    // If user did not press cancel, and they typed something before pressing "Ok"
                    if (playerName != null && !playerName.equals("")) {
                        isValidPlayerName = true;
                        System.out.println("playerName " +playerName);
                    }
                }
                // If user pressed "Ok" without typing anything
                if (playerName == null) {
                    playerName = "";
                }
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
