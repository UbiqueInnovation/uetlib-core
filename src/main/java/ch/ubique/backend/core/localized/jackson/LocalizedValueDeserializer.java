package ch.ubique.backend.core.localized.jackson;

import static ch.ubique.backend.core.localized.jackson.LocalizedValueKeyTransformers.keySerializationTransformer;

import static tools.jackson.databind.util.NameTransformer.chainedTransformer;

import ch.ubique.backend.core.localized.LocalizedValue;
import ch.ubique.backend.core.localized.annotation.JsonLocalizedValue;
import ch.ubique.backend.core.localized.annotation.JsonLocalizedValue.SerializationMethod;

import org.apache.commons.lang3.StringUtils;

import tools.jackson.core.JsonParser;
import tools.jackson.databind.BeanProperty;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.ValueDeserializer;
import tools.jackson.databind.node.ObjectNode;
import tools.jackson.databind.util.NameTransformer;

import java.io.Serializable;
import java.util.LinkedHashMap;

/**
 * Deserializes a {@link LocalizedValue} from a JSON object. It supports both unwrapped properties
 * and the different key deserialization methods configured through annotation @{@link
 * JsonLocalizedValue#keySerialization()}.
 *
 * <p>This deserializer is symmetric to {@link LocalizedValueSerializer}. It first reverses the
 * unwrapping {@link NameTransformer} if present, then reverses the key-serialization transformer,
 * resulting in canonical lower-hyphen language-tag keys that can be passed to {@code
 * builder().fullLocale().fromStringMap(...)}.
 *
 * <p>TODO with jackson 3.2: Checkout new {@link ValueDeserializer#collectAllPropertyNamesTo()}
 * because of <a
 * href="https://github.com/FasterXML/jackson-databind/issues/6001">jackson/issue/6001</a>
 */
public class LocalizedValueDeserializer
        extends ValueDeserializer<LocalizedValue<? extends Serializable>> {

    private final JsonLocalizedValue jsonLocalizedValue;
    private final NameTransformer unwrapper;
    private final NameTransformer effectiveUnwrapper;

    public LocalizedValueDeserializer() {
        this(null, null);
    }

    public LocalizedValueDeserializer(
            final JsonLocalizedValue jsonLocalizedValue, final NameTransformer unwrapper) {
        this.jsonLocalizedValue = jsonLocalizedValue;
        this.unwrapper = unwrapper;
        this.effectiveUnwrapper = buildEffectiveUnwrapper(jsonLocalizedValue, unwrapper);
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

        for (final var entry : objectNode.properties()) {
            final var unwrappedKey = effectiveUnwrapper.reverse(entry.getKey());
            if (StringUtils.isBlank(unwrappedKey)) {
                continue;
            }

            final var value = context.readTreeAsValue(entry.getValue(), Object.class);
            if (value instanceof final Serializable serializableValue) {
                values.put(unwrappedKey, serializableValue);
            }
        }

        return LocalizedValue.builder().fullLocale().fromStringMap(values).buildNullable();
    }

    @Override
    public ValueDeserializer<?> createContextual(
            final DeserializationContext context, final BeanProperty property) {
        if (property == null) {
            return this;
        }

        final var annotationFromProperty = property.getAnnotation(JsonLocalizedValue.class);
        final var effectiveAnnotation =
                annotationFromProperty != null ? annotationFromProperty : jsonLocalizedValue;
        return new LocalizedValueDeserializer(effectiveAnnotation, unwrapper);
    }

    @Override
    public ValueDeserializer<LocalizedValue<? extends Serializable>> unwrappingDeserializer(
            final DeserializationContext ctxt, final NameTransformer newUnwrapper) {
        final var combinedUnwrapper =
                unwrapper == null ? newUnwrapper : chainedTransformer(newUnwrapper, unwrapper);
        return new LocalizedValueDeserializer(jsonLocalizedValue, combinedUnwrapper);
    }

    private static NameTransformer buildEffectiveUnwrapper(
            final JsonLocalizedValue jsonLocalizedValue, final NameTransformer unwrapper) {
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
