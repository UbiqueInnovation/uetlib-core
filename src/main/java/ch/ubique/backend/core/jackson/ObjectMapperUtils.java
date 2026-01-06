package ch.ubique.backend.core.jackson;

import com.github.zafarkhaja.semver.Version;

import tools.jackson.databind.JacksonModule;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.cfg.DateTimeFeature;
import tools.jackson.databind.cfg.EnumFeature;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.module.SimpleModule;

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
                        .addModule(versionToStringModule())
                        .enable(DateTimeFeature.WRITE_DATES_AS_TIMESTAMPS)
                        .enable(DateTimeFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS)
                        .disable(DateTimeFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS)
                        .disable(DateTimeFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS)
                        .enable(EnumFeature.READ_UNKNOWN_ENUM_VALUES_USING_DEFAULT_VALUE);

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
    public static JacksonModule versionToStringModule() {
        final var versionToStringModule = new SimpleModule();
        versionToStringModule.addSerializer(Version.class, new VersionToStringSerializer());
        versionToStringModule.addDeserializer(Version.class, new StringToVersionDeserializer());
        return versionToStringModule;
    }
}
