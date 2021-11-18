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
        if (e.getButton() == MouseEvent.BUTTON1) {
            if (Controller.thisPlayer.getSelectedWeapon() == Player.PRIMARY_WEAPON
                    && Controller.thisPlayer.getWeapons().getPrimary().getCurrentDelay() == 0)
            {
                Controller.thisPlayer.getWeapons().getPrimary().shoot(e.getX(), e.getY());

                Controller.thisPlayer.getWeapons().getPrimary().setCurrentDelay(
                        Controller.thisPlayer.getWeapons().getPrimary().getMAX_DELAY());

            } else if (Controller.thisPlayer.getSelectedWeapon() == Player.SECONDARY_WEAPON
                    && Controller.thisPlayer.getWeapons().getSecondary().getCurrentDelay() == 0) {

                Controller.thisPlayer.getWeapons().getSecondary().shoot(e.getX(), e.getY());

                Controller.thisPlayer.getWeapons().getSecondary().setCurrentDelay(
                        Controller.thisPlayer.getWeapons().getSecondary().getMAX_DELAY());

            }
        }
    }

    public void mouseReleased(MouseEvent e) {

    }
}
