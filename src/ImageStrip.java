import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class ImageStrip {
    private ImageFrame head;
    private int length;
    private String imageFileNames;

    public ImageStrip(ArrayList<BufferedImage> images, String imageFileNames) {
        this.length = images.size();
        head = new ImageFrame(images.get(0));
        ImageFrame prevImage = head;
        ImageFrame newImage;

        // Make sure head is next for head
        if (length > 1) {
            // Set the next for each image we have
            for (int i = 1; i < length; i++) {
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

    public String toString() {
        return imageFileNames;
    }
}
