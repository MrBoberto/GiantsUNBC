package game;

import audio.AudioPlayer;
import audio.SFXPlayer;
import utilities.BufferedImageLoader;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Objects;

public class MainMenu {

    public static JFrame mainMenu;
    public static String playerName = "";
    BufferedImage backgroundImage;
    public static String ipaddress;
    public static int mapSelected = 1;
    private static int SERVER = 0, SINGLEPLAYER = 1;
    public static AudioPlayer soundtrack;
    public static final int VOL_MAX = 100;
    public static final int VOL_MIN = 0;
    static SFXPlayer sfxPlayer;
    GameOver gameOver;

    public MainMenu() {
        mainMenu = new JFrame("Doing your Mom");

        Image img;
        try {
            img = ImageIO.read(Objects.requireNonNull(getClass().getResource("/resources/GUI/character_closeups/character_closeup_blue.png")));
            mainMenu.setIconImage(img);
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }

        Dimension size = new Dimension(Controller.WIDTH, Controller.HEIGHT);
        mainMenu.setSize(size);
        mainMenu.setPreferredSize(size);
        mainMenu.setMaximumSize(size);
        mainMenu.setMinimumSize(size);
        mainMenu.setResizable(false);
        mainMenu.setLocationRelativeTo(null);
        mainMenu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        mainMenu.getContentPane().setBackground(Color.BLUE);
        GridBagConstraints c = new GridBagConstraints();

        sfxPlayer = new SFXPlayer();
        sfxPlayer.setFile(-2);

        // to make window appear on the screen
        // max size was incorrect on my multi-display monitor so I changed it - Noah
        System.out.println("Size" + mainMenu.getWidth()+"width"+ mainMenu.getHeight());

        backgroundImage = BufferedImageLoader.loadImage("/resources/imageRes/textBack.png");

        JPanel mainMenuPanel = new JPanel(new GridBagLayout()){

            @Override

        protected void paintComponent(Graphics g) {

                super.paintComponent(g);

                Graphics2D g2 = (Graphics2D) g;
                g2.drawImage(backgroundImage, 0, 0, mainMenu.getWidth(), mainMenu.getHeight(), null);

        }};

        mainMenuPanel.setOpaque(false);
        mainMenu.add(mainMenuPanel);

        JButton startButton = new JButton(){
            @Override
            public void paintComponent(Graphics g){
                Graphics2D g2 = (Graphics2D) g;
                Font font = new Font("Bauhaus 93", Font.PLAIN, 30);
                FontMetrics fontMetrics = g2.getFontMetrics(font);
                g2.setColor(Color.WHITE);
                g2.setFont(font);
                String text = "Click the screen to start...";
                g2.drawString(text, Controller.WIDTH/2 - fontMetrics.stringWidth(text)/2,Controller.HEIGHT * 3/4);
            }
        };
        startButton.setOpaque(false);
        startButton.setContentAreaFilled(false);
        startButton.setBorderPainted(false);
        c.anchor = GridBagConstraints.CENTER;
        c.fill = GridBagConstraints.BOTH;
        c.gridy = 0;
        c.gridx = 0;
        c.weighty = 1.0;
        c.weightx = 1.0;
        mainMenuPanel.add(startButton, c);

        startButton.addActionListener(f -> {
            sfxPlayer.play();
            mainMenuPanel.remove(startButton);

            JPanel bottomPanel = buttonsPanel(mainMenuPanel);

            c.fill = GridBagConstraints.CENTER;
            c.weighty = 0.2;
            c.weightx = 1.0;
            c.insets = new Insets(5,5,10,10);
            mainMenuPanel.add(bottomPanel, c);
            mainMenuPanel.validate();
            mainMenuPanel.repaint();
        });

        mainMenu.setVisible(true);

        try
        {
            soundtrack = new AudioPlayer("/resources/Music/The_Number_J.wav");
            soundtrack.play();
        }
        catch (Exception ex)
        {
            System.out.println("Error with playing sound.");
            ex.printStackTrace();

        }
    }

