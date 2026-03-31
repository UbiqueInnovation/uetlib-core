package ch.ubique.backend.core.localized.deserializer;

import static com.google.common.base.CaseFormat.LOWER_HYPHEN;

import ch.ubique.backend.core.localized.LocalizedValue;
import ch.ubique.backend.core.localized.annotation.JsonLocalizedValue;
import ch.ubique.backend.core.localized.annotation.JsonLocalizedValue.SerializationMethod;
import ch.ubique.backend.core.localized.serializer.LocalizedValueSerializer;

import com.google.common.base.CaseFormat;

import org.apache.commons.lang3.StringUtils;

import tools.jackson.core.JsonParser;
import tools.jackson.databind.BeanProperty;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.ValueDeserializer;
import tools.jackson.databind.node.ObjectNode;
import tools.jackson.databind.util.NameTransformer;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Locale;

/**
 * Deserializes a {@link LocalizedValue} from a JSON object. It supports both unwrapped properties
 * and the different key deserialization methods configured through annotation @{@link
 * JsonLocalizedValue#keySerialization()}.
 *
 * <p>This deserializer is intended to be symmetric to {@link LocalizedValueSerializer}. For
 * unwrapped properties Jackson provides a {@link NameTransformer}; this deserializer uses {@link
 * NameTransformer#reverse(String)} to map external flattened field names back to localized keys
 * before normalizing them.
 */
public class LocalizedValueDeserializer
        extends ValueDeserializer<LocalizedValue<? extends Serializable>> {

    private final NameTransformer nameTransformer;
    private final JsonLocalizedValue jsonLocalizedValue;

    public LocalizedValueDeserializer() {
        this(null, null);
    }

    public LocalizedValueDeserializer(
            final NameTransformer nameTransformer, final JsonLocalizedValue jsonLocalizedValue) {
        this.nameTransformer = nameTransformer;
        this.jsonLocalizedValue = jsonLocalizedValue;
    }

    @Override
    public LocalizedValue<? extends Serializable> deserialize(
            final JsonParser parser, final DeserializationContext context) {
        final var node = parser.objectReadContext().readTree(parser);
        if (!(node instanceof final ObjectNode objectNode)) {
            return (LocalizedValue<? extends Serializable>)
                    context.handleUnexpectedToken(LocalizedValue.class, parser);
        }

        final var values = new LinkedHashMap<String, Serializable>();
        final var keySerializationMethod =
                jsonLocalizedValue != null
                        ? jsonLocalizedValue.keySerialization()
                        : SerializationMethod.DEFAULT;

        for (final var entry : objectNode.properties()) {
            final var extracted = extractKey(entry.getKey());
            if (extracted == null) {
                continue;
            }

            final var normalizedKey = normalizeKey(extracted, keySerializationMethod);
            if (normalizedKey == null) {
                continue;
            }

            final var value = context.readTreeAsValue(entry.getValue(), Object.class);
            if (value instanceof final Serializable serializableValue) {
                values.put(normalizedKey, serializableValue);
            }
        }

        return LocalizedValue.builder().fullLocale().fromStringMap(values).buildNullable();
    }

    @Override
    public ValueDeserializer<?> createContextual(
            final DeserializationContext context, final BeanProperty property) {
        if (property == null) {
            return new LocalizedValueDeserializer();
        }

        final var jsonLocalizedValueAnnotation = property.getAnnotation(JsonLocalizedValue.class);
        return new LocalizedValueDeserializer(nameTransformer, jsonLocalizedValueAnnotation);
    }

    @Override
    public ValueDeserializer<LocalizedValue<? extends Serializable>> unwrappingDeserializer(
            final DeserializationContext ctxt, final NameTransformer nameTransformer) {
        return new LocalizedValueDeserializer(nameTransformer, jsonLocalizedValue);
    }

    /**
     * For unwrapped deserialization, reverse the Jackson name transformation. For regular
     * deserialization, use the raw key as-is.
     */
    private String extractKey(final String rawKey) {
        if (StringUtils.isBlank(rawKey)) {
            return null;
        }

        if (nameTransformer == null) {
            return rawKey;
        }

        return nameTransformer.reverse(rawKey);
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
