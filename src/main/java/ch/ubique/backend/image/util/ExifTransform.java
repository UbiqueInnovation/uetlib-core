package ch.ubique.backend.image.util;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.MetadataException;
import com.drew.metadata.exif.ExifDirectoryBase;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.drew.metadata.jpeg.JpegDirectory;

import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.Color;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public final class ExifTransform {

    private static final Logger logger = LoggerFactory.getLogger(ExifTransform.class);

    private ExifTransform() {}

    public static ImageInformation readImageInformation(final InputStream imageInputStream)
            throws IOException, ImageProcessingException {
        final var metadata = ImageMetadataReader.readMetadata(imageInputStream);
        final var directory = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);
        final var jpegDirectory = metadata.getFirstDirectoryOfType(JpegDirectory.class);

        final var orientation = getOrientation(directory);

        try {
            if (jpegDirectory != null) {
                final var width = jpegDirectory.getImageWidth();
                final var height = jpegDirectory.getImageHeight();
                return new ImageInformation(orientation, width, height);
            }
        } catch (final MetadataException me) {
            logger.warn("Could not get image width or height, set width/height to 0");
        }

        return new ImageInformation(orientation, 0, 0);
    }

    private static int getOrientation(final @Nullable ExifIFD0Directory directory) {
        try {
            // directory can be null if no exif data is set.
            if (directory != null) {
                return directory.getInt(ExifDirectoryBase.TAG_ORIENTATION);
            }
        } catch (final MetadataException me) {
            logger.debug("Could not get orientation, set to 1");
        }
        return 1;
    }

    // Look at http://chunter.tistory.com/143 for information
    public static AffineTransform getExifTransformation(final ImageInformation info) {
        final var t = new AffineTransform();
        switch (info.orientation) {
            case 2 -> { // Flip X
                t.scale(-1.0, 1.0);
                t.translate(-info.width, 0);
            }
            case 3 -> { // PI rotation
                t.translate(info.width, info.height);
                t.rotate(Math.PI);
            }
            case 4 -> { // Flip Y
                t.scale(1.0, -1.0);
                t.translate(0, -info.height);
            }
            case 5 -> { // - PI/2 and Flip X
                t.rotate(-Math.PI / 2);
                t.scale(-1.0, 1.0);
            }
            case 6 -> { // -PI/2 and -width
                t.translate(info.height, 0);
                t.rotate(Math.PI / 2);
            }
            case 7 -> { // PI/2 and Flip
                t.scale(-1.0, 1.0);
                t.translate(-info.height, 0);
                t.translate(0, info.width);
                t.rotate(3 * Math.PI / 2);
            }
            case 8 -> { // PI / 2
                t.translate(0, info.width);
                t.rotate(3 * Math.PI / 2);
            }
            default -> {
                /*noop*/
            }
        }

        return t;
    }

    public static BufferedImage transformImage(
            final BufferedImage image,
            final AffineTransform transform,
            final ImageInformation info) {
        final var op = new AffineTransformOp(transform, AffineTransformOp.TYPE_BICUBIC);
        // make sure to pick the correct destination image size, based on the
        // transformation.
        final int newWidth;
        final int newHeight;
        switch (info.orientation) {
            case 1, 2, 3, 4 -> {
                newWidth = image.getWidth();
                newHeight = image.getHeight();
            }
            case 5, 6, 7, 8 -> {
                newWidth = image.getHeight();
                newHeight = image.getWidth();
            }
            default -> {
                logger.warn("Unknown orientation: {}", info.orientation);
                newWidth = image.getWidth();
                newHeight = image.getHeight();
            }
        }
        var destinationImage = new BufferedImage(newWidth, newHeight, image.getType());
        final var g = destinationImage.createGraphics();
        g.setBackground(Color.WHITE);
        g.clearRect(0, 0, destinationImage.getWidth(), destinationImage.getHeight());
        destinationImage = op.filter(image, destinationImage);
        return destinationImage;
    }

    // Inner class containing image information
    public record ImageInformation(int orientation, int width, int height) {

        public String toString() {
            return String.format("%dx%d,%d", width, height, orientation);
        }
    }
}
