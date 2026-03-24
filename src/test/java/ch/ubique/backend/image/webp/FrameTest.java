package ch.ubique.backend.image.webp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import ch.ubique.backend.image.webp.Frame.BufferedImageFrame;
import ch.ubique.backend.image.webp.Frame.PathFrame;
import java.awt.image.BufferedImage;
import java.nio.file.Path;
import java.time.Duration;
import org.junit.jupiter.api.Test;

class FrameTest {

    @Test
    void bufferedImageFrameWithDuration() {
        final var image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        final var duration = Duration.ofMillis(500);
        final var frame = new BufferedImageFrame(image, duration);

        assertEquals(image, frame.image());
        assertEquals(duration, frame.duration());
    }

    @Test
    void bufferedImageFrameWithoutDuration() {
        final var image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        final var frame = new BufferedImageFrame(image);

        assertEquals(image, frame.image());
        assertNull(frame.duration());
    }

    @Test
    void bufferedImageFrameRejectsNullImage() {
        assertThrows(NullPointerException.class, () -> new BufferedImageFrame(null));
    }

    @Test
    void bufferedImageFrameWithDurationRejectsNullImage() {
        assertThrows(
                NullPointerException.class,
                () -> new BufferedImageFrame(null, Duration.ofMillis(100)));
    }

    @Test
    void pathFrameWithDuration() {
        final var path = Path.of("test.png");
        final var duration = Duration.ofMillis(200);
        final var frame = new PathFrame(path, duration);

        assertEquals(path, frame.path());
        assertEquals(duration, frame.duration());
    }

    @Test
    void pathFrameWithoutDuration() {
        final var path = Path.of("test.png");
        final var frame = new PathFrame(path);

        assertEquals(path, frame.path());
        assertNull(frame.duration());
    }

    @Test
    void pathFrameRejectsNullPath() {
        assertThrows(NullPointerException.class, () -> new PathFrame(null));
    }

    @Test
    void pathFrameWithDurationRejectsNullPath() {
        assertThrows(
                NullPointerException.class,
                () -> new PathFrame(null, Duration.ofMillis(100)));
    }
}
