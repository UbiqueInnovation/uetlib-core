package ch.ubique.backend.core.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.github.zafarkhaja.semver.Version;

import java.io.IOException;

public class StringToVersionDeserializer extends JsonDeserializer<Version> {

    @Override
    public Version deserialize(
            final JsonParser jsonParser, final DeserializationContext deserializationContext)
            throws IOException {
        final var versionString = jsonParser.getText(); // Read the version string from the JSON
        if (versionString != null) {
            return Version.parse(versionString, false); // Convert the string to a Version object
        }
        return null; // Handle null or empty string input
    }
}
