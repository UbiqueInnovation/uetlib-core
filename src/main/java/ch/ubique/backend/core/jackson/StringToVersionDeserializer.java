package ch.ubique.backend.core.jackson;

import com.github.zafarkhaja.semver.Version;

import tools.jackson.core.JsonParser;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.ValueDeserializer;

public class StringToVersionDeserializer extends ValueDeserializer<Version> {

    @Override
    public Version deserialize(
            final JsonParser jsonParser, final DeserializationContext deserializationContext) {
        final var versionString = jsonParser.getString(); // Read the version string from the JSON
        if (versionString != null) {
            return Version.parse(versionString, false); // Convert the string to a Version object
        }
        return null; // Handle null or empty string input
    }
}
