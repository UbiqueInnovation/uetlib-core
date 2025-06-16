package ch.ubique.backend.core.jackson;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.module.paranamer.ParanamerModule;
import com.github.zafarkhaja.semver.Version;

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
        final var jsonMapperBuilder =
                JsonMapper.builder()
                        // support for LocalDate to String conversion in ISO yyyy-MM-dd format
                        .addModule(javaTimeModule())
                        // ParanamerModule allows using @JsonCreator without needing @JsonProperty
                        .addModule(new ParanamerModule())
                        // Jdk8Module supports Optional
                        .addModule(new Jdk8Module())
                        // support to convert com.github.zafarkhaja.semver.Version to String and
                        // vice versa
                        .addModule(versionToStringModule())
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

    /** support to convert {@link Version} to String and vice versa */
    public static Module versionToStringModule() {
        final var versionToStringModule = new SimpleModule();
        versionToStringModule.addSerializer(Version.class, new VersionToStringSerializer());
        versionToStringModule.addDeserializer(Version.class, new StringToVersionDeserializer());
        return versionToStringModule;
    }

    /** support for LocalDate to String conversion in ISO yyyy-MM-dd format */
    public static Module javaTimeModule() {
        final var javaTimeModule = new JavaTimeModule();
        final var localDateSerializer =
                new LocalDateSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        javaTimeModule.addSerializer(LocalDate.class, localDateSerializer);
        return javaTimeModule;
    }
}
