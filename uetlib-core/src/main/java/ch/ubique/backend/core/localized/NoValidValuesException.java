package ch.ubique.backend.core.localized;

import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

public class NoValidValuesException extends RuntimeException {

    public NoValidValuesException(final Map<Locale, ?> values) {
        super(
                "No valid values specified. Values: "
                        + values.entrySet().stream()
                                .map(e -> String.format("(%s, %s)", e.getKey(), e.getValue()))
                                .collect(Collectors.joining(", ", "[", "]")));
    }
}
