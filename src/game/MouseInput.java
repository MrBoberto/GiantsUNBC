package game;

import player.Player;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MouseInput extends MouseAdapter {

    protected Controller controller;

    public MouseInput(Controller controller){
        this.controller = controller;
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
        System.out.println(e.getPoint());
        if (e.getButton() == MouseEvent.BUTTON1) {
            Controller.thisPlayer.setButton1Held(true);
            if (Controller.thisPlayer.getSelectedWeapon() == Player.PRIMARY_WEAPON
                    && Controller.thisPlayer.getArsenal().getPrimary().getCurrentDelay() == 0)
            {
                Controller.thisPlayer.getArsenal().getPrimary().shoot(e.getX(), e.getY());
                Controller.thisPlayer.getArsenal().getPrimary().playAudio();

                Controller.thisPlayer.getArsenal().getPrimary().setCurrentDelay(
                        Controller.thisPlayer.getArsenal().getPrimary().getMAX_DELAY());

            } else if (Controller.thisPlayer.getSelectedWeapon() == Player.SECONDARY_WEAPON
                    && Controller.thisPlayer.getArsenal().getSecondary().getCurrentDelay() == 0) {

                Controller.thisPlayer.getArsenal().getSecondary().shoot(e.getX(), e.getY());
                Controller.thisPlayer.getArsenal().getSecondary().playAudio();

                Controller.thisPlayer.getArsenal().getSecondary().setCurrentDelay(
                        Controller.thisPlayer.getArsenal().getSecondary().getMAX_DELAY());

            }
        }
    }

    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            Controller.thisPlayer.setButton1Held(false);
        }
    }

    public void mouseClicked(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON3) {
            if (Controller.mouseInside) {
                int selectedWeapon = Controller.thisPlayer.getSelectedWeapon();
                // Switch between primary and secondary
                if (selectedWeapon < 1) {
                    if (selectedWeapon == 0 && Controller.thisPlayer.getArsenal().getSecondary() != null) {
                        Controller.thisPlayer.setSelectedWeapon(1);
                    }
                } else {
                    if (selectedWeapon == 1 && Controller.thisPlayer.getArsenal().getSecondary() != null) {
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
