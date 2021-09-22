package GiantsSidescroller.src;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class ImageStrip {
    private ImageFrame head;
    private int length;

    public ImageStrip(ArrayList<BufferedImage> images) {
        this.length = images.size();
        head = new ImageFrame(images.get(0));
        ImageFrame nextImage;
        ImageFrame newImage = new ImageFrame(images.get(length - 1));
        newImage.setNext(head);

        // To build the list we will walk it backwards,
        // such that each new image gets the next image from a previous cycle
        if (length > 1) {
            for (int i = length - 2; i > 0; i--) {
                nextImage = newImage;
                newImage = new ImageFrame(images.get(i));
                newImage.setNext(nextImage);
            }
        }
        else {
            head.setNext(head);
        }


    }

    /**
     * Returns the ImageFrame after the given one. If no ImageFrame is given, returns head.
     * @param current ImageFrame
     * @return
     */
    public ImageFrame getNext(ImageFrame current) {
        if (current != null) {
            return current.getNext();
        }
        return head;
    }

    /** For animations that do not loop
     * @return head ImageFrame
     */
    public ImageFrame getHead() {
        return head;
    }

    /** For setting animation length automatically
     *
     * @return length int
     */
    public int getLength() {
        return length;
    }
}
