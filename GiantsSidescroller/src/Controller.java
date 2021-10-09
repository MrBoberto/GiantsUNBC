package GiantsSidescroller.src;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Controller extends JPanel implements ActionListener, KeyListener, MouseListener {
    // delay between each frame
    public final int FRAMEDELAY = 15;
    public final int WIDTH = 1280;
    public final int HEIGHT = WIDTH / 16 * 9;
    public final double GRAVITY = 0.6;
    public final double FRICTION = 1.1;
    public ArrayList<Creature> livingPlayers = new ArrayList<Creature>();
    public ArrayList<Projectile> movingAmmo = new ArrayList<Projectile>();
    private ImageStrip arsenalSlots;
    //private static JButton primaryButton;
    //private static JButton secondaryButton;

    private BufferedImage background;
    private Timer timer;
    private Player player;

    public Controller() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(new Color(0, 0, 0));
        loadImageStrips();

        //primaryButton = new JButton("(Primary)");
        //secondaryButton = new JButton("(Secondary)");
        //primaryButton.addActionListener(World.getWorld().getController());
        //secondaryButton.addActionListener(World.getWorld().getController());
        //add(primaryButton);
        //add(secondaryButton);

        timer = new Timer(FRAMEDELAY, this);
        timer.start();

        player = new Player(WIDTH / 2, HEIGHT / 2, 0);
        livingPlayers.add(player);

        loadBackground();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Point mouseLoc = MouseInfo.getPointerInfo().getLocation();
        for (int i = 0; i < livingPlayers.size(); i++) {
            livingPlayers.get(i).tick(mouseLoc);
        }
        for (int j = 0; j < movingAmmo.size(); j++) {
            movingAmmo.get(j).tick();
        }
        repaint();
    }

    private void loadImage() {

    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        drawBackground(g);

        player.draw(g, this);
        for (int i = 0; i < livingPlayers.size(); i++) {
            livingPlayers.get(i).draw(g, this);
        }
        for (int j = 0; j < movingAmmo.size(); j++) {
            movingAmmo.get(j).draw(g, this);
        }

        Toolkit.getDefaultToolkit().sync();
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        player.keyPressed(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        player.keyReleased(e);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        player.mouseClicked(e);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        player.mousePressed(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        player.mouseReleased(e);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        player.mouseEntered(e);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        player.mouseExited(e);
    }

    private void loadBackground() {
        try {
            background = ImageIO.read(new File("GiantsSidescroller/src/images/background/background1.png"));
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
        return player;
    }

    /**
     * Loads the image files into the image strips based upon their names
     */
    public void loadImageStrips() {
        ArrayList<String> imgLocStr = new ArrayList<String>();

        // Saves amount of text to be used
        String defLocStr = "GiantsSidescroller/src/images/arsenal_slot/";

        // Builds image strip for standing facing right
        for (int i = -1; i <= -1; i++) {
            imgLocStr.add("weapon (" + i + ").png");
        }
        arsenalSlots = buildImageStrip(imgLocStr, defLocStr);
        System.out.println(arsenalSlots.toString());
        imgLocStr.clear();
    }

    /**
     * Builds the ImageStrip for a specific animation
     * @param imgLocStr All file names to be loaded into the ImageStrip for animation
     * @param defaultFileLocation The file path of the images
     * @return An ImageStrip for animation
     */
    public ImageStrip buildImageStrip(ArrayList<String> imgLocStr, String defaultFileLocation) {
        // The ArrayList of image files to be put into the ImageStrip
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
        // Used for the toString() method of this ImageStrip
        for (int i = 0; i < imageFileNames.length() - 2; i++) {
            imageFileSubstring += imageFileNames.charAt(i);
        }
        return new ImageStrip(images, imageFileSubstring);
    }
}