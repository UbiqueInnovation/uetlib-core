package ch.ubique.backend.core.localized.deserializer;

import static com.google.common.base.CaseFormat.LOWER_HYPHEN;

import ch.ubique.backend.core.localized.LocalizedValue;
import ch.ubique.backend.core.localized.annotation.JsonLocalizedValue;
import ch.ubique.backend.core.localized.annotation.JsonLocalizedValue.SerializationMethod;
import ch.ubique.backend.core.localized.serializer.LocalizedValueSerializer;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.util.NameTransformer;
import com.google.common.base.CaseFormat;

import java.io.IOException;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Locale;

/**
 * Deserializes a {@link LocalizedValue} from a JSON object. It supports both unwrapped properties
 * with @{@link JsonUnwrapped} and the different key deserialization methods configured through
 * annotation @{@link JsonLocalizedValue#keySerialization()}.
 *
 * <p>This deserializer is intended to be symmetric to {@link LocalizedValueSerializer}. Keys
 * written with a given serialization method are normalized back to the locale key format expected
 * by {@link LocalizedValue} through {@code builder().fullLocale().fromStringMap(...)}.
 *
 * <p>The following examples show how to use the annotation on a field or method:
 *
 * {@snippet lang = "java":
 * record Unwrapped(@JsonUnwrapped(suffix = ":suf") LocalizedValue<String> name) {}
 *
 * record KeySerialization(
 *             @JsonLocalizedValue(keySerialization = SerializationMethod.POSIX)
 *                     LocalizedValue<String> name) {}
 *
 * record Combined(
 *             @JsonUnwrapped(prefix = "pre:", suffix = ":suf")
 *                     @JsonLocalizedValue(keySerialization = SerializationMethod.UPPERCASE)
 *                     LocalizedValue<String> name) {}
 * }
 *
 * <p>These JSON payloads are deserialized as follows:
 *
 * <p>
 *
 * {@snippet lang = "json":
 * {"de:suf":"value_de","en:suf":"value_en"}
 * }
 *
 * deserializes to
 *
 * {@snippet lang = "java":
 * Unwrapped(
 *         LocalizedValue.<String>builder()
 *                 .fullLocale()
 *                 .fromStringMap(Map.of("de", "value_de", "en", "value_en"))
 *                 .build())
 * }
 *
 * <p>
 *
 * {@snippet lang = "json":
 * {"name":{"de_CH":"value_de","en_CH":"value_en"}}
 * }
 *
 * deserializes to
 *
 * {@snippet lang = "java":
 * KeySerialization(
 *         LocalizedValue.<String>builder()
 *                 .fullLocale()
 *                 .fromStringMap(Map.of("de_CH", "value_de", "en_CH", "value_en"))
 *                 .build())
 * }
 *
 * <p>
 *
 * {@snippet lang = "json":
 * {"pre:EN-CH:suf":"value_en","pre:DE-CH:suf":"value_de"}
 * }
 *
 * deserializes to
 *
 * {@snippet lang = "java":
 * Combined(
 *         LocalizedValue.<String>builder()
 *                 .fullLocale()
 *                 .fromStringMap(Map.of("en-ch", "value_en", "de-ch", "value_de"))
 *                 .build())
 * }
 *
 * <p>Behavior notes:
 *
 * <ul>
 *   <li>For {@link SerializationMethod#DEFAULT} and {@link SerializationMethod#POSIX}, keys are
 *       kept as-is and passed to {@link LocalizedValue} for locale parsing.
 *   <li>For {@link SerializationMethod#UPPERCASE}, keys are lowercased before locale parsing.
 *   <li>For {@link SerializationMethod#CAMEL_CASE} and {@link SerializationMethod#PASCAL_CASE},
 *       keys are converted back to lower-hyphen form before locale parsing.
 *   <li>When {@link JsonUnwrapped} is used, only properties matching the configured prefix and
 *       suffix are considered part of the {@link LocalizedValue}.
 *   <li>Keys that cannot be normalized are skipped.
 * </ul>
 */
