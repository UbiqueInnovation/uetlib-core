package ch.ubique.backend.core.localized;

import static ch.ubique.backend.core.utils.OptionalUtils.ofNullable;

import static java.util.Map.Entry;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.*;

import ch.ubique.backend.core.localized.jackson.LocalizedValueDeserializer;
import ch.ubique.backend.core.localized.jackson.LocalizedValueSerializer;
import ch.ubique.backend.core.utils.CollectionUtils;
import ch.ubique.backend.core.utils.LocaleHelper;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;

import jakarta.validation.constraints.NotEmpty;

import org.apache.commons.lang3.LocaleUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.Nullable;

import tools.jackson.databind.annotation.JsonDeserialize;
import tools.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;
import java.util.*;
import java.util.function.Function;

/**
 * A LocalizedValue object does always contain at least one entry in its internal map. All
 * methods/constructors ensure that does is always the case. The key of the internal map is always a
 * legal {@link Locale}.
 *
 * @param <T> the value type of the internal map
 */
@JsonSerialize(using = LocalizedValueSerializer.class)
@JsonDeserialize(using = LocalizedValueDeserializer.class)
public class LocalizedValue<T extends Serializable> {

    @NotEmpty private final Map<Locale, T> values;

    LocalizedValue(final Map<Locale, T> values) {
        this.values = CollectionUtils.deepCopy(values);
    }

    public static <S extends Serializable> LocalizedValueBuilder<S> builder() {
        return new LocalizedValueBuilder<>();
    }

    public static LocalizedValueBuilder<String> stringBuilder() {
        return new LocalizedValueBuilder<>();
    }

    /**
     * Convenience method for single {@link Locale}. For full flexibility, {@link
     * LocalizedValue#builder()} can be used.
     */
    public static <S extends Serializable> LocalizedValue<S> from(
            final S value, final Locale locale) {
        return LocalizedValue.<S>builder().add(value, locale).build();
    }

    /**
     * Convenience method if you want to set the same value for multiple {@link Locale}s. For full
     * flexibility, {@link LocalizedValue#builder()} can be used.
     */
    public static <S extends Serializable> LocalizedValue<S> from(
            final S value, final Collection<Locale> locales) {
        return LocalizedValue.<S>builder().add(value, locales).build();
    }

    /**
     * Convenience method if you want to initialize the localized value from a map. The standard
     * validation will be called, i.e. the correct null handling is still enforced.
     */
    public static <S extends Serializable> LocalizedValue<S> fromMap(final Map<Locale, S> values) {
        return LocalizedValue.<S>builder().fromMap(values).build();
    }

    /**
     * Convenience method if you want to initialize the localized value from a map. The standard
     * validation will be called, i.e. the correct null handling is still enforced.
     */
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
    public static <S extends Serializable> LocalizedValue<S> fromStringMapFullLocale(
            final Map<String, S> values) {
        return LocalizedValue.<S>builder().fullLocale().fromStringMap(values).build();
    }

    /**
     * Convenience method if you only need English. For multiple languages, {@link
     * LocalizedValue#builder()} can be used.
     */
    public static <S extends Serializable> LocalizedValue<S> en(final S value) {
        return LocalizedValue.<S>builder().en(value).build();
    }

    /**
     * Convenience method if you only need German. For multiple languages, {@link
     * LocalizedValue#builder()} can be used.
     */
    public static <S extends Serializable> LocalizedValue<S> de(final S value) {
        return LocalizedValue.<S>builder().de(value).build();
    }

    /**
     * Convenience method if you only need French. For multiple languages, {@link
     * LocalizedValue#builder()} can be used.
     */
    public static <S extends Serializable> LocalizedValue<S> fr(final S value) {
        return LocalizedValue.<S>builder().fr(value).build();
    }

    /**
     * Convenience method if you only need Italian. For multiple languages, {@link
     * LocalizedValue#builder()} can be used.
     */
    public static <S extends Serializable> LocalizedValue<S> it(final S value) {
        return LocalizedValue.<S>builder().it(value).build();
    }

    /**
     * Convenience method if you only need Romansch (Rätoromanisch). For multiple languages, {@link
     * LocalizedValue#builder()} can be used.
     */
    public static <S extends Serializable> LocalizedValue<S> romansh(final S value) {
        return LocalizedValue.<S>builder().romansh(value).build();
    }

