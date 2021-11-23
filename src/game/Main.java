package game;



import javax.swing.*;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class Main {
    public static void startGame()  {


    }

    public static void main(String[] args) {
        new MainMenu();


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
