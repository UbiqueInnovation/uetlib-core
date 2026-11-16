package ch.uetlib.core.jackson;

import ch.uetlib.core.openenum.jackson.OpenEnumModule;

import tools.jackson.databind.JacksonModule;
import tools.jackson.databind.cfg.DateTimeFeature;
import tools.jackson.databind.cfg.EnumFeature;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.module.SimpleModule;
import tools.jackson.databind.ser.std.ToStringSerializer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

public final class JsonMapperUtils {

    private JsonMapperUtils() {}

    private static final DateTimeFormatter LOCAL_DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;
    private static final DateTimeFormatter LOCAL_DATE_TIME_FORMATTER =
            DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public static void configureDateTime(final JsonMapper.Builder jsonMapperBuilder) {
        jsonMapperBuilder
                // DateTime features
                .enable(DateTimeFeature.WRITE_DATES_AS_TIMESTAMPS)
                .enable(DateTimeFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS)
                .enable(DateTimeFeature.WRITE_DURATIONS_AS_TIMESTAMPS)
                .disable(DateTimeFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS)
                .disable(DateTimeFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS)
                .addModule(javaTimeStringModule())
                // default is UTC, NOT JVM time zone
                .defaultTimeZone(TimeZone.getDefault());
    }

    /**
     * As long as we have {@link DateTimeFeature#WRITE_DATES_AS_TIMESTAMPS} enabled, we need to
     * serialize {@link LocalDate}, {@link LocalDateTime} and {@link LocalTime} as strings.
     * Otherwise, they are serialized as arrays of numbers.
     */
    public static JacksonModule javaTimeStringModule() {
        return new SimpleModule()
                .addSerializer(LocalDate.class, ToStringSerializer.instance)
                .addSerializer(LocalDateTime.class, ToStringSerializer.instance)
                .addSerializer(LocalTime.class, ToStringSerializer.instance);
    }

    public static void configureEnum(final JsonMapper.Builder jsonMapperBuilder) {
        jsonMapperBuilder
                .enable(EnumFeature.READ_UNKNOWN_ENUM_VALUES_USING_DEFAULT_VALUE)
                .disable(EnumFeature.READ_ENUMS_USING_TO_STRING)
                .disable(EnumFeature.WRITE_ENUMS_USING_TO_STRING);
    }

    public static void configureVersion(final JsonMapper.Builder jsonMapperBuilder) {
        jsonMapperBuilder.addModule(new UetlibCoreJacksonVersionModule());
    }

    /**
     * Registers deserialization for open enums. Serialization needs no setup, so a mapper missing
     * this still writes correct JSON — it just cannot read it back.
     */
    public static void configureOpenEnum(final JsonMapper.Builder jsonMapperBuilder) {
        jsonMapperBuilder.addModule(new OpenEnumModule());
    }

    public static void configureBase(final JsonMapper.Builder jsonMapperBuilder) {
        JsonMapperUtils.configureDateTime(jsonMapperBuilder);
        JsonMapperUtils.configureEnum(jsonMapperBuilder);
        JsonMapperUtils.configureVersion(jsonMapperBuilder);
        JsonMapperUtils.configureOpenEnum(jsonMapperBuilder);
    }

    /**
     * Prefer to not use this method directly, use the Spring Boot autoconfigured one, together with
     * the autoconfiguration from {@code uetlib-spring-boot}. This one exists for cases where the
     * other one is super inconvenient.
     */
    public static JsonMapper jsonMapper() {
        final var jsonMapperBuilder = JsonMapper.builder();
        configureBase(jsonMapperBuilder);
        return jsonMapperBuilder.build();
    }
}
