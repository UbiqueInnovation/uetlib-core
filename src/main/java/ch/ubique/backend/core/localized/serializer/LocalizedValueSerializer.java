package ch.ubique.backend.core.localized.serializer;

import ch.ubique.backend.core.localized.LocalizedValue;
import ch.ubique.backend.core.localized.annotation.JsonLocalizedValue;
import ch.ubique.backend.core.localized.annotation.JsonLocalizedValue.SerializationMethod;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.pivovarit.function.ThrowingBiConsumer;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map;
import java.util.function.UnaryOperator;

/**
 * This serializer serializes a {@link LocalizedValue} to a JSON object. It handles both the
 * unwrapping case with @{@link JsonUnwrapped} and the different key serialization methods with
 * annotation @{@link JsonLocalizedValue#keySerialization()}.}
 *
 * <p>The following examples show how to use the annotation on a field/method:
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
 * <p>These are serialized to the following JSON:
 * {@snippet lang = "json":
 * {"de:suf":"value_de","en:suf":"value_en"}
 * }
 * {@snippet lang = "json":
 * {"name":{"de_CH":"value_de","en_CH":"value_en"}}
 * }
 * {@snippet lang = "json":
 * {"pre:EN-CH:suf":"value_en","pre:DE-CH:suf":"value_de"}
 * }
 */
public class LocalizedValueSerializer extends JsonSerializer<LocalizedValue<? extends Serializable>>
        implements ContextualSerializer {

    private final JsonUnwrapped jsonUnwrapped;
    private final JsonLocalizedValue jsonLocalizedValue;

    public LocalizedValueSerializer() {
        this(null, null);
    }

    public LocalizedValueSerializer(
            final JsonUnwrapped jsonUnwrapped, final JsonLocalizedValue jsonLocalizedValue) {
        this.jsonUnwrapped = jsonUnwrapped;
        this.jsonLocalizedValue = jsonLocalizedValue;
    }

    @Override
    public void serialize(
            final LocalizedValue localizedValue,
            final JsonGenerator gen,
            final SerializerProvider serializers)
            throws IOException {

        if (!isUnwrappingSerializer()) {
            gen.writeStartObject(); // Unwrap starts here
        }

        // Get the serialization method from the annotation on the field/method
        final var keySerializationMethod =
                jsonLocalizedValue != null
                        ? jsonLocalizedValue.keySerialization()
                        : SerializationMethod.DEFAULT;
        final Map<String, Object> map =
                switch (keySerializationMethod) {
                    case DEFAULT -> localizedValue.toMapWithLanguageTagKeys();
                    case POSIX -> localizedValue.toMapWithPosixLocaleKeys();
                    case UPPERCASE -> localizedValue.toMapWithUppercaseKeys();
                };

        final UnaryOperator<String> keyMapper;
        if (isUnwrappingSerializer()) {
            final var prefix = jsonUnwrapped != null ? jsonUnwrapped.prefix() : "";
            final var suffix = jsonUnwrapped != null ? jsonUnwrapped.suffix() : "";
            keyMapper = key -> prefix + key + suffix;
        } else {
            keyMapper = UnaryOperator.identity();
        }

        map.forEach(
                ThrowingBiConsumer.sneaky(
                        (key, value) -> {
                            final var mappedKey = keyMapper.apply(key);
                            gen.writeFieldName(mappedKey);
                            gen.writeObject(value);
                        }));

        // End unwrapping if needed
        if (!isUnwrappingSerializer()) {
            gen.writeEndObject(); // Unwrap ends here
        }
    }

    @Override
    public boolean isUnwrappingSerializer() {
        return jsonUnwrapped != null;
    }

    @Override
    public JsonSerializer<?> createContextual(
            final SerializerProvider prov, final BeanProperty property) {
        if (property == null) {
            return new LocalizedValueSerializer();
        }

        final var jsonLocalizedValue = property.getAnnotation(JsonLocalizedValue.class);
        final var jsonUnwrapped = property.getAnnotation(JsonUnwrapped.class);
        return new LocalizedValueSerializer(jsonUnwrapped, jsonLocalizedValue);
    }
}
