package ch.ubique.uetlib.core.hibernate.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;

import java.util.List;

class ObjectMapperToStringConverterTest {

    private final ObjectMapper objectMapper = JsonMapper.builder().build();

    private final ObjectMapperToStringConverter<List<String>> converter =
            new ObjectMapperToStringConverter<>(objectMapper, new TypeReference<>() {}) {};

    @Test
    void convertToDatabaseColumn_Value_ShouldSerializeToJson() {
        assertEquals("[\"a\",\"b\"]", converter.convertToDatabaseColumn(List.of("a", "b")));
    }

    @Test
    void convertToDatabaseColumn_Null_ShouldReturnNull() {
        assertNull(converter.convertToDatabaseColumn(null));
    }

    @Test
    void convertToEntityAttribute_Json_ShouldDeserialize() {
        assertEquals(List.of("a", "b"), converter.convertToEntityAttribute("[\"a\",\"b\"]"));
    }

    @Test
    void convertToEntityAttribute_Null_ShouldReturnNull() {
        assertNull(converter.convertToEntityAttribute(null));
    }

    @Test
    void convertToEntityAttribute_InvalidJson_ShouldReturnNull() {
        assertNull(converter.convertToEntityAttribute("this is not json"));
    }

    @Test
    void roundTrip_ShouldPreserveValue() {
        final var original = List.of("one", "two", "three");

        final var serialized = converter.convertToDatabaseColumn(original);
        final var deserialized = converter.convertToEntityAttribute(serialized);

        assertEquals(original, deserialized);
    }
}
