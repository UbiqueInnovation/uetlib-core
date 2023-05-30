package ch.ubique.backend.core.localized;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

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
                        LocalizedValue.stringBuilder().add("Literal", Locale.CHINA).build()));
        assertEquals(
                "{\"zh-CN\":\"Literal\"}",
                objectMapper.writeValueAsString(
                        LocalizedValue.stringBuilder()
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
                LocalizedValue.stringBuilder().add("Literal", Locale.CHINA).build(),
                objectMapper.readValue("{\"zh\":\"Literal\"}", LocalizedValue.class));
        assertEquals(
                LocalizedValue.stringBuilder().fullLocale().add("Literal", Locale.CHINA).build(),
                objectMapper.readValue("{\"zh-CN\":\"Literal\"}", LocalizedValue.class));
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

        assertThrows(IllegalArgumentException.class, () -> builder.add(List.of(1), " "));

        assertNotNull(LocalizedValue.builder().en(List.of()).buildNullable());
        assertNotNull(LocalizedValue.builder().de(List.of(0)).buildNullable());
    }

    @Test
    void stringBuilder() {
        final var stringBuilder = LocalizedValue.stringBuilder();
        assertThrows(NoValidValuesException.class, stringBuilder::build);
        assertNull(LocalizedValue.stringBuilder().buildNullable());
        assertTrue(LocalizedValue.stringBuilder().buildOptional().isEmpty());

        assertNull(LocalizedValue.stringBuilder().en(" ").buildNullable());
        assertThrows(IllegalArgumentException.class, () -> stringBuilder.add("abc", " "));

        assertNotNull(LocalizedValue.stringBuilder().de(".").buildNullable());
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

        final var lv2 =
                LocalizedValue.stringBuilder().fullLocale().add("Literal", Locale.CHINA).build();
        assertNull(lv2.get((Locale) null));
        assertNull(lv2.get((String) null));
        assertNull(lv2.get(Locale.ENGLISH));
        assertNull(lv2.get(Locale.CHINESE));
        assertEquals("Literal", lv2.get(Locale.CHINA));
        assertEquals("Literal", lv2.get(new Locale("zh", "CN", "XX")));

        final var lv3 =
                LocalizedValue.stringBuilder()
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
        final var lv2 =
                LocalizedValue.stringBuilder().fullLocale().add("Literal", Locale.CHINA).build();
        assertEquals(
                objectMapper.writeValueAsString(lv2),
                objectMapper.writeValueAsString(lv2.values()));

        final var lv3 = LocalizedValue.en("Test");
        assertEquals(
                objectMapper.writeValueAsString(lv3),
                objectMapper.writeValueAsString(lv3.values()));
    }

    @Test
    void valuesWithLocaleKeys() {
        final var lv1 = LocalizedValue.from("Literal", Locale.CHINA);
        assertEquals(
                lv1.values().entrySet(),
                lv1.valuesWithLocaleKeys().entrySet().stream()
                        .map(entry -> Map.entry(entry.getKey().toLanguageTag(), entry.getValue()))
                        .collect(Collectors.toUnmodifiableSet()));

        final var lv2 =
                LocalizedValue.stringBuilder().fullLocale().add("Literal", Locale.CHINA).build();
        assertEquals(
                lv2.values().entrySet(),
                lv2.valuesWithLocaleKeys().entrySet().stream()
                        .map(entry -> Map.entry(entry.getKey().toLanguageTag(), entry.getValue()))
                        .collect(Collectors.toUnmodifiableSet()));

        final var lv3 = LocalizedValue.en("Test");
        assertEquals(
                lv3.values().entrySet(),
                lv3.valuesWithLocaleKeys().entrySet().stream()
                        .map(entry -> Map.entry(entry.getKey().toLanguageTag(), entry.getValue()))
                        .collect(Collectors.toUnmodifiableSet()));
    }
}
