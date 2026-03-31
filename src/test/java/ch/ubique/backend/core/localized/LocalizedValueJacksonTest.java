package ch.ubique.backend.core.localized;

import static ch.ubique.backend.core.localized.AdditionalLocales.ENGLISH_SWITZERLAND;
import static ch.ubique.backend.core.localized.AdditionalLocales.GERMAN_SWITZERLAND;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ch.ubique.backend.core.localized.annotation.JsonLocalizedValue;
import ch.ubique.backend.core.localized.annotation.JsonLocalizedValue.SerializationMethod;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

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
            throws JsonProcessingException {
        assertEquals(expectedJson, objectMapper.writeValueAsString(localizedValueVariant(variant)));
    }

    @ParameterizedTest
    @CsvSource(
            value = {
                "{\"zh\":\"Literal\"}|zh",
                "{\"zh-CN\":\"Literal\"}|zh-CN",
                "{\"ZH-CN\":\"Literal\"}|zh-CN"
            },
            delimiter = '|')
    void localizedValueDeserialization(final String inputJson, final String expectedLanguageTag)
            throws JsonProcessingException {
        final var localizedValue = objectMapper.readValue(inputJson, LocalizedValue.class);
        assertEquals(
                Map.of(expectedLanguageTag, "Literal"), localizedValue.toMapWithLanguageTagKeys());
    }

    @ParameterizedTest
    @CsvSource(
            value = {
                "LANGUAGE_TAG|{\"zh-CN\":\"Literal\"}",
                "POSIX|{\"zh_CN\":\"Literal\"}",
                "UPPERCASE|{\"ZH-CN\":\"Literal\"}"
            },
            delimiter = '|')
    void mapKeySerializationVariants(final String keyStyle, final String expectedJson)
            throws JsonProcessingException {
        final var lv =
                LocalizedValue.stringBuilder().fullLocale().add("Literal", Locale.CHINA).build();
        assertEquals(
                expectedJson, objectMapper.writeValueAsString(serializeWithKeyStyle(lv, keyStyle)));
    }

    @Test
    void untouchedSerialization() throws JsonProcessingException {
        final var untouched =
                new Untouched(LocalizedValue.stringBuilder().de("value_de").en("value_en").build());
        assertEquals(
                "{\"name\":{\"de\":\"value_de\",\"en\":\"value_en\"}}",
                objectMapper.writeValueAsString(untouched));
    }

    @ParameterizedTest
    @CsvSource(
            value = {
                "UnwrappedSerialization1|{\"de\":\"value_de\",\"en\":\"value_en\"}",
                "UnwrappedSerialization2|{\"pre:de\":\"value_de\",\"pre:en\":\"value_en\"}",
                "UnwrappedSerialization3|{\"de:suf\":\"value_de\",\"en:suf\":\"value_en\"}",
                "UnwrappedSerialization4|{\"pre:de:suf\":\"value_de\",\"pre:en:suf\":\"value_en\"}",
                "UnwrappedSerialization5|{\"name\":{\"de\":\"value_de\",\"en\":\"value_en\"},\"pre:de:suf\":\"value_de\",\"pre:en:suf\":\"value_en\"}"
            },
            delimiter = '|')
    void unwrappedSerialization(final String className, final String expectedJson)
            throws JsonProcessingException {
        assertEquals(
                expectedJson,
                objectMapper.writeValueAsString(unwrappedSerializationVariant(className)));
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
    void unwrappedDeserialization(final String className, final String inputJson)
            throws JsonProcessingException {
        assertEquals(
                LocalizedValue.stringBuilder().de("value_de").en("value_en").build(),
                unwrappedDeserializationVariant(className, inputJson));
    }

    @ParameterizedTest
    @CsvSource(
            value = {
                "KeySerialization1|{\"name\":{\"en-CH\":\"value_enCh\",\"de-CH\":\"value_deCh\"}}",
                "KeySerialization2|{\"name\":{\"de_CH\":\"value_deCh\",\"en_CH\":\"value_enCh\"}}",
                "KeySerialization3|{\"name\":{\"EN-CH\":\"value_enCh\",\"DE-CH\":\"value_deCh\"}}",
                "KeySerialization4|{\"name\":{\"en-CH\":\"value_enCh\",\"de-CH\":\"value_deCh\"},\"nameMethod\":{\"EN-CH\":\"value_enCh\",\"DE-CH\":\"value_deCh\"}}",
                "KeySerialization5|{\"name\":{\"enCh\":\"value_enCh\",\"deCh\":\"value_deCh\"}}"
            },
            delimiter = '|')
    void keySerializationMethodSerialization(final String className, final String expectedJson)
            throws JsonProcessingException {
        assertEquals(
                expectedJson, objectMapper.writeValueAsString(keySerializationVariant(className)));
    }

    @ParameterizedTest
    @CsvSource(
            value = {
                "KeySerialization1|{\"name\":{\"en-CH\":\"value_enCh\",\"de-CH\":\"value_deCh\"}}",
                "KeySerialization2|{\"name\":{\"de_CH\":\"value_deCh\",\"en_CH\":\"value_enCh\"}}",
                "KeySerialization3|{\"name\":{\"EN-CH\":\"value_enCh\",\"DE-CH\":\"value_deCh\"}}",
                "KeySerialization4|{\"name\":{\"en-CH\":\"value_enCh\",\"de-CH\":\"value_deCh\"}}",
                "KeySerialization5|{\"name\":{\"enCh\":\"value_enCh\",\"deCh\":\"value_deCh\"}}"
            },
            delimiter = '|')
    void keySerializationMethodDeserialization(final String className, final String inputJson)
            throws JsonProcessingException {
        assertEquals(testLocalizedValue(), keyDeserializationVariant(className, inputJson));
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
            throws JsonProcessingException {
        assertEquals(
                expectedJson,
                objectMapper.writeValueAsString(combinedSerializationVariant(className)));
    }

    @ParameterizedTest
    @CsvSource(
            value = {
                "Combined1|{\"pre:EN-CH:suf\":\"value_enCh\",\"pre:DE-CH:suf\":\"value_deCh\"}",
                "Combined2|{\"nameDeCh\":\"value_deCh\",\"nameEnCh\":\"value_enCh\"}"
            },
            delimiter = '|')
    void combinedDeserialization(final String className, final String inputJson)
            throws JsonProcessingException {
        assertEquals(testLocalizedValue(), combinedDeserializationVariant(className, inputJson));
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

    private Object serializeWithKeyStyle(final LocalizedValue<String> value, final String style) {
        return switch (style) {
            case "LANGUAGE_TAG" -> value;
            case "POSIX" -> value.toMapWithPosixLocaleKeys();
            case "UPPERCASE" -> value.toMapWithUppercaseKeys();
            default ->
                    throw new IllegalArgumentException(
                            "Unknown style for serializeWithKeyStyle: " + style);
        };
    }

    private Object unwrappedSerializationVariant(final String className) {
        final var value = LocalizedValue.stringBuilder().de("value_de").en("value_en").build();
        return switch (className) {
            case "UnwrappedSerialization1" -> new Unwrapped1(value);
            case "UnwrappedSerialization2" -> new Unwrapped2(value);
            case "UnwrappedSerialization3" -> new Unwrapped3(value);
            case "UnwrappedSerialization4" -> new Unwrapped4(value);
            case "UnwrappedSerialization5" -> new UnwrappedSerialization1(value);
            default ->
                    throw new IllegalArgumentException(
                            "Unknown class name for unwrappedSerializationVariant: " + className);
        };
    }

    private LocalizedValue<String> unwrappedDeserializationVariant(
            final String className, final String inputJson) throws JsonProcessingException {
        return switch (className) {
            case "Unwrapped1" -> objectMapper.readValue(inputJson, Unwrapped1.class).name();
            case "Unwrapped2" -> objectMapper.readValue(inputJson, Unwrapped2.class).name();
            case "Unwrapped3" -> objectMapper.readValue(inputJson, Unwrapped3.class).name();
            case "Unwrapped4" -> objectMapper.readValue(inputJson, Unwrapped4.class).name();
            default ->
                    throw new IllegalArgumentException(
                            "Unknown class name for unwrappedDeserializationVariant: " + className);
        };
    }

    private Object keySerializationVariant(final String className) {
        final var value = testLocalizedValue();
        return switch (className) {
            case "KeySerialization1" -> new KeySerialization1(value);
            case "KeySerialization2" -> new KeySerialization2(value);
            case "KeySerialization3" -> new KeySerialization3(value);
            case "KeySerialization4" -> new KeySerialization4(value);
            case "KeySerialization5" -> new KeySerialization5(value);
            default ->
                    throw new IllegalArgumentException(
                            "Unknown class name for keySerializationVariant: " + className);
        };
    }

    private LocalizedValue<String> keyDeserializationVariant(
            final String className, final String inputJson) throws JsonProcessingException {
        return switch (className) {
            case "KeySerialization1" ->
                    objectMapper.readValue(inputJson, KeySerialization1.class).name;
            case "KeySerialization2" ->
                    objectMapper.readValue(inputJson, KeySerialization2.class).name;
            case "KeySerialization3" ->
                    objectMapper.readValue(inputJson, KeySerialization3.class).name;
            case "KeySerialization4" ->
                    objectMapper.readValue(inputJson, KeySerialization4.class).name;
            case "KeySerialization5" ->
                    objectMapper.readValue(inputJson, KeySerialization5.class).name;
            default ->
                    throw new IllegalArgumentException(
                            "Unknown class name for keyDeserializationVariant: " + className);
        };
    }

    private Object combinedSerializationVariant(final String className) {
        final var value = testLocalizedValue();
        return switch (className) {
            case "Combined1" -> new Combined1(value);
            case "Combined2" -> new Combined2(value);
            case "CombinedSerialization1" -> new CombinedSerialization1(value);
            default -> throw new IllegalArgumentException("Unknown class name: " + className);
        };
    }

    private LocalizedValue<String> combinedDeserializationVariant(
            final String className, final String inputJson) throws JsonProcessingException {
        return switch (className) {
            case "Combined1" -> objectMapper.readValue(inputJson, Combined1.class).name();
            case "Combined2" -> objectMapper.readValue(inputJson, Combined2.class).name();
            default -> throw new IllegalArgumentException("Unknown class name: " + className);
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

    record KeySerialization5(
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