    public JPanel buttonsPanel(JPanel mainMenuPanel) {
        GridBagConstraints c = new GridBagConstraints();
        JPanel buttonsPanel = new JPanel(new GridBagLayout());
        buttonsPanel.setOpaque(false);
        c.fill = GridBagConstraints.CENTER;
        c.weightx = 1.0;
        c.insets = new Insets(5,5,10,10);


        c.weighty = 1.0/10.0;
        c.gridx = 0;

        for (int i = 0; i <= 6; i++) {
            c.gridy = i;
            buttonsPanel.add(createNewVoidPanel(), c);
        }


        MainMenuButton PLAYButton = new MainMenuButton(e -> {

            mainMenuPanel.remove(buttonsPanel);
            mainMenuPanel.add(multiplayerMenu(mainMenuPanel));
            mainMenuPanel.validate();
            mainMenuPanel.repaint();

        }, "Multiplayer");
        c.gridy = 7;
        buttonsPanel.add(PLAYButton, c);

        MainMenuButton singleplayerButton = new MainMenuButton(e -> {

            mainMenuPanel.remove(buttonsPanel);
            c.anchor = GridBagConstraints.CENTER;
            c.fill = GridBagConstraints.BOTH;
            c.gridy = 0;
            c.gridx = 0;
            c.weighty = 1.0;
            c.weightx = 1.0;
            mainMenuPanel.add(mapSelection(mainMenuPanel, SINGLEPLAYER), c);
            mainMenuPanel.validate();
            mainMenuPanel.repaint();


        }, "Singleplayer");
        c.gridy = 8;
        buttonsPanel.add(singleplayerButton, c);

        MainMenuButton settingsButton = new MainMenuButton(e -> {
            mainMenuPanel.remove(buttonsPanel);
            c.anchor = GridBagConstraints.CENTER;
            c.fill = GridBagConstraints.BOTH;
            c.gridy = 0;
            c.gridx = 0;
            c.weighty = 1.0;
            c.weightx = 1.0;
            mainMenuPanel.add(settingsMenu(mainMenuPanel), c);
            mainMenuPanel.validate();
            mainMenuPanel.repaint();

        }, "Settings");
        c.gridy = 9;
        buttonsPanel.add(settingsButton, c);

        MainMenuButton quitButton = new MainMenuButton(e -> {
            try
            {
                soundtrack.stop();
            }
            catch (Exception ex)
            {
                System.out.println("Error with playing sound.");
                ex.printStackTrace();

            }
            mainMenu.dispose();
        }, "Quit");
        c.gridy = 10;
        buttonsPanel.add(quitButton, c);
        return buttonsPanel;
    }

    private JPanel mapSelection(JPanel mainMenuPanel, int gameType) {

        JPanel mapSelectionPanel = new JPanel(new GridBagLayout());
        mapSelectionPanel.setMinimumSize(new Dimension(Controller.WIDTH,Controller.HEIGHT));
        mapSelectionPanel.setOpaque(false);
        ArrayList<BufferedImage> maps = new ArrayList<>();


        /*
        for(File file: (Objects.requireNonNull((new File("src/resources/mapLayouts")).listFiles((dir, name) -> name.endsWith(".png"))))){
            maps.add(BufferedImageLoader.loadImage("/resources/mapLayouts/"+ file.getName()));
        }

         */

        for (int i = 1; i <= 9; i++) {
            try {
                maps.add(ImageIO.read(getClass().getResource("/resources/mapLayouts/" + "Level" + i + ".png")));
            } catch (IOException exc) {
                System.out.println("Could not find image file: " + exc.getMessage());
            }
        }


        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.insets = new Insets(5,5,10,10);
        c.weighty = 1.0/9.0;

        c.gridx = 0;

        for (int i = 0; i <= 5; i++) {
            c.gridy = i;
            mapSelectionPanel.add(createNewVoidPanel(), c);
        }
        JPanel instructions = new JPanel(){
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                Font font = new Font("Bauhaus 93", Font.PLAIN, 30);
                FontMetrics fontMetrics = g2.getFontMetrics(font);
                g2.setColor(Color.WHITE);
                g2.setFont(font);
                String text = "Select a map...     ";
                g2.drawString(text, Controller.WIDTH/2 - fontMetrics.stringWidth(text)/2,20);
            }
        };
        c.gridy = 6;
        c.ipady = 15;
        c.fill = GridBagConstraints.HORIZONTAL;
        mapSelectionPanel.add(instructions, c);


