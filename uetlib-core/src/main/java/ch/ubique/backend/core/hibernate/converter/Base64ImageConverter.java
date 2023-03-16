package ch.ubique.backend.core.hibernate.converter;

import jakarta.persistence.AttributeConverter;

import java.util.Base64;

public class Base64ImageConverter implements AttributeConverter<String, byte[]> {

    @Override
    public byte[] convertToDatabaseColumn(final String attribute) {
        return Base64.getDecoder().decode(attribute);
    }

    @Override
    public String convertToEntityAttribute(final byte[] dbData) {
        return Base64.getEncoder().encodeToString(dbData);
    }
}
