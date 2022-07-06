package dev.extrreme.logbook.utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class ImageUtility {

    private ImageUtility() {}
    /**
     * Gets an image from an image file in the resources directory
     * @param path the relative filepath of the image file from the resources directory
     * @return the image as an {@link BufferedImage}
     */
    public static BufferedImage getImage(String path) {
        BufferedImage image = null;
        InputStream is = ImageUtility.class.getResourceAsStream("/"+path);
        if (is == null) {
            return null;
        }
        try {
            image = ImageIO.read(is);
        } catch (IOException e){
            e.printStackTrace();
        }
        return image;
    }

    /**
     * Resizes an image to the specified width and height
     * @param image the image as a {@link BufferedImage} to resize
     * @param scaledWidth the width as an integer to scale the image to
     * @param scaledHeight the height as an integer to scale the image to
     * @return the image as an {@link BufferedImage}
     */
    public static BufferedImage resize(BufferedImage image, int scaledWidth, int scaledHeight) {
        BufferedImage outputImage = new BufferedImage(scaledWidth, scaledHeight, image.getType());

        Graphics2D g2d = outputImage.createGraphics();
        g2d.drawImage(image, 0, 0, scaledWidth, scaledHeight, null);
        g2d.dispose();

        return outputImage;
    }

    /**
     * Resizes an image by the specified percentage
     * @param image the image as a {@link BufferedImage} to resize
     * @param percent the percentage to reduce the image width and height by (e.g., 50)
     * @return the resized image as a {@link BufferedImage}
     */
    public static BufferedImage resizePercentage(BufferedImage image, double percent) {
        double factor = percent/100;
        return resizeFactor(image, factor);
    }

    /**
     * Resizes an image by the specified factor
     * @param image the image as a {@link BufferedImage} to resize
     * @param factor the factor to reduce the image width and height by (e.g., 0.5)
     * @return the resized image as a {@link BufferedImage}
     */
    public static BufferedImage resizeFactor(BufferedImage image, double factor) {
        int scaledWidth = (int) (image.getWidth() * factor);
        int scaledHeight = (int) (image.getHeight() * factor);
        return resize(image, scaledWidth, scaledHeight);
    }
}
