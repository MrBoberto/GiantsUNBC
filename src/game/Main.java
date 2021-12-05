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
    private static InetAddress correctAddress;//to make java happy, should not need to be initialized

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

    public static InetAddress getAddress() {
        return correctAddress;
    }

    public static void main(String[] args) {
        setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        new MainMenu();

        handleIPCorrectness();
    }

    private static void handleIPCorrectness() {
        try {
            Enumeration<NetworkInterface> Interfaces = NetworkInterface.getNetworkInterfaces();
            boolean firstAddress = false;
            while (Interfaces.hasMoreElements()) {
                NetworkInterface Interface = Interfaces.nextElement();
                Enumeration<InetAddress> Addresses = Interface.getInetAddresses();
                while (Addresses.hasMoreElements()) {
                    InetAddress Address = Addresses.nextElement();
                    if (!Address.getHostAddress().contains("f") && !Address.getHostAddress().contains(":") && !Address.getHostAddress().contains("127.0.0.1") && !firstAddress && !Address.getHostAddress().contains("192.168.56.1")) {
                        firstAddress = true;
                        correctAddress = Address;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void setLookAndFeel(String lookAndFeel) {
        try {
            UIManager.setLookAndFeel(lookAndFeel);
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
    }
}