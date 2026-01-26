package ch.ubique.backend.core.jackson;

import com.github.zafarkhaja.semver.Version;

import tools.jackson.core.JsonGenerator;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.ValueSerializer;

public class UetlibCoreVersionToStringSerializer extends ValueSerializer<Version> {

    @Override
    public void serialize(
            final Version version,
            final JsonGenerator jsonGenerator,
            final SerializationContext serializerProvider) {
        if (version != null) {
            jsonGenerator.writeString(version.toString());
        } else {
            jsonGenerator.writeNull(); // Handle null versions (optional, depending on your needs)
        }
    }
}
