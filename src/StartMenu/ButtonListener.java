package StartMenu;

import game.World;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static game.Main.window;

public class ButtonListener implements ActionListener {

private int number;




    @Override
    public void actionPerformed(ActionEvent e) {

        System.out.println("The game has begun");
        window.add(World.getWorld().getController());
        window.addKeyListener(World.getWorld().getController());
        window.addMouseListener(World.getWorld().getController());
        System.out.println(Math.toDegrees(World.getWorld().atan(1, -2, 0)));


    }

    public int getNumber() {
        return number;
    }
}
