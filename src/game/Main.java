package game;

import StartMenu.MainMenuTest;

import javax.swing.*;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class Main {
    public static JFrame window = new JFrame("The Boyz");
    public static void startGame()  {


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
