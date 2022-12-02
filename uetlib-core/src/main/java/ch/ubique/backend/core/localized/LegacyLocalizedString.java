package ch.ubique.backend.core.localized;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

public class LegacyLocalizedString extends ArrayList<LegacyLocalizedStringEntry> {

    public LegacyLocalizedString() {}

    private LegacyLocalizedString(Collection<LegacyLocalizedStringEntry> entries) {
        super(entries);
    }

    /**
     * Convenience method to create a localized text given a map of strings of the form key_langCode
     * E.g. A possible map for the input may look like this: { status_de: aktiv, status_en: active,
     * status_it: ... }
     */
    public static LegacyLocalizedString fromSuffixStrings(
            Map<String, ?> stringMap, String key, Collection<String> languages) {
        final var localizedText = new LegacyLocalizedString();
        for (final var langCode : languages) {
            final var localKey = String.format("%s_%s", key, langCode);
            if (stringMap.containsKey(localKey) && stringMap.get(localKey) != null) {
                localizedText.add(langCode, stringMap.get(localKey).toString());
            }
        }
        return localizedText.clean();
    }

    public LegacyLocalizedString add(String langKey, String value) {
        add(new LegacyLocalizedStringEntry(langKey, value));
        return this;
    }

    public LegacyLocalizedString clean() {
        var entries = stream().filter(LegacyLocalizedStringEntry::isValid).toList();

        if (entries.isEmpty()) {
            return null;
        }
        return new LegacyLocalizedString(entries);
    }

    public LegacyLocalizedString addForDeEnItFr(String value) {
        de(value);
        en(value);
        it(value);
        fr(value);
        return this;
    }

    public LegacyLocalizedString de(String value) {
        return add("de", value);
    }

    public LegacyLocalizedString en(String value) {
        return add("en", value);
    }

    public LegacyLocalizedString it(String value) {
        return add("it", value);
    }

    public LegacyLocalizedString fr(String value) {
        return add("fr", value);
    }

    @Override
    public String toString() {
        return this.stream()
                .map(LegacyLocalizedStringEntry::toString)
                .collect(Collectors.joining(", "));
    }

    @Override
    @Schema(hidden = true)
    public boolean isEmpty() {
        return super.isEmpty();
    }
}
