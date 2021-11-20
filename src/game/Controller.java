package game;

import StartMenu.MainMenuTest;
import animation.ImageStrip;
import player.MainPlayer;
import player.Player;
import tile.TileManager;
import tile.Tiles;
import weapons.Projectile;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Objects;

public abstract class Controller extends JPanel implements ActionListener, KeyListener, MouseListener {
    public static final int PORT = 55555;

    public final int FRAMEDELAY = 15;
    public final int WIDTH = 1280;
    public final int HEIGHT = WIDTH / 16 * 9;
    public final double GRAVITY = 0.6;
    public final double FRICTION = 1.1; // Friction acting on objects
    public static ArrayList<Player> livingPlayers = new ArrayList<Player>();
    public static ArrayList<Projectile> movingAmmo = new ArrayList<Projectile>();
    public MainMenuTest mainMenuTest;

    protected BufferedImage background;
    protected Timer timer;
    protected MainPlayer player;
    protected TileManager tileManager;
    protected BufferedImage tileBackGround;
    public Tiles[] tiless;
    int[][] mapTileReader ;


    int checkHeight = 0;
    int checkWidth = 0;


    public Controller(){
        tiless = new Tiles[2];

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
    Graphics2D g2 = (Graphics2D)g;
        drawBackground(g2);

        if(player != null){
            player.draw(g, this);
            for (int i = 0; i < livingPlayers.size(); i++) {
                livingPlayers.get(i).draw(g, this);
            }
            for (int j = 0; j < movingAmmo.size(); j++) {
                movingAmmo.get(j).draw(g, this);
            }
        }


        Toolkit.getDefaultToolkit().sync();
    }


    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        if(player!=null)
        player.keyPressed(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if(player!=null)
        player.keyReleased(e);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(player!=null)
        player.mouseClicked(e);

        System.out.println(this.getClass().toString());
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if(player!=null)
        player.mousePressed(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(player!=null)
        player.mouseReleased(e);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        if(player!=null)
        player.mouseEntered(e);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        if(player!=null)
        player.mouseExited(e);
    }

    private void loadBackground() {
        try {
            background = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/imageRes/backGroundMap.png")));


        } catch (IOException exc) {
            System.out.println("Could not find image file: " + exc.getMessage());
        }

        try {
            tiless[0] = new Tiles();
            tiless[0].image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/resTiles/yellowTile.png")));



        } catch (IOException exc) {
            System.out.println("Could not find image file: " + exc.getMessage());
        }
        try{
            tiless[1] = new Tiles();
            tiless[1].image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/resTiles/brickwall.png")));
            tiless[1].collision =true;
        }catch (IOException exc){
            System.out.println("Could not find image file: " + exc.getMessage());
        }
    }

    public  int[][] loadMap(){


         int[][] tileMap=
                 {       {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                         {1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1},
                         {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1},
                         {1, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1},
                         {1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1},
                         {1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1},
                         {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1},
                         {1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 0, 0, 0, 1},
                         {1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1},
                         {1, 0, 0, 1, 0, 0, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1},
                         {1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1},
                         {1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 1},
                         {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1},
                         {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 1},
                         {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 1},
                         {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                         {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}

                 };
         return tileMap;
    }


    private void drawBackground(Graphics2D g) {
        g.drawImage(
                background,
                0, 0, this.getWidth(),this.getHeight(),null);
        int[][] tile =loadMap();
        drawTiles(g,tile);



    }

    private void drawTiles(Graphics2D g, int[][] tiles){



        int col = 0;
        int row = 0;


       for(int roww = 0; roww<15  ;roww++){

           for(int coln  =0; coln<26;coln++){
             // System.out.print(tiles[roww][coln]+",");
              //System.out.println(coln);
              //int tileNum = mapTileReader[col][row];
              g.drawImage(tiless[tiles[roww][coln]].image, row, col, 50,50,null);

              if(tiles[roww][coln] == 1){
                  collisionArea(row,col,50,50);
              }
                row  = row+50;
          }
           row = 0;
           //System.out.println();
            col = col+50;

       }



    }
    public void collisionArea(int x, int y, int width, int height){

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
}
