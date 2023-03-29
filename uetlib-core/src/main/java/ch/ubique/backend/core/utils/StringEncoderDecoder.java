package ch.ubique.backend.core.utils;

public interface StringEncoderDecoder<T> {

    T decode(final String value);

    String encode(final T value);
}
