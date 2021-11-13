package player;

import game.World;

import java.awt.*;

public class OtherPlayer extends Player {
    public OtherPlayer(double x, double y, double angle) {
        super(x, y, angle);
    }

    public void tick() {

        boundRect = new Rectangle((int) this.x - currentImage.getImage().getWidth() / 2,
                (int) this.y - currentImage.getImage().getHeight() / 2, currentImage.getImage().getWidth(),
                currentImage.getImage().getHeight());
    }

    @Override
    public void tick(Point MouseLoc) {
        if (super.getX() <= currentImage.getImage().getWidth() / 2) {
            super.setX(currentImage.getImage().getWidth() / 2);
        } else if (super.getX() >= World.controller.WIDTH - currentImage.getImage().getWidth() / 2) {
            super.setX(World.controller.WIDTH - currentImage.getImage().getWidth() / 2);
        }

        if (super.getY() <= currentImage.getImage().getHeight() / 2) {
            super.setY(currentImage.getImage().getHeight() / 2);
            velY = 1;
        } else if (super.getY() >= World.controller.HEIGHT - currentImage.getImage().getHeight() / 2) {
            super.setY(World.controller.HEIGHT - currentImage.getImage().getHeight() / 2);
            velY = 0;
            isFalling = false;
        }
        boundRect = new Rectangle((int) this.x - currentImage.getImage().getWidth() / 2,
                (int) this.y - currentImage.getImage().getHeight() / 2, currentImage.getImage().getWidth(),
                currentImage.getImage().getHeight());
    }
}
