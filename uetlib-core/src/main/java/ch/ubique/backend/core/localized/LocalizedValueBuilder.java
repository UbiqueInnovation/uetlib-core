package ch.ubique.backend.core.localized;

import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import org.apache.commons.lang3.LocaleUtils;
import org.apache.commons.lang3.StringUtils;

public final class LocalizedValueBuilder<T> {

    private static final Locale romansh = new Locale("rm");
    private final Map<Locale, T> values = new HashMap<>();
    private boolean keepLanguageOnly = true;

    private boolean uppercaseLanguageKeys;

    LocalizedValueBuilder() {}

    public LocalizedValueBuilder<T> en(final T value) {
        values.put(Locale.ENGLISH, value);
        return this;
    }

    public LocalizedValueBuilder<T> fr(final T value) {
        values.put(Locale.FRENCH, value);
        return this;
    }

    public LocalizedValueBuilder<T> it(final T value) {
        values.put(Locale.ITALIAN, value);
        return this;
    }

    public LocalizedValueBuilder<T> de(final T value) {
        values.put(Locale.GERMAN, value);
        return this;
    }

    /** Rätoromanisch */
    public LocalizedValueBuilder<T> romansh(final T value) {
        values.put(romansh, value);
        return this;
    }

    public LocalizedValueBuilder<T> add(final T value, final String localeCode) {
        values.put(LocaleUtils.toLocale(localeCode), value);
        return this;
    }

    public LocalizedValueBuilder<T> add(final T value, final Collection<Locale> locales) {
        locales.forEach(locale -> add(value, locale));
        return this;
    }

    public LocalizedValueBuilder<T> add(final T value, final Locale locale) {
        values.put(locale, value);
        return this;
    }

    public LocalizedValueBuilder<T> fromMap(final Map<Locale, T> values) {
        values.forEach((locale, value) -> add(value, locale));
        return this;
    }

    public LocalizedValueBuilder<T> fromStringMap(final Map<String, T> values) {
        values.forEach((locale, value) -> add(value, locale));
        return this;
    }

    /**
     * By default, only the language is kept. I.e. if you add a text for "en_US", the final map will
     * contain the entry "en" by default.
     *
     * <p>Calling this method prevents the default behavior.
     */
    public LocalizedValueBuilder<T> fullLocale() {
        keepLanguageOnly = false;
        return this;
    }

    /**
     * Call this method if you want the language keys to be upper case, e.g. 'DE' instead of 'de'.
     *
     * <p>By default, the language key is lower case, according to the ISO standard.
     */
    public LocalizedValueBuilder<T> uppercaseLanguageKeys() {
        uppercaseLanguageKeys = true;
        return this;
    }

    @NotNull
    public LocalizedValue<T> build() {
        return buildOptional().orElseThrow(() -> new NoValidValuesException(values));
    }

    @NotNull
    public Optional<LocalizedValue<T>> buildOptional() {
        final var cleanValues =
                values.entrySet().stream()
                        .filter(this::isEntryValid)
                        .collect(Collectors.toMap(this::finalKey, Map.Entry::getValue));

        if (cleanValues.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(new LocalizedValue<>(cleanValues));
    }

    private boolean isEntryValid(final Map.Entry<Locale, T> entry) {
        if (entry.getKey() == null || entry.getValue() == null) {
            return false;
        }

        // Special case string: also disallow blank strings
        if (entry.getValue() instanceof String string && StringUtils.isBlank(string)) {
            return false;
        }

        return true;
    }

    private String finalKey(final Entry<Locale, ?> entry) {
        final var locale = finalLocale(entry.getKey());
        if (uppercaseLanguageKeys) {
            return locale.toString().toUpperCase();
        }
        return locale.toString();
    }

    private Locale finalLocale(final Locale locale) {
        if (keepLanguageOnly) {
            return Locale.forLanguageTag(locale.getLanguage());
        }
        return locale;
    }

    @Nullable
    public LocalizedValue<T> buildNullable() {
        return buildOptional().orElse(null);
    }
}
