package ch.ubique.backend.image.webp;

import ch.ubique.backend.core.file.temp.TempFileManager;
import ch.ubique.backend.image.util.ImageType;
import ch.ubique.backend.image.util.ImageUtils;

import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Collection;
import java.util.List;

import javax.imageio.ImageIO;

public final class WebpUtils {

    private static final Logger logger = LoggerFactory.getLogger(WebpUtils.class);

    private WebpUtils() {}

    public static byte[] fromBufferedImage(final BufferedImage bufferedImage) {
        final var tempDir = createTmpDir();
        final var completeTmpPath = tempDir.getLeft().resolve("tmp.png");

        final var outputPathTempWebp = completeTmpPath.resolveSibling("tmp.webp");
        try (final var out = new FileOutputStream(completeTmpPath.toFile())) {
            ImageIO.write(bufferedImage, "png", out);
        } catch (final IOException e) {
            logger.error("Failed to write temp PNG file. ", e);
            return new byte[0];
        }
        final var result = convertPngToWebp(completeTmpPath, outputPathTempWebp);
        tempDir.getRight().deleteTempFiles();
        return result;
    }

    public static byte[] addMetadata(final Path inputWebp, final String xmp) {
        return addMetadata(inputWebp, xmp, new TempFileManager());
    }

    public static byte[] addMetadata(final byte[] inputWebp, final String xmp) {
        final var tempFileManager = new TempFileManager();
        final Path tmpInputWebp;

        try {
            tmpInputWebp = tempFileManager.createTempDir().resolve("tmp.webp");
            Files.write(tmpInputWebp, inputWebp);
        } catch (final IOException e) {
            logger.error("Failed to create temp directory. ", e);
            return new byte[0];
        }
        return addMetadata(tmpInputWebp, xmp, tempFileManager);
    }

    /**
     * Deletes all temp files from TempFileManager that is passed in
     *
     * @param inputWebp the input file for conversion
     * @param xmp the value of the XMP Metadata, is being trimmed and line breaks are removed
     * @param tempFileManager a TempFileManager to create temporary files. All directories are
     *     cleared at the end of this method.
     * @return a byte array containing the data for the new WebP Image
     */
    private static byte[] addMetadata(
            final Path inputWebp, final String xmp, final TempFileManager tempFileManager) {
        final Path completeTmpPath;

        try {
            completeTmpPath = tempFileManager.createTempDir().resolve("tmp.xmp");
        } catch (final IOException e) {
            logger.error("Failed to create temp directory. ", e);
            return new byte[0];
        }
        try (final var out = new FileOutputStream(completeTmpPath.toFile())) {
            out.write(xmp.trim().replace("\n", "").replace("\r", "").getBytes());
        } catch (final IOException e) {
            logger.error("Failed to write temp XMP file. ", e);
            return new byte[0];
        }
        final var result =
                addMetadataToWebp(
                        inputWebp, completeTmpPath.resolveSibling("out.webp"), completeTmpPath);
        tempFileManager.deleteTempFiles();
        return result;
    }

    private static byte[] convertPngToWebp(final Path completeTmpPath, final Path outputPathWebp) {
        try {
            return ImageWebpLibraryWrapper.convertToWebP(
                    completeTmpPath, outputPathWebp, 100, Duration.ofSeconds(30));
        } catch (final Exception e) {
            logger.error("Failed to convert image to WebP", e);
            return new byte[0];
        }
    }

    private static byte[] addMetadataToWebp(
            final Path inputWebp, final Path outputWebp, final Path xmpFile) {
        return ImageWebpLibraryWrapper.setXMPMetadata(inputWebp, outputWebp, xmpFile);
    }

    public static byte[] animationFromBufferedImages(
            final Collection<BufferedImage> bufferedImages, final WebpEncodingConfig config) {
        final var tempDir = createTmpDir();
        final var completeTmpPath = tempDir.getLeft();

        final var pathsToPngs = writeTmpPngs(bufferedImages, completeTmpPath);
        final var result = convertImagesToWebp(pathsToPngs, config, completeTmpPath);
        tempDir.getRight().deleteTempFiles();
        return result;
    }

    public static byte[] animationFromExistingPngs(
            final Collection<Path> pathsToPngs, final WebpEncodingConfig config) {
        final var tempDir = createTmpDir();
        final var completeTmpPath = tempDir.getLeft();

        final var result = convertImagesToWebp(pathsToPngs, config, completeTmpPath);
        tempDir.getRight().deleteTempFiles();
        return result;
    }

    public static byte[] animationFromImagesAsByteArrays(
            final Collection<byte[]> pngImages, final WebpEncodingConfig config) {
        final var tempDir = createTmpDir();
        final var completeTmpPath = tempDir.getLeft();

        final var pathsToPngs = writeTmpPngBytes(pngImages, completeTmpPath);
        final var result = convertImagesToWebp(pathsToPngs, config, completeTmpPath);
        tempDir.getRight().deleteTempFiles();
        return result;
    }

    /**
     * Converts the given PNGs to WebP. The path to the PNGs is deleted afterwards.
     *
     * @param pathsToPngs
     * @param config
     * @param completeTmpPath
     * @return
     */
    private static byte[] convertImagesToWebp(
            final Collection<Path> pathsToPngs,
            final WebpEncodingConfig config,
            final Path completeTmpPath) {
        final var imageList = getPartialImages(pathsToPngs, config);

        try {
            return ImageWebpLibraryWrapper.convertListToWebP(
                    imageList, completeTmpPath.resolve("tmp.webp"), config.processTimeout());
        } catch (final Exception e) {
            logger.error("Failed to convert images to WebP", e);
            return new byte[0];
        }
    }

    private static List<PartialImage> getPartialImages(
            final Collection<Path> pathsToPngs, final WebpEncodingConfig config) {
        return pathsToPngs.stream().map(path -> new PartialImage(path.toFile(), config)).toList();
    }

    private static Collection<Path> writeTmpPngs(
            final Collection<BufferedImage> bufferedImages, final Path completeTmpPath)
            throws UncheckedIOException {
        try {
            return ImageUtils.writeImageListToFiles(bufferedImages, ImageType.PNG, completeTmpPath);
        } catch (final IOException e) {
            throw new UncheckedIOException("Failed to write temp PNG files", e);
        }
    }

    private static Collection<Path> writeTmpPngBytes(
            final Collection<byte[]> pngs, final Path completeTmpPath) throws UncheckedIOException {
        try {
            return ImageUtils.writeImageBytesListToFiles(pngs, ImageType.PNG, completeTmpPath);
        } catch (final IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private static Pair<Path, TempFileManager> createTmpDir() throws UncheckedIOException {
        final var tempFileManager = new TempFileManager();
        try {
            return Pair.of(tempFileManager.createTempDir(), tempFileManager);
        } catch (final IOException e) {
            throw new UncheckedIOException("Failed to create temp directory", e);
        }
    }
}
