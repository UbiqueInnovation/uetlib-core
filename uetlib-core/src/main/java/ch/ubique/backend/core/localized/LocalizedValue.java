package ch.ubique.backend.core.localized;

import ch.ubique.backend.core.utils.LocaleHelper;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.mchange.v2.ser.SerializableUtils;
import com.pivovarit.function.exception.WrappedException;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import org.apache.commons.lang3.LocaleUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

public class LocalizedValue<T extends Serializable> {

    @NotEmpty private final Map<Locale, T> values;

    LocalizedValue(final Map<Locale, T> values) {
        try {
            //noinspection unchecked
            this.values = (Map<Locale, T>) SerializableUtils.deepCopy(values);
        } catch (final IOException | ClassNotFoundException e) {
            throw new WrappedException(e);
        }
    }

    @NotNull
    public static <S extends Serializable> LocalizedValueBuilder<S> builder() {
        return new LocalizedValueBuilder<>();
    }

    @NotNull
    public static LocalizedValueBuilder<String> stringBuilder() {
        return new LocalizedValueBuilder<>();
    }

    /**
     * Convenience method for single {@link Locale}. For full flexibility, {@link
     * LocalizedValue#builder()} can be used.
     */
    @NotNull
    public static <S extends Serializable> LocalizedValue<S> from(
            final S value, final Locale locale) {
        return LocalizedValue.<S>builder().add(value, locale).build();
    }

    /**
     * Convenience method if you want to set the same value for multiple {@link Locale}s. For full
     * flexibility, {@link LocalizedValue#builder()} can be used.
     */
    @NotNull
    public static <S extends Serializable> LocalizedValue<S> from(
            final S value, final Collection<Locale> locales) {
        return LocalizedValue.<S>builder().add(value, locales).build();
    }

    /**
     * Convenience method if you want to initialize the localized value from a map. The standard
     * validation will be called, i.e. the correct null handling is still enforced.
     */
    @NotNull
    public static <S extends Serializable> LocalizedValue<S> fromMap(final Map<Locale, S> values) {
        return LocalizedValue.<S>builder().fromMap(values).build();
    }

    /**
     * Convenience method if you want to initialize the localized value from a map. The standard
     * validation will be called, i.e. the correct null handling is still enforced.
     */
    @NotNull
    public static <S extends Serializable> LocalizedValue<S> fromStringMap(
            final Map<String, S> values) {
        return LocalizedValue.<S>builder().fromStringMap(values).build();
    }

    /**
     * Convenience method if you want to initialize the localized value from a map and retain the
     * full locale. The standard validation will be called, i.e. the correct null handling is still
     * enforced.
     */
    @JsonCreator
    @NotNull
    public static <S extends Serializable> LocalizedValue<S> fromStringMapFullLocale(
            final Map<String, S> values) {
        return LocalizedValue.<S>builder().fullLocale().fromStringMap(values).build();
    }

    /**
     * Convenience method if you only need English. For multiple languages, {@link
     * LocalizedValue#builder()} can be used.
     */
    @NotNull
    public static <S extends Serializable> LocalizedValue<S> en(final S value) {
        return LocalizedValue.<S>builder().en(value).build();
    }

    /**
     * Convenience method if you only need German. For multiple languages, {@link
     * LocalizedValue#builder()} can be used.
     */
    @NotNull
    public static <S extends Serializable> LocalizedValue<S> de(final S value) {
        return LocalizedValue.<S>builder().de(value).build();
    }

    /**
     * Convenience method if you only need French. For multiple languages, {@link
     * LocalizedValue#builder()} can be used.
     */
    @NotNull
    public static <S extends Serializable> LocalizedValue<S> fr(final S value) {
        return LocalizedValue.<S>builder().fr(value).build();
    }

    /**
     * Convenience method if you only need Italian. For multiple languages, {@link
     * LocalizedValue#builder()} can be used.
     */
    @NotNull
    public static <S extends Serializable> LocalizedValue<S> it(final S value) {
        return LocalizedValue.<S>builder().it(value).build();
    }

