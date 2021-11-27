package game;

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

    JPanel pauseMenuPanel;
    JFrame frame;
    Controller controller;
    KeyInput keyInput;
    static SFXPlayer sfxPlayer;
    public static final int VOL_MAX = 100;
    public static final int VOL_MIN = 0;

    public PauseMenu(JFrame frame, Controller controller) {
        this.frame = frame;
        this.controller = controller;
        BufferedImage backgroundImage;
        backgroundImage = BufferedImageLoader.loadImage("/resources/imageRes/textBack.png");
        sfxPlayer = new SFXPlayer();
        sfxPlayer.setFile(-2);

        pauseMenuPanel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                Graphics2D g2 = (Graphics2D)g;
                g2.drawImage(backgroundImage,0,0, frame.getWidth(), frame.getHeight(),null);
            }
        };

        pauseMenuPanel.setFocusable(true);
        pauseMenuPanel.requestFocusInWindow();
        pauseMenuPanel.addKeyListener(this);
        pauseMenuPanel.setOpaque(false);

        GridBagConstraints c = new GridBagConstraints();

        c.fill = GridBagConstraints.CENTER;
        c.weightx = 1.0;
        c.insets = new Insets(5,5,10,10);

        c.weighty = 1.0/10.0;
        c.gridx = 0;

        pauseMenuPanel.add(buttonsMenu(), c);

        controller.getGameWindow().setCanPause(true);
        System.out.println(controller.getGameWindow().canPause());
    }

    private JPanel buttonsMenu() {
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
        c.gridy = 3;
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
            pauseMenuPanel.repaint();

        }, "Settings");
        c.gridy = 5;
        buttonsPanel.add(settingsButton, c);

        PauseMenuButton menuButton = new PauseMenuButton(e -> {
            try
            {
                controller.soundtrack.stop();
            }
            catch (Exception ex)
            {
                System.out.println("Error with stopping soundtrack.");
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


        MainMenu.MainMenuButton videoButton = new MainMenu.MainMenuButton(e -> {

            /*
            mainMenuPanel.remove(settingsMenu);
            c.anchor = GridBagConstraints.CENTER;
            c.fill = GridBagConstraints.BOTH;
            c.gridy = 0;
            c.gridx = 0;
            c.weighty = 1.0;
            c.weightx = 1.0;
            mainMenuPanel.add(mapSelection(mainMenuPanel, SERVER), c);
            mainMenuPanel.validate();
            mainMenuPanel.repaint();

             */

        }, "Video Settings");
        c.gridy = 7;
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
        c.gridy = 8;
        settingsMenu.add(audioButton, c);

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
        }, "Back");
        c.gridy = 9;
        settingsMenu.add(backButton ,c);

        return settingsMenu;
    }

    private JPanel audioMenu() {
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

        PauseMenu.PauseMenuSlider masterSlider = new PauseMenu.PauseMenuSlider("Master Volume", VOL_MIN, VOL_MAX);
        c.gridy = 3;
        audioMenu.add(masterSlider, c);
        audioMenu.add(masterSlider.getJSlider(), c);

        PauseMenu.PauseMenuSlider gameSlider = new PauseMenu.PauseMenuSlider("Game Volume", VOL_MIN, VOL_MAX);
        c.gridy = 5;
        audioMenu.add(gameSlider, c);
        audioMenu.add(gameSlider.getJSlider(), c);

        PauseMenu.PauseMenuSlider musicSlider = new PauseMenu.PauseMenuSlider("Music Volume", VOL_MIN, VOL_MAX);
        c.gridy = 7;
        audioMenu.add(musicSlider, c);
        audioMenu.add(musicSlider.getJSlider(), c);

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
        c.gridy = 9;
        audioMenu.add(backButton ,c);

        return audioMenu;
    }

    public JPanel getJPanel() {
        return pauseMenuPanel;
    }

    private JPanel createNewVoidPanel(){
        JPanel voidPanel = new JPanel();
        voidPanel.setBackground(new Color(0,0,0,0));
        return voidPanel;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            System.out.println("ESCAPE DETECTED");
            if (controller.getGameWindow().canPause) {
                pauseMenuPanel.removeKeyListener(this);
                controller.closePauseMenu();
                controller.getGameWindow().setCanPause(false);
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) controller.getGameWindow().setCanPause(true);
    }

    static class PauseMenuButton extends JComponent implements MouseListener {
        boolean mouseEntered = false;
        BufferedImage unselectedTexture;
        BufferedImage selectedTexture;
        String text;

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
            ActionEvent evt = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "", e.getWhen(), e.getModifiers());
            synchronized(listeners)
            {
                for (int i = 0; i < listeners.size(); i++)
                {
                    ActionListener tmp = listeners.get(i);
                    tmp.actionPerformed(evt);
                }
            }
        }

        public void addActionListener(ActionListener listener)
        {
            listeners.add(listener);
        }
    }

    static class PauseMenuSlider extends JComponent implements ChangeListener {
        String text;
        JSlider jSlider;
        BufferedImage unselectedTexture;

        public PauseMenuSlider(String text, int min, int max) {
            super();

            // Default to 100
            int init = 100;
            switch (text) {
                case "Master Volume":
                    init = Main.getVolumeMaster();
                    break;
                case "Game Volume":
                    init = Main.getVolumeSFXActual();
                    break;
                case "Music Volume":
                    init = Main.getVolumeMusicActual();
                    break;
            }
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
                case "Master Volume":
                    Main.setVolumeMaster(jSlider.getValue());
                    AudioPlayer.setVolume();
                    sfxPlayer.setVolume();
                    break;
                case "Game Volume":
                    Main.setVolumeSFX(jSlider.getValue());
                    sfxPlayer.setVolume();
                    break;
                case "Music Volume":
                    Main.setVolumeMusic(jSlider.getValue());
                    AudioPlayer.setVolume();
                    break;
            }

        }
    }
}
