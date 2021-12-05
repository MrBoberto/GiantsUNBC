package animation;

/**
 * This file is part of a solution to
 *		CPSC300 Term Project Fall 2021
 *
 * A frame (or node) in an animation strip
 *
 * @author The Boyz
 * @version 1
 */

import java.awt.image.BufferedImage;

public class ImageFrame {
    private ImageFrame next;
    private final BufferedImage image;

    public ImageFrame(BufferedImage image) {
        this.image = image;
    }

    public ImageFrame getNext() {
        return next;
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setNext(ImageFrame next) {
        this.next = next;
    }
}
