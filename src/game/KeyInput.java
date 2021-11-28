package game;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyInput extends KeyAdapter {
    protected Controller controller;

    public KeyInput(Controller controller){
        this.controller = controller;
        /* empty */
    }

    public void keyPressed(KeyEvent e){

        //for (int i = 0; i < Controller.players.size(); i++) {
            //GameObject tmp = Controller.players.get(i);

            //if(tmp != null){
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            if (controller.getGameWindow().canPause) {
                controller.openPauseMenu();
                controller.getGameWindow().setCanPause(false);
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_W) {
            Controller.thisPlayer.setUp(true);
        }
        if (e.getKeyCode() == KeyEvent.VK_A) {
            Controller.thisPlayer.setLeft(true);
        }
        if (e.getKeyCode() == KeyEvent.VK_S) {
            Controller.thisPlayer.setDown(true);
        }
        if (e.getKeyCode() == KeyEvent.VK_D) {
            Controller.thisPlayer.setRight(true);
        }
        if (e.getKeyCode() == KeyEvent.VK_1 || e.getKeyCode() == KeyEvent.VK_NUMPAD1) {
            if (Controller.thisPlayer.getSelectedWeapon() == 0) {
                Controller.thisPlayer.getArsenal().setPrimary(1);
            } else {
                Controller.thisPlayer.getArsenal().setSecondary(1);
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_2 || e.getKeyCode() == KeyEvent.VK_NUMPAD2) {
            if (Controller.thisPlayer.getSelectedWeapon() == 0) {
                Controller.thisPlayer.getArsenal().setPrimary(2);
            } else {
                Controller.thisPlayer.getArsenal().setSecondary(2);
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_3 || e.getKeyCode() == KeyEvent.VK_NUMPAD3) {
            if (Controller.thisPlayer.getSelectedWeapon() == 0) {
                Controller.thisPlayer.getArsenal().setPrimary(3);
            } else {
                Controller.thisPlayer.getArsenal().setSecondary(3);
            }
        }

//                if (key == KeyEvent.VK_SHIFT) {
//                    shiftIsHeld = true;
//                    isSneaking = true;
//                }
//                if (key == KeyEvent.VK_CONTROL) {
//                    ctrlIsHeld = true;
//                }
//                if (key == KeyEvent.VK_T) {
//                    tIsHeld = true;
//                }
            //2}
        //}
    }

    public void keyReleased(KeyEvent e){
        int key = e.getKeyCode();

        for (int i = 0; i < Controller.players.size(); i++) {
            GameObject tmp = Controller.players.get(i);

            if(tmp != null){

                switch (key){
                    case KeyEvent.VK_ESCAPE -> controller.getGameWindow().setCanPause(true);
                    case KeyEvent.VK_W -> Controller.thisPlayer.setUp(false);
                    case KeyEvent.VK_A -> Controller.thisPlayer.setLeft(false);
                    case KeyEvent.VK_S -> Controller.thisPlayer.setDown(false);
                    case KeyEvent.VK_D -> Controller.thisPlayer.setRight(false);
                }

//                if (key == KeyEvent.VK_SHIFT) {
//                    shiftIsHeld = true;
//                    isSneaking = true;
//                }
//                if (key == KeyEvent.VK_CONTROL) {
//                    ctrlIsHeld = true;
//                }
//                if (key == KeyEvent.VK_T) {
//                    tIsHeld = true;
//                }
            }
        }
    }
}
