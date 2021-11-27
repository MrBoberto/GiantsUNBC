package game;



import javax.swing.*;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

public class Main {

    private static int volumeMaster = 100;
    private static int volumeMusic = 100;
    private static int volumeSFX = 100;

    public static void startGame()  {


    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        new MainMenu();


        try {
            String ipAddress = String.valueOf(InetAddress.getLocalHost());
            String[] ipAddressClean = ipAddress.split("/", 2);
            System.out.println("The ip address: "+ipAddress);
            Enumeration<NetworkInterface> Interfaces = NetworkInterface.getNetworkInterfaces();
            while(Interfaces.hasMoreElements())
            {
                NetworkInterface Interface = (NetworkInterface)Interfaces.nextElement();
                Enumeration<InetAddress> Addresses = Interface.getInetAddresses();
                while(Addresses.hasMoreElements())
                {
                    InetAddress Address = (InetAddress)Addresses.nextElement();
                    if (!Address.getHostAddress().contains("f")&&!Address.getHostAddress().contains(":")&&!Address.getHostAddress().contains("127.0.0.1"))
                    {
                        if (Address.isReachable(5000)) {
                            System.out.println(Address.getHostAddress() + " is on the network");
                        }
                        //System.out.println("IS THIS THE REAL IP ADDRESS:" + Address.getHostAddress());
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(Main::startGame);
    }

    public static int getVolumeMaster() {
        return volumeMaster;
    }

    public static void setVolumeMaster(int volumeMaster) {
        Main.volumeMaster = volumeMaster;
    }

    public static int getVolumeMusic() {
        return volumeMusic * volumeMaster / 100;
    }

    public static void setVolumeMusic(int volumeMusic) {
        Main.volumeMusic = volumeMusic;
    }

    public static int getVolumeSFX() {
        return volumeSFX * volumeMaster / 100;
    }

    public static void setVolumeSFX(int volumeSFX) {
        Main.volumeSFX = volumeSFX;
    }

    public static int getVolumeMusicActual() {
        return volumeMusic;
    }

    public static int getVolumeSFXActual() {
        return volumeSFX;
    }
}
