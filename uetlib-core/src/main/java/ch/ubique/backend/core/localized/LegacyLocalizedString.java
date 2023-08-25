package ch.ubique.backend.core.localized;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @deprecated since 1.5, use {@link LocalizedValue}
 */
@Deprecated(since = "1.5", forRemoval = true)
public class LegacyLocalizedString extends ArrayList<LegacyLocalizedStringEntry> {

    public LegacyLocalizedString() {}

    private LegacyLocalizedString(final Collection<LegacyLocalizedStringEntry> entries) {
        super(entries);
    }

    /**
     * Convenience method to create a localized text given a map of strings of the form key_langCode
     * E.g. A possible map for the input may look like this: { status_de: aktiv, status_en: active,
     * status_it: ... }
     */
    public static LegacyLocalizedString fromSuffixStrings(
            final Map<String, ?> stringMap, final String key, final Collection<String> languages) {
        final var localizedText = new LegacyLocalizedString();
        for (final var langCode : languages) {
            final var localKey = String.format("%s_%s", key, langCode);
            if (stringMap.containsKey(localKey) && stringMap.get(localKey) != null) {
                localizedText.add(langCode, stringMap.get(localKey).toString());
            }
        }
        return localizedText.clean();
    }

    public LegacyLocalizedString add(final String langKey, final String value) {
        add(new LegacyLocalizedStringEntry(langKey, value));
        return this;
    }

    public LegacyLocalizedString clean() {
        final var entries = stream().filter(LegacyLocalizedStringEntry::isValid).toList();

        if (entries.isEmpty()) {
            return null;
        }
        return new LegacyLocalizedString(entries);
    }

    public LegacyLocalizedString addForDeEnItFr(final String value) {
        de(value);
        en(value);
        it(value);
        fr(value);
        return this;
    }

    public LegacyLocalizedString de(final String value) {
        return add("de", value);
    }

    public LegacyLocalizedString en(final String value) {
        return add("en", value);
    }

    public LegacyLocalizedString it(final String value) {
        return add("it", value);
    }

    public LegacyLocalizedString fr(final String value) {
        return add("fr", value);
    }

    @Override
    public String toString() {
        return stream().map(LegacyLocalizedStringEntry::toString).collect(Collectors.joining(", "));
    }

    @Override
    @Schema(hidden = true)
    public boolean isEmpty() {
        return super.isEmpty();
    }
}
