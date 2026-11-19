package ch.uetlib.core.image.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Locale;

class ImageTypeTest {

    private Locale defaultLocale;

    @BeforeEach
    void useTurkishDefaultLocale() {
        defaultLocale = Locale.getDefault();
        // Turkish lowercases "I" to a dotless "ı" and uppercases "i" to a dotted "İ".
        Locale.setDefault(Locale.forLanguageTag("tr-TR"));
    }

    @AfterEach
    void restoreDefaultLocale() {
        Locale.setDefault(defaultLocale);
    }

    @Test
    void fromStringIgnoresTheDefaultLocale() {
        assertEquals(ImageType.GIF, ImageType.fromString("GIF"));
        assertEquals(ImageType.JPEG, ImageType.fromString("JPG"));
        assertEquals(ImageType.WBMP, ImageType.fromString("wbmp"));
        assertEquals(ImageType.JPEG, ImageType.fromString("jpeg"));
        assertEquals(ImageType.PNG, ImageType.fromString("PNG"));
        assertEquals(ImageType.BMP, ImageType.fromString("Bmp"));
        assertThrows(IllegalArgumentException.class, () -> ImageType.fromString("tiff"));
    }

    @Test
    void toStringAndImageIoTypeIgnoreTheDefaultLocale() {
        assertEquals("gif", ImageType.GIF.toString());
        assertEquals("GIF", ImageType.GIF.imageIOType());
    }
}
