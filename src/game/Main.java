package game;

import javax.swing.*;

public class Main {

    public static void startupGui() {
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
        SwingUtilities.invokeLater(Main::startupGui);
    }
}