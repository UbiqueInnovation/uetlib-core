package ch.ubique.backend.core.hibernate.converter;

import java.util.Base64;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class Base64ImageConverter implements AttributeConverter<String, byte[]> {

    @Override
    public byte[] convertToDatabaseColumn(String attribute) {
        return Base64.getDecoder().decode(attribute);
    }

    @Override
    public String convertToEntityAttribute(byte[] dbData) {
        return Base64.getEncoder().encodeToString(dbData);
    }
}
