package StartMenu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenuTest {

    public static JFrame wow;
    Container con;
    JPanel titleNamePanel;
    JLabel titleNameLabel;
    JPanel startButtonPanel;
    public static String playerName = "";

    ButtonListener buttonListener = new ButtonListener();


    Font titleFont = new Font("Times New Roman",Font.PLAIN,90);
    Font button = new Font("Times New Roman",Font.PLAIN,30);
    JButton startButton, quit, name;
    public static void main(String[] args) {
        new MainMenuTest();

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
        System.out.println(JFrame.MAXIMIZED_BOTH);
        wow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        wow.getContentPane().setBackground(Color.BLACK);
        wow.setLayout(null);
        // to make window appear on the screen
        con  = wow.getContentPane();

        titleNamePanel = new JPanel();
        titleNamePanel.setBounds(100,100,600,150);
        titleNamePanel.setBackground(Color.black);

        titleNameLabel = new JLabel("THE BOYZ");
        titleNameLabel.setForeground(Color.RED);
        titleNameLabel.setFont(titleFont);



        startButtonPanel = new JPanel();
        startButtonPanel.setBackground(Color.BLACK);
        startButtonPanel.setBounds(300,400,100,200);

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
                System.out.println(" Please enter your name");
                boolean isValidPlayerName = false;
                while (!isValidPlayerName && playerName != null) {
                    playerName = (JOptionPane.showInputDialog("Name for server 1 | Name for server 2"));
                    if (!playerName.equals("")) {
                        isValidPlayerName = true;
                    }
                }
                if (playerName == null) {
                    playerName = "";
                }
            }
        });


        titleNamePanel.add(titleNameLabel);
        startButtonPanel.add(startButton);
        startButtonPanel.add(name);
        startButtonPanel.add(quit);

        con.add(startButtonPanel);
        con.add(titleNamePanel);
        wow.setVisible(true);

    }

}
