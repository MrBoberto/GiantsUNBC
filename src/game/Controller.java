package game;

import audio.AudioPlayer;
import inventory_items.InventoryItem;
import inventory_items.LightningSwordItem;
import mapObjects.Block;
import player.Arsenal;
import player.MainPlayer;
import player.OtherPlayer;
import player.Player;
import power_ups.PowerUp;
import utilities.BufferedImageLoader;
import weapons.ammo.Bullet;
import weapons.aoe.Explosion;
import weapons.aoe.Slash;
import weapons.guns.AssaultRifle;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("ForLoopReplaceableByForEach")
public abstract class Controller extends Canvas implements Runnable {

    //Constants
    public static final int PORT = 55555;
    public static final int WIDTH = 1280;
    public static final int HEIGHT = WIDTH / 16 * 9;
    public static final double FRICTION = 0.5; // Friction acting on objects
    public static final int PLAYER_LIVES = 2;

    //Mouse controllers
    public static boolean mouseInside;
    public static boolean isWon;
    public boolean hasPauseMenu;
    public boolean isPauseMenuScreen = true;        // Only false in pause menu submenus
    public PauseMenu pauseMenu;

    //Multiplayer
    protected InputConnection inputConnection;
    protected OutputConnection outputConnection;

    //Game loop
    protected boolean isRunning;
    private Thread thread;

    //All GameObjects
    public static List<Bullet> movingAmmo;
    public static List<Player> players;
    public static List<Block> blocks;
    public static List<GameObject> eyeCandy;
    public static List<Explosion> explosions;
    public static List<Slash> slashes;
    public static List<PowerUp> powerUps;
    public static List<InventoryItem> inventoryItems;
    public static List<Arsenal> arsenals;
    public static MainPlayer thisPlayer;
    public static OtherPlayer otherPlayer;
    public static GameWindow gameWindow;

    //Global PowerUps variables
    protected static int currentPowerUpCooldown;
    public static final int COOLDOWN_BETWEEN_POWER_UPS = 3 * 60; //in game ticks. 3 seconds before a new power up can appear.

    //Global InventoryItems variables
    protected static int currentInventoryItemCooldown;
    public static final int COOLDOWN_BETWEEN_INVENTORY_ITEMS = 5 * 60; //in game ticks. 3 seconds before a new inventory item can appear.

    //Players spawn points
    public static int thisX;
    public static int thisY;
    public static int otherX;
    public static int otherY;

    //Music
    AudioPlayer soundtrack;

    //Levels
    protected BufferedImage level;
    protected BufferedImage background;
    public static final int GRID_SIZE = 58;

    protected Controller() {

        resetValues();

        createGameWindow();

        //Load background
        background = BufferedImageLoader.loadImage("/resources/Textures/BG/sci-fi_background.png");

        loadImageStrips();

        // For focus of key inputs after component switch
        setFocusable(true);

    }

    private void loadImageStrips() {
        Explosion.loadImageStrips();
        Slash.loadImageStrips();
        LightningSwordItem.loadImageStrips(this);
        PauseMenu.loadImageStrips();
    }

    private void createGameWindow() {
        gameWindow = new GameWindow(WIDTH, HEIGHT, "THE BOYZ, Your IP is: " + Main.getAddress().getHostAddress(), this);
        World.setGameWindow(gameWindow);
        this.addKeyListener(new KeyInput());
        this.addMouseListener(new MouseInput());
    }

    private void resetValues() {
        movingAmmo = Collections.synchronizedList(new ArrayList<>());
        players = Collections.synchronizedList(new ArrayList<>());
        blocks = Collections.synchronizedList(new ArrayList<>());
        eyeCandy = Collections.synchronizedList(new ArrayList<>());
        explosions = Collections.synchronizedList(new ArrayList<>());
        slashes = Collections.synchronizedList(new ArrayList<>());
        powerUps = Collections.synchronizedList(new ArrayList<>());
        inventoryItems = Collections.synchronizedList(new ArrayList<>());
        arsenals = Collections.synchronizedList(new ArrayList<>());

        isRunning = false;

        mouseInside = false;
        isWon = false;
        hasPauseMenu = false;

        currentPowerUpCooldown = 0;
        currentInventoryItemCooldown = 0;

        //Players spawn points
        thisX = 0;
        thisY = 0;
        otherX = 0;
        otherY = 0;
    }

    public void start() {
        isRunning = true;

        loadSoundtrack();

        thread = new Thread(this);
        thread.start();
    }

