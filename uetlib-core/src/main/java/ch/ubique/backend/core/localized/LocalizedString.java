package ch.ubique.backend.core.localized;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

public class LocalizedString extends ArrayList<LocalizedStringEntry> {

    public LocalizedString() {}

    private LocalizedString(Collection<LocalizedStringEntry> entries) {
        super(entries);
    }

    public LocalizedString add(String langKey, String value) {
        add(new LocalizedStringEntry(langKey, value));
        return this;
    }

    public LocalizedString addForDeEnItFr(String value) {
        de(value);
        en(value);
        it(value);
        fr(value);
        return this;
    }

    /**
     * Convenience method to create a localized text given a map of strings of the form key_langCode
     * E.g. A possible map for the input may look like this: { status_de: aktiv, status_en: active,
     * status_it: ... }
     */
    public static LocalizedString fromSuffixStrings(
            Map<String, ?> stringMap, String key, Collection<String> languages) {
        final var localizedText = new LocalizedString();
        for (final var langCode : languages) {
            final var localKey = String.format("%s_%s", key, langCode);
            if (stringMap.containsKey(localKey) && stringMap.get(localKey) != null) {
                localizedText.add(langCode, stringMap.get(localKey).toString());
            }
        }
        return localizedText.clean();
    }

    public LocalizedString de(String value) {
        return add("de", value);
    }

    public LocalizedString en(String value) {
        return add("en", value);
    }

    public LocalizedString it(String value) {
        return add("it", value);
    }

    public LocalizedString fr(String value) {
        return add("fr", value);
    }

    public LocalizedString clean() {
        var entries = stream().filter(LocalizedStringEntry::isValid).toList();

        if (entries.isEmpty()) {
            return null;
        }
        return new LocalizedString(entries);
    }

    @Override
    public String toString() {
        return this.stream().map(LocalizedStringEntry::toString).collect(Collectors.joining(", "));
    }

    @Override
    @Schema(hidden = true)
    public boolean isEmpty() {
        return super.isEmpty();
    }
}
