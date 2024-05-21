package ch.ubique.backend.core.hibernate.converter;

import ch.ubique.backend.core.jackson.ObjectMapperUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pivovarit.function.exception.WrappedException;

import jakarta.persistence.AttributeConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@SuppressWarnings("com.haulmont.jpb.ConverterNotAnnotatedInspection")
public class ObjectMapperToStringConverter<T> implements AttributeConverter<T, String> {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(ObjectMapperToStringConverter.class);

    private final ObjectMapper objectMapper = ObjectMapperUtils.objectMapper();

    private final TypeReference<T> typeReference;

    public ObjectMapperToStringConverter(final TypeReference<T> typeReference) {
        this.typeReference = typeReference;
    }

    @Override
    public String convertToDatabaseColumn(final T attributes) {
        if (attributes == null) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(attributes);
        } catch (final JsonProcessingException e) {
            throw new WrappedException(e);
        }
    }

    @Override
    public T convertToEntityAttribute(final String dbData) {
        if (dbData == null) {
            return null;
        }
        try {
            return objectMapper.readValue(dbData, typeReference);
        } catch (final IOException e) {
            LOGGER.info("Could not deserialize {} as {}.", dbData, typeReference, e);
            return null;
        }
    }
}
