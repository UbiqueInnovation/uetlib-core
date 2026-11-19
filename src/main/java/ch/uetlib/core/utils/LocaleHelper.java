package ch.uetlib.core.utils;

import static java.util.Map.entry;
import static java.util.stream.Collectors.*;

import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.lang3.LocaleUtils;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.Nullable;

import java.util.*;
import java.util.Map.Entry;

public final class LocaleHelper {

    static final Locale EMPTY = Locale.forLanguageTag("");

    private LocaleHelper() {}

    /**
     * Try to create the locale object first using {@link Locale#forLanguageTag(String)} and then
     * using {@link LocaleUtils#toLocale(Locale)}.
     */
    @Contract("null -> null; !null -> new")
    @Nullable
    public static Locale fromLanguageTagThenPosix(final @Nullable String localeCode) {
        if (null == localeCode) {
            return null;
        }
        return fromNonNullLanguageTagThenPosix(localeCode);
    }

    public static Locale fromNonNullLanguageTagThenPosix(final String localeCode) {
        final var byLanguageTag = Locale.forLanguageTag(localeCode);
        if (!EMPTY.equals(byLanguageTag)) {
            return byLanguageTag;
        }

        return LocaleUtils.toLocale(localeCode);
    }

    @Nullable
    public static Locale findBestMatchLocale(
            final @Nullable Locale locale, final Iterable<Locale> candidates) {
        if (locale == null) {
            return null;
        }
        final var lookupList = LocaleUtils.localeLookupList(locale);
        return lookupList.stream()
                .map(lookupLocale -> IterableUtils.find(candidates, lookupLocale::equals))
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }

    @Nullable
    public static String findBestMatchLocaleString(
            final Locale locale, final Collection<String> candidateStrings) {
        final var candidates = candidateStrings.stream().map(Locale::forLanguageTag).toList();
        return Optional.ofNullable(findBestMatchLocale(locale, candidates))
                .map(Locale::toLanguageTag)
                .orElse(null);
    }

    public static Optional<String> localValue(
            final Map<Locale, String> values, final Locale locale) {
        final var matchedLocale = findBestMatchLocale(locale, values.keySet());
        return Optional.ofNullable(values.get(matchedLocale));
    }

    public static Optional<Locale> getLocale(final String key) {
        try {
            return Optional.ofNullable(LocaleUtils.toLocale(key))
                    .filter(LocaleUtils::isAvailableLocale);
        } catch (final IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    public static Map<Locale, String> getLocalizedStrings(
            final Map<String, ?> dictionary, final String keyPrefix, final String separator) {
        return dictionary.entrySet().stream()
                .map(entry -> tryToGetLocalizedString(entry, keyPrefix, separator))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(toMap(Entry::getKey, Entry::getValue, (existing, other) -> existing));
    }

    private static Optional<Map.Entry<Locale, String>> tryToGetLocalizedString(
            final Map.Entry<String, ?> entry, final String prefix, final String separator) {

        final var value = entry.getValue();
        if (value == null) {
            return Optional.empty();
        }

        final var key = entry.getKey();
        if (!key.startsWith(prefix + separator)) {
            return Optional.empty();
        }

        return Arrays.stream(key.split(separator))
                .skip(1)
                .findFirst() // get first part after first separator
                .flatMap(LocaleHelper::getLocale)
                .map(locale -> entry(locale, value.toString()));
    }
}
