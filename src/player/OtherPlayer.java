package player;

import game.ClientController;
import game.ServerController;
import game.World;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class OtherPlayer extends Player {
    public OtherPlayer(double x, double y, double angle, Color color) {
        super(x, y, angle, color);
        // Graphics-related
        loadImageStrips();
        currentImage = standing.getHead();
    }


    public void tick() {
        boundRect = new Rectangle((int)this.x - currentImage.getImage().getWidth() / 2,
                (int)this.y - currentImage.getImage().getHeight() / 2, currentImage.getImage().getWidth(),
                currentImage.getImage().getHeight());
    }

    @Override
    public void render(Graphics g) {
        loadImage();

        // Sets up the axis of rotation
        AffineTransform affTra = AffineTransform.getTranslateInstance(
                x - currentImage.getImage().getWidth() / 2.0,
                y - currentImage.getImage().getHeight() / 2.0);
        // Rotates the frame
        affTra.rotate(super.getAngle(), currentImage.getImage().getWidth() / 2.0,
                currentImage.getImage().getHeight() / 2.0);
        Graphics2D g2d = (Graphics2D) g;

        // Draws the rotated image
        g2d.drawImage(currentImage.getImage(), affTra, World.controller);

        // Draws the player's hitbox
        g.setColor(playerColour);
        g.drawRect((int) x - currentImage.getImage().getWidth() / 2,
                (int) y - currentImage.getImage().getHeight() / 2, currentImage.getImage().getWidth(),
                currentImage.getImage().getHeight());

        Font font = new Font("Arial", Font.BOLD, 20);
        FontMetrics stringSize = g2d.getFontMetrics(font);

        g2d.drawRect((int) x - currentImage.getImage().getWidth() / 2,
                (int) y - currentImage.getImage().getHeight() / 2 - 2,
                (int) (currentImage.getImage().getWidth() * health / 100),
                1);

        g2d.drawString(playerName, (int) x - (stringSize.stringWidth(playerName)) / 4,
                (int) y - 5 - currentImage.getImage().getHeight() / 2);
    }
}
