package ch.ubique.backend.core.localized.serializer;

import ch.ubique.backend.core.localized.LocalizedValue;
import ch.ubique.backend.core.localized.annotation.JsonLocalizedValue;
import ch.ubique.backend.core.localized.annotation.JsonLocalizedValue.SerializationMethod;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.google.common.base.CaseFormat;
import com.pivovarit.function.ThrowingBiConsumer;

import tools.jackson.core.JsonGenerator;
import tools.jackson.databind.BeanProperty;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.ValueSerializer;

import java.io.Serializable;
import java.util.Map;
import java.util.function.UnaryOperator;

/**
 * Serializes a {@link LocalizedValue} to a JSON object. It supports both unwrapped properties
 * with @{@link JsonUnwrapped} and the different key serialization methods configured through
 * annotation @{@link JsonLocalizedValue#keySerialization()}.
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
 * <p>These Java snippets are serialized as follows:
 *
 * <p>
 *
 * {@snippet lang = "java":
 * Unwrapped(
 *         LocalizedValue.<String>builder()
 *                 .fullLocale()
 *                 .fromStringMap(Map.of("de", "value_de", "en", "value_en"))
 *                 .build())
 * }
 *
 * serializes to
 *
 * {@snippet lang = "json":
 * {"de:suf":"value_de","en:suf":"value_en"}
 * }
 *
 * <p>
 *
 * {@snippet lang = "java":
 * KeySerialization(
 *         LocalizedValue.<String>builder()
 *                 .fullLocale()
 *                 .fromStringMap(Map.of("de_CH", "value_de", "en_CH", "value_en"))
 *                 .build())
 * }
 *
 * serializes to
 *
 * {@snippet lang = "json":
 * {"name":{"de_CH":"value_de","en_CH":"value_en"}}
 * }
 *
 * serializes to
 *
 * {@snippet lang = "java":
 * Combined(
 *         LocalizedValue.<String>builder()
 *                 .fullLocale()
 *                 .fromStringMap(Map.of("en-ch", "value_en", "de-ch", "value_de"))
 *                 .build())
 * }
 *
 * serializes to
 *
 * {@snippet lang = "json":
 * {"pre:EN-CH:suf":"value_en","pre:DE-CH:suf":"value_de"}
 * }
 *
 * <p>Behavior notes:
 *
 * <ul>
 *   <li>If {@link JsonLocalizedValue} is not present, {@link SerializationMethod#DEFAULT} is used.
 *   <li>If {@link JsonUnwrapped} is present, keys are emitted directly into the surrounding JSON
 *       object after applying the configured prefix and suffix.
 *   <li>Otherwise, a nested JSON object is written for the {@link LocalizedValue}.
 * </ul>
 */
public class LocalizedValueSerializer
        extends ValueSerializer<LocalizedValue<? extends Serializable>> {

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

    public void serialize(
            final LocalizedValue localizedValue,
            final JsonGenerator gen,
            final SerializationContext serializers) {

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
                    case CAMEL_CASE ->
                            localizedValue.toMapWithCaseFormatKeys(CaseFormat.LOWER_CAMEL);
                    case PASCAL_CASE ->
                            localizedValue.toMapWithCaseFormatKeys(CaseFormat.UPPER_CAMEL);
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
                            gen.writeName(mappedKey);
                            gen.writePOJO(value);
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
    public ValueSerializer<?> createContextual(
            final SerializationContext prov, final BeanProperty property) {
        if (property == null) {
            return new LocalizedValueSerializer();
        }

        final var jsonLocalizedValueAnnotation = property.getAnnotation(JsonLocalizedValue.class);
        final var jsonUnwrappedAnnotation = property.getAnnotation(JsonUnwrapped.class);
        return new LocalizedValueSerializer(jsonUnwrappedAnnotation, jsonLocalizedValueAnnotation);
    }
}
