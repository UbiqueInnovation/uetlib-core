package ch.ubique.backend.image.webp;

import org.jspecify.annotations.Nullable;

import java.awt.image.BufferedImage;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Objects;

/**
 * Represents a single frame in a WebP animation. Each frame can optionally specify its own
 * duration, overriding the default from {@link WebpEncodingConfig}.
 */
public sealed interface Frame permits Frame.BufferedImageFrame, Frame.PathFrame {

    /** Returns the per-frame duration, or {@code null} to use the config default. */
    @Nullable Duration duration();

    record BufferedImageFrame(BufferedImage image, @Nullable Duration duration) implements Frame {
        public BufferedImageFrame {
            Objects.requireNonNull(image, "image must not be null");
        }

        public BufferedImageFrame(final BufferedImage image) {
            this(image, null);
        }
    }

    record PathFrame(Path path, @Nullable Duration duration) implements Frame {
        public PathFrame {
            Objects.requireNonNull(path, "path must not be null");
        }

        public PathFrame(final Path path) {
            this(path, null);
        }
    }
}
