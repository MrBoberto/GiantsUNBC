package animation;

/**
 * This file is part of a solution to
 *		CPSC300 Term Project Fall 2021
 *
 * An strip of frames for animation
 *
 * @author The Boyz
 * @version 1
 */

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class ImageStrip {
    private final ImageFrame head;
    private final int animationLength;
    private final String imageFileNames;

    public ImageStrip(ArrayList<BufferedImage> images, String imageFileNames) {
        this.animationLength = images.size();
        head = new ImageFrame(images.get(0));
        ImageFrame prevImage = head;
        ImageFrame newImage;

        // Make sure head is next for head
        if (animationLength > 1) {
            // Set the next for each image we have
            for (int i = 1; i < animationLength; i++) {
                newImage = new ImageFrame(images.get(i));
                prevImage.setNext(newImage);
                prevImage = newImage;
            }
            // We still need to set the last image to point to head
            prevImage.setNext(head);
        }
        // Loop head
        else {
            head.setNext(head);
        }

        // In case we want to know all the file names and locations
        this.imageFileNames = imageFileNames;
    }

    /**
     * Returns the animation.ImageFrame after the given one. If no animation.ImageFrame is given, returns head.
     *
     * @param current animation.ImageFrame
     * @return
     */
    public ImageFrame getNext(ImageFrame current) {
        if (current != null) {
            return current.getNext();
        }
        return head;
    }

    /**
     * For animations that do not loop
     *
     * @return head animation.ImageFrame
     */
    public ImageFrame getHead() {
        return head;
    }

    /**
     * For setting animation length automatically
     *
     * @return length int
     */
    public int getAnimationLength() {
        return animationLength;
    }

    public String toString() {
        return imageFileNames;
    }
}
