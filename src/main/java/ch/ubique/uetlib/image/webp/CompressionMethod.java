package ch.ubique.uetlib.image.webp;

public enum CompressionMethod {
    M0(0),
    M1(1),
    M2(2),
    M3(3),
    M4(4),
    M5(5),
    M6(6);

    private final int method;

    CompressionMethod(final int m) {
        this.method = m;
    }

    public int getMethod() {
        return method;
    }
}
