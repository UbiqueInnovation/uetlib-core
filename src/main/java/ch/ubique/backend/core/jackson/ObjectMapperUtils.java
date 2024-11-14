package ch.ubique.backend.core.jackson;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.module.paranamer.ParanamerModule;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.function.Consumer;

public final class ObjectMapperUtils {

    private ObjectMapperUtils() {}

    public static JsonMapper.Builder objectMapperBuilder() {
        return objectMapperBuilder(ignore -> {});
    }

    /** Pass a consumer to customize the default object mapper */
    public static JsonMapper.Builder objectMapperBuilder(
            final Consumer<JsonMapper.Builder> customizer) {
        final var javaTimeModule = new JavaTimeModule();
        final var localDateSerializer =
                new LocalDateSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        javaTimeModule.addSerializer(LocalDate.class, localDateSerializer);

        final var jsonMapperBuilder =
                JsonMapper.builder()
                        .addModule(javaTimeModule)
                        // ParanamerModule allows using @JsonCreator without needing @JsonProperty
                        .addModule(new ParanamerModule())
                        // Jdk8Module supports Optionals
                        .addModule(new Jdk8Module())
                        .disable(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS)
                        .disable(DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS)
                        .enable(
                                DeserializationFeature
                                        .READ_UNKNOWN_ENUM_VALUES_USING_DEFAULT_VALUE);

        customizer.accept(jsonMapperBuilder);

        return jsonMapperBuilder;
    }

    public static ObjectMapper objectMapper() {
        return objectMapper(ignore -> {});
    }

    /** Pass a consumer to customize the default object mapper */
    public static ObjectMapper objectMapper(final Consumer<JsonMapper.Builder> customizer) {
        return objectMapperBuilder(customizer).build();
    }
}
