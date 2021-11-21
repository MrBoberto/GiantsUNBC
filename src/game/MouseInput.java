package game;

import player.Player;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MouseInput extends MouseAdapter {

    public MouseInput(){
        /* empty */
    }

    public void mousePressed(MouseEvent e) {
        // Shoot at the selected point
        /*
        if (e.getButton() == MouseEvent.BUTTON1) {
            if (Controller.mouseInside) {
                Controller.isMouse1Held = true;
            }
        }

         */
        if (e.getButton() == MouseEvent.BUTTON1) {
            if (Controller.thisPlayer.getSelectedWeapon() == Player.PRIMARY_WEAPON
                    && Controller.thisPlayer.getWeapons().getPrimary().getCurrentDelay() == 0)
            {
                Controller.thisPlayer.getWeapons().getPrimary().shoot(e.getX(), e.getY());
                Controller.thisPlayer.getWeapons().getPrimary().playAudio();

                Controller.thisPlayer.getWeapons().getPrimary().setCurrentDelay(
                        Controller.thisPlayer.getWeapons().getPrimary().getMAX_DELAY());

            } else if (Controller.thisPlayer.getSelectedWeapon() == Player.SECONDARY_WEAPON
                    && Controller.thisPlayer.getWeapons().getSecondary().getCurrentDelay() == 0) {

                Controller.thisPlayer.getWeapons().getSecondary().shoot(e.getX(), e.getY());
                Controller.thisPlayer.getWeapons().getSecondary().playAudio();

                Controller.thisPlayer.getWeapons().getSecondary().setCurrentDelay(
                        Controller.thisPlayer.getWeapons().getSecondary().getMAX_DELAY());

            }
        }
    }

    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            Controller.isMouse1Held = false;
        }
    }

    public void mouseClicked(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON3) {
            if (Controller.mouseInside) {
                int selectedWeapon = Controller.thisPlayer.getSelectedWeapon();
                // Switch between primary and secondary
                if (selectedWeapon < 1) {
                    if (selectedWeapon == 0 && Controller.thisPlayer.getWeapons().getSecondary() != null) {
                        Controller.thisPlayer.setSelectedWeapon(1);
                    }
                } else {
                    if (selectedWeapon == 1 && Controller.thisPlayer.getWeapons().getSecondary() != null) {
                        Controller.thisPlayer.setSelectedWeapon(0);
                    }
                }
            }
        }
    }

    public void mouseEntered(MouseEvent e) {
        Controller.mouseInside = true;
    }

    public void mouseExited(MouseEvent e) {
        Controller.mouseInside = false;
    }
}