    /**
     * Return a new LocalizedValue with the merged set of entries, where the later argument values
     * override the previous.
     */
    public static <S extends Serializable> LocalizedValue<S> mergeAll(
            final Collection<LocalizedValue<S>> mergees) {
        final var mergedValues =
                mergees.stream()
                        .map(mergee -> mergee.values)
                        .reduce(
                                new HashMap<>(),
                                (left, right) -> {
                                    left.putAll(right);
                                    return left;
                                });
        return LocalizedValue.<S>builder()
                .fromMap(deepCopyValues(mergedValues))
                .fullLocale()
                .build();
    }

    private static <S extends Serializable> Map<Locale, S> deepCopyValues(
            final Map<Locale, S> values) {
        return CollectionUtils.deepCopy(values);
    }

    /**
     * Return a new LocalizedValue with the merged set of entries, where the argument values
     * override the current object. Shortcut for {@link #mergeAll} for {@code n == 2}.
     */
    public LocalizedValue<T> mergeWith(final LocalizedValue<T> mergee) {
        return mergeAll(List.of(this, mergee));
    }

    /**
     * Given a {@link Locale}, returns the map value with the best matching key according to the
     * lookup list generated by {@link LocaleUtils#localeLookupList(Locale)}, by first converting
     * the given String to a {@link Locale} using {@link
     * LocaleHelper#fromLanguageTagThenPosix(String)}.
     *
     * @param key to find best matching key for
     * @return T value given best matching key
     * @throws NoSuchElementException if the given key does not match any entry
     */
    public T get(final @Nullable String key) {
        return getOptional(key).orElseThrow();
    }

    /**
     * Given a {@link Locale}, returns the map value with the best matching key according to the
     * lookup list generated by {@link LocaleUtils#localeLookupList(Locale)}, by first converting
     * the given String to a {@link Locale} using {@link
     * LocaleHelper#fromLanguageTagThenPosix(String)}.
     *
     * @param key to find best matching key for
     * @return T value given best matching key or null if the given key does match any entry
     */
    @Nullable
    public T getNullable(final @Nullable String key) {
        return getOptional(key).orElse(null);
    }

    /**
     * Given a {@link Locale}, returns the map value with the best matching key according to the
     * lookup list generated by {@link LocaleUtils#localeLookupList(Locale)}, by first converting
     * the given String to a {@link Locale} using {@link
     * LocaleHelper#fromLanguageTagThenPosix(String)}.
     *
     * @param key to find best matching key for
     * @return T value given best matching key or {@link Optional#empty()} if the given key does
     *     match any entry
     */
    public Optional<T> getOptional(final @Nullable String key) {
        return getOptional(LocaleHelper.fromLanguageTagThenPosix(key));
    }

    /**
     * Given a {@link Locale}, returns the map value with the best matching key according to the
     * lookup list generated by {@link LocaleUtils#localeLookupList(Locale)}.
     *
     * @param locale to find best matching key for
     * @return T value given best matching key
     * @throws NoSuchElementException if the given key does not match any key any entry
     */
    public T get(final @Nullable Locale locale) {
        return getOptional(locale).orElseThrow();
    }

    /**
     * Given a {@link Locale}, returns the map value with the best matching key according to the
     * lookup list generated by {@link LocaleUtils#localeLookupList(Locale)}.
     *
     * @param locale to find best matching key for
     * @return T value given best matching key or null if the given key does match any entry
     */
    @Nullable
    public T getNullable(final @Nullable Locale locale) {
        return getOptional(locale).orElse(null);
    }

    /**
     * Given a {@link Locale}, returns the map value with the best matching key according to the
     * lookup list generated by {@link LocaleUtils#localeLookupList(Locale)}.
     *
     * @param locale to find best matching key for
     * @return T value given best matching key or {@link Optional#empty()} if the given key does
     *     match any entry
     */
    public Optional<T> getOptional(final @Nullable Locale locale) {
        final var matchedKey = LocaleHelper.findBestMatchLocale(locale, values.keySet());
        // return Optional.ofNullable(matchedKey).map(values::get); currently is shown to be incorrect by NullAway
        if (matchedKey == null) {
            return Optional.empty();
        }
        return ofNullable(values.get(matchedKey));
    }

    /**
     * @return T value best matching en
     * @throws NoSuchElementException if there is no entry matching en
     */
    public T en() {
        return enOptional().orElseThrow();
    }

    /**
     * @return T value best matching de
     * @throws NoSuchElementException if there is no entry matching de
     */
    public T de() {
        return deOptional().orElseThrow();
    }

    /**
     * @return T value best matching fr
     * @throws NoSuchElementException if there is no entry matching fr
     */
    public T fr() {
        return frOptional().orElseThrow();
    }

    /**
     * @return T value best matching it
     * @throws NoSuchElementException if there is no entry matching it
     */
    public T it() {
        return itOptional().orElseThrow();
    }

