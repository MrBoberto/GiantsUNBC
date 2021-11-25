package game;

import utilities.BufferedImageLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Objects;

public class MainMenu {

    public static JFrame mainMenu;
    public static String playerName = "";
    BufferedImage backgroundImage;
    public static String ipaddress;
    public static int mapSelected = 0;
    private static int SERVER = 0, SINGLEPLAYER = 1;

    public MainMenu() {
        mainMenu = new JFrame("Doing your Mom");

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


        // to make window appear on the screen
        // max size was incorrect on my multi-display monitor so I changed it - Noah
        System.out.println("Size" + mainMenu.getWidth()+"width"+ mainMenu.getHeight());

        backgroundImage = BufferedImageLoader.loadImage("/resources/imageRes/textBack.png");

        JPanel mainMenuPanel = new JPanel(new GridBagLayout()){
            @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D)g;
            g2.drawImage(backgroundImage,0,0, mainMenu.getWidth(), mainMenu.getHeight(),null);

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

    }

    private JPanel buttonsPanel(JPanel mainMenuPanel) {
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
            mainMenuPanel.add(mapSelection(mainMenuPanel, SINGLEPLAYER));
            mainMenuPanel.validate();
            mainMenuPanel.repaint();


        }, "Singleplayer");
        c.gridy = 8;
        buttonsPanel.add(singleplayerButton, c);

        MainMenuButton settingsButton = new MainMenuButton(e -> {



        }, "Settings");
        c.gridy = 9;
        buttonsPanel.add(settingsButton, c);

        MainMenuButton quitButton = new MainMenuButton(e -> {
            mainMenu.dispose();
        }, "Quit");
        c.gridy = 10;
        buttonsPanel.add(quitButton, c);
        return buttonsPanel;
    }

    private JPanel mapSelection(JPanel mainMenuPanel, int gameType) {

        JPanel mapSelectionPanel = new JPanel(new GridBagLayout());
        ArrayList<BufferedImage> maps = new ArrayList<>();
        for(File file: (Objects.requireNonNull((new File("src/resources/mapLayouts")).listFiles((dir, name) -> name.endsWith(".png"))))){
            maps.add(BufferedImageLoader.loadImage("/resources/mapLayouts/"+ file.getName()));
        }

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.CENTER;
        c.weightx = 1.0;
        c.insets = new Insets(5,5,10,10);
        c.weighty = 1.0/(maps.size() + 6);

        for (int i = 0; i <= 6; i++) {
            c.gridy = i;
            mapSelectionPanel.add(createNewVoidPanel(), c);
        }

        c.gridx = 7;
        JPanel mapsPanel = new JPanel(new GridLayout(2,maps.size()/2));
        for (int i = 0, mapsSize = maps.size(); i < mapsSize; i++) {
            BufferedImage map = maps.get(i);
            JButton button = new JButton(){
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2 = (Graphics2D) g;
                    g2.drawImage(map,0,0,map.getWidth(),map.getHeight(),null);
                }
            };
            int finalI = i+1;
            button.addActionListener(e -> {
                mapSelected = finalI;
            });
            button.setFocusPainted(false);
            mapsPanel.add(button);
        }
        mapSelectionPanel.add(mapsPanel, c);

        MainMenuButton startButton = new MainMenuButton(e -> {

            mainMenu.dispose();
            if(gameType == SERVER){
                new World(1);
            } else {
                new World(3);
            }


        }, "Start");
        c.gridy = 8;
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
            mainMenuPanel.add(mapSelection(mainMenuPanel, SERVER));
            mainMenuPanel.validate();
            mainMenuPanel.repaint();

        }, "Server");
        c.gridy = 7;
        multiplayerMenu.add(serverButton, c);

        MainMenuButton clientButton = new MainMenuButton(e -> {
            ipaddress = JOptionPane.showInputDialog ("Please enter the server's ip address:");
            mainMenu.dispose();

            new World(2);

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
        multiplayerMenu.add(backButton ,c);

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
}

