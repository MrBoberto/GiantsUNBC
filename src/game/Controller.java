package game;

import animation.ImageStrip;
import player.MainPlayer;
import player.OtherPlayer;
import player.Player;
import weapons.ammo.Bullet;
import weapons.ammo.Projectile;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public abstract class Controller extends JPanel implements ActionListener, KeyListener, MouseListener {
    public static final int PORT = 55555;

    public static final int FRAMEDELAY = 15;
    public static final int WIDTH = 1280;
    public static final int HEIGHT = WIDTH / 16 * 9;
    public static final double GRAVITY = 0.6;
    public static final double FRICTION = 1.1; // Friction acting on objects
    public static ArrayList<Bullet> movingAmmo = new ArrayList<>();

    protected BufferedImage background;
    protected Timer timer;
    public static MainPlayer thisPlayer = new MainPlayer(WIDTH, HEIGHT, 0);
    public static OtherPlayer otherPlayer = new OtherPlayer(50,50,0);

    //Multiplayer
    protected InputConnection inputConnection;
    protected OutputConnection outputConnection;
    public static int PLAYER_COUNT = 2;

    public enum Type{Fireball, Nato, Shot}

    protected Controller(){
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(new Color(0, 0, 0));
        loadImageStrips();

        timer = new Timer(FRAMEDELAY, this);
        timer.start();

        loadBackground();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        drawBackground(g);

        if(thisPlayer != null){
            thisPlayer.draw(g, this);
                Controller.thisPlayer.draw(g, this);
                Controller.otherPlayer.draw(g, this);
            for (Bullet bullet : movingAmmo) {
                bullet.draw(g, this);
            }
        }


        Toolkit.getDefaultToolkit().sync();
    }


    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        if(thisPlayer !=null)
        thisPlayer.keyPressed(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if(thisPlayer !=null)
        thisPlayer.keyReleased(e);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(thisPlayer !=null)
        thisPlayer.mouseClicked(e);

        System.out.println(this.getClass().toString());
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if(thisPlayer !=null)
        thisPlayer.mousePressed(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(thisPlayer !=null)
        thisPlayer.mouseReleased(e);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        if(thisPlayer !=null)
        thisPlayer.mouseEntered(e);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        if(thisPlayer !=null)
        thisPlayer.mouseExited(e);
    }

    private void loadBackground() {
        try {
            background = ImageIO.read(new File("resources/Textures/BG/background1.png"));
        } catch (IOException exc) {
            System.out.println("Could not find image file: " + exc.getMessage());
        }
    }

    private void drawBackground(Graphics g) {
        g.drawImage(
                background,
                0, 0, this
        );
    }

    public Player getPlayer() {
        return thisPlayer;
    }

    /**
     * Loads the image files into the image strips based upon their names
     */
    public void loadImageStrips() {
        ArrayList<String> imgLocStr = new ArrayList<String>();

        // Saves amount of text to be used
        String defLocStr = "resources/GUI/arsenal_slot/";

        // Builds image strip for standing facing right
        for (int i = -1; i <= -1; i++) {
            imgLocStr.add("weapon (" + i + ").png");
        }
//        arsenalSlots = buildImageStrip(imgLocStr, defLocStr);
//        System.out.println(arsenalSlots.toString());
        imgLocStr.clear();
    }

    /**
     * Builds the animation.ImageStrip for a specific animation
     * @param imgLocStr All file names to be loaded into the animation.ImageStrip for animation
     * @param defaultFileLocation The file path of the images
     * @return An animation.ImageStrip for animation
     */
    public ImageStrip buildImageStrip(ArrayList<String> imgLocStr, String defaultFileLocation) {
        // The ArrayList of image files to be put into the animation.ImageStrip
        ArrayList<BufferedImage> images = new ArrayList<>();
        // Used to track images that are loaded
        String imageFileNames = "";
        String imageFileSubstring = "";
        for (int i = 0; i < imgLocStr.size(); i++) {
            try {
                images.add(ImageIO.read(new File(defaultFileLocation + "" + imgLocStr.get(i))));
            } catch (IOException exc) {
                System.out.println("Could not find image file: " + exc.getMessage());
            }
            imageFileNames += defaultFileLocation + imgLocStr.get(i) + ", ";
        }
        // Used for the toString() method of this animation.ImageStrip
        for (int i = 0; i < imageFileNames.length() - 2; i++) {
            imageFileSubstring += imageFileNames.charAt(i);
        }
        return new ImageStrip(images, imageFileSubstring);
    }

    public abstract void packetReceived(Object object);

    public abstract void close();

    public OutputConnection getOutputConnection() {
        return outputConnection;
    }
}
