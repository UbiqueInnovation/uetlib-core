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
        final var v1 = LocalizedValue.en("Test");
        LocalizedValue.from("Literal", Locale.CHINESE);
        Assertions.assertEquals("{\"en\":\"Test\"}", objectMapper.writeValueAsString(v1));
    }

    @Test
    void testBuilder() {
        Assertions.assertThrows(
                NoValidValuesException.class, () -> LocalizedValue.builder().build());
        Assertions.assertNull(LocalizedValue.builder().buildNullable());
        Assertions.assertTrue(LocalizedValue.builder().buildOptional().isEmpty());

        Assertions.assertNull(LocalizedValue.builder().en(" ").buildNullable());
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> LocalizedValue.builder().add("abc", " ").build());

        Assertions.assertNotNull(LocalizedValue.builder().de(".").buildNullable());
    }
}
