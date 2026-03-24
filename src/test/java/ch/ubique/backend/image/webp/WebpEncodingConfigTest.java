package ch.ubique.backend.image.webp;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Duration;
import org.junit.jupiter.api.Test;

class WebpEncodingConfigTest {

    @Test
    void withDurationReturnsCopyWithNewDuration() {
        final var original = WebpEncodingConfig.builder()
                .duration(Duration.ofSeconds(1))
                .quality(LosslessWebp.INSTANCE)
                .method(CompressionMethod.M4)
                .compressionFactor(75)
                .processTimeout(Duration.ofSeconds(30))
                .build();

        final var newDuration = Duration.ofMillis(500);
        final var modified = original.withDuration(newDuration);

        assertEquals(newDuration, modified.duration());
        assertEquals(original.quality(), modified.quality());
        assertEquals(original.method(), modified.method());
        assertEquals(original.compressionFactor(), modified.compressionFactor());
        assertEquals(original.processTimeout(), modified.processTimeout());
    }

    @Test
    void withDurationDoesNotMutateOriginal() {
        final var originalDuration = Duration.ofSeconds(2);
        final var original = WebpEncodingConfig.builder()
                .duration(originalDuration)
                .build();

        original.withDuration(Duration.ofMillis(100));

        assertEquals(originalDuration, original.duration());
    }
}