    /**
     * @return T value best matching romansh
     * @throws NoSuchElementException if there is no entry matching romansh
     */
    public T romansh() {
        return romanshOptional().orElseThrow();
    }

    /**
     * @return T value best matching en or null
     */
    @Nullable
    public T enNullable() {
        return enOptional().orElse(null);
    }

    /**
     * @return T value best matching de or null
     */
    @Nullable
    public T deNullable() {
        return deOptional().orElse(null);
    }

    /**
     * @return T value best matching fr or null
     */
    @Nullable
    public T frNullable() {
        return frOptional().orElse(null);
    }

    /**
     * @return T value best matching it or null
     */
    @Nullable
    public T itNullable() {
        return itOptional().orElse(null);
    }

    /**
     * @return T value best matching romansh or null
     */
    @Nullable
    public T romanshNullable() {
        return romanshOptional().orElse(null);
    }

    /**
     * @return Optional value best matching en or {@link Optional#empty()}
     */
    public Optional<T> enOptional() {
        return getOptional(Locale.ENGLISH);
    }

    /**
     * @return Optional value best matching de or {@link Optional#empty()}
     */
    public Optional<T> deOptional() {
        return getOptional(Locale.GERMAN);
    }

    /**
     * @return Optional value best matching fr or {@link Optional#empty()}
     */
    public Optional<T> frOptional() {
        return getOptional(Locale.FRENCH);
    }

    /**
     * @return Optional value best matching it or {@link Optional#empty()}
     */
    public Optional<T> itOptional() {
        return getOptional(Locale.ITALIAN);
    }

    /**
     * @return Optional value best matching romansh or {@link Optional#empty()}
     */
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
    public T coalesce(final String... keys) {
        return coalesceOptional(keys).orElseThrow();
    }

    /**
     * Applies the keys in order to {@link #get(Locale)} and returns the first non-null value.
     * Returns any value from the internal map if none match.
     *
     * @return T
     */
    public T coalesceOrGetAny(final Locale... locales) {
        return coalesceOptional(locales).orElseGet(this::getAny);
    }

    /**
     * Applies the keys in order to {@link #get(String)} and returns the first non-null value.
     * Returns any value from the internal map if none match.
     *
     * @return T
     */
    public T coalesceOrGetAny(final String... keys) {
        return coalesceOptional(keys).orElseGet(this::getAny);
    }

    /**
     * Applies the keys in order to {@link #get(String)} and returns the first non-null value.
     * Returns any value from the internal map if none match.
     *
     * @return Optional
     */
    public Optional<T> coalesceOptional(final Locale... locales) {
        return Arrays.stream(locales).map(this::getOptional).flatMap(Optional::stream).findFirst();
    }

    /**
     * Applies the keys in order to {@link #get(String)} and returns the first non-null value.
     * Returns any value from the internal map if none match.
     *
     * @return Optional
     */
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
    @JsonIgnore
    public T getAny() {
        return values.values().stream().findAny().orElseThrow();
    }

    /**
     * @return a deep copy of the internal map
     */
    @NotEmpty
    public Map<Locale, T> toMapWithLocaleKeys() {
        return deepCopyValues(values);
    }

    /**
     * default serialization method using IETF BCP 47 Language Tag.
     *
     * <ul>
     *   <li>{@code Locale.of("en")} would be serialized to {@code "en"}
     *   <li>{@code Locale.of("de", "CH")} would be serialized to {@code "de-CH"}
     * </ul>
     */
    @JsonValue
    @NotEmpty
    public Map<String, T> toMapWithLanguageTagKeys() {
        return values.entrySet().stream()
                .collect(toMap(entry -> entry.getKey().toLanguageTag(), Entry::getValue));
    }

    /**
     * Transform values using the provided mapper function.
     *
     * @return A new LocalizedValue<V> with transformed values and the same keys.
     * @throws NullPointerException if the mapper function is null or returns null for any input.
     */
    @Contract(value = "_ -> new", pure = true)
    public <V extends Serializable> LocalizedValue<V> mapValues(final Function<T, V> valueMapper) {
        return new LocalizedValue<>(
                values.entrySet().stream()
                        .collect(
                                toMap(
                                        Entry::getKey,
                                        entry ->
                                                requireNonNull(
                                                        valueMapper.apply(entry.getValue()),
                                                        "Value mapper cannot return null for input "
                                                                + entry.getValue()))));
    }

    @Override
    public boolean equals(final @Nullable Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final var that = (LocalizedValue<?>) o;

        return new EqualsBuilder().append(values, that.values).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(values).toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("values", values)
                .build();
    }
}
