package ch.ubique.backend.core.utils;

import java.util.stream.IntStream;
import java.util.stream.Stream;

public final class StringUtils {

    private StringUtils() {}

    /**
     * Returns a {@link Stream#empty()} if {@code string == null || size <= 0 || size >
     * string.length()}
     */
    public static Stream<String> substringWindows(final String string, final int size) {
        if (string == null || size <= 0 || size > string.length()) {
            return Stream.empty();
        }
        return IntStream.range(0, string.length() - size + 1)
                .mapToObj(start -> string.substring(start, start + size));
    }
}
