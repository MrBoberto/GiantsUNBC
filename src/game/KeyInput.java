package game;

/**
 * This file is part of a solution to
 *		CPSC300 Term Project Fall 2021
 *
 * Detects and responds to key activity from the main player
 *
 * @author The Boyz
 * @version 1
 */

import packets.ArsenalPacket;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyInput extends KeyAdapter {
    public KeyInput() {
        /* empty */
    }

    public void keyPressed(KeyEvent e) {

        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            if (World.controller.getGameWindow().canPause) {
                World.controller.openPauseMenu();

                // Ensures the pause command is not executed too quickly to process
                World.controller.getGameWindow().setCanPause(false);
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
        if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
            Controller.thisPlayer.startDashTimer();
        }
        if (e.getKeyCode() == KeyEvent.VK_1 || e.getKeyCode() == KeyEvent.VK_NUMPAD1) {
            handleNumPad(1);
        }
        if (e.getKeyCode() == KeyEvent.VK_2 || e.getKeyCode() == KeyEvent.VK_NUMPAD2) {
            handleNumPad(2);
        }
        if (e.getKeyCode() == KeyEvent.VK_3 || e.getKeyCode() == KeyEvent.VK_NUMPAD3) {
            handleNumPad(3);
        }
        if (e.getKeyCode() == KeyEvent.VK_4 || e.getKeyCode() == KeyEvent.VK_NUMPAD4) {
            handleNumPad(4);
        }
    }

    private void handleNumPad(int numPad) {
        if (Controller.thisPlayer.getSelectedWeapon() == 0) {
            Controller.thisPlayer.getArsenal().setPrimary(numPad);
        } else {
            Controller.thisPlayer.getArsenal().setSecondary(numPad);
        }
        if (!(World.controller instanceof SingleController)) {
            World.controller.getOutputConnection().sendPacket(new ArsenalPacket(
                    Controller.thisPlayer.getArsenal().getPrimary().getWeaponType(),
                    Controller.thisPlayer.getArsenal().getSecondary().getWeaponType(),
                    Controller.thisPlayer.getSelectedWeapon(),
                    Controller.thisPlayer.getArsenal().getInventory()));
        }
    }

    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        for (int i = 0; i < Controller.players.size(); i++) {
            GameObject tmp = Controller.players.get(i);

            if (tmp != null) {
                switch (key) {
                    case KeyEvent.VK_ESCAPE -> World.controller.getGameWindow().setCanPause(true);
                    case KeyEvent.VK_W -> Controller.thisPlayer.setUp(false);
                    case KeyEvent.VK_A -> Controller.thisPlayer.setLeft(false);
                    case KeyEvent.VK_S -> Controller.thisPlayer.setDown(false);
                    case KeyEvent.VK_D -> Controller.thisPlayer.setRight(false);
                }

            }
        }
    }
}
