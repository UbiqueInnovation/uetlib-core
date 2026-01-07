package ch.ubique.backend.core.hibernate.converter;

import ch.ubique.backend.core.jackson.ObjectMapperUtils;

import jakarta.persistence.AttributeConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tools.jackson.core.JacksonException;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

@SuppressWarnings("unused")
public abstract class ObjectMapperToStringConverter<T> implements AttributeConverter<T, String> {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final ObjectMapper objectMapper = ObjectMapperUtils.objectMapper();

    private final TypeReference<T> typeReference;

    protected ObjectMapperToStringConverter(final TypeReference<T> typeReference) {
        this.typeReference = typeReference;
    }

    @Override
    public String convertToDatabaseColumn(final T attributes) {
        if (attributes == null) {
            return null;
        }
        return objectMapper.writeValueAsString(attributes);
    }

    @Override
    public T convertToEntityAttribute(final String dbData) {
        if (dbData == null) {
            return null;
        }
        try {
            return objectMapper.readValue(dbData, typeReference);
        } catch (final JacksonException e) {
            logger.info("Could not deserialize {} as {}.", dbData, typeReference, e);
            return null;
        }
    }
}
