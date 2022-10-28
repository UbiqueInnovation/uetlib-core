package ch.ubique.backend.core.utils;

import org.apache.commons.lang3.LocaleUtils;

import java.util.*;
import java.util.stream.Collectors;

public class LocaleHelper {

    private LocaleHelper() {}

    public static Optional<String> localValue(Map<Locale, String> values, Locale locale) {
        final var matchedLocale = Locale.lookup(List.of(new Locale.LanguageRange(locale.toLanguageTag())), values.keySet());
        return Optional.ofNullable(matchedLocale).map(values::get);
    }

    public static Optional<Locale> getLocale(String key) {
        try {
            final var locale = LocaleUtils.toLocale(key);
            return LocaleUtils.isAvailableLocale(locale) ? Optional.of(locale) : Optional.empty();
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    public static Map<Locale, String> getLocalizedStrings(final Map<String, ?> dictionary, final String keyPrefix, String separator) {
        return dictionary.entrySet().stream()
                .map(entry -> tryToGetLocalizedString(entry, keyPrefix, separator))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.groupingBy(Map.Entry::getKey))
                .entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().get(0).getValue()));
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
