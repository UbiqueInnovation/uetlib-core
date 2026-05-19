package ch.ubique.backend.core.localized;

import static ch.ubique.backend.core.localized.AdditionalLocales.ENGLISH_SWITZERLAND;
import static ch.ubique.backend.core.localized.AdditionalLocales.GERMAN_SWITZERLAND;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.skyscreamer.jsonassert.JSONCompareMode.STRICT;

import ch.ubique.backend.core.localized.annotation.JsonLocalizedValue;
import ch.ubique.backend.core.localized.annotation.JsonLocalizedValue.SerializationMethod;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.skyscreamer.jsonassert.JSONAssert;

import tools.jackson.databind.MapperFeature;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.SerializationFeature;
import tools.jackson.databind.json.JsonMapper;

import java.util.Locale;
import java.util.Map;

class LocalizedValueJacksonTest {

    private final ObjectMapper objectMapper =
            JsonMapper.builder()
                    .enable(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY)
                    .enable(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS)
                    .build();

    @ParameterizedTest
    @CsvSource(
            value = {
                "ZH|{\"zh\":\"Literal\"}",
                "FULL_ZH_CN|{\"zh-CN\":\"Literal\"}",
                "EN|{\"en\":\"Test\"}"
            },
            delimiter = '|')
    void localizedValueSerialization(final String variant, final String expectedJson)
            throws JSONException {
        JSONAssert.assertEquals(
                expectedJson,
                objectMapper.writeValueAsString(localizedValueVariant(variant)),
                STRICT);
    }

    @ParameterizedTest
    @CsvSource(
            value = {
                "{\"zh\":\"Literal\"}|zh",
                "{\"zh-CN\":\"Literal\"}|zh-CN",
                "{\"ZH-CN\":\"Literal\"}|zh-CN"
            },
            delimiter = '|')
    void localizedValueDeserialization(final String inputJson, final String expectedLanguageTag) {
        final var localizedValue = objectMapper.readValue(inputJson, LocalizedValue.class);
        assertEquals(
                Map.of(expectedLanguageTag, "Literal"), localizedValue.toMapWithLanguageTagKeys());
    }

    @Test
    void mapKeySerializationVariants() throws JSONException {
        final var lv =
                LocalizedValue.stringBuilder().fullLocale().add("Literal", Locale.CHINA).build();
        JSONAssert.assertEquals(
                "{\"zh-CN\":\"Literal\"}",
                objectMapper.writeValueAsString(lv.toMapWithLanguageTagKeys()),
                STRICT);
    }

    @ParameterizedTest
    @CsvSource(
            value = "Untouched|{\"name\":{\"de-CH\":\"value_deCh\",\"en-CH\":\"value_enCh\"}}",
            delimiter = '|')
    void untouchedSerialization(final String className, final String expectedJson)
            throws JSONException {
        JSONAssert.assertEquals(expectedJson, serializeTestValue(className), STRICT);
    }

    @ParameterizedTest
    @CsvSource(
            value = {
                "Unwrapped1|{\"de-CH\":\"value_deCh\",\"en-CH\":\"value_enCh\"}",
                "Unwrapped2|{\"pre:de-CH\":\"value_deCh\",\"pre:en-CH\":\"value_enCh\"}",
                "Unwrapped3|{\"de-CH:suf\":\"value_deCh\",\"en-CH:suf\":\"value_enCh\"}",
                "Unwrapped4|{\"pre:de-CH:suf\":\"value_deCh\",\"pre:en-CH:suf\":\"value_enCh\"}",
                "UnwrappedSerialization1|{\"name\":{\"de-CH\":\"value_deCh\",\"en-CH\":\"value_enCh\"},\"pre:de-CH:suf\":\"value_deCh\",\"pre:en-CH:suf\":\"value_enCh\"}"
            },
            delimiter = '|')
    void unwrappedSerialization(final String className, final String expectedJson)
            throws JSONException {
        JSONAssert.assertEquals(expectedJson, serializeTestValue(className), STRICT);
    }

    @ParameterizedTest
    @CsvSource(
            value = {
                "Unwrapped1|{\"de\":\"value_de\",\"en\":\"value_en\"}",
                "Unwrapped2|{\"pre:de\":\"value_de\",\"pre:en\":\"value_en\"}",
                "Unwrapped3|{\"de:suf\":\"value_de\",\"en:suf\":\"value_en\"}",
                "Unwrapped4|{\"pre:de:suf\":\"value_de\",\"pre:en:suf\":\"value_en\"}"
            },
            delimiter = '|')
    void unwrappedDeserialization(final String className, final String inputJson) {
        assertEquals(
                LocalizedValue.stringBuilder().de("value_de").en("value_en").build(),
                deserializeTestValue(className, inputJson));
    }

