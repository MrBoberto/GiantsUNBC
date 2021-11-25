package game;

import audio.AudioPlayer;
import mapObjects.Block;
import player.MainPlayer;
import player.OtherPlayer;
import player.Player;
import utilities.BufferedImageLoader;
import weapons.ammo.Bullet;
import weapons.guns.AssaultRifle;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;

public abstract class Controller extends Canvas implements Runnable {

    //Constants
    public static final int PORT = 55555;
    public static final int WIDTH = 1280;
    public static final int HEIGHT = WIDTH / 16 * 9;
    public static final double FRICTION = 0.5; // Friction acting on objects

    //Mouse controllers
    public static boolean mouseInside = false;
    public static boolean isMouse1Held = false;

    //Multiplayer
    protected InputConnection inputConnection;
    protected OutputConnection outputConnection;

    //Game loop
    private boolean isRunning = false;
    private Thread thread;

    //All GameObjects
    public static List<Bullet> movingAmmo = Collections.synchronizedList(new ArrayList<>());
    public static List<Player> players = Collections.synchronizedList(new ArrayList<>());
    public static List<Block> blocks = Collections.synchronizedList(new ArrayList<>());
    public static List<GameObject> eyeCandy = Collections.synchronizedList(new ArrayList<>());
    public static MainPlayer thisPlayer;
    public static OtherPlayer otherPlayer;

    //Players spawn points
    public static int thisX = 0;
    public static int thisY = 0;
    public static int otherX = 0;
    public static int otherY = 0;

    //Music
    AudioPlayer soundtrack;

    //Levels
    protected BufferedImage level;
    protected BufferedImage background;
    public static final int GRID_SIZE = 58;

    protected Controller() {

        //Load background
        background = BufferedImageLoader.loadImage("/resources/Textures/BG/wood_background.png");

        //Loading level
        level = BufferedImageLoader.loadImage("/resources/mapLayouts/Level1.png");
        loadLevel(level);
    }

    public void start(){
        isRunning = true;


        // Load soundtrack
        try
        {
            int randomMusic = World.getSRandom().nextInt(10);
            if (randomMusic < 5) {
                soundtrack = new AudioPlayer("/resources/Music/Trananozixa.wav");
            } else {
                soundtrack = new AudioPlayer("/resources/Music/The_Colour_Three.wav");
            }
            soundtrack.play();
        }
        catch (Exception ex)
        {
            System.out.println("Error with playing sound.");
            ex.printStackTrace();

        }

        thread = new Thread(this);
        thread.start();
    }

