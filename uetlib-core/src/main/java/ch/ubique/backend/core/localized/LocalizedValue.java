package ch.ubique.backend.core.localized;

import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Collection;
import java.util.Collections;
import java.util.Locale;
import java.util.Map;

public sealed class LocalizedValue<T> permits LocalizedString {

    @JsonValue private final Map<Locale, T> values;

    LocalizedValue(final Map<Locale, T> values) {
        this.values = Collections.unmodifiableMap(values);
    }

    /**
     * Convenience method for single {@link Locale}. For full flexibility, {@link
     * LocalizedValue#builder()} can be used.
     */
    public static <S> LocalizedValue<S> from(final S value, final Locale locale) {
        return LocalizedValue.<S>builder().add(value, locale).build();
    }

    public static <S> LocalizedValueBuilder<S> builder() {
        return new LocalizedValueBuilder<>();
    }

    /**
     * Convenience method if you want to set the same value for multiple {@link Locale}s. For full
     * flexibility, {@link LocalizedValue#builder()} can be used.
     */
    public static <S> LocalizedValue<S> from(final S value, final Collection<Locale> locales) {
        return LocalizedValue.<S>builder().add(value, locales).build();
    }

    /**
     * Convenience method if you only need English. For multiple languages, {@link
     * LocalizedValue#builder()} can be used.
     */
    public static <S> LocalizedValue<S> en(final S value) {
        return LocalizedValue.<S>builder().en(value).build();
    }

    /**
     * Convenience method if you only need German. For multiple languages, {@link
     * LocalizedValue#builder()} can be used.
     */
    public static <S> LocalizedValue<S> de(final S value) {
        return LocalizedValue.<S>builder().de(value).build();
    }

    /**
     * Convenience method if you only need French. For multiple languages, {@link
     * LocalizedValue#builder()} can be used.
     */
    public static <S> LocalizedValue<S> fr(final S value) {
        return LocalizedValue.<S>builder().fr(value).build();
    }

    /**
     * Convenience method if you only need Italian. For multiple languages, {@link
     * LocalizedValue#builder()} can be used.
     */
    public static <S> LocalizedValue<S> it(final S value) {
        return LocalizedValue.<S>builder().it(value).build();
    }

    /**
     * Convenience method if you only need Romansch (Rätoromanisch). For multiple languages, {@link
     * LocalizedValue#builder()} can be used.
     */
    public static <S> LocalizedValue<S> romansh(final S value) {
        return LocalizedValue.<S>builder().romansh(value).build();
    }

    public Map<Locale, T> values() {
        return values;
    }
}
