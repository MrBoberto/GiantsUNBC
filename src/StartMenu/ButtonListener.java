package StartMenu;

import game.World;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static StartMenu.MainMenuTest.wow;
import static game.Main.window;

public class ButtonListener implements ActionListener {

private int number;




    @Override
    public void actionPerformed(ActionEvent e) {

        System.out.println("The game has begun");
        wow.dispose();
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

    public int getNumber() {
        return number;
    }
}
