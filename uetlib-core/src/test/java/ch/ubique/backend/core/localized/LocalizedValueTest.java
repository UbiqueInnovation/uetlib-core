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
                "{\"zh_CN\":\"Literal\"}",
                objectMapper.writeValueAsString(
                        LocalizedValue.stringBuilder()
                                .fullLocale()
                                .add("Literal", Locale.CHINA)
                                .build()
                                .withPosixLocaleKeys()));
        assertEquals(
                "{\"ZH-CN\":\"Literal\"}",
                objectMapper.writeValueAsString(
                        LocalizedValue.stringBuilder()
                                .fullLocale()
                                .add("Literal", Locale.CHINA)
                                .build()
                                .withUppercaseKeys()));
        assertEquals(
                "{\"ZH\":\"Literal\"}",
                objectMapper.writeValueAsString(
                        LocalizedValue.stringBuilder()
                                .add("Literal", Locale.CHINA)
                                .build()
                                .withUppercaseKeys()));
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

        assertEquals(
                LocalizedValue.from("Literal", Locale.CHINA),
                objectMapper.readValue("{\"ZH\":\"Literal\"}", LocalizedValue.class));
        assertEquals(
                LocalizedValue.stringBuilder().fullLocale().add("Literal", Locale.CHINA).build(),
                objectMapper.readValue("{\"ZH-CN\":\"Literal\"}", LocalizedValue.class));
    }

    @Test
    void builder() {
        final var builder = LocalizedValue.builder();
        assertThrows(NoValidValuesException.class, builder::build);
        assertNull(LocalizedValue.builder().buildNullable());
        assertTrue(LocalizedValue.builder().buildOptional().isEmpty());

        final var list = List.of(1);
        assertThrows(IllegalArgumentException.class, () -> builder.add(list, " "));

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
        assertNull(lv1.get("en"));
        assertEquals("Literal", lv1.get(Locale.CHINESE));
        assertEquals("Literal", lv1.get(Locale.CHINA));
        assertEquals("Literal", lv1.get(new Locale("zh", "CN", "XX")));

        final var lv2 =
                LocalizedValue.stringBuilder().fullLocale().add("Literal", Locale.CHINA).build();
        assertNull(lv2.get((Locale) null));
        assertNull(lv2.get((String) null));
        assertNull(lv2.get(Locale.ENGLISH));
        assertNull(lv2.get("en"));
        assertNull(lv2.get(Locale.CHINESE));
        assertNull(lv2.get("zh"));
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
        assertNull(lv3.get("en"));
        assertNull(lv3.get(Locale.CHINESE));
        assertNull(lv3.get("zh"));
        assertNull(lv3.get(Locale.CHINA));
        assertNull(lv3.get("zh-CN"));
        assertNull(lv3.get("zh_CN"));
        assertEquals("Literal", lv3.get(new Locale("zh", "CN", "XX")));
    }

    @Test
    void json() throws JsonProcessingException {
        final var lv1 = LocalizedValue.from("Literal", Locale.CHINA);
        assertEquals("{\"zh\":\"Literal\"}", objectMapper.writeValueAsString(lv1));
        final var lv2 =
                LocalizedValue.stringBuilder().fullLocale().add("Literal", Locale.CHINA).build();
        assertEquals("{\"zh-CN\":\"Literal\"}", objectMapper.writeValueAsString(lv2));

        final var lv3 = LocalizedValue.en("Test");
        assertEquals("{\"en\":\"Test\"}", objectMapper.writeValueAsString(lv3));
    }

    @Test
    void jsonWithPosixLocaleKeys() throws JsonProcessingException {
        final var lv1 = LocalizedValue.from("Literal", Locale.CHINA);
        assertEquals(
                "{\"zh\":\"Literal\"}", objectMapper.writeValueAsString(lv1.withPosixLocaleKeys()));
        final var lv2 =
                LocalizedValue.stringBuilder().fullLocale().add("Literal", Locale.CHINA).build();
        assertEquals(
                "{\"zh_CN\":\"Literal\"}",
                objectMapper.writeValueAsString(lv2.withPosixLocaleKeys()));

        final var lv3 = LocalizedValue.en("Test");
        assertEquals(
                "{\"en\":\"Test\"}", objectMapper.writeValueAsString(lv3.withPosixLocaleKeys()));
    }

    @Test
    void jsonWithUppercaseKeys() throws JsonProcessingException {
        final var lv1 = LocalizedValue.from("Literal", Locale.CHINA);
        assertEquals(
                "{\"ZH\":\"Literal\"}", objectMapper.writeValueAsString(lv1.withUppercaseKeys()));
        final var lv2 =
                LocalizedValue.stringBuilder().fullLocale().add("Literal", Locale.CHINA).build();
        assertEquals(
                "{\"ZH-CN\":\"Literal\"}",
                objectMapper.writeValueAsString(lv2.withUppercaseKeys()));

        final var lv3 = LocalizedValue.en("Test");
        assertEquals("{\"EN\":\"Test\"}", objectMapper.writeValueAsString(lv3.withUppercaseKeys()));
    }

    @Test
    void values() {
        final var lv1 = LocalizedValue.from("Literal", Locale.CHINA);
        assertEquals(Map.of(Locale.forLanguageTag("zh"), "Literal"), lv1.values());

        final var lv2 =
                LocalizedValue.stringBuilder().fullLocale().add("Literal", Locale.CHINA).build();
        assertEquals(Map.of(Locale.forLanguageTag("zh-CN"), "Literal"), lv2.values());

        final var lv3 = LocalizedValue.en("Test");
        assertEquals(Map.of(Locale.forLanguageTag("en"), "Test"), lv3.values());
    }

    @Test
    void withLanguageTagKeys() {
        final var lv1 = LocalizedValue.from("Literal", Locale.CHINA);
        assertEquals(Map.of("zh", "Literal"), lv1.withLanguageTagKeys());

        final var lv2 =
                LocalizedValue.stringBuilder().fullLocale().add("Literal", Locale.CHINA).build();
        assertEquals(Map.of("zh-CN", "Literal"), lv2.withLanguageTagKeys());

        final var lv3 = LocalizedValue.en("Test");
        assertEquals(Map.of("en", "Test"), lv3.withLanguageTagKeys());
    }

    @Test
    void withPosixLocaleKeys() {
        final var lv1 = LocalizedValue.from("Literal", Locale.CHINA);
        assertEquals(Map.of("zh", "Literal"), lv1.withPosixLocaleKeys());

        final var lv2 =
                LocalizedValue.stringBuilder().fullLocale().add("Literal", Locale.CHINA).build();
        assertEquals(Map.of("zh_CN", "Literal"), lv2.withPosixLocaleKeys());

        final var lv3 = LocalizedValue.en("Test");
        assertEquals(Map.of("en", "Test"), lv3.withPosixLocaleKeys());
    }

    @Test
    void withUppercaseKeys() {
        final var lv1 = LocalizedValue.from("Literal", Locale.CHINA);
        assertEquals(Map.of("ZH", "Literal"), lv1.withUppercaseKeys());

        final var lv2 =
                LocalizedValue.stringBuilder().fullLocale().add("Literal", Locale.CHINA).build();
        assertEquals(Map.of("ZH-CN", "Literal"), lv2.withUppercaseKeys());

        final var lv3 = LocalizedValue.en("Test");
        assertEquals(Map.of("EN", "Test"), lv3.withUppercaseKeys());
    }
}
