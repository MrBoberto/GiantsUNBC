package game;

/**
 * This file is part of a solution to
 *		CPSC300 Term Project Fall 2021
 *
 * This is the pause menu class for the game. In single player, the pause menu causes controller ticks to return before
 * any changes are made
 *
 * @author The Boyz
 * @version 1
 */

import animation.ImageFrame;
import animation.ImageStrip;
import audio.AudioPlayer;
import audio.SFXPlayer;
import utilities.BufferedImageLoader;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class PauseMenu implements KeyListener {

    final JPanel pauseMenuPanel;
    final JFrame frame;
    final Controller controller;
    static SFXPlayer sfxPlayer;
    static ImageStrip blueBackground;
    static ImageStrip redBackground;
    static ImageStrip thisBackground;
    static ImageFrame currentBackground;
    BufferedImage backgroundImage;          // Still version

    public static final int VOL_MAX = 100;
    public static final int VOL_MIN = 0;
    enum PanelType{ButtonsMenu, SettingsMenu, AudioMenu}
    public PanelType panelType = PanelType.ButtonsMenu;

    public PauseMenu(JFrame frame, Controller controller) {
        this.frame = frame;
        this.controller = controller;

        setBackground(controller);
        currentBackground = thisBackground.getHead();

        sfxPlayer = new SFXPlayer();
        sfxPlayer.setFile(-2);

        pauseMenuPanel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                Graphics2D g2 = (Graphics2D)g;
                if (controller.isPauseMenuScreen) {
                    g2.drawImage(currentBackground.getImage(),0,0, frame.getWidth(), frame.getHeight(),null);
                    currentBackground = currentBackground.getNext();
                } else {
                    g2.drawImage(backgroundImage,0,0, frame.getWidth(), frame.getHeight(),null);
                }
            }
        };

        pauseMenuPanel.addKeyListener(this);
        pauseMenuPanel.setFocusable(true);
        pauseMenuPanel.setOpaque(false);

        GridBagConstraints c = new GridBagConstraints();

        c.fill = GridBagConstraints.CENTER;
        c.weightx = 1.0;
        c.insets = new Insets(5,5,10,10);

        c.weighty = 1.0/10.0;
        c.gridx = 0;

        pauseMenuPanel.add(buttonsMenu(), c);

        controller.getGameWindow().setCanPause(true);
    }

    public void requestFocusInWindow() {
        pauseMenuPanel.requestFocusInWindow();
    }

    private void setBackground(Controller controller) {
        if (controller instanceof ClientController) {
            thisBackground = redBackground;
            backgroundImage = BufferedImageLoader.loadImage("/resources/GUI/pause_menu/pause_back (10).png");
        } else {
            thisBackground = blueBackground;
            backgroundImage = BufferedImageLoader.loadImage("/resources/GUI/pause_menu/pause_back (5).png");
        }
    }

    private JPanel buttonsMenu() {
        panelType = PanelType.ButtonsMenu;
        JPanel buttonsPanel = new JPanel(new GridBagLayout());
        buttonsPanel.setOpaque(false);
        pauseMenuPanel.add(buttonsPanel);

        GridBagConstraints c = new GridBagConstraints();

        c.fill = GridBagConstraints.CENTER;
        c.weightx = 1.0;
        c.insets = new Insets(5,5,10,10);

        c.weighty = 1.0/10.0;
        c.gridx = 0;

        for (int i = 0; i <= 6; i++) {
            c.gridy = i;
            buttonsPanel.add(createNewVoidPanel(), c);
        }

        PauseMenuButton backToGameButton = new PauseMenuButton(e -> {
            pauseMenuPanel.remove(buttonsPanel);
            controller.closePauseMenu();
        }, "Back To Game");
        c.gridy = 7;
        buttonsPanel.add(backToGameButton, c);

        PauseMenuButton settingsButton = new PauseMenuButton(e -> {
            pauseMenuPanel.remove(buttonsPanel);
            c.anchor = GridBagConstraints.CENTER;
            c.fill = GridBagConstraints.BOTH;
            c.gridy = 0;
            c.gridx = 0;
            c.weighty = 1.0;
            c.weightx = 1.0;
            pauseMenuPanel.add(settingsMenu(), c);
            pauseMenuPanel.validate();
            controller.isPauseMenuScreen = false;
            pauseMenuPanel.repaint();

        }, "Settings");
        c.gridy = 8;
        buttonsPanel.add(settingsButton, c);

        PauseMenuButton controlsButton = new PauseMenuButton(e -> JOptionPane.showMessageDialog(buttonsPanel, """
                        W -> Forward
                        A -> Left
                        S -> Back
                        D -> Right
                        SHIFT -> Dash
                        1 -> Swap with Slot 1
                        2 -> Swap with Slot 2
                        3 -> Swap with Slot 3
                        4 -> Swap with Slot 4
                        LEFT MOUSE BUTTON -> Shoot
                        RIGHT MOUSE BUTTON -> Swap between Primary and Secondary weapon""",
                "Controls:", JOptionPane.INFORMATION_MESSAGE), "Controls");
        c.gridy = 9;
        buttonsPanel.add(controlsButton, c);

        PauseMenuButton menuButton = new PauseMenuButton(e -> {
            try
            {
                controller.soundtrack.stop();
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
            controller.isRunning = false;
            frame.dispose();
            new MainMenu();
        }, "Quit to Menu");
        c.gridy = 10;
        buttonsPanel.add(menuButton, c);

        return buttonsPanel;
    }

    private JPanel settingsMenu() {
        panelType = PanelType.SettingsMenu;
        GridBagConstraints c = new GridBagConstraints();
        JPanel settingsMenu = new JPanel(new GridBagLayout());
        settingsMenu.setOpaque(false);
        c.fill = GridBagConstraints.CENTER;
        c.weightx = 1.0;
        c.insets = new Insets(5,5,10,10);


        c.weighty = 1.0/9.0;
        c.gridx = 0;

        for (int i = 0; i <= 6; i++) {
            c.gridy = i;
            settingsMenu.add(createNewVoidPanel(), c);
        }

        PauseMenu.PauseMenuButton backButton = new PauseMenu.PauseMenuButton(f -> {
            pauseMenuPanel.remove(settingsMenu);

            JPanel bottomPanel = buttonsMenu();

            c.fill = GridBagConstraints.CENTER;
            c.weighty = 0.2;
            c.weightx = 1.0;
            c.insets = new Insets(5,5,10,10);
            pauseMenuPanel.add(bottomPanel, c);
            pauseMenuPanel.validate();
            pauseMenuPanel.repaint();
            controller.isPauseMenuScreen = true;
        }, "Back");
        c.gridy = 8;
        settingsMenu.add(backButton ,c);

        MainMenu.MainMenuButton videoButton = new MainMenu.MainMenuButton(e -> {

        }, "Video Settings");
        c.gridy = 9;
        settingsMenu.add(videoButton, c);

        MainMenu.MainMenuButton audioButton = new MainMenu.MainMenuButton(e -> {

            pauseMenuPanel.remove(settingsMenu);
            c.anchor = GridBagConstraints.CENTER;
            c.fill = GridBagConstraints.BOTH;
            c.gridy = 0;
            c.gridx = 0;
            c.weighty = 1.0;
            c.weightx = 1.0;
            pauseMenuPanel.add(audioMenu(), c);
            pauseMenuPanel.validate();
            pauseMenuPanel.repaint();

        }, "Audio Settings");
        c.gridy = 10;
        settingsMenu.add(audioButton, c);

        return settingsMenu;
    }

    private JPanel audioMenu() {
        panelType = PanelType.AudioMenu;
        GridBagConstraints c = new GridBagConstraints();
        JPanel audioMenu = new JPanel(new GridBagLayout());
        audioMenu.setOpaque(false);
        c.fill = GridBagConstraints.CENTER;
        c.weightx = 1.0;
        c.insets = new Insets(5,5,10,10);


        c.weighty = 1.0/9.0;
        c.gridx = 0;

        for (int i = 0; i <= 6; i++) {
            c.gridy = i;
            audioMenu.add(createNewVoidPanel(), c);
        }

        MainMenu.MainMenuButton backButton = new MainMenu.MainMenuButton(f -> {
            pauseMenuPanel.remove(audioMenu);
            c.anchor = GridBagConstraints.CENTER;
            c.fill = GridBagConstraints.BOTH;
            c.gridy = 0;
            c.gridx = 0;
            c.weighty = 1.0;
            c.weightx = 1.0;
            pauseMenuPanel.add(settingsMenu(), c);
            pauseMenuPanel.validate();
            pauseMenuPanel.repaint();
        }, "Back");
        c.gridy = 7;
        audioMenu.add(backButton ,c);

        PauseMenu.PauseMenuSlider masterSlider = new PauseMenu.PauseMenuSlider("Master Volume", VOL_MIN, VOL_MAX);
        c.gridy = 8;
        audioMenu.add(masterSlider, c);
        audioMenu.add(masterSlider.getJSlider(), c);

        PauseMenu.PauseMenuSlider gameSlider = new PauseMenu.PauseMenuSlider("Game Volume", VOL_MIN, VOL_MAX);
        c.gridy = 9;
        audioMenu.add(gameSlider, c);
        audioMenu.add(gameSlider.getJSlider(), c);

        PauseMenu.PauseMenuSlider musicSlider = new PauseMenu.PauseMenuSlider("Music Volume", VOL_MIN, VOL_MAX);
        c.gridy = 10;
        audioMenu.add(musicSlider, c);
        audioMenu.add(musicSlider.getJSlider(), c);

        return audioMenu;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        System.out.println("Key Press Detected");
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            if (panelType == PanelType.ButtonsMenu) {
                if (controller.getGameWindow().canPause) {
                    frame.remove(pauseMenuPanel);
                    controller.closePauseMenu();
                    controller.getGameWindow().setCanPause(false);
                }
            } else if (panelType == PanelType.SettingsMenu) {
                pauseMenuPanel.remove(0);

                JPanel bottomPanel = buttonsMenu();

                GridBagConstraints c = new GridBagConstraints();
                c.fill = GridBagConstraints.CENTER;
                c.weighty = 0.2;
                c.weightx = 1.0;
                c.insets = new Insets(5,5,10,10);
                pauseMenuPanel.add(bottomPanel, c);
                pauseMenuPanel.validate();
                pauseMenuPanel.repaint();
                controller.isPauseMenuScreen = true;
            } else {
                pauseMenuPanel.remove(0);

                GridBagConstraints c = new GridBagConstraints();
                c.anchor = GridBagConstraints.CENTER;
                c.fill = GridBagConstraints.BOTH;
                c.gridy = 0;
                c.gridx = 0;
                c.weighty = 1.0;
                c.weightx = 1.0;
                pauseMenuPanel.add(settingsMenu(), c);
                pauseMenuPanel.validate();
                pauseMenuPanel.repaint();
            }

        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) controller.getGameWindow().setCanPause(true);
    }

    public JPanel getJPanel() {
        return pauseMenuPanel;
    }

    private JPanel createNewVoidPanel(){
        JPanel voidPanel = new JPanel();
        voidPanel.setBackground(new Color(0,0,0,0));
        return voidPanel;
    }

    static class PauseMenuButton extends JComponent implements MouseListener {
        boolean mouseEntered = false;
        final BufferedImage unselectedTexture;
        final BufferedImage selectedTexture;
        final String text;

        private final ArrayList<ActionListener> listeners = new ArrayList<>();

        public PauseMenuButton(ActionListener e, String text){
            super();
            enableInputMethods(true);
            addMouseListener(this);
            listeners.add(e);

            unselectedTexture = BufferedImageLoader.loadImage("/resources/GUI/main_menu/unselected_option.png");
            selectedTexture = BufferedImageLoader.loadImage("/resources/GUI/main_menu/selected_option.png");
            setOpaque(false);
            setSize(selectedTexture.getWidth(),selectedTexture.getHeight());
            this.text = text;
            setBackground(new Color(0,255,0,0));
        }

        @Override
        public Dimension getPreferredSize(){
            return new Dimension(selectedTexture.getWidth() + 20,selectedTexture.getHeight() + 20);
        }

        @Override
        public Dimension getMinimumSize(){
            return getPreferredSize();
        }

        @Override
        public Dimension getMaximumSize(){
            return getPreferredSize();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setFont(new Font("Bauhaus 93", Font.PLAIN, 30));
            if(mouseEntered){
                g2.drawImage(selectedTexture,20,0,selectedTexture.getWidth(),selectedTexture.getHeight(),null);
                g2.setColor(Color.WHITE);
                g2.drawString(text,50,50);
            } else {
                g2.drawImage(unselectedTexture,0,0,unselectedTexture.getWidth(),unselectedTexture.getHeight(),null);
                g2.setColor(Color.GRAY);
                g2.drawString(text,30,50);
            }
        }

        @Override
        public void mouseClicked(MouseEvent e) {}

        @Override
        public void mousePressed(MouseEvent e) {
            sfxPlayer.play();
            notifyListeners(e);
            repaint();
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            repaint();
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            mouseEntered = true;
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            repaint();
        }

        @Override
        public void mouseExited(MouseEvent e) {
            mouseEntered = false;
            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            repaint();
        }

        private void notifyListeners(MouseEvent e)
        {
            ActionEvent evt = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "", e.getWhen(), e.getModifiersEx());
            synchronized(listeners)
            {
                for (ActionListener tmp : listeners) {
                    tmp.actionPerformed(evt);
                }
            }
        }

    }

    class PauseMenuSlider extends JComponent implements ChangeListener {
        final String text;
        final JSlider jSlider;
        final BufferedImage unselectedTexture;

        public PauseMenuSlider(String text, int min, int max) {
            super();

            // Default to 100
            int init = switch (text) {
                case "Master Volume" -> Main.getVolumeMaster();
                case "Game Volume" -> Main.getVolumeSFXActual();
                case "Music Volume" -> Main.getVolumeMusicActual();
                default -> 100;
            };
            jSlider = new JSlider(min, max, init);

            unselectedTexture = BufferedImageLoader.loadImage("/resources/GUI/main_menu/unselected_option.png");

            enableInputMethods(true);
            jSlider.addChangeListener(this);

            setOpaque(false);
            this.text = text;
            setBackground(new Color(0,255,0,0));
        }

        public JSlider getJSlider() {
            return jSlider;
        }

        @Override
        public Dimension getPreferredSize(){
            return new Dimension(unselectedTexture.getWidth() + 20,unselectedTexture.getHeight() + 20);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setFont(new Font("Bauhaus 93", Font.PLAIN, 30));
            g2.drawImage(unselectedTexture,0,0,unselectedTexture.getWidth(),unselectedTexture.getHeight(),null);
            g2.setColor(Color.GRAY);
            g2.drawString(text,30,30);
        }

        @Override
        public Dimension getMinimumSize(){
            return getPreferredSize();
        }

        @Override
        public Dimension getMaximumSize(){
            return getPreferredSize();
        }

        @Override
        public void stateChanged(ChangeEvent e) {
            switch (text) {
                case "Master Volume" -> {
                    Main.setVolumeMaster(jSlider.getValue());
                    AudioPlayer.setVolume();
                    sfxPlayer.setVolume();
                }
                case "Game Volume" -> {
                    Main.setVolumeSFX(jSlider.getValue());
                    sfxPlayer.setVolume();
                }
                case "Music Volume" -> {
                    Main.setVolumeMusic(jSlider.getValue());
                    AudioPlayer.setVolume();
                }
            }

            pauseMenuPanel.requestFocusInWindow();
        }
    }

    public static void loadImageStrips() {
        ArrayList<String> imgLocStr = new ArrayList<>();
        String defLocStr;

        // Saves amount of text to be used
        defLocStr = "/resources/GUI/pause_menu/pause_back (";

        // Builds image strip for standing facing right
        for (int i = 1; i <= 4; i++) {
            imgLocStr.add(i + ").png");
        }
        blueBackground = buildImageStrip(imgLocStr, defLocStr);
        imgLocStr.clear();

        // Builds image strip for standing facing right
        for (int i = 6; i <= 9; i++) {
            imgLocStr.add(i + ").png");
        }
        redBackground = buildImageStrip(imgLocStr, defLocStr);
        imgLocStr.clear();
    }

    /**
     * Builds the animation.ImageStrip for a specific animation
     *
     * @param imgLocStr           All file names to be loaded into the animation.ImageStrip for animation
     * @param defaultFileLocation The file path of the images
     * @return An animation.ImageStrip for animation
     */
    public static ImageStrip buildImageStrip(ArrayList<String> imgLocStr, String defaultFileLocation) {
        // The ArrayList of image files to be put into the animation.ImageStrip
        ArrayList<BufferedImage> images = new ArrayList<>();
        // Used to track images that are loaded
        StringBuilder imageFileNames = new StringBuilder();
        StringBuilder imageFileSubstring = new StringBuilder();
        for (String s : imgLocStr) {
            images.add(BufferedImageLoader.loadImage(defaultFileLocation + "" + s));
            imageFileNames.append(defaultFileLocation).append(s).append(", ");
        }
        // Used for the toString() method of this animation.ImageStrip
        for (int i = 0; i < imageFileNames.length() - 2; i++) {
            imageFileSubstring.append(imageFileNames.charAt(i));
        }
        return new ImageStrip(images, imageFileSubstring.toString());
    }
}
