package ch.ubique.backend.image.webp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import ch.ubique.backend.image.webp.Frame.BufferedImageFrame;
import ch.ubique.backend.image.webp.Frame.PathFrame;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

class WebpWriterTest {

    @BeforeAll
    static void requireWebpTools() {
        assumeTrue(
                ImageWebpLibraryWrapper.isImg2WebPAvailable(),
                "img2webp binary not available — skipping tests");
        assumeTrue(
                ImageWebpLibraryWrapper.isWebPMuxAvailable(),
                "webpmux binary not available — skipping tests");
    }

    @Test
    void perFrameDurationsAreWrittenToWebp() throws IOException {
        try (final var writer = new WebpWriter()) {
            final var frames =
                    List.<Frame>of(
                            new BufferedImageFrame(
                                    createTestImage(Color.RED), Duration.ofMillis(100)),
                            new BufferedImageFrame(
                                    createTestImage(Color.GREEN), Duration.ofMillis(300)),
                            new BufferedImageFrame(
                                    createTestImage(Color.BLUE), Duration.ofMillis(750)));

            final var webp = writer.encodeFrames(frames);
            final var durations = readFrameDurations(webp);

            assertEquals(List.of(100, 300, 750), durations);
        }
    }

    @Test
    void framesWithoutDurationUseConfigDefault() throws IOException {
        final var defaultDuration = Duration.ofMillis(200);
        final var config = WebpEncodingConfig.builder().duration(defaultDuration).build();

        try (final var writer = new WebpWriter(config)) {
            final var frames =
                    List.<Frame>of(
                            new BufferedImageFrame(createTestImage(Color.RED)),
                            new BufferedImageFrame(createTestImage(Color.BLUE)));

            final var webp = writer.encodeFrames(frames);
            final var durations = readFrameDurations(webp);

            assertEquals(List.of(200, 200), durations);
        }
    }

    @Test
    void mixedExplicitAndDefaultDurations() throws IOException {
        final var defaultDuration = Duration.ofMillis(1000);
        final var config = WebpEncodingConfig.builder().duration(defaultDuration).build();

        try (final var writer = new WebpWriter(config)) {
            final var frames =
                    List.<Frame>of(
                            new BufferedImageFrame(
                                    createTestImage(Color.RED), Duration.ofMillis(150)),
                            new BufferedImageFrame(createTestImage(Color.GREEN)),
                            new BufferedImageFrame(
                                    createTestImage(Color.BLUE), Duration.ofMillis(500)));

            final var webp = writer.encodeFrames(frames);
            final var durations = readFrameDurations(webp);

            assertEquals(List.of(150, 1000, 500), durations);
        }
    }

    @Test
    void pathFrameDurationsAreWrittenToWebp() throws IOException {
        final var tmpDir = Files.createTempDirectory("webp-test-");
        try {
            final var png1 = writeTempPng(tmpDir, "frame1.png", Color.RED);
            final var png2 = writeTempPng(tmpDir, "frame2.png", Color.BLUE);

            try (final var writer = new WebpWriter()) {
                final var frames =
                        List.<Frame>of(
                                new PathFrame(png1, Duration.ofMillis(200)),
                                new PathFrame(png2, Duration.ofMillis(800)));

                final var webp = writer.encodeFrames(frames);
                final var durations = readFrameDurations(webp);

                assertEquals(List.of(200, 800), durations);
            }
        } finally {
            deleteRecursively(tmpDir);
        }
    }

    @Test
    void mixedFrameTypesDurationsAreWrittenToWebp() throws IOException {
        final var tmpDir = Files.createTempDirectory("webp-test-");
        try {
            final var png = writeTempPng(tmpDir, "frame.png", Color.GREEN);

            try (final var writer = new WebpWriter()) {
                final var frames =
                        List.<Frame>of(
                                new BufferedImageFrame(
                                        createTestImage(Color.RED), Duration.ofMillis(100)),
                                new PathFrame(png, Duration.ofMillis(400)),
                                new BufferedImageFrame(
                                        createTestImage(Color.BLUE), Duration.ofMillis(900)));

                final var webp = writer.encodeFrames(frames);
                final var durations = readFrameDurations(webp);

                assertEquals(List.of(100, 400, 900), durations);
            }
        } finally {
            deleteRecursively(tmpDir);
        }
    }

    /**
     * Reads the per-frame durations from a WebP animation file using {@code webpmux -info}.
     *
     * <p>The output contains one line per frame with columns including duration in milliseconds:
     *
     * <pre>
     *   1:    10    10    no        0        0      100       none    no         34    lossless
     * </pre>
     */
    private static List<Integer> readFrameDurations(final Path webpFile) throws IOException {
        final var process =
                new ProcessBuilder("webpmux", "-info", webpFile.toString())
                        .redirectErrorStream(true)
                        .start();
        final var output = new String(process.getInputStream().readAllBytes());

        final var durations = new ArrayList<Integer>();
        final var frameLinePattern =
                Pattern.compile("^\\s*\\d+:\\s+\\d+\\s+\\d+\\s+\\S+\\s+\\d+\\s+\\d+\\s+(\\d+)");
        for (final var line : output.lines().toList()) {
            final var matcher = frameLinePattern.matcher(line);
            if (matcher.find()) {
                durations.add(Integer.parseInt(matcher.group(1)));
            }
        }
        return durations;
    }

    private static BufferedImage createTestImage(final Color color) {
        final var image = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
        final var graphics = image.createGraphics();
        graphics.setColor(color);
        graphics.fillRect(0, 0, 10, 10);
        graphics.dispose();
        return image;
    }

    private static Path writeTempPng(final Path dir, final String name, final Color color)
            throws IOException {
        final var path = dir.resolve(name);
        ImageIO.write(createTestImage(color), "png", path.toFile());
        return path;
    }

    private static void deleteRecursively(final Path dir) throws IOException {
        Files.walk(dir)
                .sorted(java.util.Comparator.reverseOrder())
                .forEach(p -> p.toFile().delete());
    }
}
