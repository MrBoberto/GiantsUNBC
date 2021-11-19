package game;

import animation.ImageStrip;
import player.MainPlayer;
import player.OtherPlayer;
import player.Player;
import weapons.ammo.Bullet;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public abstract class Controller extends Canvas implements Runnable {
    public static final int PORT = 55555;
    // public static final int FRAMEDELAY = 15;
    public static final int WIDTH = 1280;
    public static final int HEIGHT = WIDTH / 16 * 9;
    public static final double FRICTION = 0.5; // Friction acting on objects
    public static boolean mouseInside = false;
    public static boolean isMouse1Held = false;
    //public static Point mouseLoc = new Point(0, 0);


    protected BufferedImage background;
    public static MainPlayer thisPlayer;
    public static OtherPlayer otherPlayer;



    //Multiplayer
    protected InputConnection inputConnection;
    protected OutputConnection outputConnection;

    private boolean isRunning = false;
    private Thread thread;

    //All GameObjects
    public static ArrayList<Bullet> movingAmmo = new ArrayList<>();
    public static ArrayList<Player> players = new ArrayList<>();

    protected Controller() {
        new GameWindow(WIDTH,HEIGHT,"THE BOYZ", this);

        this.addKeyListener(new KeyInput());
        this.addMouseListener(new MouseInput());



    }

    public void start(){
        isRunning = true;
        loadBackground();
        thread = new Thread(this);
        thread.start();
    }

    private void stop(){
        isRunning = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void run(){
        this.requestFocus();
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        long timer = System.currentTimeMillis();
        int frames = 0;
        while(isRunning){
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            while(delta >= 1){
                tick();
                delta--;
            }
            render();
            frames++;

            if(System.currentTimeMillis() - timer > 1000){
                timer += 1000;
                frames = 0;
            }
        }
        stop();
    }

    public void tick(){
        for (int i = 0; i < players.size(); i++) {
            if(players.get(i) != null)
            players.get(i).tick();
        }

        for (int i = 0; i < movingAmmo.size(); i++) {
            if(movingAmmo.get(i) != null)
                movingAmmo.get(i).tick();
        }


        /*
        if (isMouse1Held) {
            if (Controller.thisPlayer.getSelectedWeapon() == Player.PRIMARY_WEAPON
                    && Controller.thisPlayer.getWeapons().getPrimary().getCurrentDelay() == 0)
            {
                Point mouseLocRelativeToScreen = MouseInfo.getPointerInfo().getLocation();
                if (mouseInside) {
                    double mouseX = mouseLocRelativeToScreen.getX() - this.getLocationOnScreen().getX();
                    double mouseY = mouseLocRelativeToScreen.getY() - this.getLocationOnScreen().getY();
                    mouseLoc = new Point((int) mouseX, (int) mouseY);
                }
                Controller.thisPlayer.getWeapons().getPrimary().shoot(
                        mouseLoc.x,
                        mouseLoc.y);

                Controller.thisPlayer.getWeapons().getPrimary().setCurrentDelay(
                        Controller.thisPlayer.getWeapons().getPrimary().getMAX_DELAY());

            } else if (Controller.thisPlayer.getSelectedWeapon() == Player.SECONDARY_WEAPON
                    && Controller.thisPlayer.getWeapons().getSecondary().getCurrentDelay() == 0) {

                Point mouseLocRelativeToScreen = MouseInfo.getPointerInfo().getLocation();
                if (mouseInside) {
                    double mouseX = mouseLocRelativeToScreen.getX() - this.getLocationOnScreen().getX();
                    double mouseY = mouseLocRelativeToScreen.getY() - this.getLocationOnScreen().getY();
                    mouseLoc = new Point((int) mouseX, (int) mouseY);
                }
                Controller.thisPlayer.getWeapons().getSecondary().shoot(
                        mouseLoc.x,
                        mouseLoc.y);

                Controller.thisPlayer.getWeapons().getSecondary().setCurrentDelay(
                        Controller.thisPlayer.getWeapons().getSecondary().getMAX_DELAY());

            }
        }

         */
    }

    public void render(){
        BufferStrategy bs = this.getBufferStrategy();
        if(bs == null){
            this.createBufferStrategy(3);
            return;
        }

        Graphics g = bs.getDrawGraphics();

        // Update graphics in this section:
        //////////////////////////////////////
        drawBackground(g);

        for (int i = 0; i < players.size(); i++) {
            if(players.get(i) != null)
            players.get(i).render(g);
        }

        for (int i = 0; i < movingAmmo.size(); i++) {
            if(movingAmmo.get(i) != null)
                movingAmmo.get(i).render(g);
        }

        //////////////////////////////////////
        g.dispose();
        bs.show();
    }

    private void loadBackground() {
        try {
            background = ImageIO.read(new File("resources/Textures/BG/background1.png"));
        } catch (IOException exc) {
            System.out.println("Could not find image file: " + exc.getMessage());
        }
    }

    private void drawBackground(Graphics g) {
        g.drawImage(background, 0, 0, this);
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
     *
     * @param imgLocStr           All file names to be loaded into the animation.ImageStrip for animation
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