    @ParameterizedTest
    @CsvSource(
            value = {
                "KeySerialization1|{\"name\":{\"en-CH\":\"value_enCh\",\"de-CH\":\"value_deCh\"}}",
                "KeySerialization2|{\"name\":{\"EN-CH\":\"value_enCh\",\"DE-CH\":\"value_deCh\"}}",
                "KeySerialization3|{\"name\":{\"en-CH\":\"value_enCh\",\"de-CH\":\"value_deCh\"},\"nameMethod\":{\"EN-CH\":\"value_enCh\",\"DE-CH\":\"value_deCh\"}}",
                "KeySerialization4|{\"name\":{\"enCh\":\"value_enCh\",\"deCh\":\"value_deCh\"}}"
            },
            delimiter = '|')
    void keySerializationMethodSerialization(final String className, final String expectedJson)
            throws JSONException {
        JSONAssert.assertEquals(expectedJson, serializeTestValue(className), STRICT);
    }

    @ParameterizedTest
    @CsvSource(
            value = {
                "KeySerialization1|{\"name\":{\"en-CH\":\"value_enCh\",\"de-CH\":\"value_deCh\"}}",
                "KeySerialization2|{\"name\":{\"EN-CH\":\"value_enCh\",\"DE-CH\":\"value_deCh\"}}",
                "KeySerialization3|{\"name\":{\"en-CH\":\"value_enCh\",\"de-CH\":\"value_deCh\"}}",
                "KeySerialization4|{\"name\":{\"enCh\":\"value_enCh\",\"deCh\":\"value_deCh\"}}"
            },
            delimiter = '|')
    void keySerializationMethodDeserialization(final String className, final String inputJson) {
        assertEquals(testLocalizedValue(), deserializeTestValue(className, inputJson));
    }

    @ParameterizedTest
    @CsvSource(
            value = {
                "Combined1|{\"pre:EN-CH:suf\":\"value_enCh\",\"pre:DE-CH:suf\":\"value_deCh\"}",
                "Combined2|{\"nameDeCh\":\"value_deCh\",\"nameEnCh\":\"value_enCh\"}",
                "CombinedSerialization1|{\"name\":{\"en-CH\":\"value_enCh\",\"de-CH\":\"value_deCh\"},\"pre:EN-CH:suf\":\"value_enCh\",\"pre:DE-CH:suf\":\"value_deCh\"}"
            },
            delimiter = '|')
    void combinedSerialization(final String className, final String expectedJson)
            throws JSONException {
        JSONAssert.assertEquals(expectedJson, serializeTestValue(className), STRICT);
    }

    @ParameterizedTest
    @CsvSource(
            value = {
                "Combined1|{\"pre:EN-CH:suf\":\"value_enCh\",\"pre:DE-CH:suf\":\"value_deCh\"}",
                "Combined2|{\"nameDeCh\":\"value_deCh\",\"nameEnCh\":\"value_enCh\"}"
            },
            delimiter = '|')
    void combinedDeserialization(final String className, final String inputJson) {
        assertEquals(testLocalizedValue(), deserializeTestValue(className, inputJson));
    }

    private Object localizedValueVariant(final String variant) {
        return switch (variant) {
            case "ZH" -> LocalizedValue.from("Literal", Locale.CHINA);
            case "FULL_ZH_CN" ->
                    LocalizedValue.stringBuilder()
                            .fullLocale()
                            .add("Literal", Locale.CHINA)
                            .build();
            case "EN" -> LocalizedValue.en("Test");
            default ->
                    throw new IllegalArgumentException(
                            "Unknown variant for localizedValueVariant: " + variant);
        };
    }

    private String serializeTestValue(final String className) {
        final var value = testLocalizedValue();
        final var variant =
                switch (className) {
                    case "Untouched" -> new Untouched(value);
                    case "Unwrapped1" -> new Unwrapped1(value);
                    case "Unwrapped2" -> new Unwrapped2(value);
                    case "Unwrapped3" -> new Unwrapped3(value);
                    case "Unwrapped4" -> new Unwrapped4(value);
                    case "UnwrappedSerialization1" -> new UnwrappedSerialization1(value);
                    case "KeySerialization1" -> new KeySerialization1(value);
                    case "KeySerialization2" -> new KeySerialization2(value);
                    case "KeySerialization3" -> new KeySerialization3(value);
                    case "KeySerialization4" -> new KeySerialization4(value);
                    case "Combined1" -> new Combined1(value);
                    case "Combined2" -> new Combined2(value);
                    case "CombinedSerialization1" -> new CombinedSerialization1(value);
                    default ->
                            throw new IllegalArgumentException(
                                    "Unknown test class name: " + className);
                };
        return objectMapper.writeValueAsString(variant);
    }

