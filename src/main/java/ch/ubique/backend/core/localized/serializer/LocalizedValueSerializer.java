package ch.ubique.backend.core.localized.serializer;

import ch.ubique.backend.core.localized.LocalizedValue;
import ch.ubique.backend.core.localized.annotation.JsonLocalizedValue;
import ch.ubique.backend.core.localized.annotation.JsonLocalizedValue.SerializationMethod;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map;

public class LocalizedValueSerializer extends JsonSerializer<LocalizedValue<? extends Serializable>>
        implements ContextualSerializer {

    private final SerializationMethod serializationMethod;

    @SuppressWarnings("unused") // used by Jackson to instantiate the contextual serializer
    public LocalizedValueSerializer() {
        this(null);
    }

    public LocalizedValueSerializer(final SerializationMethod serializationMethod) {
        this.serializationMethod = serializationMethod;
    }

    @Override
    public void serialize(
            final LocalizedValue value,
            final JsonGenerator gen,
            final SerializerProvider serializers)
            throws IOException {
        // Get the serialization method from the annotation on the field/method
        final Map<String, Object> map =
                switch (serializationMethod) {
                    case DEFAULT -> value.toMapWithLanguageTagKeys();
                    case POSIX -> value.toMapWithPosixLocaleKeys();
                    case UPPERCASE -> value.toMapWithUppercaseKeys();
                };

        gen.writeObject(map);
    }

    @Override
    public JsonSerializer<?> createContextual(
            final SerializerProvider prov, final BeanProperty property) {
        final var jsonLocalizedValue = property.getAnnotation(JsonLocalizedValue.class);
        if (jsonLocalizedValue == null) {
            throw new IllegalStateException(
                    "Annotation 'JsonLocalizedValue' not found on field/method "
                            + property.getName());
        }
        return new LocalizedValueSerializer(jsonLocalizedValue.serializationMethod());
    }
}
