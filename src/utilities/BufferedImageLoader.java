package utilities;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public final class BufferedImageLoader {
    private static BufferedImage image;

    public static BufferedImage loadImage(String path){
        try {
            image = ImageIO.read(Objects.requireNonNull(BufferedImageLoader.class.getResource(path)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }
}