    private LocalizedValue<String> deserializeTestValue(
            final String className, final String inputJson) {
        return switch (className) {
            case "Untouched" -> objectMapper.readValue(inputJson, Untouched.class).name();
            case "Unwrapped1" -> objectMapper.readValue(inputJson, Unwrapped1.class).name();
            case "Unwrapped2" -> objectMapper.readValue(inputJson, Unwrapped2.class).name();
            case "Unwrapped3" -> objectMapper.readValue(inputJson, Unwrapped3.class).name();
            case "Unwrapped4" -> objectMapper.readValue(inputJson, Unwrapped4.class).name();
            case "UnwrappedSerialization1" ->
                    objectMapper.readValue(inputJson, UnwrappedSerialization1.class).name();
            case "KeySerialization1" ->
                    objectMapper.readValue(inputJson, KeySerialization1.class).name();
            case "KeySerialization2" ->
                    objectMapper.readValue(inputJson, KeySerialization2.class).name();
            case "KeySerialization3" ->
                    objectMapper.readValue(inputJson, KeySerialization3.class).name();
            case "KeySerialization4" ->
                    objectMapper.readValue(inputJson, KeySerialization4.class).name();
            case "Combined1" -> objectMapper.readValue(inputJson, Combined1.class).name();
            case "Combined2" -> objectMapper.readValue(inputJson, Combined2.class).name();
            case "CombinedSerialization1" ->
                    objectMapper.readValue(inputJson, CombinedSerialization1.class).name();
            default ->
                    throw new IllegalArgumentException(
                            "Unknown class name for unwrappedDeserializationVariant: " + className);
        };
    }

    private static LocalizedValue<String> testLocalizedValue() {
        return LocalizedValue.stringBuilder()
                .add("value_deCh", GERMAN_SWITZERLAND)
                .add("value_enCh", ENGLISH_SWITZERLAND)
                .fullLocale()
                .build();
    }

    record Untouched(LocalizedValue<String> name) {}

    record Unwrapped1(@JsonUnwrapped LocalizedValue<String> name) {}

    record Unwrapped2(@JsonUnwrapped(prefix = "pre:") LocalizedValue<String> name) {}

    record Unwrapped3(@JsonUnwrapped(suffix = ":suf") LocalizedValue<String> name) {}

    record Unwrapped4(
            @JsonUnwrapped(prefix = "pre:", suffix = ":suf") LocalizedValue<String> name) {}

    record UnwrappedSerialization1(LocalizedValue<String> name) {

        @JsonProperty
        @JsonUnwrapped(prefix = "pre:", suffix = ":suf")
        public LocalizedValue<String> nameMethod() {
            return name;
        }
    }

    record KeySerialization1(@JsonLocalizedValue LocalizedValue<String> name) {}

    record KeySerialization2(
            @JsonLocalizedValue(keySerialization = SerializationMethod.UPPERCASE)
                    LocalizedValue<String> name) {}

    record KeySerialization3(LocalizedValue<String> name) {

        @JsonProperty
        @JsonLocalizedValue(keySerialization = SerializationMethod.UPPERCASE)
        public LocalizedValue<String> nameMethod() {
            return name;
        }
    }

    record KeySerialization4(
            @JsonLocalizedValue(keySerialization = SerializationMethod.CAMEL_CASE)
                    LocalizedValue<String> name) {}

    record Combined1(
            @JsonUnwrapped(prefix = "pre:", suffix = ":suf")
                    @JsonLocalizedValue(keySerialization = SerializationMethod.UPPERCASE)
                    LocalizedValue<String> name) {}

    record Combined2(
            @JsonUnwrapped(prefix = "name")
                    @JsonLocalizedValue(keySerialization = SerializationMethod.PASCAL_CASE)
                    LocalizedValue<String> name) {}

    record CombinedSerialization1(LocalizedValue<String> name) {

        @JsonProperty
        @JsonUnwrapped(prefix = "pre:", suffix = ":suf")
        @JsonLocalizedValue(keySerialization = SerializationMethod.UPPERCASE)
        public LocalizedValue<String> nameMethod() {
            return name;
        }
    }
}
