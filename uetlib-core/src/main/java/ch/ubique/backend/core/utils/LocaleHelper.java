package ch.ubique.backend.core.utils;

import jakarta.validation.constraints.NotNull;

import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.lang3.LocaleUtils;

import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public final class LocaleHelper {

    private LocaleHelper() {}

    public static Locale findBestMatchLocale(
            final Locale locale, final Iterable<Locale> candidates) {
        final var lookupList = LocaleUtils.localeLookupList(locale);
        return lookupList.stream()
                .map(lookupLocale -> IterableUtils.find(candidates, lookupLocale::equals))
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }

    public static String findBestMatchLocaleString(
            final Locale locale, final @NotNull Collection<String> candidateStrings) {
        final var candidates = candidateStrings.stream().map(Locale::forLanguageTag).toList();
        final var lookupList = LocaleUtils.localeLookupList(locale);
        return lookupList.stream()
                .map(lookupLocale -> IterableUtils.find(candidates, lookupLocale::equals))
                .filter(Objects::nonNull)
                .findFirst()
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
                .collect(
                        Collectors.toMap(
                                Entry::getKey, Entry::getValue, (existing, other) -> existing));
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
                .map(locale -> Map.entry(locale, value.toString()));
    }
}