public class LocalizedValueDeserializer
        extends JsonDeserializer<LocalizedValue<? extends Serializable>>
        implements ContextualDeserializer {

    private final JsonUnwrapped jsonUnwrapped;
    private final JsonLocalizedValue jsonLocalizedValue;

    public LocalizedValueDeserializer() {
        this(null, null);
    }

    public LocalizedValueDeserializer(
            final JsonUnwrapped jsonUnwrapped, final JsonLocalizedValue jsonLocalizedValue) {
        this.jsonUnwrapped = jsonUnwrapped;
        this.jsonLocalizedValue = jsonLocalizedValue;
    }

    @Override
    public LocalizedValue<? extends Serializable> deserialize(
            final JsonParser parser, final DeserializationContext context) throws IOException {
        final var node = parser.getCodec().readTree(parser);
        if (!(node instanceof final ObjectNode objectNode)) {
            return (LocalizedValue<? extends Serializable>)
                    context.handleUnexpectedToken(LocalizedValue.class, parser);
        }

        final var values = new LinkedHashMap<String, Serializable>();
        final var keySerializationMethod =
                jsonLocalizedValue != null
                        ? jsonLocalizedValue.keySerialization()
                        : SerializationMethod.DEFAULT;

        final var prefix = jsonUnwrapped != null ? jsonUnwrapped.prefix() : "";
        final var suffix = jsonUnwrapped != null ? jsonUnwrapped.suffix() : "";

        for (final var entry : objectNode.properties()) {
            final var extracted = extractKey(entry.getKey(), prefix, suffix);
            if (extracted == null) {
                continue;
            }

            final var normalizedKey = normalizeKey(extracted, keySerializationMethod);
            if (normalizedKey == null) {
                continue;
            }

            final var value = parser.getCodec().treeToValue(entry.getValue(), Object.class);
            if (value instanceof final Serializable serializableValue) {
                values.put(normalizedKey, serializableValue);
            }
        }

        return LocalizedValue.builder().fullLocale().fromStringMap(values).buildNullable();
    }

    @Override
    public JsonDeserializer<?> createContextual(
            final DeserializationContext context, final BeanProperty property) {
        if (property == null) {
            return new LocalizedValueDeserializer();
        }

        final var jsonLocalizedValueAnnotation = property.getAnnotation(JsonLocalizedValue.class);
        final var jsonUnwrappedAnnotation = property.getAnnotation(JsonUnwrapped.class);
        return new LocalizedValueDeserializer(
                jsonUnwrappedAnnotation, jsonLocalizedValueAnnotation);
    }

    @Override
    public JsonDeserializer<LocalizedValue<? extends Serializable>> unwrappingDeserializer(
            final NameTransformer transformer) {
        return new LocalizedValueDeserializer(jsonUnwrapped, jsonLocalizedValue);
    }

    /**
     * Extracts the localized key from an unwrapped property name by removing the configured prefix
     * and suffix. Returns {@code null} if the property does not match the expected unwrapped shape.
     */
    private static String extractKey(
            final String rawKey, final String prefix, final String suffix) {
        if (!rawKey.startsWith(prefix) || !rawKey.endsWith(suffix)) {
            return null;
        }

        final var withoutPrefix = rawKey.substring(prefix.length());
        if (suffix.isEmpty()) {
            return withoutPrefix;
        }
        return withoutPrefix.substring(0, withoutPrefix.length() - suffix.length());
    }

    /**
     * Normalizes a serialized locale key back into a format accepted by {@link LocalizedValue}. The
     * normalization depends on the configured {@link SerializationMethod}. Returns {@code null} if
     * the key cannot be normalized.
     */
    private static String normalizeKey(
            final String rawKey, final JsonLocalizedValue.SerializationMethod serializationMethod) {
        if (rawKey == null || rawKey.isBlank()) {
            return null;
        }

        return switch (serializationMethod) {
            case DEFAULT, POSIX -> rawKey;
            case UPPERCASE -> rawKey.toLowerCase(Locale.ROOT);
            case CAMEL_CASE -> convertCase(rawKey, CaseFormat.LOWER_CAMEL);
            case PASCAL_CASE -> convertCase(rawKey, CaseFormat.UPPER_CAMEL);
        };
    }

    /**
     * Converts a locale key from the given {@link CaseFormat} back to lower-hyphen form. Returns
     * {@code null} if the input does not match the expected source case format.
     */
    private static String convertCase(final String rawKey, final CaseFormat sourceCaseFormat) {
        try {
            return sourceCaseFormat.to(LOWER_HYPHEN, rawKey);
        } catch (final IllegalArgumentException ignored) {
            return null;
        }
    }
}
