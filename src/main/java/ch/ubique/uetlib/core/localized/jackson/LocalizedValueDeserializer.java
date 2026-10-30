package ch.ubique.uetlib.core.localized.jackson;

import static ch.ubique.uetlib.core.localized.jackson.LocalizedValueKeyTransformers.keySerializationTransformer;
import static tools.jackson.databind.util.NameTransformer.chainedTransformer;

import ch.ubique.uetlib.core.localized.LocalizedValue;
import ch.ubique.uetlib.core.localized.annotation.JsonLocalizedValue;
import ch.ubique.uetlib.core.localized.annotation.JsonLocalizedValue.SerializationMethod;
import java.io.Serializable;
import java.util.LinkedHashMap;
import org.apache.commons.lang3.StringUtils;
import org.jspecify.annotations.Nullable;
import tools.jackson.core.JsonParser;
import tools.jackson.databind.BeanProperty;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.JavaType;
import tools.jackson.databind.ValueDeserializer;
import tools.jackson.databind.node.ObjectNode;
import tools.jackson.databind.util.NameTransformer;

/**
 * Deserializes a {@link LocalizedValue} from a JSON object. It supports both unwrapped properties
 * and the different key deserialization methods configured through annotation @{@link
 * JsonLocalizedValue#keySerialization()}.
 *
 * <p>This deserializer is symmetric to {@link LocalizedValueSerializer}. It first reverses the
 * unwrapping {@link NameTransformer} if present, then reverses the key-serialization transformer,
 * resulting in canonical lower-hyphen language-tag keys that can be passed to {@code
 * builder().fullLocale().fromStringMap(...)}.
 */
public class LocalizedValueDeserializer
        extends ValueDeserializer<LocalizedValue<? extends Serializable>> {

    private final @Nullable JsonLocalizedValue jsonLocalizedValue;
    private final @Nullable NameTransformer unwrapper;
    private final NameTransformer effectiveUnwrapper;
    private final @Nullable JavaType valueType; // resolved T, null when unknown

    public LocalizedValueDeserializer() {
        this(null, null, null);
    }

    public LocalizedValueDeserializer(
            final @Nullable JsonLocalizedValue jsonLocalizedValue,
            final @Nullable NameTransformer unwrapper,
            final @Nullable JavaType valueType) {
        this.jsonLocalizedValue = jsonLocalizedValue;
        this.unwrapper = unwrapper;
        this.effectiveUnwrapper = buildEffectiveUnwrapper(jsonLocalizedValue, unwrapper);
        this.valueType = valueType;
    }

    @Override
    @Nullable
    public LocalizedValue<? extends Serializable> deserialize(
            final JsonParser parser, final DeserializationContext context) {
        final var node = parser.objectReadContext().readTree(parser);
        if (!(node instanceof final ObjectNode objectNode)) {
            return (LocalizedValue<? extends Serializable>)
                    context.handleUnexpectedToken(LocalizedValue.class, parser);
        }

        final var values = new LinkedHashMap<String, Serializable>();

        for (final var entry : objectNode.properties()) {
            final var unwrappedKey = effectiveUnwrapper.reverse(entry.getKey());
            if (StringUtils.isBlank(unwrappedKey)) {
                continue;
            }

            final var value =
                    valueType != null
                            ? context.readTreeAsValue(entry.getValue(), valueType)
                            : context.readTreeAsValue(entry.getValue(), Object.class);
            if (value instanceof final Serializable serializableValue) {
                values.put(unwrappedKey, serializableValue);
            }
        }

        return LocalizedValue.builder().fullLocale().fromStringMap(values).buildNullable();
    }

    @Override
    public ValueDeserializer<?> createContextual(
            final @Nullable DeserializationContext context, final @Nullable BeanProperty property) {
        final var annotationFromProperty =
                property != null ? property.getAnnotation(JsonLocalizedValue.class) : null;
        final var effectiveAnnotation =
                annotationFromProperty != null ? annotationFromProperty : jsonLocalizedValue;

        final var resolvedValueType = resolveValueType(context, property);

        return new LocalizedValueDeserializer(
                effectiveAnnotation,
                unwrapper,
                resolvedValueType != null ? resolvedValueType : valueType);
    }

    private static @Nullable JavaType resolveValueType(
            final @Nullable DeserializationContext context, final @Nullable BeanProperty property) {
        // property type for bean properties; contextual type for root-level reads
        final JavaType declaredType;
        if (property != null) {
            declaredType = property.getType();
        } else if (context != null) {
            declaredType = context.getContextualType();
        } else {
            declaredType = null;
        }

        if (declaredType == null
                || !LocalizedValue.class.isAssignableFrom(declaredType.getRawClass())) {
            return null;
        }
        final var contained = declaredType.containedTypeOrUnknown(0);
        // Raw LocalizedValue binds T to Object/its bound — nothing more specific to deserialize as.
        return contained.isJavaLangObject() || contained.hasRawClass(Serializable.class)
                ? null
                : contained;
    }

    @Override
    public ValueDeserializer<LocalizedValue<? extends Serializable>> unwrappingDeserializer(
            final DeserializationContext ctxt, final NameTransformer newUnwrapper) {
        final var combinedUnwrapper =
                unwrapper == null ? newUnwrapper : chainedTransformer(newUnwrapper, unwrapper);
        return new LocalizedValueDeserializer(jsonLocalizedValue, combinedUnwrapper, valueType);
    }

    private static NameTransformer buildEffectiveUnwrapper(
            final @Nullable JsonLocalizedValue jsonLocalizedValue,
            final @Nullable NameTransformer unwrapper) {
        final var keySerializationMethod =
                jsonLocalizedValue != null
                        ? jsonLocalizedValue.keySerialization()
                        : SerializationMethod.DEFAULT;

        final var keySerializationTransformer = keySerializationTransformer(keySerializationMethod);

        return unwrapper != null
                ? chainedTransformer(unwrapper, keySerializationTransformer)
                : keySerializationTransformer;
    }
}
