package ch.uetlib.core.image.webp;

import java.io.File;

/**
 * Represents a single image file with its encoding configuration.
 *
 * @param file the image file
 * @param config the encoding configuration for this image
 * @param ownedFile whether this record owns the file (i.e., the file is a temporary file that
 *     should be cleaned up by the caller)
 */
public record PartialImage(File file, WebpEncodingConfig config, boolean ownedFile) {
    public PartialImage(final File file, final WebpEncodingConfig config) {
        this(file, config, false);
    }
}
