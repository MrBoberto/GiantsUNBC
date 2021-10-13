package player;

import game.World;

import java.awt.*;

public class OtherPlayer extends Player{
    public OtherPlayer(int playerNumber, double x, double y, double angle) {
        super(playerNumber, x, y, angle);
    }

    @Override
    public void tick(Point MouseLoc) {
        if (super.getX() <= currentImage.getImage().getWidth() / 2) {
            super.setX(currentImage.getImage().getWidth() / 2);
        } else if (super.getX() >= World.getWorld().getController().WIDTH - currentImage.getImage().getWidth() / 2) {
            super.setX(World.getWorld().getController().WIDTH - currentImage.getImage().getWidth() / 2);
        }

        if (super.getY() <= currentImage.getImage().getHeight() / 2) {
            super.setY(currentImage.getImage().getHeight() / 2);
            velY = 1;
        } else if (super.getY() >= World.getWorld().getController().HEIGHT - currentImage.getImage().getHeight() / 2) {
            super.setY(World.getWorld().getController().HEIGHT - currentImage.getImage().getHeight() / 2);
            velY = 0;
            isFalling = false;
        }
        boundRect = new Rectangle(pos.x - currentImage.getImage().getWidth() / 2,
                pos.y - currentImage.getImage().getHeight() / 2, currentImage.getImage().getWidth(),
                currentImage.getImage().getHeight());
    }
}
