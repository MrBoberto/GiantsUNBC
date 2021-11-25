package game;

import utilities.BufferedImageLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

public class MainMenu {

    public static JFrame mainMenu;
    public static String playerName = "";
    BufferedImage backgroundImage;

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
                draw(g2);

            }
        };

        mainMenuPanel.setMaximumSize(new Dimension(Controller.WIDTH, Controller.HEIGHT / 6));
        c.anchor = GridBagConstraints.PAGE_END;
        c.fill = GridBagConstraints.HORIZONTAL;
        mainMenu.add(mainMenuPanel);
        c.weighty = 0.8;
        mainMenuPanel.add(createNewVoidPanel(), c);

        JPanel bottomPanel = new JPanel(new GridBagLayout());
        bottomPanel.setBackground(new Color(0,0,0,0));
        c.weighty = 0.2;
        c.weightx = 1.0;
        c.insets = new Insets(10,10,10,10);
        mainMenuPanel.add(bottomPanel, c);

        JButton PLAYButton = new JButton();
        PLAYButton.setText("PLAY");
        c.weightx = 0.2;
        c.gridx = 0;
        c.gridy = 0;
        PLAYButton.addActionListener(e -> {

            System.out.println("The game has begun");
            mainMenu.dispose();

            new World();
        });
        bottomPanel.add(PLAYButton, c);

        JPanel settingsPanel = new JPanel(new GridLayout(2, 1));
        settingsPanel.setBackground(new Color(0,0,0,0));

        settingsPanel.add(createNewVoidPanel());
        JButton settingsButton = new JButton("Settings");
        settingsPanel.add(settingsButton);



        for (int i = 1; i <= 5; i++) {
            c.gridx = i;
            bottomPanel.add(createNewVoidPanel(), c);

        }

        c.gridx = 5;
        bottomPanel.add(settingsPanel, c);

        JPanel quitPanel = new JPanel(new GridLayout(2, 1));
        quitPanel.setBackground(new Color(0,0,0,0));
        quitPanel.add(createNewVoidPanel());
        JButton quit = new JButton("Quit");
        quit.addActionListener(e -> mainMenu.dispose());
        quitPanel.add(quit);

        c.gridx = 7;
        bottomPanel.add(quitPanel,c);

        for (Component component : bottomPanel.getComponents()) {
            if (component instanceof JButton) {
                component.setFont(new Font("Arial", Font.BOLD, 80));
                component.setForeground(Color.YELLOW);
                ((JButton) component).setOpaque(false);
                ((JButton) component).setBorderPainted(false);
                ((JButton) component).setFocusPainted(false);
            } else if (component instanceof JPanel){
                for (Component subComponent : ((JPanel) component).getComponents()){
                    if (subComponent instanceof JButton) {
                        subComponent.setFont(new Font("Bauhaus 93", Font.PLAIN, 20));
                        subComponent.setForeground(Color.YELLOW);
                        ((JButton) subComponent).setOpaque(false);
                        ((JButton) subComponent).setBorderPainted(false);
                        ((JButton) subComponent).setFocusPainted(false);
                    }
                }
            }
        }

        mainMenu.setVisible(true);

    }
    public void draw(Graphics2D g2){
        g2.drawImage(backgroundImage,0,0, mainMenu.getWidth(), mainMenu.getHeight(),null);
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
        public MainMenuButton(){
            super();
            enableInputMethods(true);
            addMouseListener(this);
        }

        @Override
        public Dimension getPreferredSize(){
            return new Dimension(super.getPreferredSize());
        }

        @Override
        public Dimension getMinimumSize(){
            return new Dimension(super.getMinimumSize());
        }

        @Override
        public Dimension getMaximumSize(){
            return new Dimension(super.getMaximumSize());
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
        }

        @Override
        public void mouseClicked(MouseEvent e) {

        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
    }
}

