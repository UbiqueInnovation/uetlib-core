package ch.ubique.backend.core.localized;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import java.util.Locale;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class LocalizedValueTest {

    private final ObjectMapper objectMapper = new JsonMapper();

    @Test
    void testSerialization() throws JsonProcessingException {
        final var v1 = LocalizedString.en("Test");
        LocalizedString.from("Literal", Locale.CHINESE);
        Assertions.assertEquals("{\"en\":\"Test\"}", objectMapper.writeValueAsString(v1));
    }

    @Test
    void testBuilder() {
        Assertions.assertThrows(
                NoValidValuesException.class, () -> LocalizedValue.builder().build());
        Assertions.assertNull(LocalizedValue.builder().buildNullable());
        Assertions.assertTrue(LocalizedValue.builder().buildOptional().isEmpty());

        Assertions.assertNull(LocalizedString.builder().en(" ").buildNullable());
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> LocalizedString.builder().add(" ", "abc").build());

        Assertions.assertNotNull(LocalizedString.builder().de(".").buildNullable());
    }
}
