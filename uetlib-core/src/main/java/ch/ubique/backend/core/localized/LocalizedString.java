package ch.ubique.backend.core.localized;

import java.util.Locale;
import java.util.Map;

public final class LocalizedString extends LocalizedValue<String> {

    LocalizedString(final Map<Locale, String> values) {
        super(values);
    }

    public static LocalizedValueBuilder<String> builder() {
        return new LocalizedValueBuilder<>();
    }
}
