package ch.ubique.backend.image.webp;

import static ch.ubique.backend.image.webp.ImageWebpLibraryWrapper.convertListToWebP;

import ch.ubique.backend.core.executor.VirtualThreadUtils;

import org.apache.commons.io.FileUtils;

import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import javax.imageio.ImageIO;

@SuppressWarnings("unused")
public class WebpWriter implements AutoCloseable {

    private final Path workingDirectory;
    private final WebpEncodingConfig config;

    public WebpWriter() throws IOException {
        this(WebpEncodingConfig.builder().build());
    }

    public WebpWriter(final WebpEncodingConfig config) throws IOException {
        this.config = config;
        workingDirectory = Files.createTempDirectory("webp-writer-");
    }

    public Path encodeImages(final List<BufferedImage> frames, final String xmpMetadata)
            throws IOException {
        final var webp = encodeImages(frames);
        final var metadata = createMetadataFile(xmpMetadata);
        try {
            return createWebpWithMetadata(webp, metadata);
        } finally {
            deleteFile(webp);
            deleteFile(metadata);
        }
    }

    public Path encodeImages(final List<BufferedImage> frames) throws IOException {
        final var framePaths = VirtualThreadUtils.throwingMapAll(this::write, frames);
        try {
            return encode(framePaths);
        } finally {
            framePaths.forEach(this::deleteFile);
        }
    }

    public Path encode(final List<Path> framePaths) throws IOException {
        final var webpPath = createTempFile("webp");
        final var processResult = convertListToWebP(framePaths, config, webpPath);
        processResult.throwIfNotSuccessful();
        return webpPath;
    }

    public Path createMetadataFile(final String xmpMetadata) throws IOException {
        final var metadataFile = createTempFile("xmp");
        try (final var out = new FileOutputStream(metadataFile.toFile())) {
            out.write(xmpMetadata.trim().replace("\n", "").replace("\r", "").getBytes());
        }
        return metadataFile;
    }

    public Path createWebpWithMetadata(final Path webp, final Path metadata) throws IOException {
        final var webpWithMetadata = createTempFile("webp");
        final var processResult =
                ImageWebpLibraryWrapper.createWebpWithMetadata(webp, metadata, webpWithMetadata);
        processResult.throwIfNotSuccessful();
        return webpWithMetadata;
    }

    private Path write(final BufferedImage image) throws IOException {
        final var imagePath = createTempFile("png");
        try (final var out = new FileOutputStream(imagePath.toFile())) {
            ImageIO.write(image, "png", out);
            return imagePath;
        }
    }

    private Path createTempFile(final String extension) throws IOException {
        return Files.createTempFile(workingDirectory, null, "." + extension);
    }

    private void deleteFile(final Path path) {
        FileUtils.deleteQuietly(path.toFile());
    }

    @Override
    public void close() throws IOException {
        FileUtils.deleteDirectory(workingDirectory.toFile());
    }
}
