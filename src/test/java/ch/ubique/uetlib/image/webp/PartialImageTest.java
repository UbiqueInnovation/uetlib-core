package ch.ubique.uetlib.image.webp;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import org.junit.jupiter.api.Test;

class PartialImageTest {

    private static final WebpEncodingConfig DEFAULT_CONFIG = WebpEncodingConfig.builder().build();

    @Test
    void twoArgConstructorDefaultsOwnedFileToFalse() {
        final var partialImage = new PartialImage(new File("test.png"), DEFAULT_CONFIG);

        assertFalse(partialImage.ownedFile());
    }

    @Test
    void threeArgConstructorRespectsOwnedFileFlag() {
        final var owned = new PartialImage(new File("test.png"), DEFAULT_CONFIG, true);
        final var notOwned = new PartialImage(new File("test.png"), DEFAULT_CONFIG, false);

        assertTrue(owned.ownedFile());
        assertFalse(notOwned.ownedFile());
    }
}
