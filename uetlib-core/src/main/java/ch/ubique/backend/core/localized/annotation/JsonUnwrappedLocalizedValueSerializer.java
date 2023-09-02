package ch.ubique.backend.core.localized.annotation;

import ch.ubique.backend.core.localized.LocalizedValue;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.pivovarit.function.ThrowingBiConsumer;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.UnaryOperator;

public class JsonUnwrappedLocalizedValueSerializer
        extends JsonSerializer<LocalizedValue<? extends Serializable>>
        implements ContextualSerializer {

    private final String prefix;
    private final String suffix;

    public JsonUnwrappedLocalizedValueSerializer() {
        this(null, null);
    }

    public JsonUnwrappedLocalizedValueSerializer(final String prefix, final String suffix) {
        this.prefix = Objects.requireNonNullElse(prefix, "");
        this.suffix = Objects.requireNonNullElse(suffix, "");
    }

    @Override
    public void serialize(
            final LocalizedValue<? extends Serializable> localizedValue,
            final JsonGenerator gen,
            final SerializerProvider serializers) {
        final UnaryOperator<String> keyMapper = key -> prefix + key + suffix;

        localizedValue
                .toMapWithLanguageTagKeys()
                .forEach(
                        ThrowingBiConsumer.sneaky(
                                (key, value) -> {
                                    final var mappedKey = keyMapper.apply(key);
                                    gen.writeFieldName(mappedKey);
                                    gen.writeObject(value);
                                }));
    }

    @Override
    public JsonSerializer<?> createContextual(
            final SerializerProvider prov, final BeanProperty property) {
        final var jsonUnwrapped = property.getAnnotation(JsonUnwrapped.class);
        if (jsonUnwrapped == null) {
            throw new IllegalStateException(
                    "Annotation 'JsonUnwrapped' not found on field " + property.getName());
        }
        return new JsonUnwrappedLocalizedValueSerializer(
                jsonUnwrapped.prefix(), jsonUnwrapped.suffix());
    }

    @Override
    public boolean isUnwrappingSerializer() {
        return true;
    }
}
