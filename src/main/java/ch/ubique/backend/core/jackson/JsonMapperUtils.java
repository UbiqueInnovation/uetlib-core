package ch.ubique.backend.core.jackson;

import tools.jackson.databind.cfg.DateTimeFeature;
import tools.jackson.databind.cfg.EnumFeature;
import tools.jackson.databind.json.JsonMapper;

import java.util.TimeZone;

public final class JsonMapperUtils {

    private JsonMapperUtils() {}

    public static void configureDateTime(final JsonMapper.Builder jsonMapperBuilder) {
        jsonMapperBuilder
                // DateTime features
                .enable(DateTimeFeature.WRITE_DATES_AS_TIMESTAMPS)
                .enable(DateTimeFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS)
                .disable(DateTimeFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS)
                .disable(DateTimeFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS)
                // default is UTC, NOT JVM time zone
                .defaultTimeZone(TimeZone.getDefault());
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
}