    /**
     * Convenience method if you only need Romansch (Rätoromanisch). For multiple languages, {@link
     * LocalizedValue#builder()} can be used.
     */
    @NotNull
    public static <S extends Serializable> LocalizedValue<S> romansh(final S value) {
        return LocalizedValue.<S>builder().romansh(value).build();
    }

    /**
     * Given a {@link Locale}, returns the map value with the best matching key according to the
     * lookup list generated by {@link LocaleUtils#localeLookupList(Locale)}, by first converting
     * the given String to a {@link Locale} using {@link
     * LocaleHelper#fromLanguageTagThenPosix(String)}.
     *
     * @param key to find best matching key for
     * @return T value given best matching key
     */
    @NotNull
    public T get(final String key) {
        return getOptional(key).orElseThrow();
    }

    /**
     * Given a {@link Locale}, returns the map value with the best matching key according to the
     * lookup list generated by {@link LocaleUtils#localeLookupList(Locale)}, by first converting
     * the given String to a {@link Locale} using {@link
     * LocaleHelper#fromLanguageTagThenPosix(String)}.
     *
     * @param key to find best matching key for
     * @return T value given best matching key
     */
    @Nullable
    public T getNullable(final String key) {
        return getOptional(key).orElse(null);
    }

    /**
     * Given a {@link Locale}, returns the map value with the best matching key according to the
     * lookup list generated by {@link LocaleUtils#localeLookupList(Locale)}, by first converting
     * the given String to a {@link Locale} using {@link
     * LocaleHelper#fromLanguageTagThenPosix(String)}.
     *
     * @param key to find best matching key for
     * @return T value given best matching key
     */
    @NotNull
    public Optional<T> getOptional(final String key) {
        return getOptional(LocaleHelper.fromLanguageTagThenPosix(key));
    }

    /**
     * Given a {@link Locale}, returns the map value with the best matching key according to the
     * lookup list generated by {@link LocaleUtils#localeLookupList(Locale)}.
     *
     * @param locale to find best matching key for
     * @return T value given best matching key
     */
    @NotNull
    public T get(final Locale locale) {
        return getOptional(locale).orElseThrow();
    }

    /**
     * Given a {@link Locale}, returns the map value with the best matching key according to the
     * lookup list generated by {@link LocaleUtils#localeLookupList(Locale)}.
     *
     * @param locale to find best matching key for
     * @return T value given best matching key
     */
    @Nullable
    public T getNullable(final Locale locale) {
        return getOptional(locale).orElse(null);
    }

    /**
     * Given a {@link Locale}, returns the map value with the best matching key according to the
     * lookup list generated by {@link LocaleUtils#localeLookupList(Locale)}.
     *
     * @param locale to find best matching key for
     * @return T value given best matching key
     */
    @NotNull
    public Optional<T> getOptional(final Locale locale) {
        final var matchedKey = LocaleHelper.findBestMatchLocale(locale, values.keySet());
        return Optional.ofNullable(matchedKey).map(values::get);
    }

    @NotNull
    public T en() {
        return enOptional().orElseThrow();
    }

    @NotNull
    public T de() {
        return deOptional().orElseThrow();
    }

    @NotNull
    public T fr() {
        return frOptional().orElseThrow();
    }

    @NotNull
    public T it() {
        return itOptional().orElseThrow();
    }

    @NotNull
    public T romansh() {
        return romanshOptional().orElseThrow();
    }

    @Nullable
    public T enNullable() {
        return enOptional().orElse(null);
    }

    @Nullable
    public T deNullable() {
        return deOptional().orElse(null);
    }

    @Nullable
    public T frNullable() {
        return frOptional().orElse(null);
    }

    @Nullable
    public T itNullable() {
        return itOptional().orElse(null);
    }

    @Nullable
    public T romanshNullable() {
        return romanshOptional().orElse(null);
    }

    @NotNull
    public Optional<T> enOptional() {
        return getOptional(Locale.ENGLISH);
    }

    @NotNull
    public Optional<T> deOptional() {
        return getOptional(Locale.GERMAN);
    }

    @NotNull
    public Optional<T> frOptional() {
        return getOptional(Locale.FRENCH);
    }

