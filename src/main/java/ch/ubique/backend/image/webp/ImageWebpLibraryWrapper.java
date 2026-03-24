package ch.ubique.backend.image.webp;

import ch.ubique.backend.core.process.ProcessConfig;
import ch.ubique.backend.core.process.ProcessResult;
import ch.ubique.backend.core.utils.ProcessUtils;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

final class ImageWebpLibraryWrapper {

    private static final Logger logger = LoggerFactory.getLogger(ImageWebpLibraryWrapper.class);
    private static final String CWEBP_BIN_PATH = "cwebp";
    private static final String IMG2WEBP_BIN_PATH = "img2webp";
    private static final String WEBPMUX_BIN_PATH = "webpmux";

    private ImageWebpLibraryWrapper() {
    }

    public static boolean isCWebPAvailable() {
        return isLibAvailable(CWEBP_BIN_PATH);
    }

    public static boolean isImg2WebPAvailable() {
        return isLibAvailable(IMG2WEBP_BIN_PATH);
    }

    public static boolean isWebPMuxAvailable() {
        return isLibAvailable(WEBPMUX_BIN_PATH);
    }

    public static boolean isLibAvailable(final String binPath) {
        try {
            Runtime.getRuntime().exec(new String[]{binPath, "-version"});
            return true;
        } catch (final IOException e) {
            logger.error("Failed to execute {}", binPath, e);
            return false;
        }
    }

    public static byte[] convertToWebP(
            final Path imageFile, final Path targetFile, final int quality,
            final Duration processTimeout) {
        try {
            final var command =
                    List.of(
                            CWEBP_BIN_PATH,
                            "-q",
                            String.valueOf(quality),
                            imageFile.toAbsolutePath().toString(),
                            "-o",
                            targetFile.toAbsolutePath().toString());
            final var config =
                    ProcessConfig.builder()
                            .command(command)
                            .timeout(processTimeout)
                            .logLevelStdout(Level.DEBUG)
                            .logLevelStderr(Level.INFO);
            final var result = ProcessUtils.executeProcess(config.build());
            if (result.success()) {
                // Process was not killed and terminated with exit code 0
                return Files.readAllBytes(targetFile);
            }
            return new byte[0];
        } catch (final Exception e) {
            logger.error("Failed to convert image to WebP", e);
            return new byte[0];
        }
    }

    /**
     * Converts a collection of images to a WebP animation.
     *
     * @param frames List of png-paths used as frames for the resulting webp
     */
    public static ProcessResult convertListToWebP(
            final List<Path> frames, final WebpEncodingConfig config, final Path targetPath)
            throws IOException {

        final var perImageArguments =
                frames.stream().flatMap(frame -> perImageArguments(frame, config));
        final var command = createWebAnimationCommand(targetPath, perImageArguments);
        return execute(command, config.processTimeout());
    }

    /**
     * Converts a list of partial images to a WebP animation. Each partial image carries its own
     * {@link WebpEncodingConfig}, allowing per-frame duration and encoding settings.
     *
     * @param frames List of partial images with per-frame encoding configurations
     * @param targetPath Path for the resulting WebP file
     * @param processTimeout Timeout for the conversion process
     */
    public static ProcessResult convertPartialImagesToWebP(
            final List<PartialImage> frames, final Path targetPath,
            final Duration processTimeout) throws IOException {
        final var perImageArguments =
                frames.stream().flatMap(ImageWebpLibraryWrapper::perImageArguments);
        final var command = createWebAnimationCommand(targetPath, perImageArguments);
        return execute(command, processTimeout);
    }

    /**
     * Converts a collection of images to a WebP animation. Relies upon the fact that the directory
     * the images are in is being deleted afterward (so should not be called directly, only through
     * {@link WebpUtils}).
     *
     * @param imageFiles The collection of images to convert
     * @return The byte array of the WebP animation
     */
    public static byte[] convertListToWebP(
            final Collection<PartialImage> imageFiles, final Path tmpFileOutputPath,
            final Duration processTimeout) {
        if (imageFiles.stream().anyMatch(f -> !f.file().exists())) {
            throw new IllegalArgumentException("Not all files exist");
        }

        final var command =
                createWebAnimationCommand(
                        tmpFileOutputPath,
                        imageFiles.stream().flatMap(ImageWebpLibraryWrapper::perImageArguments));
        try {
            final var processResult = execute(command, processTimeout);
            if (processResult.success()) {
                return Files.readAllBytes(tmpFileOutputPath);
            } else {
                return new byte[0];
            }
        } catch (final Exception e) {
            logger.error("Failed to convert image to WebP", e);
        }
        return new byte[0];
    }

    private static List<String> createWebAnimationCommand(
            final Path targetPath, final Stream<String> perImageArguments) {
        final var baseCommand =
                Stream.of(
                        IMG2WEBP_BIN_PATH,
                        "-loop",
                        "0", // Loop forever
                        "-o",
                        targetPath.toString());
        return Stream.concat(baseCommand, perImageArguments).toList();
    }

    private static ProcessResult execute(final List<String> command, final Duration processTimeout)
            throws IOException {
        final var config =
                ProcessConfig.builder()
                        .command(command)
                        .timeout(processTimeout)
                        .logLevelStdout(Level.DEBUG)
                        .logLevelStderr(Level.DEBUG)
                        .build();
        return ProcessUtils.executeProcess(config);
    }

    public static byte[] setXMPMetadata(
            final Path webpInput, final Path webpOutput, final Path xmpPath) {
        try {
            final var processResult = createWebpWithMetadata(webpInput, xmpPath, webpOutput);
            if (processResult.success()) {
                return Files.readAllBytes(webpOutput);
            } else {
                return new byte[0];
            }
        } catch (final IOException e) {
            logger.error("Failed to set XMP metadata", e);
        }
        return new byte[0];
    }

    public static ProcessResult createWebpWithMetadata(
            final Path inputWebp, final Path inputXmp, final Path target) throws IOException {
        final var command =
                List.of(
                        WEBPMUX_BIN_PATH,
                        "-set",
                        "xmp",
                        inputXmp.toString(),
                        inputWebp.toString(),
                        "-o",
                        target.toString());
        final var config =
                ProcessConfig.builder()
                        .command(command)
                        .timeout(Duration.ofSeconds(5))
                        .logLevelStdout(Level.DEBUG)
                        .logLevelStderr(Level.DEBUG);
        return ProcessUtils.executeProcess(config.build());
    }

    private static Stream<String> perImageArguments(final PartialImage f) {
        return perImageArguments(f.file().toPath(), f.config());
    }

    private static Stream<String> perImageArguments(
            final Path imagePath, final WebpEncodingConfig config) {
        return Stream.of(
                "-d",
                String.valueOf(config.duration().toMillis()),
                config.quality() instanceof LosslessWebp ? "-lossless" : "-lossy",
                "-q",
                String.valueOf(config.compressionFactor()),
                "-m",
                String.valueOf(config.method().getMethod()),
                imagePath.toString());
    }
}
