package GiantsSidescroller.src;

import javax.swing.*;
import java.util.Scanner;

public class Main {
    public static void startupGui() {
        JFrame window = new JFrame("Giants");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        window.add(World.getWorld().getMap());
        window.addKeyListener(World.getWorld().getMap());

        window.setResizable(false);
        window.pack();

        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                startupGui();
            }
        });
    }
}
