package ch.ubique.uetlib.core.jackson;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.Test;

import tools.jackson.databind.annotation.JsonDeserialize;
import tools.jackson.databind.json.JsonMapper;

import java.util.List;

class EmptyStringAsNullDeserializerTest {

    private final JsonMapper mapper = JsonMapper.builder().build();

    @Test
    void convertsEmptyStringsForDifferentTypes() {
        final var values = mapper.readValue("{\"number\":\"\",\"names\":\"\"}", Values.class);

        assertNull(values.number());
        assertNull(values.names());
    }

    @Test
    void delegatesOtherValuesToTheirNormalDeserializers() {
        final var values = mapper.readValue("{\"number\":42,\"names\":[\"first\"]}", Values.class);

        assertEquals(42, values.number());
        assertEquals(List.of("first"), values.names());
    }

    record Values(
            @JsonDeserialize(using = EmptyStringAsNullDeserializer.class) @Nullable Integer number,
            @JsonDeserialize(using = EmptyStringAsNullDeserializer.class)
                    @Nullable List<String> names) {}
}
