package game;

import javax.swing.*;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class Main {

    public static void startupGui()  {
        JFrame window = new JFrame("The Boyz");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);



        window.add(World.getWorld().getController());
        window.addKeyListener(World.getWorld().getController());
        window.addMouseListener(World.getWorld().getController());

        window.setResizable(false);
        window.pack();

        ImageIcon icon = new ImageIcon("resources/GUI/icon/icon.png");
        window.setIconImage(icon.getImage());

        window.setLocationRelativeTo(null);
        window.setVisible(true);

        System.out.println(Math.toDegrees(World.getWorld().atan(1, -2, 0)));
    }

    public static void main(String[] args) {
        try {
            System.out.println("The ip address: "+InetAddress.getLocalHost());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(Main::startupGui);
    }
}
