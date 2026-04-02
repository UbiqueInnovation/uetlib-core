package ch.ubique.backend.core.localized.jackson;

import static ch.ubique.backend.core.localized.jackson.LocalizedValueKeyTransformers.keySerializationTransformer;

import static tools.jackson.databind.util.NameTransformer.chainedTransformer;

import ch.ubique.backend.core.localized.LocalizedValue;
import ch.ubique.backend.core.localized.annotation.JsonLocalizedValue;
import ch.ubique.backend.core.localized.annotation.JsonLocalizedValue.SerializationMethod;

import com.pivovarit.function.ThrowingBiConsumer;

import tools.jackson.core.JsonGenerator;
import tools.jackson.databind.BeanProperty;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.ValueSerializer;
import tools.jackson.databind.util.NameTransformer;

import java.io.Serializable;
import java.util.Map;

/**
 * Serializes a {@link LocalizedValue} to a JSON object. It supports both unwrapped properties and
 * the different key serialization methods configured through annotation @{@link
 * JsonLocalizedValue#keySerialization()}.
 *
 * <p>Serialization always starts from {@link LocalizedValue#toMapWithLanguageTagKeys()}, i.e. the
 * canonical lower-hyphen BCP 47 language tag representation. Additional key formats such as upper
 * case, camel case, or pascal case are derived by applying a {@link NameTransformer} to these
 * canonical keys.
 *
 * <p>For unwrapped serialization Jackson provides a second {@link NameTransformer}. This serializer
 * first applies the key-serialization transformer and then the unwrapping transformer.
 */
public class LocalizedValueSerializer
        extends ValueSerializer<LocalizedValue<? extends Serializable>> {

    private final JsonLocalizedValue jsonLocalizedValue;
    private final NameTransformer unwrapper;
    private final NameTransformer effectiveTransformer;

    public LocalizedValueSerializer() {
        this(null, null);
    }

    public LocalizedValueSerializer(
            final JsonLocalizedValue jsonLocalizedValue, final NameTransformer unwrapper) {
        this.jsonLocalizedValue = jsonLocalizedValue;
        this.unwrapper = unwrapper;
        this.effectiveTransformer = buildEffectiveTransformer(jsonLocalizedValue, unwrapper);
    }

    @Override
    public void serialize(
            final LocalizedValue localizedValue,
            final JsonGenerator gen,
            final SerializationContext ctxt) {

        if (!isUnwrappingSerializer()) {
            gen.writeStartObject();
        }

        //noinspection unchecked
        final Map<String, Object> stringKeyMap = localizedValue.toMapWithLanguageTagKeys();
        stringKeyMap.forEach(
                ThrowingBiConsumer.sneaky(
                        (key, value) -> {
                            gen.writeName(effectiveTransformer.transform(key));
                            gen.writePOJO(value);
                        }));

        if (!isUnwrappingSerializer()) {
            gen.writeEndObject();
        }
    }

    @Override
    public boolean isUnwrappingSerializer() {
        return unwrapper != null;
    }

    @Override
    public ValueSerializer<?> createContextual(
            final SerializationContext ctxt, final BeanProperty property) {
        if (property == null) {
            return new LocalizedValueSerializer();
        }

        final var jsonLocalizedValueAnnotation = property.getAnnotation(JsonLocalizedValue.class);
        return new LocalizedValueSerializer(jsonLocalizedValueAnnotation, unwrapper);
    }

    @Override
    public ValueSerializer<LocalizedValue<? extends Serializable>> unwrappingSerializer(
            final NameTransformer unwrapper) {
        return new LocalizedValueSerializer(jsonLocalizedValue, unwrapper);
    }

    private static NameTransformer buildEffectiveTransformer(
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
