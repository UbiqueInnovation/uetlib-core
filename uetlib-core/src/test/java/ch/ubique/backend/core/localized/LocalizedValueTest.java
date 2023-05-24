package ch.ubique.backend.core.localized;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Locale;

class LocalizedValueTest {

    private final ObjectMapper objectMapper = new JsonMapper();

    @Test
    void testSerialization() throws JsonProcessingException {
        Assertions.assertEquals(
                "{\"zh\":\"Literal\"}",
                objectMapper.writeValueAsString(LocalizedValue.from("Literal", Locale.CHINESE)));
        Assertions.assertEquals(
                "{\"en\":\"Test\"}", objectMapper.writeValueAsString(LocalizedValue.en("Test")));
    }

    @Test
    void testDeserialization() throws JsonProcessingException {
        Assertions.assertEquals(
                LocalizedValue.from("Literal", Locale.CHINESE),
                objectMapper.readValue("{\"zh\":\"Literal\"}", LocalizedValue.class));
        Assertions.assertEquals(
                LocalizedValue.en("Test"),
                objectMapper.readValue("{\"en\":\"Test\"}", LocalizedValue.class));
    }

    @Test
    void testBuilder() {
        final var builder = LocalizedValue.builder();
        Assertions.assertThrows(NoValidValuesException.class, builder::build);
        Assertions.assertNull(LocalizedValue.builder().buildNullable());
        Assertions.assertTrue(LocalizedValue.builder().buildOptional().isEmpty());

        Assertions.assertNull(LocalizedValue.builder().en(" ").buildNullable());
        Assertions.assertThrows(IllegalArgumentException.class, () -> builder.add("abc", " "));

        Assertions.assertNotNull(LocalizedValue.builder().de(".").buildNullable());
    }
}
