package ch.ubique.backend.image.webp;

public enum CompressionMethod {
    M0(0),
    M1(1),
    M2(2),
    M3(3),
    M4(4),
    M5(5),
    M6(6);

    private int method;

    CompressionMethod(int m) {
        this.method = m;
    }

    public int getMethod() {
        return method;
    }
}
