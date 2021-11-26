package game;



import javax.swing.*;
import java.net.InetAddress;
import java.net.UnknownHostException;

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
        } catch (UnknownHostException e) {
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
        return (int) volumeMusic * volumeMaster / 100;
    }

    public static void setVolumeMusic(int volumeMusic) {
        Main.volumeMusic = volumeMusic;
    }

    public static int getVolumeSFX() {
        return (int) volumeSFX * volumeMaster / 100;
    }

    public static void setVolumeSFX(int volumeSFX) {
        Main.volumeSFX = volumeSFX;
    }
}
