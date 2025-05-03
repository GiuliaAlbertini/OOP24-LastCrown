package it.unibo.oop.lastcrown.view;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

/**
 * Load the frames corresponding to the given paths and size.
 */
public final class ImageLoader {
    private static final Logger LOG = Logger.getLogger(ImageLoader.class.getName());
    private ImageLoader() { }
    /**
     * @param paths Images paths
     * @param width width of the returning images
     * @param height heigth of the returning images
     * @return a list of the images corresponding to the given paths and size
     */
    public static synchronized List<BufferedImage> getAnimationFrames(final List<String> paths,
     final int width, final int height) {

        final List<BufferedImage> frames = new ArrayList<>();
        for (final String path: paths) {
            frames.addLast(getSingleFrame(path, width, height));
        }
        return frames;
    }

    /**
     * @param path Image path
     * @param width width of the returning image
     * @param height height of the returning image
     * @return the image corresponding to the given path and size
     */
    public static synchronized BufferedImage getImage(final String path, final int width, final int height) {
        return getSingleFrame(path, width, height);
    }

    private static BufferedImage getSingleFrame(final String path, final int width, final int height) {
        BufferedImage image = null;
        try {
            final BufferedImage originalImage = ImageIO.read(new File(path));
            image = new BufferedImage(width, height, originalImage.getType());
            final Graphics2D g2d = image.createGraphics();
            g2d.drawImage(originalImage, 0, 0, width, height, null);
            g2d.dispose();
        } catch (final IOException e) {
            LOG.fine("Error during image loading");
        }
        return image;
    }

    /**
     * Resizes the images of the given list.
     * @param frames the list of the images to be resized
     * @param newWidth new horizontal dimension of the images
     * @param newHeight new vertical dimension of the images
     * @return a new list of buffered image with the specified size
     */
    public static List<BufferedImage> resizeFrames(final List<BufferedImage> frames, final int newWidth, final int newHeight) {
        for (int i = 0; i < frames.size(); i++) {
            final BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, frames.get(i).getType());
            final Graphics2D g2d = resizedImage.createGraphics();
            g2d.drawImage(frames.get(i), 0, 0, newWidth, newHeight, null);
            g2d.dispose();
            frames.set(i, resizedImage);
        }
        return frames;
    }
}
