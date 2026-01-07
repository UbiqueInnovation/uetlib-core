package ch.ubique.backend.core.localized;

import static ch.ubique.backend.core.localized.AdditionalLocales.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import static java.util.Map.entry;
import static java.util.Map.ofEntries;

import ch.ubique.backend.core.localized.annotation.JsonLocalizedValue;
import ch.ubique.backend.core.localized.annotation.JsonLocalizedValue.SerializationMethod;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

import org.junit.jupiter.api.Test;

import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.SerializationFeature;
import tools.jackson.databind.json.JsonMapper;

import java.io.Serializable;
import java.time.Instant;
import java.util.*;

@SuppressWarnings("DataFlowIssue")
class LocalizedValueTest {

    private final ObjectMapper objectMapper =
            JsonMapper.builder().enable(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS).build();

    @Test
    void serialization() {
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
    void deserialization() {
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
        assertEquals("Literal", lv1.get(Locale.of("zh", "CN", "XX")));

        final var lv2 =
                LocalizedValue.stringBuilder().fullLocale().add("Literal", Locale.CHINA).build();
        assertThrows(NoSuchElementException.class, () -> lv2.get((Locale) null));
        assertThrows(NoSuchElementException.class, () -> lv2.get((String) null));
        assertThrows(NoSuchElementException.class, () -> lv2.get(Locale.ENGLISH));
        assertThrows(NoSuchElementException.class, () -> lv2.get("en"));
        assertThrows(NoSuchElementException.class, () -> lv2.get(Locale.CHINESE));
        assertThrows(NoSuchElementException.class, () -> lv2.get("zh"));
        assertEquals("Literal", lv2.get(Locale.CHINA));
        assertEquals("Literal", lv2.get(Locale.of("zh", "CN", "XX")));

        final var lv3 =
                LocalizedValue.stringBuilder()
                        .fullLocale()
                        .add("Literal", Locale.of("zh", "CN", "XX"))
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
        assertEquals("Literal", lv3.get(Locale.of("zh", "CN", "XX")));
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
        assertEquals("Literal", lv1.getNullable(Locale.of("zh", "CN", "XX")));

        final var lv2 =
                LocalizedValue.stringBuilder().fullLocale().add("Literal", Locale.CHINA).build();

        assertNull(lv2.getNullable((Locale) null));
        assertNull(lv2.getNullable((String) null));
        assertNull(lv2.getNullable(Locale.ENGLISH));
        assertNull(lv2.getNullable("en"));
        assertNull(lv2.getNullable(Locale.CHINESE));
        assertNull(lv2.getNullable("zh"));
        assertEquals("Literal", lv2.getNullable(Locale.CHINA));
        assertEquals("Literal", lv2.getNullable(Locale.of("zh", "CN", "XX")));

        final var lv3 =
                LocalizedValue.stringBuilder()
                        .fullLocale()
                        .add("Literal", Locale.of("zh", "CN", "XX"))
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
        assertEquals("Literal", lv3.getNullable(Locale.of("zh", "CN", "XX")));
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
        assertEquals(Optional.of("Literal"), lv1.getOptional(Locale.of("zh", "CN", "XX")));

        final var lv2 =
                LocalizedValue.stringBuilder().fullLocale().add("Literal", Locale.CHINA).build();
        assertEquals(Optional.empty(), lv2.getOptional((Locale) null));
        assertEquals(Optional.empty(), lv2.getOptional((String) null));
        assertEquals(Optional.empty(), lv2.getOptional(Locale.ENGLISH));
        assertEquals(Optional.empty(), lv2.getOptional("en"));
        assertEquals(Optional.empty(), lv2.getOptional(Locale.CHINESE));
        assertEquals(Optional.empty(), lv2.getOptional("zh"));
        assertEquals(Optional.of("Literal"), lv2.getOptional(Locale.CHINA));
        assertEquals(Optional.of("Literal"), lv2.getOptional(Locale.of("zh", "CN", "XX")));

        final var lv3 =
                LocalizedValue.stringBuilder()
                        .fullLocale()
                        .add("Literal", Locale.of("zh", "CN", "XX"))
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

        assertEquals(Optional.of("Literal"), lv3.getOptional(Locale.of("zh", "CN", "XX")));
    }

    @Test
    void json() {
        final var lv1 = LocalizedValue.from("Literal", Locale.CHINA);
        assertEquals(
                "{\"zh\":\"Literal\"}",
                objectMapper.writeValueAsString(lv1.toMapWithLanguageTagKeys()));
        final var lv2 =
                LocalizedValue.stringBuilder().fullLocale().add("Literal", Locale.CHINA).build();
        assertEquals("{\"zh-CN\":\"Literal\"}", objectMapper.writeValueAsString(lv2));

        final var lv3 = LocalizedValue.en("Test");
        assertEquals("{\"en\":\"Test\"}", objectMapper.writeValueAsString(lv3));
    }

    @Test
    void jsonWithPosixLocaleKeys() {
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
    void jsonWithUppercaseKeys() {
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

        // cannot test notEquals, cannot rely on it being unequal

        assertTrue(Set.of(a, c).contains(b));

        assertThrows(
                IllegalArgumentException.class,
                () -> Set.of(a, b)); // IllegalArgumentException: duplicate element
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
                        ofEntries(
                                entry("de", "de"),
                                entry("en", "en"),
                                entry("fr", "fr"),
                                entry("rm", "rm"),
                                entry("it", "it")));

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
        final var deCH1 =
                LocalizedValue.stringBuilder()
                        .add("deCH1", GERMAN_SWITZERLAND)
                        .fullLocale()
                        .build();
        final var en1 = LocalizedValue.en("en1");

        final var de2en1 = LocalizedValue.stringBuilder().de("de2").en("en1").build();
        final var de1deCH1 =
                LocalizedValue.stringBuilder()
                        .de("de1")
                        .add("deCH1", GERMAN_SWITZERLAND)
                        .fullLocale()
                        .build();

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
        assertEquals(de1deCH1, LocalizedValue.mergeAll(List.of(de1, deCH1)));
    }

    @Test
    void mergeWith() {
        final var de1 = LocalizedValue.de("de1");
        final var de2 = LocalizedValue.de("de2");
        final var deCH1 =
                LocalizedValue.stringBuilder()
                        .add("deCH1", GERMAN_SWITZERLAND)
                        .fullLocale()
                        .build();
        final var en1 = LocalizedValue.en("en1");

        final var de1en1 = LocalizedValue.stringBuilder().de("de1").en("en1").build();
        final var de1deCH1 =
                LocalizedValue.stringBuilder()
                        .de("de1")
                        .add("deCH1", GERMAN_SWITZERLAND)
                        .fullLocale()
                        .build();

        assertThrows(NullPointerException.class, () -> de1.mergeWith(null));

        assertEquals(de1, de1.mergeWith(de1));
        assertEquals(de1, de2.mergeWith(de1));
        assertEquals(de2, de1.mergeWith(de2));
        assertEquals(de1en1, de1.mergeWith(en1));
        assertEquals(de1deCH1, de1.mergeWith(deCH1));
    }

    @Test
    void untouched() {
        final var untouched =
                new Untouched(LocalizedValue.stringBuilder().de("value_de").en("value_en").build());
        assertEquals(
                "{\"name\":{\"de\":\"value_de\",\"en\":\"value_en\"}}",
                objectMapper.writeValueAsString(untouched));
    }

    @Test
    void jsonUnwrapped() {
        final var unwrapped1 =
                new Unwrapped1(
                        LocalizedValue.stringBuilder().de("value_de").en("value_en").build());
        assertEquals(
                "{\"de\":\"value_de\",\"en\":\"value_en\"}",
                objectMapper.writeValueAsString(unwrapped1));

        final var unwrapped2 =
                new Unwrapped2(
                        LocalizedValue.stringBuilder().de("value_de").en("value_en").build());
        assertEquals(
                "{\"pre:de\":\"value_de\",\"pre:en\":\"value_en\"}",
                objectMapper.writeValueAsString(unwrapped2));

        final var unwrapped3 =
                new Unwrapped3(
                        LocalizedValue.stringBuilder().de("value_de").en("value_en").build());
        assertEquals(
                "{\"de:suf\":\"value_de\",\"en:suf\":\"value_en\"}",
                objectMapper.writeValueAsString(unwrapped3));

        final var unwrapped4 =
                new Unwrapped4(
                        LocalizedValue.stringBuilder().de("value_de").en("value_en").build());
        assertEquals(
                "{\"pre:de:suf\":\"value_de\",\"pre:en:suf\":\"value_en\"}",
                objectMapper.writeValueAsString(unwrapped4));

        final var unwrapped5 =
                new Unwrapped5(
                        LocalizedValue.stringBuilder().de("value_de").en("value_en").build());
        assertEquals(
                "{\"name\":{\"de\":\"value_de\",\"en\":\"value_en\"},\"pre:de:suf\":\"value_de\",\"pre:en:suf\":\"value_en\"}",
                objectMapper.writeValueAsString(unwrapped5));
    }

    @Test
    void keySerializationMethod() {
        final var keySerialization1 =
                new KeySerialization1(
                        LocalizedValue.stringBuilder()
                                .add("value_de", GERMAN_SWITZERLAND)
                                .add("value_en", ENGLISH_SWITZERLAND)
                                .fullLocale()
                                .build());
        assertEquals(
                "{\"name\":{\"en-CH\":\"value_en\",\"de-CH\":\"value_de\"}}",
                objectMapper.writeValueAsString(keySerialization1));

        final var keySerialization2 =
                new KeySerialization2(
                        LocalizedValue.stringBuilder()
                                .add("value_de", GERMAN_SWITZERLAND)
                                .add("value_en", ENGLISH_SWITZERLAND)
                                .fullLocale()
                                .build());
        assertEquals(
                "{\"name\":{\"de_CH\":\"value_de\",\"en_CH\":\"value_en\"}}",
                objectMapper.writeValueAsString(keySerialization2));

        final var keySerialization3 =
                new KeySerialization3(
                        LocalizedValue.stringBuilder()
                                .add("value_de", GERMAN_SWITZERLAND)
                                .add("value_en", ENGLISH_SWITZERLAND)
                                .fullLocale()
                                .build());
        assertEquals(
                "{\"name\":{\"EN-CH\":\"value_en\",\"DE-CH\":\"value_de\"}}",
                objectMapper.writeValueAsString(keySerialization3));

        final var keySerialization4 =
                new KeySerialization4(
                        LocalizedValue.stringBuilder()
                                .add("value_de", GERMAN_SWITZERLAND)
                                .add("value_en", ENGLISH_SWITZERLAND)
                                .fullLocale()
                                .build());
        assertEquals(
                "{\"name\":{\"en-CH\":\"value_en\",\"de-CH\":\"value_de\"},\"nameMethod\":{\"EN-CH\":\"value_en\",\"DE-CH\":\"value_de\"}}",
                objectMapper.writeValueAsString(keySerialization4));
    }

    @Test
    void combinedSerialization() {
        final var combined1 =
                new Combined1(
                        LocalizedValue.stringBuilder()
                                .add("value_de", GERMAN_SWITZERLAND)
                                .add("value_en", ENGLISH_SWITZERLAND)
                                .fullLocale()
                                .build());
        assertEquals(
                "{\"pre:EN-CH:suf\":\"value_en\",\"pre:DE-CH:suf\":\"value_de\"}",
                objectMapper.writeValueAsString(combined1));

        final var combined2 =
                new Combined2(
                        LocalizedValue.stringBuilder()
                                .add("value_de", GERMAN_SWITZERLAND)
                                .add("value_en", ENGLISH_SWITZERLAND)
                                .fullLocale()
                                .build());
        assertEquals(
                "{\"name\":{\"en-CH\":\"value_en\",\"de-CH\":\"value_de\"},\"pre:EN-CH:suf\":\"value_en\",\"pre:DE-CH:suf\":\"value_de\"}",
                objectMapper.writeValueAsString(combined2));
    }

    record Untouched(LocalizedValue<String> name) {}

    record Unwrapped1(@JsonUnwrapped LocalizedValue<String> name) {}

    record Unwrapped2(@JsonUnwrapped(prefix = "pre:") LocalizedValue<String> name) {}

    record Unwrapped3(@JsonUnwrapped(suffix = ":suf") LocalizedValue<String> name) {}

    record Unwrapped4(
            @JsonUnwrapped(prefix = "pre:", suffix = ":suf") LocalizedValue<String> name) {}

    record Unwrapped5(LocalizedValue<String> name) {

        @JsonProperty
        @JsonUnwrapped(prefix = "pre:", suffix = ":suf")
        public LocalizedValue<String> nameMethod() {
            return name;
        }
    }

    record KeySerialization1(@JsonLocalizedValue LocalizedValue<String> name) {}

    record KeySerialization2(
            @JsonLocalizedValue(keySerialization = SerializationMethod.POSIX)
                    LocalizedValue<String> name) {}

    record KeySerialization3(
            @JsonLocalizedValue(keySerialization = SerializationMethod.UPPERCASE)
                    LocalizedValue<String> name) {}

    record KeySerialization4(LocalizedValue<String> name) {

        @JsonProperty
        @JsonLocalizedValue(keySerialization = SerializationMethod.UPPERCASE)
        public LocalizedValue<String> nameMethod() {
            return name;
        }
    }

    record Combined1(
            @JsonUnwrapped(prefix = "pre:", suffix = ":suf")
                    @JsonLocalizedValue(keySerialization = SerializationMethod.UPPERCASE)
                    LocalizedValue<String> name) {}

    record Combined2(LocalizedValue<String> name) {

        @JsonProperty
        @JsonUnwrapped(prefix = "pre:", suffix = ":suf")
        @JsonLocalizedValue(keySerialization = SerializationMethod.UPPERCASE)
        public LocalizedValue<String> nameMethod() {
            return name;
        }
    }
}
