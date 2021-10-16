import javax.swing.*;
import java.awt.*;

public class Window extends JFrame {

    private ServerGame game;
    public Expo expo;
    public Window(ServerGame game, int width, int height){
        this.game = game;

        setTitle("Your Mom");// title of the main window
        setResizable(false);
        getContentPane().setPreferredSize(new Dimension(width, height));// set the size of the window
        pack();


        setLocationRelativeTo(null);// puts the window in the middle of the screen
        setVisible(true);// to make our window visible

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// close the program when the window is close
    }
}
