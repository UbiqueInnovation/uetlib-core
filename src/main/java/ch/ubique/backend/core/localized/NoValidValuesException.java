package ch.ubique.backend.core.localized;

import org.jspecify.annotations.Nullable;

import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

public class NoValidValuesException extends RuntimeException {

    public <T> NoValidValuesException(final Map<Locale, @Nullable T> values) {
        super(
                "No valid values specified. Values: "
                        + values.entrySet().stream()
                                .map(e -> String.format("(%s, %s)", e.getKey(), e.getValue()))
                                .collect(Collectors.joining(", ", "[", "]")));
    }
}