    protected void stop(){
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

    /**
     * Game logic tick. Happens 60 times per second
     */
    public void tick(){
        for (int i = 0; i < players.size(); i++) {
            if(players.get(i) != null)
            players.get(i).tick();
        }

        for (int i = 0; i < movingAmmo.size(); i++) {
            if(movingAmmo.get(i) != null)
                movingAmmo.get(i).tick();
        }

        if (thisPlayer.isButton1Held() && thisPlayer.getSelectedWeapon() == 0 && thisPlayer.getWeaponSerial() == 003
                && thisPlayer.getWeapons().getPrimary().getCurrentDelay() == 0) {
            Point mouseRelativeToScreen = MouseInfo.getPointerInfo().getLocation();
            Point mouseRelativeToGame = new Point(mouseRelativeToScreen.x - getLocationOnScreen().x,
                    mouseRelativeToScreen.y - getLocationOnScreen().y);
            thisPlayer.getWeapons().getPrimary().shoot(mouseRelativeToGame.x, mouseRelativeToGame.y);

            Controller.thisPlayer.getWeapons().getPrimary().setCurrentDelay(
                    AssaultRifle.MAX_DELAY);
        } else if (thisPlayer.isButton1Held() && thisPlayer.getSelectedWeapon() == 1 && thisPlayer.getWeaponSerial() == 003
                && thisPlayer.getWeapons().getSecondary().getCurrentDelay() == 0) {
            Point mouseRelativeToScreen = MouseInfo.getPointerInfo().getLocation();
            Point mouseRelativeToGame = new Point(mouseRelativeToScreen.x - getLocationOnScreen().x,
                    mouseRelativeToScreen.y - getLocationOnScreen().y);
            thisPlayer.getWeapons().getSecondary().shoot(mouseRelativeToGame.x, mouseRelativeToGame.y);

            Controller.thisPlayer.getWeapons().getSecondary().setCurrentDelay(
                    AssaultRifle.MAX_DELAY);
        }
    }

    /**
     * Graphics tick. Happens a whole bunch of times per second.
     */
    public void render(){
        BufferStrategy bs = this.getBufferStrategy();
        if(bs == null){
            this.createBufferStrategy(3);
            return;
        }

        Graphics g = bs.getDrawGraphics();

        // Update graphics in this section:
        //////////////////////////////////////

        g.drawImage(background,0,0,WIDTH,HEIGHT,this);

        //Render eye candy
        for (int i = 0; i < eyeCandy.size(); i++) {
            if(eyeCandy.get(i) != null){
                eyeCandy.get(i).render(g);
            }
        }

        for (int i = 0; i < blocks.size(); i++) {
            if (blocks.get(i) != null){
                blocks.get(i).render(g);
            }
        }

        for (int i = 0; i < movingAmmo.size(); i++) {
            if(movingAmmo.get(i) != null)
                movingAmmo.get(i).render(g);
        }



        for (int i = 0; i < players.size(); i++) {
            if(players.get(i) != null) {
                players.get(i).render(g);
            }
        }

        //////////////////////////////////////
        g.dispose();
        bs.show();
    }

    public void renderWinner(int winnerNumber, double[][] playerInfo) {
        System.out.println("renderWinner");

        BufferStrategy bs = this.getBufferStrategy();
        if(bs == null){
            this.createBufferStrategy(3);
            return;
        }

        Graphics g = bs.getDrawGraphics();

        // Update graphics in this section:
        //////////////////////////////////////

        g.drawImage(background,0,0,WIDTH,HEIGHT,this);

        //Render eye candy
        for (int i = 0; i < eyeCandy.size(); i++) {
            if(eyeCandy.get(i) != null){
                eyeCandy.get(i).render(g);
            }
        }

        for (int i = 0; i < blocks.size(); i++) {
            if (blocks.get(i) != null){
                blocks.get(i).render(g);
            }
        }

        for (int i = 0; i < movingAmmo.size(); i++) {
            if(movingAmmo.get(i) != null)
                movingAmmo.get(i).render(g);
        }



        for (int i = 0; i < players.size(); i++) {
            if(players.get(i) != null) {
                players.get(i).render(g);
            }
        }

        Player winner;
        if (winnerNumber == thisPlayer.getPlayerNumber()) {
            winner = thisPlayer;
        } else {
            winner = otherPlayer;
        }

        Graphics2D g2D = (Graphics2D) bs.getDrawGraphics();

        g2D.setColor(Color.BLACK);
        Font font = new Font("Arial", Font.BOLD, 25);
        g2D.setFont(font);
        FontMetrics stringSize = g2D.getFontMetrics(font);

        g2D.drawString("The winner is " + winner.getPlayerName(),
                WIDTH / 2 - (stringSize.stringWidth("The winner is ")), HEIGHT / 10);
        g2D.drawString("Scores:" + winner.getPlayerName(), WIDTH / 2 - (stringSize.stringWidth("Scores:")),
                HEIGHT / 5);
        g2D.drawString("The winner is " + winner.getPlayerName(),
                WIDTH / 2 - (stringSize.stringWidth("The winner is ")), 3 * HEIGHT / 10);
        g2D.drawString(
                "      Kills      Deaths         K/D     Bullets     Bullets     Walking    Number of",
                WIDTH / 2 - (stringSize.stringWidth(
                        "      Kills      Deaths         K/D     Bullets     Bullets     Walking    Number of")),
                2 * HEIGHT / 5);
        g2D.drawString(
                "                                           Shot         Hit    Distance    Power-ups",
                WIDTH / 2 - (stringSize.stringWidth(
                        "                                           Shot         Hit    Distance    Power-ups")), HEIGHT / 2);

        for (int i = 0; i < players.size(); i++) {
            //Save data to send to client
            Player player = players.get(i);

            //Determine format
            String format = String.format(" %10d  %10d  %10f  %10d  %10d  %10d  %10s %n",
                    player.getKillCount(),
                    player.getDeathCount(),
                    player.getKdr(),
                    player.getBulletCount(),
                    player.getBulletHitCount(),
                    player.getWalkingDistance(),
                    "???");

            if (i == 0) {
                g2D.drawString(format,
                        WIDTH / 2 - (stringSize.stringWidth(format)), 3 * HEIGHT / 5);
            } else {
                g2D.drawString(format,
                        WIDTH / 2 - (stringSize.stringWidth(format)), 7 * HEIGHT / 10);
            }
        }

        //////////////////////////////////////
        g.dispose();
        g2D.dispose();
        bs.show();
    }

    //Loading the level
    private void loadLevel(BufferedImage image){
        int w = image.getWidth();
        int h = image.getHeight();

        for (int xx = 0; xx < w; xx++) {
            for (int yy = 0; yy < h; yy++) {

                //Getting colors from pixels
                int pixel = image.getRGB(xx,yy);
                int red = (pixel >> 16) & 0xff;
                int green = (pixel >> 8) & 0xff;
                int blue = (pixel) & 0xff;

                //Player 2 spawn point if red
                if(red == 255 && green == 0 && blue == 0){
                    otherX = xx*GRID_SIZE + GRID_SIZE/2;
                    otherY = yy*GRID_SIZE + GRID_SIZE/2;
                }

                //Create block if white
                if(red == 255 && green == 255 && blue == 255){
                    blocks.add(new Block(xx*GRID_SIZE, yy*GRID_SIZE));
                }

                //Player 1 spawn point if blue
                if(red == 0 && green == 0 && blue == 255){
                    thisX = xx*GRID_SIZE + GRID_SIZE/2;
                    thisY = yy*GRID_SIZE + GRID_SIZE/2;
                }
            }
        }
    }


    public Player getPlayer() {
        return thisPlayer;
    }

    public abstract void packetReceived(Object object);

    public abstract void close();

    public OutputConnection getOutputConnection() {
        return outputConnection;
    }
}
