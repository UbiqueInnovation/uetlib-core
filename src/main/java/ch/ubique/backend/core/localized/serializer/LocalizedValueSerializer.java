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
import tools.jackson.databind.util.NameTransformer;

import java.io.Serializable;
import java.util.Map;
import java.util.function.UnaryOperator;

/**
 * Serializes a {@link LocalizedValue} to a JSON object. It supports both unwrapped properties
 * with @{@link JsonUnwrapped} and the different key serialization methods configured through
 * annotation @{@link JsonLocalizedValue#keySerialization()}.
 *
 * <p>For unwrapped serialization Jackson provides a {@link NameTransformer}. This serializer uses
 * that transformer when available so field-name rewriting stays aligned with Jackson's own
 * unwrapping mechanism.
 */
public class LocalizedValueSerializer
        extends ValueSerializer<LocalizedValue<? extends Serializable>> {

    private final NameTransformer nameTransformer;
    private final JsonLocalizedValue jsonLocalizedValue;

    public LocalizedValueSerializer() {
        this(null, null);
    }

    public LocalizedValueSerializer(
            final NameTransformer nameTransformer, final JsonLocalizedValue jsonLocalizedValue) {
        this.nameTransformer = nameTransformer;
        this.jsonLocalizedValue = jsonLocalizedValue;
    }

    @Override
    public void serialize(
            final LocalizedValue localizedValue,
            final JsonGenerator gen,
            final SerializationContext ctxt) {

        if (!isUnwrappingSerializer()) {
            gen.writeStartObject();
        }

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

        final UnaryOperator<String> keyMapper =
                isUnwrappingSerializer() ? nameTransformer::transform : UnaryOperator.identity();

        map.forEach(
                ThrowingBiConsumer.sneaky(
                        (key, value) -> {
                            gen.writeName(keyMapper.apply(key));
                            gen.writePOJO(value);
                        }));

        if (!isUnwrappingSerializer()) {
            gen.writeEndObject();
        }
    }

    @Override
    public boolean isUnwrappingSerializer() {
        return nameTransformer != null;
    }

    @Override
    public ValueSerializer<?> createContextual(
            final SerializationContext ctxt, final BeanProperty property) {
        if (property == null) {
            return new LocalizedValueSerializer();
        }

        final var jsonLocalizedValueAnnotation = property.getAnnotation(JsonLocalizedValue.class);
        return new LocalizedValueSerializer(nameTransformer, jsonLocalizedValueAnnotation);
    }

    @Override
    public ValueSerializer<LocalizedValue<? extends Serializable>> unwrappingSerializer(
            final NameTransformer transformer) {
        return new LocalizedValueSerializer(transformer, jsonLocalizedValue);
    }
}