    @NotNull
    public Optional<T> itOptional() {
        return getOptional(Locale.ITALIAN);
    }

    @NotNull
    public Optional<T> romanshOptional() {
        return getOptional(AdditionalLocales.ROMANSH);
    }

    /**
     * Applies the keys in order to {@link #get(Locale)} and returns the first non-null value.
     * Throws {@link NoSuchElementException} if none match.
     *
     * @return T
     * @throws NoSuchElementException if none match.
     */
    @NotNull
    public T coalesce(final Locale... locales) {
        return coalesceOptional(locales).orElseThrow();
    }

    /**
     * Applies the keys in order to {@link #get(String)} and returns the first non-null value.
     * Throws {@link NoSuchElementException} if none match.
     *
     * @return T
     * @throws NoSuchElementException if none match.
     */
    @NotNull
    public T coalesce(final String... keys) {
        return coalesceOptional(keys).orElseThrow();
    }

    /**
     * Applies the keys in order to {@link #get(Locale)} and returns the first non-null value.
     * Returns any value from the internal map if none match.
     *
     * @return T
     */
    @NotNull
    public T coalesceOrGetAny(final Locale... locales) {
        return coalesceOptional(locales).orElseGet(this::getAny);
    }

    /**
     * Applies the keys in order to {@link #get(String)} and returns the first non-null value.
     * Returns any value from the internal map if none match.
     *
     * @return T
     */
    @NotNull
    public T coalesceOrGetAny(final String... keys) {
        return coalesceOptional(keys).orElseGet(this::getAny);
    }

    @NotNull
    public Optional<T> coalesceOptional(final Locale... locales) {
        return Arrays.stream(locales).map(this::getOptional).flatMap(Optional::stream).findFirst();
    }

    @NotNull
    public Optional<T> coalesceOptional(final String... keys) {
        return Arrays.stream(keys).map(this::getOptional).flatMap(Optional::stream).findFirst();
    }

    /**
     * Returns any value from the internal map.
     *
     * @return T
     * @throws NoSuchElementException if no value exists (This should be disallowed by the builder
     *     methods of {@link LocalizedValue}).
     */
    @NotNull
    public T getAny() {
        return values.values().stream().findAny().orElseThrow();
    }

    /**
     * @return a deep copy of the internal map
     */
    @NotEmpty
    public Map<Locale, T> toMapWithLocaleKeys() {
        try {
            //noinspection unchecked
            return (Map<Locale, T>) SerializableUtils.deepCopy(values);
        } catch (final IOException | ClassNotFoundException e) {
            throw new WrappedException(e);
        }
    }

    /**
     * default serialization method using IETF BCP 47 Language Tag.
     *
     * <p>{@code new Locale("de", "CH")} would be serialized to {@code "de-CH"}
     */
    @JsonValue
    @NotEmpty
    public Map<String, T> toMapWithLanguageTagKeys() {
        return values.entrySet().stream()
                .collect(
                        Collectors.toMap(
                                entry -> entry.getKey().toLanguageTag(), Map.Entry::getValue));
    }

    /**
     * alternative serialization method using Posix Locales.
     *
     * <p>{@code new Locale("de", "CH")} would be serialized to {@code "de_CH"}
     */
    @NotEmpty
    public Map<String, T> toMapWithPosixLocaleKeys() {
        return values.entrySet().stream()
                .collect(Collectors.toMap(entry -> entry.getKey().toString(), Map.Entry::getValue));
    }

    /**
     * alternative serialization method using upper case keys.
     *
     * <p>{@code new Locale("de", "CH")} would be serialized to {@code "DE-CH"}
     */
    @NotEmpty
    public Map<String, T> toMapWithUppercaseKeys() {
        return values.entrySet().stream()
                .collect(
                        Collectors.toMap(
                                entry -> entry.getKey().toLanguageTag().toUpperCase(),
                                Map.Entry::getValue));
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        final var that = (LocalizedValue<?>) o;

        return new EqualsBuilder().append(values, that.values).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(values).toHashCode();
    }

    @Override
    @NotNull
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("values", values)
                .build();
    }
}
