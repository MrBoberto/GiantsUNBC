package game;

import player.Player;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyInput extends KeyAdapter {
    public KeyInput(){
        /* empty */
    }

    public void keyPressed(KeyEvent e){
        int key = e.getKeyCode();

        for (int i = 0; i < Controller.gameObjects.size(); i++) {
            GameObject tmp = Controller.gameObjects.get(i);

            if(tmp instanceof Player){

                switch (key){
                    case KeyEvent.VK_W -> Controller.thisPlayer.setUp(true);
                    case KeyEvent.VK_A -> Controller.thisPlayer.setLeft(true);
                    case KeyEvent.VK_S -> Controller.thisPlayer.setDown(true);
                    case KeyEvent.VK_D -> Controller.thisPlayer.setRight(true);
                }

//                if (key == KeyEvent.VK_SHIFT) {
//                    shiftIsHeld = true;
//                    isSneaking = true;
//                }
//                if (key == KeyEvent.VK_SPACE) {
//                    // Will eventually be removed
//                    spaceIsHeld = true;
//                    isJumping = true;
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

    public void keyReleased(KeyEvent e){
        int key = e.getKeyCode();

        for (int i = 0; i < Controller.gameObjects.size(); i++) {
            GameObject tmp = Controller.gameObjects.get(i);

            if(tmp instanceof Player){

                switch (key){
                    case KeyEvent.VK_W -> Controller.thisPlayer.setUp(false);
                    case KeyEvent.VK_A -> Controller.thisPlayer.setLeft(false);
                    case KeyEvent.VK_S -> Controller.thisPlayer.setDown(false);
                    case KeyEvent.VK_D -> Controller.thisPlayer.setRight(false);
                }

//                if (key == KeyEvent.VK_SHIFT) {
//                    shiftIsHeld = true;
//                    isSneaking = true;
//                }
//                if (key == KeyEvent.VK_SPACE) {
//                    // Will eventually be removed
//                    spaceIsHeld = true;
//                    isJumping = true;
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
