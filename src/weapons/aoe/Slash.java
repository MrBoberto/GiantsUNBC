package weapons.aoe;

/**
 * This file is part of a solution to
 *		CPSC300 Term Project Fall 2021
 *
 * A slash area of effect attack from a player's lightning sword that can inflict damage upon their opponent. Has
 * colour corresponding to player using the lightning sword
 *
 * @author The Boyz
 * @version 1
 */

import animation.ImageFrame;
import animation.ImageStrip;
import game.*;
import player.Player;
import utilities.BufferedImageLoader;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.ArrayList;

public class Slash extends GameObject implements Serializable {
    protected static ImageStrip animation_blue;
    protected static ImageStrip animation_red;
    protected static ImageStrip animation_thanos;
    protected final ImageStrip animation;
    protected ImageFrame currentFrame;
    private int age = 0;
    public final int MAX_AGE = 5;
    protected boolean harmful = true;
    public static final int DAMAGE = 55;
    final Rectangle boundRect;
    protected final int playerIBelongToNumber;
    public final boolean isLeft;

    public Slash(double x, double y, double angle, boolean isLeft, int playerIBelongToNumber) {
        super(x, y);

        this.isLeft = isLeft;
        this.angle = angle;

        this.playerIBelongToNumber = playerIBelongToNumber;

        if (playerIBelongToNumber == Player.CLIENT_PLAYER && World.controller instanceof SingleController) {
            animation = animation_thanos;
        } else if (playerIBelongToNumber == Player.SERVER_PLAYER) {
            animation = animation_blue;
        } else {
            animation = animation_red;
        }

        loadImage();

        boundRect = new Rectangle((int)this.x - currentFrame.getImage().getWidth() / 2,
                (int)this.y - currentFrame.getImage().getHeight() / 2, currentFrame.getImage().getWidth(),
                currentFrame.getImage().getHeight());

        Controller.slashes.add(this);
    }

    /**
     * Determines which animation is being played and which frame should currently be played
     */
    protected void loadImage() {
        if (age == 0) {
            if (isLeft) {
                currentFrame = animation.getHead();
            } else {
                currentFrame = animation.getHead().getNext().getNext().getNext().getNext().getNext().getNext();
            }

        } else {
            if (currentFrame.getNext() != animation.getHead() && currentFrame.getNext()
                    != animation.getHead().getNext().getNext().getNext().getNext().getNext().getNext()) {
                currentFrame = animation.getNext(currentFrame);
            }
        }
    }

    @Override
    public void tick() {
        if (age >= MAX_AGE) return;

        age++;

        if (!harmful) return;

        if (age > 0 && boundRect != null) {
            harmful = false;
        }
    }

    public boolean hasDied() {
        return age >= MAX_AGE;
    }

    @Override
    public void render(Graphics g) {
        loadImage();

        if ((age != 0 && currentFrame.getNext() == animation.getHead())) return;

        Graphics2D g2d = (Graphics2D) g;

        AffineTransform affTra = AffineTransform.getTranslateInstance(
                x - currentFrame.getImage().getWidth() / 2.0,
                y - currentFrame.getImage().getHeight() / 2.0);

        double tempAngle = angle + Math.PI;

        if (tempAngle > Math.PI) {
            tempAngle -= 2 * Math.PI;
        }
        tempAngle *= -1;

        affTra.rotate(tempAngle, currentFrame.getImage().getWidth() / 2.0,
                currentFrame.getImage().getHeight() / 2.0);

        g2d.drawImage(currentFrame.getImage(), affTra, World.controller);
    }

    public int getPlayerIBelongToNumber() {
        return playerIBelongToNumber;
    }

    @Override
    public Rectangle getBounds() {
        return boundRect;
    }


    /**
     * Loads the image files into the image strips based upon their names
     */
    public static void loadImageStrips() {
        ArrayList<String> imgLocStr = new ArrayList<>();
        String defLocStr;

        // Saves amount of text to be used
        defLocStr = "/resources/VFX/slash/slash_";

        // Builds image strip for blue slash animation
        for (int i = 1; i <= 12; i++) {
            imgLocStr.add("blue/slash (" + i + ").png");
        }
        animation_blue = buildImageStrip(imgLocStr, defLocStr);
//        System.out.println(standing.toString());
        imgLocStr.clear();

        // Builds image strip for red slash animation
        for (int i = 1; i <= 12; i++) {
            imgLocStr.add("red/slash (" + i + ").png");
        }
        animation_red = buildImageStrip(imgLocStr, defLocStr);
//        System.out.println(standing.toString());
        imgLocStr.clear();

        // Builds image strip for explosion animation
        for (int i = 1; i <= 12; i++) {
            imgLocStr.add("thanos/slash (" + i + ").png");
        }
        animation_thanos = buildImageStrip(imgLocStr, defLocStr);
//        System.out.println(standing.toString());
        imgLocStr.clear();
    }

    /**
     * Builds the animation.ImageStrip for a specific animation
     *
     * @param imgLocStr           All file names to be loaded into the animation.ImageStrip for animation
     * @param defaultFileLocation The file path of the images
     * @return An animation.ImageStrip for animation
     */
    public static ImageStrip buildImageStrip(ArrayList<String> imgLocStr, String defaultFileLocation) {
        // The ArrayList of image files to be put into the animation.ImageStrip
        ArrayList<BufferedImage> images = new ArrayList<>();
        // Used to track images that are loaded
        StringBuilder imageFileNames = new StringBuilder();
        StringBuilder imageFileSubstring = new StringBuilder();
        for (String s : imgLocStr) {
            images.add(BufferedImageLoader.loadImage(defaultFileLocation + "" + s));
            imageFileNames.append(defaultFileLocation).append(s).append(", ");
        }
        // Used for the toString() method of this animation.ImageStrip
        for (int i = 0; i < imageFileNames.length() - 2; i++) {
            imageFileSubstring.append(imageFileNames.charAt(i));
        }
        return new ImageStrip(images, imageFileSubstring.toString());
    }
}
