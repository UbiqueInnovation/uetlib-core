package ch.ubique.backend.core.jackson;

import com.github.zafarkhaja.semver.Version;

import tools.jackson.databind.JacksonModule;
import tools.jackson.databind.cfg.DateTimeFeature;
import tools.jackson.databind.cfg.EnumFeature;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.module.SimpleModule;

import java.util.TimeZone;
import java.util.function.Consumer;

public final class ObjectMapperUtils {

    private ObjectMapperUtils() {}

    public static JsonMapper.Builder jsonMapperBuilder() {
        return jsonMapperBuilder(ignore -> {});
    }

    /** Pass a consumer to customize the default object mapper */
    public static JsonMapper.Builder jsonMapperBuilder(
            final Consumer<JsonMapper.Builder> customizer) {
        final var jsonMapperBuilder =
                JsonMapper.builder()
                        .addModule(versionToStringModule())

                        // DateTime features
                        .enable(DateTimeFeature.WRITE_DATES_AS_TIMESTAMPS)
                        .enable(DateTimeFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS)
                        .disable(DateTimeFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS)
                        .disable(DateTimeFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS)
                        .defaultTimeZone(TimeZone.getDefault()) // default is UTC, NOT JVM time zone

                        // Enum features
                        .enable(EnumFeature.READ_UNKNOWN_ENUM_VALUES_USING_DEFAULT_VALUE)
                        .disable(EnumFeature.READ_ENUMS_USING_TO_STRING)
                        .disable(EnumFeature.WRITE_ENUMS_USING_TO_STRING);

        customizer.accept(jsonMapperBuilder);

        return jsonMapperBuilder;
    }

    public static JsonMapper jsonMapper() {
        return jsonMapper(ignore -> {});
    }

    /** Pass a consumer to customize the default object mapper */
    public static JsonMapper jsonMapper(final Consumer<JsonMapper.Builder> customizer) {
        return jsonMapperBuilder(customizer).build();
    }

    /** support to convert {@link Version} to String and vice versa */
    public static JacksonModule versionToStringModule() {
        final var versionToStringModule = new SimpleModule();
        versionToStringModule.addSerializer(Version.class, new VersionToStringSerializer());
        versionToStringModule.addDeserializer(Version.class, new StringToVersionDeserializer());
        return versionToStringModule;
    }
}
