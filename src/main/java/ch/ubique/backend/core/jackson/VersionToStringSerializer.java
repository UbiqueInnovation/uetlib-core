package ch.ubique.backend.core.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.github.zafarkhaja.semver.Version;

import java.io.IOException;

public class VersionToStringSerializer extends JsonSerializer<Version> {

    @Override
    public void serialize(
            final Version version,
            final JsonGenerator jsonGenerator,
            final SerializerProvider serializerProvider)
            throws IOException {
        if (version != null) {
            jsonGenerator.writeString(version.toString());
        } else {
            jsonGenerator.writeNull(); // Handle null versions (optional, depending on your needs)
        }
    }
}
