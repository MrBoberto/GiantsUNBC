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

        //for (int i = 0; i < Controller.players.size(); i++) {
            //GameObject tmp = Controller.players.get(i);

            //if(tmp != null){

                switch (key) {
                    case KeyEvent.VK_W:
                        Controller.thisPlayer.setUp(true); break;
                    case KeyEvent.VK_A:
                        Controller.thisPlayer.setLeft(true); break;
                    case KeyEvent.VK_S:
                        Controller.thisPlayer.setDown(true); break;
                    case KeyEvent.VK_D:
                        Controller.thisPlayer.setRight(true); break;
                    case KeyEvent.VK_1: //case KeyEvent.VK_NUMPAD1:
                        if (Controller.thisPlayer.getSelectedWeapon() == 0) {
                            Controller.thisPlayer.getWeapons().setPrimary(1);
                        } else {
                            Controller.thisPlayer.getWeapons().setSecondary(1);
                        }
                        System.out.println("KeyInput: " + Controller.thisPlayer.getWeapons());
                        break;
                    case KeyEvent.VK_2: case KeyEvent.VK_NUMPAD2:
                        if (Controller.thisPlayer.getSelectedWeapon() == 0) {
                            Controller.thisPlayer.getWeapons().setPrimary(2);
                        } else {
                            Controller.thisPlayer.getWeapons().setSecondary(2);
                        }
                        System.out.println(Controller.thisPlayer.getWeapons());
                        break;
                    case KeyEvent.VK_3: case KeyEvent.VK_NUMPAD3:
                        if (Controller.thisPlayer.getSelectedWeapon() == 0) {
                            Controller.thisPlayer.getWeapons().setPrimary(3);
                        } else {
                            Controller.thisPlayer.getWeapons().setSecondary(3);
                        }
                        System.out.println(Controller.thisPlayer.getWeapons());
                        break;
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
