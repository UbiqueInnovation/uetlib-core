package ch.ubique.backend.core.localized.validation;

import ch.ubique.backend.core.localized.LocalizedValue;

import jakarta.validation.valueextraction.ExtractedValue;
import jakarta.validation.valueextraction.ValueExtractor;

import org.jspecify.annotations.Nullable;

/**
 * A {@link ValueExtractor} for {@link LocalizedValue}, enabling bean validation on each localized
 * value within the container.
 *
 * <p>This extractor iterates over all entries in the internal map of the {@code LocalizedValue} and
 * passes each value, keyed by its locale, to the validation engine. This allows constraints (such
 * as {@code @NotNull}, {@code @Size}, etc.) to be applied to each localized value individually.
 *
 * @see ValueExtractor
 * @see LocalizedValue
 */
public class LocalizedValueValueExtractor
        implements ValueExtractor<LocalizedValue<@ExtractedValue ?>> {

    @Override
    public void extractValues(
            final @Nullable LocalizedValue<?> originalValue,
            final ValueExtractor.ValueReceiver receiver) {
        if (originalValue != null) {
            // Here, we extract all values for validation
            originalValue
                    .toMapWithLocaleKeys()
                    .forEach((key, value) -> receiver.keyedValue(null, key, value));
        } else {
            receiver.value(null, null);
        }
    }
}