    private void loadSoundtrack() {
        try {
            int randomMusic = World.sRandom.nextInt(10);
            if (randomMusic < 5) {
                soundtrack = new AudioPlayer("/resources/Music/Trananozixa.wav");
            } else {
                soundtrack = new AudioPlayer("/resources/Music/The_Colour_Three.wav");
            }
            soundtrack.play();
        } catch (Exception ex) {
            System.out.println("Error with playing sound.");
            ex.printStackTrace();

        }
    }

    protected void stop() {
        isRunning = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mouseInside = false;
        isWon = false;
        hasPauseMenu = false;
        try {
            soundtrack.stop();
        } catch (Exception ex) {
            System.out.println("Error with stopping sound.");
            ex.printStackTrace();

        }


    }

    public void run() {
        this.requestFocus();
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        long timer = System.currentTimeMillis();
        int frames = 0;
        while (isRunning) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            while (delta >= 1) {
                tick();
                delta--;
            }
            render();
            frames++;

            if (System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
                frames = 0;
            }

        }
        stop();
    }

    public void openPauseMenu() {
        hasPauseMenu = true;
        pauseMenu = new PauseMenu(gameWindow.getFrame(), this);
        gameWindow.getFrame().remove(this);
        gameWindow.getFrame().add(pauseMenu.getJPanel());
        System.out.println(pauseMenu.requestFocusInWindow());
        gameWindow.getFrame().revalidate();
    }

    public void closePauseMenu() {
        gameWindow.getFrame().remove(pauseMenu.getJPanel());
        gameWindow.getFrame().add(this);
        gameWindow.getFrame().revalidate();
        gameWindow.setCanPause(true);

        // Allows key inputs to be used after the component switch
        requestFocusInWindow();
        this.createBufferStrategy(3);
        hasPauseMenu = false;
    }

    public GameWindow getGameWindow() {
        return gameWindow;
    }

    /**
     * Game logic tick. Happens 60 times per second
     */
    public void tick() {
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i) != null)
                players.get(i).tick();
        }

        for (int i = 0; i < movingAmmo.size(); i++) {
            if (movingAmmo.get(i) != null)
                movingAmmo.get(i).tick();
        }

        for (int i = 0; i < powerUps.size(); i++) {
            if (powerUps.get(i) != null) {
                powerUps.get(i).tick();
            }
        }

        for (int i = 0; i < inventoryItems.size(); i++) {
            if (inventoryItems.get(i) != null) {
                inventoryItems.get(i).tick();
            }
        }

        for (int i = 0; i < explosions.size(); i++) {
            if (explosions.get(i) != null)
                explosions.get(i).tick();
        }

        for (int i = 0; i < slashes.size(); i++) {
            if (slashes.get(i) != null)
                slashes.get(i).tick();
        }

        for (int i = 0; i < arsenals.size(); i++) {
            if (arsenals.get(i) != null) {
                arsenals.get(i).tick();
            }
        }

        for (int j = 0; j < explosions.size(); j++) {
            if (explosions.get(j) != null) {
                Explosion explosion = explosions.get(j);
                if (explosion.hasDied()) {
                    explosions.remove(explosion);
                }
            }
        }

        for (int j = 0; j < slashes.size(); j++) {
            if (slashes.get(j) != null) {
                Slash slash = slashes.get(j);
                if (slash.hasDied()) {
                    slashes.remove(slash);
                }
            }
        }

        handleAssaultRifle();
    }

    private void handleAssaultRifle() {
        if (thisPlayer.isButton1Held() && thisPlayer.getSelectedWeapon() == 0 && thisPlayer.getWeaponSerial() == 003
                && thisPlayer.getArsenal().getPrimary().getCurrentDelay() == 0) {
            Point mouseRelativeToScreen = MouseInfo.getPointerInfo().getLocation();
            Point mouseRelativeToGame = new Point(mouseRelativeToScreen.x - getLocationOnScreen().x,
                    mouseRelativeToScreen.y - getLocationOnScreen().y);
            thisPlayer.getArsenal().getPrimary().shoot(mouseRelativeToGame.x, mouseRelativeToGame.y);

            Controller.thisPlayer.getArsenal().getPrimary().setCurrentDelay(
                    AssaultRifle.MAX_DELAY);
        } else if (thisPlayer.isButton1Held() && thisPlayer.getSelectedWeapon() == 1 && thisPlayer.getWeaponSerial() == 003
                && thisPlayer.getArsenal().getSecondary() != null && thisPlayer.getArsenal().getSecondary().getCurrentDelay() == 0) {
            Point mouseRelativeToScreen = MouseInfo.getPointerInfo().getLocation();
            Point mouseRelativeToGame = new Point(mouseRelativeToScreen.x - getLocationOnScreen().x,
                    mouseRelativeToScreen.y - getLocationOnScreen().y);
            thisPlayer.getArsenal().getSecondary().shoot(mouseRelativeToGame.x, mouseRelativeToGame.y);

            Controller.thisPlayer.getArsenal().getSecondary().setCurrentDelay(
                    AssaultRifle.MAX_DELAY);
        }
    }

    /**
     * Graphics tick. Happens a bunch of times per second.
     */
    public void render() {

        if (hasPauseMenu) {
            if (isPauseMenuScreen) {
                // Play pause menu animation
                gameWindow.getFrame().repaint();
            }
            return;
        }

        if (isWon || !isRunning) {
            return;
        }

        BufferStrategy bs = this.getBufferStrategy();
        if (bs == null) {
            this.createBufferStrategy(3);
            return;
        }

        Graphics g = bs.getDrawGraphics();

        // Update graphics in this section:
        //////////////////////////////////////

        g.drawImage(background, 0, 0, WIDTH, HEIGHT, this);

        for (int i = 0; i < eyeCandy.size(); i++) {
            if (eyeCandy.get(i) != null) {
                eyeCandy.get(i).render(g);
            }
        }

        for (int i = 0; i < blocks.size(); i++) {
            if (blocks.get(i) != null) {
                blocks.get(i).render(g);
            }
        }

        for (int i = 0; i < powerUps.size(); i++) {
            if (powerUps.get(i) != null) {
                powerUps.get(i).render(g);
            }
        }

        for (int i = 0; i < inventoryItems.size(); i++) {
            if (inventoryItems.get(i) != null) {
                inventoryItems.get(i).render(g);
            }
        }

        for (int i = 0; i < movingAmmo.size(); i++) {
            if (movingAmmo.get(i) != null)
                movingAmmo.get(i).render(g);
        }

        for (int i = 0; i < explosions.size(); i++) {
            if (explosions.get(i) != null)
                explosions.get(i).render(g);
        }

        for (int i = 0; i < slashes.size(); i++) {
            if (slashes.get(i) != null)
                slashes.get(i).render(g);
        }

        for (int i = 0; i < players.size(); i++) {
            if (players.get(i) != null) {
                players.get(i).render(g);
            }
        }

        //Render in-game UI
        for (int i = 0; i < arsenals.size(); i++) {
            if (arsenals.get(i) != null) {
                arsenals.get(i).render(g);
            }
        }

        //////////////////////////////////////
        g.dispose();
        bs.show();
    }

    public void renderWinner(int winnerNumber) {
        gameWindow.frame.dispose();
        try {
            soundtrack.stop();
        } catch (Exception ex) {
            System.out.println("Error with stopping sound.");
            ex.printStackTrace();
        }

        System.out.println("renderWinner");


        Player winner;
        Player loser;
        if (winnerNumber == thisPlayer.getPlayerNumber()) {
            winner = thisPlayer;
            loser = otherPlayer;
        } else {
            winner = otherPlayer;
            loser = thisPlayer;
        }


        isRunning = false;
        gameWindow.frame.dispose();
        World.setGameOver(new GameOver(loser, winner, HEIGHT, players, WIDTH));

    }

    //Loading the level
    void loadLevel(BufferedImage image) {
        int w = image.getWidth();
        int h = image.getHeight();

        for (int xx = 0; xx < w; xx++) {
            for (int yy = 0; yy < h; yy++) {

                //Getting colors from pixels
                int pixel = image.getRGB(xx, yy);
                int red = (pixel >> 16) & 0xff;
                int green = (pixel >> 8) & 0xff;
                int blue = (pixel) & 0xff;

                //Player 2 spawn point if red
                if (red == 255 && green == 0 && blue == 0) {
                    otherX = xx * GRID_SIZE + GRID_SIZE / 2;
                    otherY = yy * GRID_SIZE + GRID_SIZE / 2;
                }

                //Create block if white
                if (red == 255 && green == 255 && blue == 255) {
                    blocks.add(new Block(xx * GRID_SIZE, yy * GRID_SIZE));
                }

                //Player 1 spawn point if blue
                if (red == 0 && green == 0 && blue == 255) {
                    thisX = xx * GRID_SIZE + GRID_SIZE / 2;
                    thisY = yy * GRID_SIZE + GRID_SIZE / 2;
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
