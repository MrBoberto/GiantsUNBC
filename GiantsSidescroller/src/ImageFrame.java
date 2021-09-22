package GiantsSidescroller.src;

import java.awt.image.BufferedImage;

public class ImageFrame {
    private ImageFrame next;
    private BufferedImage image;

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
