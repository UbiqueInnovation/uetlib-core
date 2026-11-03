package ch.ubique.uetlib.core.image.webp;

public record LosslessWebp() implements WebpQuality {
    public static final LosslessWebp INSTANCE = new LosslessWebp();
}
