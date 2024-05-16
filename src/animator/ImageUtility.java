package animator;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageUtility {
    static BufferedImage scale(BufferedImage sourceImg, int targetWidth, int targetHeight){
        BufferedImage scaledImage = new BufferedImage(targetWidth, targetHeight, sourceImg.getType());
        Graphics2D g2d = scaledImage.createGraphics();
        g2d.drawImage(sourceImg, 0, 0, targetWidth, targetHeight, null);
        g2d.dispose();
        return scaledImage;
    }
}
