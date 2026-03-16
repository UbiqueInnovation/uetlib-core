package ch.ubique.backend.core.localized;

import static java.util.stream.Collectors.*;

import ch.ubique.backend.core.utils.LocaleHelper;

import org.apache.commons.lang3.StringUtils;
import org.jspecify.annotations.Nullable;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

public final class LocalizedValueBuilder<T extends Serializable> {

    private final Map<Locale, @Nullable T> values = new HashMap<>();
    private boolean keepLanguageOnly = true;

    LocalizedValueBuilder() {}

    public LocalizedValueBuilder<T> en(final @Nullable T value) {
        values.put(Locale.ENGLISH, value);
        return this;
    }

    public LocalizedValueBuilder<T> fr(final @Nullable T value) {
        values.put(Locale.FRENCH, value);
        return this;
    }

    public LocalizedValueBuilder<T> it(final @Nullable T value) {
        values.put(Locale.ITALIAN, value);
        return this;
    }

    public LocalizedValueBuilder<T> de(final @Nullable T value) {
        values.put(Locale.GERMAN, value);
        return this;
    }

    /** Rätoromanisch */
    public LocalizedValueBuilder<T> romansh(final @Nullable T value) {
        values.put(AdditionalLocales.ROMANSH, value);
        return this;
    }

    public LocalizedValueBuilder<T> add(final @Nullable T value, final String localeCode) {
        values.put(LocaleHelper.fromLanguageTagThenPosix(localeCode), value);
        return this;
    }

    public LocalizedValueBuilder<T> add(final @Nullable T value, final Collection<Locale> locales) {
        locales.forEach(locale -> add(value, locale));
        return this;
    }

    public LocalizedValueBuilder<T> add(final @Nullable T value, final Locale locale) {
        values.put(locale, value);
        return this;
    }

    public LocalizedValueBuilder<T> fromMap(final Map<Locale, ? extends @Nullable T> values) {
        values.forEach((locale, value) -> add(value, locale));
        return this;
    }

    public LocalizedValueBuilder<T> fromStringMap(final Map<String, ? extends @Nullable T> values) {
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
     * {@link Locale}s are serialized using {@link Locale#toLanguageTag()} which produces <a
     * href="https://en.wikipedia.org/wiki/IETF_language_tag">BCP 47 Language Tags</a>.
     */
    public LocalizedValue<T> build() {
        return buildOptional().orElseThrow(() -> new NoValidValuesException(values));
    }

    public Optional<LocalizedValue<T>> buildOptional() {
        final var cleanValues =
                values.entrySet().stream()
                        .filter(this::isEntryValid)
                        .collect(toMap(this::finalLocale, Entry::getValue));

        if (cleanValues.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(new LocalizedValue<>(cleanValues));
    }

    private boolean isEntryValid(final Map.Entry<@Nullable Locale, @Nullable T> entry) {
        if (entry.getKey() == null || entry.getValue() == null) {
            return false;
        }

        // Special case string: also disallow blank strings
        return !(entry.getValue() instanceof final String string && StringUtils.isBlank(string));
    }

    private Locale finalLocale(final Entry<Locale, ?> entry) {
        final var locale = entry.getKey();
        return keepLanguageOnly ? Locale.of(locale.getLanguage()) : locale;
    }

    @Nullable
    public LocalizedValue<T> buildNullable() {
        return buildOptional().orElse(null);
    }
}
