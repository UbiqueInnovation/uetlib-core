package ch.ubique.uetlib.image.webp;

public record LosslessWebp() implements WebpQuality {
    public static final LosslessWebp INSTANCE = new LosslessWebp();
}
