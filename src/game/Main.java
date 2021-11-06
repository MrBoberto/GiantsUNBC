package game;

import StartMenu.MainMenuTest;

import javax.swing.*;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class Main {
    public static JFrame window = new JFrame("The Boyz");
    public static void startGame()  {

//        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//
//        //window.add(World.getWorld().getController());
//       // window.addKeyListener(World.getWorld().getController());
//        //window.addMouseListener(World.getWorld().getController());
//
//        window.setResizable(false);
//        window.pack();
//
//        ImageIcon icon = new ImageIcon("resources/GUI/icon/icon.png");
//        window.setIconImage(icon.getImage());
//
//        window.setLocationRelativeTo(null);
//        window.setVisible(true);

       // System.out.println(Math.toDegrees(World.getWorld().atan(1, -2, 0)));
    }

    public static void main(String[] args) {
        MainMenuTest mainMenuTest = new MainMenuTest();
         int check=   mainMenuTest.getButtonListener().getNumber();


        try {
            String ipAddress = String.valueOf(InetAddress.getLocalHost());
            String[] ipAddressClean = ipAddress.split("/", 2);
            System.out.println("The ip address: "+ipAddress);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(Main::startGame);
    }
}
