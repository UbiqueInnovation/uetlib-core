package ch.ubique.backend.core.localized;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;

import org.junit.jupiter.api.Test;

import java.util.Locale;

class LocalizedValueTest {

    private final ObjectMapper objectMapper = new JsonMapper();

    @Test
    void serialization() throws JsonProcessingException {
        assertEquals(
                "{\"zh\":\"Literal\"}",
                objectMapper.writeValueAsString(LocalizedValue.from("Literal", Locale.CHINA)));
        assertEquals(
                "{\"zh\":\"Literal\"}",
                objectMapper.writeValueAsString(
                        LocalizedValue.builder().add("Literal", Locale.CHINA).build()));
        assertEquals(
                "{\"zh_CN\":\"Literal\"}",
                objectMapper.writeValueAsString(
                        LocalizedValue.builder()
                                .fullLocale()
                                .add("Literal", Locale.CHINA)
                                .build()));
        assertEquals(
                "{\"en\":\"Test\"}", objectMapper.writeValueAsString(LocalizedValue.en("Test")));
    }

    @Test
    void deserialization() throws JsonProcessingException {
        assertEquals(
                LocalizedValue.from("Literal", Locale.CHINA),
                objectMapper.readValue("{\"zh\":\"Literal\"}", LocalizedValue.class));
        assertEquals(
                LocalizedValue.builder().add("Literal", Locale.CHINA).build(),
                objectMapper.readValue("{\"zh\":\"Literal\"}", LocalizedValue.class));
        assertEquals(
                LocalizedValue.builder().fullLocale().add("Literal", Locale.CHINA).build(),
                objectMapper.readValue("{\"zh_CN\":\"Literal\"}", LocalizedValue.class));
        assertEquals(
                LocalizedValue.en("Test"),
                objectMapper.readValue("{\"en\":\"Test\"}", LocalizedValue.class));
    }

    @Test
    void builder() {
        final var builder = LocalizedValue.builder();
        assertThrows(NoValidValuesException.class, builder::build);
        assertNull(LocalizedValue.builder().buildNullable());
        assertTrue(LocalizedValue.builder().buildOptional().isEmpty());

        assertNull(LocalizedValue.builder().en(" ").buildNullable());
        assertThrows(IllegalArgumentException.class, () -> builder.add("abc", " "));

        assertNotNull(LocalizedValue.builder().de(".").buildNullable());
    }

    @Test
    void get() {
        final var lv1 = LocalizedValue.from("Literal", Locale.CHINA);
        assertNull(lv1.get((Locale) null));
        assertNull(lv1.get((String) null));
        assertNull(lv1.get(Locale.ENGLISH));
        assertEquals("Literal", lv1.get(Locale.CHINESE));
        assertEquals("Literal", lv1.get(Locale.CHINA));
        assertEquals("Literal", lv1.get(new Locale("zh", "CN", "XX")));

        final var lv2 = LocalizedValue.builder().fullLocale().add("Literal", Locale.CHINA).build();
        assertNull(lv2.get((Locale) null));
        assertNull(lv2.get((String) null));
        assertNull(lv2.get(Locale.ENGLISH));
        assertNull(lv2.get(Locale.CHINESE));
        assertEquals("Literal", lv2.get(Locale.CHINA));
        assertEquals("Literal", lv2.get(new Locale("zh", "CN", "XX")));

        final var lv3 =
                LocalizedValue.builder()
                        .fullLocale()
                        .add("Literal", new Locale("zh", "CN", "XX"))
                        .build();
        assertNull(lv3.get((Locale) null));
        assertNull(lv3.get((String) null));
        assertNull(lv3.get(Locale.ENGLISH));
        assertNull(lv3.get(Locale.CHINESE));
        assertNull(lv3.get(Locale.CHINA));
        assertEquals("Literal", lv3.get(new Locale("zh", "CN", "XX")));
    }

    @Test
    void values() throws JsonProcessingException {
        final var lv1 = LocalizedValue.from("Literal", Locale.CHINA);
        assertEquals(
                objectMapper.writeValueAsString(lv1),
                objectMapper.writeValueAsString(lv1.values()));
        final var lv2 = LocalizedValue.builder().fullLocale().add("Literal", Locale.CHINA).build();
        assertEquals(
                objectMapper.writeValueAsString(lv2),
                objectMapper.writeValueAsString(lv2.values()));

        final var lv3 = LocalizedValue.en("Test");
        assertEquals(
                objectMapper.writeValueAsString(lv3),
                objectMapper.writeValueAsString(lv3.values()));
    }
}
