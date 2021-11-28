package game;



import javax.swing.*;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.Enumeration;

public class Main {

    private static int volumeMaster = 100;
    private static int volumeMusic = 100;
    private static int volumeSFX = 100;

    public static void startGame()  {


    }

    public Main() throws UnknownHostException {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        new MainMenu();

        InetAddress correctAddress =InetAddress.getLocalHost(); //to make java happy, should not need to be initailzed
        try {
            String ipAddress = String.valueOf(InetAddress.getLocalHost());
            String[] ipAddressClean = ipAddress.split("/", 2);
            System.out.println("The ip address: "+ipAddress);
            Enumeration<NetworkInterface> Interfaces = NetworkInterface.getNetworkInterfaces();
            boolean firstAddress = false;
            while(Interfaces.hasMoreElements())
            {
                NetworkInterface Interface = Interfaces.nextElement();
                Enumeration<InetAddress> Addresses = Interface.getInetAddresses();
                while(Addresses.hasMoreElements())
                {
                    InetAddress Address = Addresses.nextElement();
                    if (!Address.getHostAddress().contains("f")&&!Address.getHostAddress().contains(":")&&!Address.getHostAddress().contains("127.0.0.1")&&!firstAddress)
                    {
                            System.out.println(Address.getHostAddress() + " is on the network");
                            firstAddress = true;
                            correctAddress =Address;
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

    public static void main(String[] args) throws UnknownHostException {
        Main main = new Main();
    }
}


