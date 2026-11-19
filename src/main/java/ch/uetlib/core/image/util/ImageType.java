package ch.uetlib.core.image.util;

import java.util.Locale;

public enum ImageType {
    JPEG,
    PNG,
    GIF,
    BMP,
    WBMP;

    public static ImageType fromString(final String s) {
        return switch (s.toLowerCase(Locale.ROOT)) {
            case "jpeg", "jpg" -> JPEG;
            case "png" -> PNG;
            case "gif" -> GIF;
            case "bmp" -> BMP;
            case "wbmp" -> WBMP;
            default -> throw new IllegalArgumentException("Unknown image type: " + s);
        };
    }

    @Override
    public String toString() {
        return name().toLowerCase(Locale.ROOT);
    }

    public String imageIOType() {
        return name().toUpperCase(Locale.ROOT);
    }
}
