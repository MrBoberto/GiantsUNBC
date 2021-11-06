package StartMenu;

import javax.naming.Name;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class MainMenuTest {

    public static JFrame window;
    Container con;
    JPanel titleNamePanel;
    JLabel titleNameLabel;
    JPanel startButtonPanel;

    ButtonListener buttonListener = new ButtonListener();

    Font titleFont = new Font("Times New Roman",Font.PLAIN,90);
    Font button = new Font("Times New Roman",Font.PLAIN,30);
    JButton startButton, quit, name;
    public static void main(String[] args) {
        new MainMenuTest();

    }

    public MainMenuTest(){
        window = new JFrame("Your Mom");
        window.setSize(800,600);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.getContentPane().setBackground(Color.BLACK);
        window.setLayout(null);
        window.setVisible(true);// to make window appear on the screen
        con  = window.getContentPane();

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
        //startButton.add(ActionListener(buttonListener));



        quit = new JButton("quit");
        quit.setBackground(Color.black);
        quit.setForeground(Color.RED);
        quit.setFont(button);
        quit.setBounds(300,400,100,400);

        name = new JButton("Name");
        name.setBackground(Color.black);
        name.setForeground(Color.RED);
        name.setFont(button);
        name.setBounds(300,400,100,300);


        titleNamePanel.add(titleNameLabel);
        startButtonPanel.add(startButton);
        startButtonPanel.add(name);
        startButtonPanel.add(quit);

        con.add(startButtonPanel);
        con.add(titleNamePanel);

    }

}
