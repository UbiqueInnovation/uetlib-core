package ch.ubique.backend.image.webp;

import java.io.File;

public record PartialImage(File file, WebpEncodingConfig config) {}
