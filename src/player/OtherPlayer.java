package player;

/**
 * This file is part of a solution to
 *		CPSC300 Term Project Fall 2021
 *
 * The player which is not controlled by the user
 *
 * @author The Boyz
 * @version 1
 */

import game.World;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class OtherPlayer extends Player {
    public OtherPlayer(double x, double y, int playerNumber, Color color) {
        super(x, y, playerNumber, color);
        // Graphics-related
        loadImageStrips();
        currentImage = standing.getHead();
    }


    public void tick() {
        super.tick();

        dashTimer--;

        updateBounds();
    }

    private void updateBounds() {
        boundRect = new Rectangle((int)this.x - currentImage.getImage().getWidth() / 4,
                (int)this.y - currentImage.getImage().getHeight() / 4, currentImage.getImage().getWidth() / 2,
                currentImage.getImage().getHeight() / 2);
    }

    @Override
    public void render(Graphics g) {
        super.render(g);

        if(isTimeForNextFrame()){
            loadImage();
            resetAnimationTimer();
        }

        Graphics2D g2d = (Graphics2D) g;

        drawImages(g2d);
        drawNameAndHealthBar(g2d);
    }

    private void drawImages(Graphics2D g2d) {
        // Sets up the axis of rotation
        AffineTransform affTra = AffineTransform.getTranslateInstance(
                x - currentImage.getImage().getWidth() / 2.0,
                y - currentImage.getImage().getHeight() / 2.0);
        // Rotates the frame
        affTra.rotate(super.getAngle(), currentImage.getImage().getWidth() / 2.0,
                currentImage.getImage().getHeight() / 2.0);
        // Draws the rotated image
        if(skipFrame) {
            skipFrame = false;
        } else {
            g2d.drawImage(currentImage.getImage(), affTra, World.controller);
        }

        if (weaponSerial >= 0 && weaponSerial < 5) {
            g2d.drawImage(weaponTextures.get(weaponSerial), affTra, World.controller);
        } else if (weaponSerial == 5) {
            g2d.drawImage(currentSwordFrame.getImage(), affTra, World.controller);
        }
    }

    private void drawNameAndHealthBar(Graphics2D g2d) {
        g2d.setColor(playerColour);

        Font font = new Font("Arial", Font.BOLD, 15);
        g2d.setFont(font);
        FontMetrics stringSize = g2d.getFontMetrics(font);

        g2d.fillRect((int) x - currentImage.getImage().getWidth() / 4,
                (int) y - currentImage.getImage().getHeight() / 4 - 5,
                (currentImage.getImage().getWidth() * health / 200),
                5);

        if (playerName == null) return;

        g2d.drawString(playerName, (int) x - (stringSize.stringWidth(playerName)) / 2,
                (int) y - 10 - currentImage.getImage().getHeight() / 4);
    }
}
