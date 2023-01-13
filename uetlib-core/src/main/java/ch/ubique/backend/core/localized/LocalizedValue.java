package ch.ubique.backend.core.localized;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;
import com.mchange.v2.ser.SerializableUtils;
import com.pivovarit.function.exception.WrappedException;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Locale;
import java.util.Map;

public class LocalizedValue<T> {

    @JsonValue private final Map<String, T> values;

    LocalizedValue(final Map<String, T> values) {
        this.values = Collections.unmodifiableMap(values);
    }

    public static <S> LocalizedValueBuilder<S> builder() {
        return new LocalizedValueBuilder<>();
    }

    public static LocalizedValueBuilder<String> stringBuilder() {
        return new LocalizedValueBuilder<>();
    }

    /**
     * Convenience method for single {@link Locale}. For full flexibility, {@link
     * LocalizedValue#builder()} can be used.
     */
    public static <S> LocalizedValue<S> from(final S value, final Locale locale) {
        return LocalizedValue.<S>builder().add(value, locale).build();
    }

    /**
     * Convenience method if you want to set the same value for multiple {@link Locale}s. For full
     * flexibility, {@link LocalizedValue#builder()} can be used.
     */
    public static <S> LocalizedValue<S> from(final S value, final Collection<Locale> locales) {
        return LocalizedValue.<S>builder().add(value, locales).build();
    }

    /**
     * Convenience method if you want to initialize the localized value from a map. The standard
     * validation will be called, i.e. the correct null handling is still enforced.
     */
    public static <S> LocalizedValue<S> fromMap(final Map<Locale, S> values) {
        return LocalizedValue.<S>builder().fromMap(values).build();
    }

    /**
     * Convenience method if you want to initialize the localized value from a map. The standard
     * validation will be called, i.e. the correct null handling is still enforced.
     */
    public static <S> LocalizedValue<S> fromStringMap(final Map<String, S> values) {
        return LocalizedValue.<S>builder().fromStringMap(values).build();
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

    /**
     * @return a deep copy of the internal map
     */
    @JsonIgnore
    public Map<String, T> values() {
        try {
            //noinspection unchecked
            return (Map<String, T>) SerializableUtils.deepCopy(values);
        } catch (final IOException | ClassNotFoundException e) {
            throw new WrappedException(e);
        }
    }
}
