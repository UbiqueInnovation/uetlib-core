package ch.ubique.backend.core.localized;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;

import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.time.Instant;
import java.util.*;

@SuppressWarnings("DataFlowIssue")
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
                                .toMapWithPosixLocaleKeys()));
        assertEquals(
                "{\"ZH-CN\":\"Literal\"}",
                objectMapper.writeValueAsString(
                        LocalizedValue.stringBuilder()
                                .fullLocale()
                                .add("Literal", Locale.CHINA)
                                .build()
                                .toMapWithUppercaseKeys()));
        assertEquals(
                "{\"ZH\":\"Literal\"}",
                objectMapper.writeValueAsString(
                        LocalizedValue.stringBuilder()
                                .add("Literal", Locale.CHINA)
                                .build()
                                .toMapWithUppercaseKeys()));
        assertEquals(
                "{\"en\":\"Test\"}", objectMapper.writeValueAsString(LocalizedValue.en("Test")));

        assertEquals(
                "{\"de\":\"Test\"}", objectMapper.writeValueAsString(LocalizedValue.de("Test")));

        assertEquals(
                "{\"fr\":\"Test\"}", objectMapper.writeValueAsString(LocalizedValue.fr("Test")));

        assertEquals(
                "{\"it\":\"Test\"}", objectMapper.writeValueAsString(LocalizedValue.it("Test")));

        assertEquals(
                "{\"rm\":\"Test\"}",
                objectMapper.writeValueAsString(LocalizedValue.romansh("Test")));
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
        assertThrows(IllegalArgumentException.class, () -> builder.add((Serializable) list, " "));

        assertNotNull(LocalizedValue.builder().en((Serializable) List.of()).buildNullable());
        assertNotNull(LocalizedValue.builder().de((Serializable) List.of(0)).buildNullable());
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
        assertThrows(NoSuchElementException.class, () -> lv1.get((Locale) null));
        assertThrows(NoSuchElementException.class, () -> lv1.get((String) null));
        assertThrows(NoSuchElementException.class, () -> lv1.get(Locale.ENGLISH));
        assertThrows(NoSuchElementException.class, () -> lv1.get("en"));
        assertEquals("Literal", lv1.get(Locale.CHINESE));
        assertEquals("Literal", lv1.get(Locale.CHINA));
        assertEquals("Literal", lv1.get(new Locale("zh", "CN", "XX")));

        final var lv2 =
                LocalizedValue.stringBuilder().fullLocale().add("Literal", Locale.CHINA).build();
        assertThrows(NoSuchElementException.class, () -> lv2.get((Locale) null));
        assertThrows(NoSuchElementException.class, () -> lv2.get((String) null));
        assertThrows(NoSuchElementException.class, () -> lv2.get(Locale.ENGLISH));
        assertThrows(NoSuchElementException.class, () -> lv2.get("en"));
        assertThrows(NoSuchElementException.class, () -> lv2.get(Locale.CHINESE));
        assertThrows(NoSuchElementException.class, () -> lv2.get("zh"));
        assertEquals("Literal", lv2.get(Locale.CHINA));
        assertEquals("Literal", lv2.get(new Locale("zh", "CN", "XX")));

        final var lv3 =
                LocalizedValue.stringBuilder()
                        .fullLocale()
                        .add("Literal", new Locale("zh", "CN", "XX"))
                        .build();

        assertThrows(NoSuchElementException.class, () -> lv3.get((Locale) null));
        assertThrows(NoSuchElementException.class, () -> lv3.get((String) null));
        assertThrows(NoSuchElementException.class, () -> lv3.get(Locale.ENGLISH));
        assertThrows(NoSuchElementException.class, () -> lv3.get("en"));
        assertThrows(NoSuchElementException.class, () -> lv3.get(Locale.CHINESE));
        assertThrows(NoSuchElementException.class, () -> lv3.get("zh"));
        assertThrows(NoSuchElementException.class, () -> lv3.get(Locale.CHINA));
        assertThrows(NoSuchElementException.class, () -> lv3.get("zh-CN"));
        assertThrows(NoSuchElementException.class, () -> lv3.get("zh_CN"));
        assertEquals("Literal", lv3.get(new Locale("zh", "CN", "XX")));
    }

    @Test
    void getNullable() {
        final var lv1 = LocalizedValue.from("Literal", Set.of(Locale.CHINA, Locale.GERMANY));
        assertNull(lv1.getNullable((Locale) null));
        assertNull(lv1.getNullable((String) null));
        assertNull(lv1.getNullable(Locale.ENGLISH));
        assertNull(lv1.getNullable("en"));
        assertEquals("Literal", lv1.getNullable(Locale.CHINESE));
        assertEquals("Literal", lv1.getNullable(Locale.CHINA));
        assertEquals("Literal", lv1.getNullable(new Locale("zh", "CN", "XX")));

        final var lv2 =
                LocalizedValue.stringBuilder().fullLocale().add("Literal", Locale.CHINA).build();

        assertNull(lv2.getNullable((Locale) null));
        assertNull(lv2.getNullable((String) null));
        assertNull(lv2.getNullable(Locale.ENGLISH));
        assertNull(lv2.getNullable("en"));
        assertNull(lv2.getNullable(Locale.CHINESE));
        assertNull(lv2.getNullable("zh"));
        assertEquals("Literal", lv2.getNullable(Locale.CHINA));
        assertEquals("Literal", lv2.getNullable(new Locale("zh", "CN", "XX")));

        final var lv3 =
                LocalizedValue.stringBuilder()
                        .fullLocale()
                        .add("Literal", new Locale("zh", "CN", "XX"))
                        .build();

        assertNull(lv3.getNullable((Locale) null));
        assertNull(lv3.getNullable((String) null));
        assertNull(lv3.getNullable(Locale.ENGLISH));
        assertNull(lv3.getNullable("en"));
        assertNull(lv3.getNullable(Locale.CHINESE));
        assertNull(lv3.getNullable("zh"));
        assertNull(lv3.getNullable(Locale.CHINA));
        assertNull(lv3.getNullable("zh-CN"));
        assertNull(lv3.getNullable("zh_CN"));
        assertEquals("Literal", lv3.getNullable(new Locale("zh", "CN", "XX")));
    }

    @Test
    void getOptional() {
        final var lv1 = LocalizedValue.from("Literal", Locale.CHINA);
        assertEquals(Optional.empty(), lv1.getOptional((Locale) null));
        assertEquals(Optional.empty(), lv1.getOptional((String) null));
        assertEquals(Optional.empty(), lv1.getOptional(Locale.ENGLISH));
        assertEquals(Optional.empty(), lv1.getOptional("en"));
        assertEquals(Optional.of("Literal"), lv1.getOptional(Locale.CHINESE));
        assertEquals(Optional.of("Literal"), lv1.getOptional(Locale.CHINA));
        assertEquals(Optional.of("Literal"), lv1.getOptional(new Locale("zh", "CN", "XX")));

        final var lv2 =
                LocalizedValue.stringBuilder().fullLocale().add("Literal", Locale.CHINA).build();
        assertEquals(Optional.empty(), lv2.getOptional((Locale) null));
        assertEquals(Optional.empty(), lv2.getOptional((String) null));
        assertEquals(Optional.empty(), lv2.getOptional(Locale.ENGLISH));
        assertEquals(Optional.empty(), lv2.getOptional("en"));
        assertEquals(Optional.empty(), lv2.getOptional(Locale.CHINESE));
        assertEquals(Optional.empty(), lv2.getOptional("zh"));
        assertEquals(Optional.of("Literal"), lv2.getOptional(Locale.CHINA));
        assertEquals(Optional.of("Literal"), lv2.getOptional(new Locale("zh", "CN", "XX")));

        final var lv3 =
                LocalizedValue.stringBuilder()
                        .fullLocale()
                        .add("Literal", new Locale("zh", "CN", "XX"))
                        .build();
        assertEquals(Optional.empty(), lv3.getOptional((Locale) null));
        assertEquals(Optional.empty(), lv3.getOptional((String) null));
        assertEquals(Optional.empty(), lv3.getOptional(Locale.ENGLISH));
        assertEquals(Optional.empty(), lv3.getOptional("en"));
        assertEquals(Optional.empty(), lv3.getOptional(Locale.CHINESE));
        assertEquals(Optional.empty(), lv3.getOptional("zh"));
        assertEquals(Optional.empty(), lv3.getOptional(Locale.CHINA));
        assertEquals(Optional.empty(), lv3.getOptional("zh-CN"));
        assertEquals(Optional.empty(), lv3.getOptional("zh_CN"));

        assertEquals(Optional.of("Literal"), lv3.getOptional(new Locale("zh", "CN", "XX")));
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
                "{\"zh\":\"Literal\"}",
                objectMapper.writeValueAsString(lv1.toMapWithPosixLocaleKeys()));
        final var lv2 =
                LocalizedValue.stringBuilder().fullLocale().add("Literal", Locale.CHINA).build();
        assertEquals(
                "{\"zh_CN\":\"Literal\"}",
                objectMapper.writeValueAsString(lv2.toMapWithPosixLocaleKeys()));

        final var lv3 = LocalizedValue.en("Test");
        assertEquals(
                "{\"en\":\"Test\"}",
                objectMapper.writeValueAsString(lv3.toMapWithPosixLocaleKeys()));
    }

    @Test
    void jsonWithUppercaseKeys() throws JsonProcessingException {
        final var lv1 = LocalizedValue.from("Literal", Locale.CHINA);
        assertEquals(
                "{\"ZH\":\"Literal\"}",
                objectMapper.writeValueAsString(lv1.toMapWithUppercaseKeys()));
        final var lv2 =
                LocalizedValue.stringBuilder().fullLocale().add("Literal", Locale.CHINA).build();
        assertEquals(
                "{\"ZH-CN\":\"Literal\"}",
                objectMapper.writeValueAsString(lv2.toMapWithUppercaseKeys()));

        final var lv3 = LocalizedValue.en("Test");
        assertEquals(
                "{\"EN\":\"Test\"}", objectMapper.writeValueAsString(lv3.toMapWithUppercaseKeys()));
    }

    @Test
    void values() {
        final var lv1 = LocalizedValue.from("Literal", Locale.CHINA);
        assertEquals(Map.of(Locale.forLanguageTag("zh"), "Literal"), lv1.toMapWithLocaleKeys());

        final var lv2 =
                LocalizedValue.stringBuilder().fullLocale().add("Literal", Locale.CHINA).build();
        assertEquals(Map.of(Locale.forLanguageTag("zh-CN"), "Literal"), lv2.toMapWithLocaleKeys());

        final var lv3 = LocalizedValue.en("Test");
        assertEquals(Map.of(Locale.forLanguageTag("en"), "Test"), lv3.toMapWithLocaleKeys());
    }

    @Test
    void withLanguageTagKeys() {
        final var lv1 = LocalizedValue.from("Literal", Locale.CHINA);
        assertEquals(Map.of("zh", "Literal"), lv1.toMapWithLanguageTagKeys());

        final var lv2 =
                LocalizedValue.stringBuilder().fullLocale().add("Literal", Locale.CHINA).build();
        assertEquals(Map.of("zh-CN", "Literal"), lv2.toMapWithLanguageTagKeys());

        final var lv3 = LocalizedValue.en("Test");
        assertEquals(Map.of("en", "Test"), lv3.toMapWithLanguageTagKeys());
    }

    @Test
    void withPosixLocaleKeys() {
        final var lv1 = LocalizedValue.from("Literal", Locale.CHINA);
        assertEquals(Map.of("zh", "Literal"), lv1.toMapWithPosixLocaleKeys());

        final var lv2 =
                LocalizedValue.stringBuilder().fullLocale().add("Literal", Locale.CHINA).build();
        assertEquals(Map.of("zh_CN", "Literal"), lv2.toMapWithPosixLocaleKeys());

        final var lv3 = LocalizedValue.en("Test");
        assertEquals(Map.of("en", "Test"), lv3.toMapWithPosixLocaleKeys());
    }

    @Test
    void withUppercaseKeys() {
        final var lv1 = LocalizedValue.from("Literal", Locale.CHINA);
        assertEquals(Map.of("ZH", "Literal"), lv1.toMapWithUppercaseKeys());

        final var lv2 =
                LocalizedValue.stringBuilder().fullLocale().add("Literal", Locale.CHINA).build();
        assertEquals(Map.of("ZH-CN", "Literal"), lv2.toMapWithUppercaseKeys());

        final var lv3 = LocalizedValue.fromMap(Map.of(Locale.US, "Test"));
        assertEquals(Map.of("EN", "Test"), lv3.toMapWithUppercaseKeys());
    }

    @Test
    void coalesce() {
        final var sample = LocalizedValue.stringBuilder().romansh("rm").de("de").build();

        assertEquals("rm", sample.coalesce("es", "it", "rm"));
        assertThrows(NoSuchElementException.class, () -> sample.coalesce("es", "it"));
        assertEquals("de", sample.coalesce(Locale.US, Locale.GERMANY, Locale.CHINA));
        assertThrows(NoSuchElementException.class, () -> sample.coalesce(Locale.US, Locale.CHINA));
    }

    @Test
    void coalesceOrGetAny() {
        final var sample = LocalizedValue.fromStringMap(Map.of("rm", "rm"));

        assertEquals("rm", sample.coalesceOrGetAny("es", "it", "en"));
        assertEquals("rm", sample.coalesceOrGetAny(Locale.US, Locale.CHINA, Locale.GERMANY));
    }

    @Test
    void coalesceOptional() {
        final var sample = LocalizedValue.stringBuilder().romansh("rm").de("de").fr("fr").build();

        assertEquals(Optional.of("rm"), sample.coalesceOptional("es", "it", "rm"));
        assertFalse(sample.coalesceOptional("es", "it").isPresent());
        assertEquals(
                Optional.of("de"),
                sample.coalesceOptional(Locale.US, Locale.GERMANY, Locale.CHINA));
        assertFalse(sample.coalesceOptional(Locale.US, Locale.CHINA).isPresent());
    }

    @Test
    void getAny() {
        assertDoesNotThrow(LocalizedValue.romansh("rm")::getAny);
        assertDoesNotThrow(LocalizedValue.fr("fr")::getAny);
        assertEquals("rm", LocalizedValue.romansh("rm").getAny());
    }

    @SuppressWarnings("EqualsWithItself")
    @Test
    void testEqualsAndHashCode() {
        final var a = LocalizedValue.stringBuilder().en("en").de("de").fr("fr").build();
        final var b = LocalizedValue.stringBuilder().en("en").de("de").fr("fr").build();
        final var c = LocalizedValue.stringBuilder().en("en").de("de").it("it").build();

        assertEquals(a, a);
        assertEquals(b, b);
        assertEquals(c, c);

        assertEquals(a, b);
        assertEquals(b, a);

        assertNotEquals(a, c);
        assertNotEquals(b, c);
        assertNotEquals(c, a);
        assertNotEquals(c, b);

        assertEquals(a.hashCode(), a.hashCode());
        assertEquals(b.hashCode(), b.hashCode());
        assertEquals(c.hashCode(), c.hashCode());

        assertEquals(a.hashCode(), b.hashCode());
        assertEquals(b.hashCode(), a.hashCode());

        assertNotEquals(a.hashCode(), c.hashCode());
        assertNotEquals(b.hashCode(), c.hashCode());
        assertNotEquals(c.hashCode(), a.hashCode());
        assertNotEquals(c.hashCode(), b.hashCode());

        assertTrue(Set.of(a, c).contains(b));

        assertThrows(
                IllegalArgumentException.class,
                () -> Set.of(a, b)); // IllegalArgumentException: duplicate element
    }

    @Test
    void toStringTest() {
        final var a = LocalizedValue.stringBuilder().en("en").de("de").fr("fr").build();
        final var b = LocalizedValue.stringBuilder().en("en").de("de").fr("fr").build();
        final var c = LocalizedValue.stringBuilder().en("en").de("de").it("it").build();

        assertEquals(a.toString(), b.toString());
        assertNotEquals(a.toString(), c.toString());

        assertEquals("LocalizedValue[values={de=de, fr=fr, en=en}]", a.toString());
        assertEquals("LocalizedValue[values={de=de, en=en, it=it}]", c.toString());
    }

    @Test
    void testGetLanguage() {
        final var now = Instant.now();
        final var nowPlusMinute = now.plusSeconds(60);
        final var localizedInstant =
                LocalizedValue.<Instant>builder().en(now).fr(nowPlusMinute).build();

        assertEquals(now, localizedInstant.en());
        assertEquals(nowPlusMinute, localizedInstant.fr());

        assertEquals(now, localizedInstant.enNullable());
        assertEquals(nowPlusMinute, localizedInstant.frNullable());

        assertEquals(Optional.of(now), localizedInstant.enOptional());
        assertEquals(Optional.of(nowPlusMinute), localizedInstant.frOptional());

        assertThrows(NoSuchElementException.class, localizedInstant::de);
        assertThrows(NoSuchElementException.class, localizedInstant::it);
        assertThrows(NoSuchElementException.class, localizedInstant::romansh);

        assertNull(localizedInstant.deNullable());
        assertNull(localizedInstant.itNullable());
        assertNull(localizedInstant.romanshNullable());

        assertFalse(localizedInstant.deOptional().isPresent());
        assertFalse(localizedInstant.itOptional().isPresent());
        assertFalse(localizedInstant.romanshOptional().isPresent());
    }

    @Test
    void testGetKeysNotSwapped() {

        final var localizedString =
                LocalizedValue.fromStringMap(
                        Map.ofEntries(
                                Map.entry("de", "de"),
                                Map.entry("en", "en"),
                                Map.entry("fr", "fr"),
                                Map.entry("rm", "rm"),
                                Map.entry("it", "it")));

        assertEquals("en", localizedString.en());
        assertEquals("fr", localizedString.fr());
        assertEquals("it", localizedString.it());
        assertEquals("de", localizedString.de());
        assertEquals("rm", localizedString.romansh());

        assertEquals("en", localizedString.enNullable());
        assertEquals("fr", localizedString.frNullable());
        assertEquals("it", localizedString.itNullable());
        assertEquals("de", localizedString.deNullable());
        assertEquals("rm", localizedString.romanshNullable());

        assertEquals(Optional.of("en"), localizedString.enOptional());
        assertEquals(Optional.of("fr"), localizedString.frOptional());
        assertEquals(Optional.of("it"), localizedString.itOptional());
        assertEquals(Optional.of("de"), localizedString.deOptional());
        assertEquals(Optional.of("rm"), localizedString.romanshOptional());
    }

    @Test
    void mergeAll() {
        final var de1 = LocalizedValue.de("de1");
        final var de2 = LocalizedValue.de("de2");
        final var en1 = LocalizedValue.en("en1");
        final var de1en1 = LocalizedValue.stringBuilder().de("de1").en("en1").build();
        final var de2en1 = LocalizedValue.stringBuilder().de("de2").en("en1").build();

        assertThrows(NullPointerException.class, () -> LocalizedValue.mergeAll(null));
        final var emptyList = List.<LocalizedValue<Serializable>>of();
        assertThrows(NoValidValuesException.class, () -> LocalizedValue.mergeAll(emptyList));

        assertEquals(de1, LocalizedValue.mergeAll(List.of(de1)));
        assertEquals(de1, LocalizedValue.mergeAll(List.of(de2, de1)));
        assertEquals(de2, LocalizedValue.mergeAll(List.of(de1, de2)));
        assertEquals(de1, LocalizedValue.mergeAll(List.of(de2, de1, de1)));
        assertEquals(de1, LocalizedValue.mergeAll(List.of(de1, de2, de1)));
        assertEquals(de2, LocalizedValue.mergeAll(List.of(de1, de1, de2)));
        assertEquals(de2en1, LocalizedValue.mergeAll(List.of(de1, en1, de2)));


        assertThrows(NullPointerException.class, () -> de1.mergeWith(null));

        assertEquals(de1, de1.mergeWith(de1));
        assertEquals(de1, de2.mergeWith(de1));
        assertEquals(de2, de1.mergeWith(de2));
        assertEquals(de1en1, de1.mergeWith(en1));
    }
}
