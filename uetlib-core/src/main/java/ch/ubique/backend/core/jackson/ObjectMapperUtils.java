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

public final class ObjectMapperUtils {

    private ObjectMapperUtils() {}

    public static JsonMapper.Builder objectMapperBuilder() {
        final var javaTimeModule = new JavaTimeModule();
        final var localDateSerializer =
                new LocalDateSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        javaTimeModule.addSerializer(LocalDate.class, localDateSerializer);

        return JsonMapper.builder()
                .addModule(javaTimeModule)
                // ParanamerModule allows using @JsonCreator without needing @JsonProperty
                .addModule(new ParanamerModule())
                // Jdk8Module supports Optionals
                .addModule(new Jdk8Module())
                .disable(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS)
                .disable(DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS)
                .enable(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_USING_DEFAULT_VALUE);
    }

    public static ObjectMapper objectMapper() {
        return objectMapperBuilder().build();
    }
}