        JPanel mapsPanel = new JPanel(new GridLayout(2, maps.size()/2,10,10));
        mapsPanel.setOpaque(false);

        for (int i = 0, mapsSize = maps.size(); i < mapsSize; i++) {
            BufferedImage map = maps.get(i);
            int finalI1 = i;
            JButton button = new JButton(){
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2 = (Graphics2D) g;
                    g2.drawImage(map,0,0,map.getWidth() * 5, map.getHeight() * 5,null);
                    if(mapSelected == finalI1 +1){
                        g2.setColor(Color.RED);
                        g2.setStroke(new BasicStroke(3));
                        g2.drawRect(0,0,map.getWidth() * 5, map.getHeight() * 5);
                        mapsPanel.repaint();
                    }
                }
            };
            int finalI = i+1;
            button.addActionListener(e -> mapSelected = finalI);
            button.setFocusPainted(false);
            button.setPreferredSize(new Dimension(map.getWidth() * 5, map.getHeight() * 5));

            mapsPanel.add(button);
        }
        c.gridy = 7;
        c.ipady = 0;
        c.fill = GridBagConstraints.CENTER;
        mapSelectionPanel.add(mapsPanel, c);

        MainMenuButton startButton = new MainMenuButton(e -> {
            try
            {
                soundtrack.stop();
            }
            catch (Exception ex)
            {
                System.out.println("Error with stopping sound.");
                ex.printStackTrace();

            }
            mainMenu.dispose();
            if(gameType == SERVER){
                try {
                    new World(1);
                } catch (UnknownHostException ex) {
                    ex.printStackTrace();
                }
            } else {
                try {
                    new World(3);
                } catch (UnknownHostException ex) {
                    ex.printStackTrace();
                }
            }


        }, "Start");
        c.gridy = 8;
        c.ipady = 0 ;
        c.fill = GridBagConstraints.CENTER;
        mapSelectionPanel.add(startButton, c);
        MainMenuButton backButton = new MainMenuButton(e -> {
            mainMenuPanel.remove(mapSelectionPanel);

            JPanel bottomPanel = buttonsPanel(mainMenuPanel);

            c.fill = GridBagConstraints.CENTER;
            c.weighty = 0.2;
            c.weightx = 1.0;
            c.insets = new Insets(5,5,10,10);
            mainMenuPanel.add(bottomPanel, c);
            mainMenuPanel.validate();
            mainMenuPanel.repaint();
        }, "Back");
        c.gridy = 9;
        mapSelectionPanel.add(backButton, c);

        return mapSelectionPanel;
    }

    private JPanel settingsMenu(JPanel mainMenuPanel) {
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


        MainMenuButton videoButton = new MainMenuButton(e -> {

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

        MainMenuButton audioButton = new MainMenuButton(e -> {

            mainMenuPanel.remove(settingsMenu);
            c.anchor = GridBagConstraints.CENTER;
            c.fill = GridBagConstraints.BOTH;
            c.gridy = 0;
            c.gridx = 0;
            c.weighty = 1.0;
            c.weightx = 1.0;
            mainMenuPanel.add(audioMenu(mainMenuPanel), c);
            mainMenuPanel.validate();
            mainMenuPanel.repaint();

        }, "Audio Settings");
        c.gridy = 8;
        settingsMenu.add(audioButton, c);

        MainMenuButton backButton = new MainMenuButton(f -> {
            mainMenuPanel.remove(settingsMenu);

            JPanel bottomPanel = buttonsPanel(mainMenuPanel);

            c.fill = GridBagConstraints.CENTER;
            c.weighty = 0.2;
            c.weightx = 1.0;
            c.insets = new Insets(5,5,10,10);
            mainMenuPanel.add(bottomPanel, c);
            mainMenuPanel.validate();
            mainMenuPanel.repaint();
        }, "Back");
        c.gridy = 9;
        settingsMenu.add(backButton ,c);

        return settingsMenu;
    }

    private JPanel audioMenu(JPanel mainMenuPanel) {
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

        MainMenuSlider masterSlider = new MainMenuSlider("Master Volume", VOL_MIN, VOL_MAX);
        c.gridy = 3;
        audioMenu.add(masterSlider, c);
        audioMenu.add(masterSlider.getJSlider(), c);

        MainMenuSlider gameSlider = new MainMenuSlider("Game Volume", VOL_MIN, VOL_MAX);
        c.gridy = 5;
        audioMenu.add(gameSlider, c);
        audioMenu.add(gameSlider.getJSlider(), c);

        MainMenuSlider musicSlider = new MainMenuSlider("Music Volume", VOL_MIN, VOL_MAX);
        c.gridy = 7;
        audioMenu.add(musicSlider, c);
        audioMenu.add(musicSlider.getJSlider(), c);

        MainMenuButton backButton = new MainMenuButton(f -> {
            mainMenuPanel.remove(audioMenu);
            c.anchor = GridBagConstraints.CENTER;
            c.fill = GridBagConstraints.BOTH;
            c.gridy = 0;
            c.gridx = 0;
            c.weighty = 1.0;
            c.weightx = 1.0;
            mainMenuPanel.add(settingsMenu(mainMenuPanel), c);
            mainMenuPanel.validate();
            mainMenuPanel.repaint();
        }, "Back");
        c.gridy = 9;
        audioMenu.add(backButton ,c);

        return audioMenu;
    }

    private JPanel multiplayerMenu(JPanel mainMenuPanel) {
        GridBagConstraints c = new GridBagConstraints();
        JPanel multiplayerMenu = new JPanel(new GridBagLayout());
        multiplayerMenu.setOpaque(false);
        c.fill = GridBagConstraints.CENTER;
        c.weightx = 1.0;
        c.insets = new Insets(5,5,10,10);


        c.weighty = 1.0/9.0;
        c.gridx = 0;

        for (int i = 0; i <= 6; i++) {
            c.gridy = i;
            multiplayerMenu.add(createNewVoidPanel(), c);
        }


        MainMenuButton serverButton = new MainMenuButton(e -> {

            mainMenuPanel.remove(multiplayerMenu);
            c.anchor = GridBagConstraints.CENTER;
            c.fill = GridBagConstraints.BOTH;
            c.gridy = 0;
            c.gridx = 0;
            c.weighty = 1.0;
            c.weightx = 1.0;
            mainMenuPanel.add(mapSelection(mainMenuPanel, SERVER), c);
            mainMenuPanel.validate();
            mainMenuPanel.repaint();

        }, "Server");
        c.gridy = 7;
        multiplayerMenu.add(serverButton, c);

        MainMenuButton clientButton = new MainMenuButton(e -> {
            ipaddress = JOptionPane.showInputDialog ("Please enter the server's ip address:");
            try
            {
                soundtrack.stop();
            }
            catch (Exception ex) {
                System.out.println("Error with stopping soundtrack.");
                ex.printStackTrace();

            }
            mainMenu.dispose();

            try {
                new World(2);
            } catch (UnknownHostException ex) {
                ex.printStackTrace();
            }

        }, "Client");
        c.gridy = 8;
        multiplayerMenu.add(clientButton, c);

        MainMenuButton backButton = new MainMenuButton(f -> {
            mainMenuPanel.remove(multiplayerMenu);

            JPanel bottomPanel = buttonsPanel(mainMenuPanel);

            c.fill = GridBagConstraints.CENTER;
            c.weighty = 0.2;
            c.weightx = 1.0;
            c.insets = new Insets(5,5,10,10);
            mainMenuPanel.add(bottomPanel, c);
            mainMenuPanel.validate();
            mainMenuPanel.repaint();
        }, "Back");
        c.gridy = 9;
        multiplayerMenu.add(backButton, c);

        return multiplayerMenu;
    }

    public  String getPlayerName() {
        return playerName;
    }

    private JPanel createNewVoidPanel(){
        JPanel voidPanel = new JPanel();
        voidPanel.setBackground(new Color(0,0,0,0));
        return voidPanel;
    }

    static class MainMenuButton extends JComponent implements MouseListener {
        boolean mouseEntered = false;
        BufferedImage unselectedTexture;
        BufferedImage selectedTexture;
        String text;

        private final ArrayList<ActionListener> listeners = new ArrayList<>();

        public MainMenuButton(ActionListener e, String text){
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

    static class MainMenuSlider extends JComponent implements ChangeListener {
        String text;
        JSlider jSlider;
        BufferedImage unselectedTexture;

        public MainMenuSlider(String text, int min, int max) {
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

