//
//
//import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.awt.event.KeyEvent;
//import javax.swing.AbstractAction;
//import javax.swing.Action;
//import javax.swing.ActionMap;
//import javax.swing.InputMap;
//import javax.swing.JFrame;
//import javax.swing.JPanel;
//import javax.swing.KeyStroke;
//import javax.swing.Timer;
//import javax.swing.UIManager;
//import javax.swing.UnsupportedLookAndFeelException;
//
//public class Expo2 extends JPanel {
//
//    int x = 0;
//    int y = 0;
//
//    public enum VerticalKey {
//
//        UP, DOWN, NONE;
//    }
//
//    public enum HorizontalKey {
//
//        LEFT, RIGHT, NONE;
//    }
//
//    private VerticalKey verticalKeyState = VerticalKey.NONE;
//    private HorizontalKey horizontalKeyState = HorizontalKey.NONE;
//
//    public Expo2() {
//        bindKeyStrokeTo(WHEN_IN_FOCUSED_WINDOW, "pressed.s", KeyStroke.getKeyStroke(KeyEvent.VK_S, 0), new VerticalAction(VerticalKey.DOWN));
//        bindKeyStrokeTo(WHEN_IN_FOCUSED_WINDOW, "released.s", KeyStroke.getKeyStroke(KeyEvent.VK_S, 0, true), new VerticalAction(VerticalKey.NONE));
//        bindKeyStrokeTo(WHEN_IN_FOCUSED_WINDOW, "pressed.w", KeyStroke.getKeyStroke(KeyEvent.VK_W, 0), new VerticalAction(VerticalKey.UP));
//        bindKeyStrokeTo(WHEN_IN_FOCUSED_WINDOW, "released.w", KeyStroke.getKeyStroke(KeyEvent.VK_W, 0, true), new VerticalAction(VerticalKey.NONE));
//
//        bindKeyStrokeTo(WHEN_IN_FOCUSED_WINDOW, "pressed.a", KeyStroke.getKeyStroke(KeyEvent.VK_A, 0), new HorizontalAction(HorizontalKey.LEFT));
//        bindKeyStrokeTo(WHEN_IN_FOCUSED_WINDOW, "released.a", KeyStroke.getKeyStroke(KeyEvent.VK_A, 0, true), new HorizontalAction(HorizontalKey.NONE));
//        bindKeyStrokeTo(WHEN_IN_FOCUSED_WINDOW, "pressed.d", KeyStroke.getKeyStroke(KeyEvent.VK_D, 0), new HorizontalAction(HorizontalKey.RIGHT));
//        bindKeyStrokeTo(WHEN_IN_FOCUSED_WINDOW, "released.d", KeyStroke.getKeyStroke(KeyEvent.VK_D, 0, true), new HorizontalAction(HorizontalKey.NONE));
//
//
//        Timer timer = new Timer(40, new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                switch (verticalKeyState) {
//                    case UP:
//                        y -= 2;
//                        break;
//                    case DOWN:
//                        y += 2;
//                        break;
//                }
//
//                switch (horizontalKeyState) {
//                    case LEFT:
//                        x -= 2;
//                        break;
//                    case RIGHT:
//                        x += 2;
//                        break;
//                }
//
//                if (y + 100 > getHeight()) {
//                    y = getHeight() - 100;
//                } else if (y < 0) {
//                    y = 0;
//                }
//                if (x + 100 > getWidth()) {
//                    x = getWidth() - 100;
//                } else if (x < 0) {
//                    x = 0;
//                }
//
//                repaint();
//            }
//        });
//        timer.start();
//    }
//
//    public void bindKeyStrokeTo(int condition, String name, KeyStroke keyStroke, Action action) {
//        InputMap im = getInputMap(condition);
//        ActionMap am = getActionMap();
//
//        im.put(keyStroke, name);
//        am.put(name, action);
//    }
//
//    @Override
//    protected void paintComponent(Graphics g) {
//        super.paintComponent(g);
//        g.setColor(Color.BLUE);
//        g.drawRect(x, y, 100, 100);
//    }
//
//    public void setVerticalKeyState(VerticalKey verticalKeyState) {
//        this.verticalKeyState = verticalKeyState;
//        System.out.println(verticalKeyState);
//    }
//
//    public void setHorizontalKeyState(HorizontalKey horizontalKeyState) {
//        this.horizontalKeyState = horizontalKeyState;
//        System.out.println(horizontalKeyState);
//    }
//
//    public class VerticalAction extends AbstractAction {
//
//        private VerticalKey verticalKey;
//
//        public VerticalAction(VerticalKey verticalKeys) {
//            this.verticalKey = verticalKeys;
//        }
//
//        @Override
//        public void actionPerformed(ActionEvent e) {
//            setVerticalKeyState(verticalKey);
//        }
//
//    }
//    public class HorizontalAction extends AbstractAction {
//
//        private HorizontalKey horizontalKey;
//
//        public HorizontalAction(HorizontalKey horizontalKey) {
//            this.horizontalKey = horizontalKey;
//        }
//
//        @Override
//        public void actionPerformed(ActionEvent e) {
//            setHorizontalKeyState(horizontalKey);
//        }
//
//    }
//
//    @Override
//    public Dimension getPreferredSize() {
//        return new Dimension(500, 500);
//    }
//}