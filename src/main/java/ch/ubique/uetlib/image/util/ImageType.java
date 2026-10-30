package ch.ubique.uetlib.image.util;

public enum ImageType {
    JPEG,
    PNG,
    GIF,
    BMP,
    WBMP;

    public static ImageType fromString(final String s) {
        return switch (s.toLowerCase()) {
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
        return name().toLowerCase();
    }

    public String imageIOType() {
        return name().toUpperCase();
    }
}
