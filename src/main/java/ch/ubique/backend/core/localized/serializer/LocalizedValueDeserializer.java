package ch.ubique.backend.core.localized.serializer;

import static com.google.common.base.CaseFormat.LOWER_HYPHEN;

import ch.ubique.backend.core.localized.LocalizedValue;
import ch.ubique.backend.core.localized.annotation.JsonLocalizedValue;
import ch.ubique.backend.core.localized.annotation.JsonLocalizedValue.SerializationMethod;

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
 * Deserializes a {@link LocalizedValue} while supporting {@link JsonUnwrapped} and {@link
 * JsonLocalizedValue#keySerialization()} in the same way as {@link LocalizedValueSerializer}.
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

    private static String convertCase(final String rawKey, final CaseFormat sourceCaseFormat) {
        try {
            return sourceCaseFormat.to(LOWER_HYPHEN, rawKey);
        } catch (final IllegalArgumentException ignored) {
            return null;
        }
    }
}
