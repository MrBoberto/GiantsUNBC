package game;

import audio.AudioPlayer;
import audio.SFXPlayer;
import utilities.BufferedImageLoader;

import javax.swing.*;
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

    public PauseMenu(JFrame frame, Controller controller) {
        this.frame = frame;
        this.controller = controller;
        BufferedImage backgroundImage;
        GridBagConstraints c = new GridBagConstraints();
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
        c.fill = GridBagConstraints.CENTER;
        c.weightx = 1.0;
        c.insets = new Insets(5,5,10,10);

        c.weighty = 1.0/10.0;
        c.gridx = 0;

        for (int i = 0; i <= 6; i++) {
            c.gridy = i;
            pauseMenuPanel.add(createNewVoidPanel(), c);
        }

        PauseMenuButton backToGameButton = new PauseMenuButton(e -> {
            pauseMenuPanel.removeKeyListener(this);
            controller.closePauseMenu();
        }, "Back To Game");

        c.gridy = 9;
        pauseMenuPanel.add(backToGameButton, c);
        controller.getGameWindow().setCanPause(true);
        System.out.println(controller.getGameWindow().canPause());
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
}
