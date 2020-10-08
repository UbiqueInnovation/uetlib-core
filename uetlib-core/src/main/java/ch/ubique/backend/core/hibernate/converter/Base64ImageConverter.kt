package ch.ubique.backend.core.hibernate.converter

import java.util.*
import javax.persistence.AttributeConverter
import javax.persistence.Converter

@Converter(autoApply = true)
class Base64ImageConverter : AttributeConverter<String?, ByteArray> {
    override fun convertToDatabaseColumn(attribute: String?): ByteArray {
        return Base64.getDecoder().decode(attribute)
    }

    override fun convertToEntityAttribute(dbData: ByteArray): String? {
        return Base64.getEncoder().encodeToString(dbData)
    }
}