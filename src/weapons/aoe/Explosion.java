package weapons.aoe;

import animation.ImageFrame;
import animation.ImageStrip;
import game.GameObject;
import game.World;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

public class Explosion extends GameObject implements Serializable {
    protected static ImageStrip animation;
    protected ImageFrame currentFrame;
    private int age = 0;
    public final int MAX_AGE = 28;
    protected boolean harmful = true;
    public static final int DAMAGE = 80;
    final Rectangle boundRect;
    protected final int playerIBelongToNumber;

    public Explosion(double x, double y, int playerIBelongToNumber) {
        super(x, y);
        loadImage();

        this.playerIBelongToNumber = playerIBelongToNumber;

        System.out.println("EXPLOSION");

        boundRect = new Rectangle((int)this.x - currentFrame.getImage().getWidth() / 3,
                (int)this.y - currentFrame.getImage().getHeight() / 3, 2 * currentFrame.getImage().getWidth() / 3,
                2 * currentFrame.getImage().getHeight() / 3);

        System.out.println("boundRect created.");
    }

    /**
     * Determines which animation is being played and which frame should currently be played
     */
    protected void loadImage() {
        if (age == 0) {
            currentFrame = animation.getHead();
        } else {
            if (currentFrame.getNext() != animation.getHead()) {
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

    public boolean isHarmful() {
        return harmful;
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
        defLocStr = "/resources/VFX/explosion/";

        // Builds image strip for explosion animation
        for (int i = 1; i <= 29; i++) {
            imgLocStr.add("explosion (" + i + ").png");
        }
        animation = buildImageStrip(imgLocStr, defLocStr);
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
            try {
                images.add(ImageIO.read(Objects.requireNonNull(Explosion.class.getResource(defaultFileLocation + "" + s))));
            } catch (IOException exc) {
                System.out.println("Could not find image file: " + exc.getMessage());
            }
            imageFileNames.append(defaultFileLocation).append(s).append(", ");
        }
        // Used for the toString() method of this animation.ImageStrip
        for (int i = 0; i < imageFileNames.length() - 2; i++) {
            imageFileSubstring.append(imageFileNames.charAt(i));
        }
        return new ImageStrip(images, imageFileSubstring.toString());
    }
}
