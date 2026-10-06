package ch.ubique.backend.core.openenum.jackson;

import static ch.ubique.backend.core.openenum.jackson.OpenEnumModuleTest.Color.Known.GREEN;
import static ch.ubique.backend.core.openenum.jackson.OpenEnumModuleTest.Color.Known.RED;

import static org.junit.jupiter.api.Assertions.*;

import ch.ubique.backend.core.jackson.JsonMapperUtils;
import ch.ubique.backend.core.openenum.OpenEnumSet;
import ch.ubique.backend.core.openenum.OpenEnumValue;
import ch.ubique.backend.core.openenum.UnknownOpenEnumValue;

import org.junit.jupiter.api.Test;

import tools.jackson.databind.json.JsonMapper;

class OpenEnumModuleTest {

    sealed interface Color extends OpenEnumValue<String> {

        OpenEnumSet<Color, String> KNOWN_VALUES = OpenEnumSet.of(Known.class, Unknown::new);

        enum Known implements Color {
            RED("red"),
            GREEN("green");

            private final String value;

            Known(final String value) {
                this.value = value;
            }

            @Override
            public String value() {
                return value;
            }
        }

        record Unknown(String value) implements Color, UnknownOpenEnumValue<String> {}
    }

    /** An open enum over a non-string raw type, to pin that {@code V} is resolved, not assumed. */
    sealed interface Priority extends OpenEnumValue<Integer> {

        OpenEnumSet<Priority, Integer> CODES = OpenEnumSet.of(Known.class, Unknown::new);

        enum Known implements Priority {
            LOW(1),
            HIGH(9);

            private final Integer value;

            Known(final Integer value) {
                this.value = value;
            }

            @Override
            public Integer value() {
                return value;
            }
        }

        record Unknown(Integer value) implements Priority, UnknownOpenEnumValue<Integer> {}
    }

    record Holder(Color color) {}

    private final JsonMapper mapper = JsonMapperUtils.jsonMapper();

    @Test
    void deserializesKnownAndUnknownWithoutPerTypeRegistration() {
        assertSame(RED, mapper.readValue("\"red\"", Color.class));
        assertEquals(new Color.Unknown("cyan"), mapper.readValue("\"cyan\"", Color.class));
    }

    @Test
    void resolvesTheRawValueTypeFromTheDomainType() {
        assertSame(Priority.Known.HIGH, mapper.readValue("9", Priority.class));
        assertEquals(new Priority.Unknown(4), mapper.readValue("4", Priority.class));
    }

    @Test
    void roundTripsUnknownValuesLosslesslyInsideAnObject() {
        final var json = "{\"color\":\"cyan\"}";

        assertEquals(json, mapper.writeValueAsString(mapper.readValue(json, Holder.class)));
    }

    @Test
    void nullStaysNullRatherThanBecomingAnUnknownCarrier() {
        assertNull(mapper.readValue("null", Color.class));
        assertNull(mapper.readValue("{\"color\":null}", Holder.class).color());
    }

    @Test
    void serializationNeedsNoModule() {
        // @JsonValue carries this, so a bare mapper cannot get writes wrong by being unconfigured.
        final var bare = JsonMapper.builder().build();

        assertEquals("\"green\"", bare.writeValueAsString(GREEN));
        assertEquals("\"cyan\"", bare.writeValueAsString(new Color.Unknown("cyan")));
    }

    @Test
    void readingWithoutTheModuleFailsLoudlyInsteadOfSilently() {
        final var bare = JsonMapper.builder().build();

        // The deliberate asymmetry: an unconfigured mapper cannot instantiate the sealed
        // interface, so a missing module surfaces as an error rather than as wrong data.
        assertThrows(Exception.class, () -> bare.readValue("\"red\"", Color.class));
    }

    @Test
    void isAlsoPickedUpByJacksonsOptInModuleDiscovery() {
        // Discovery is opt-in (findAndAddModules), which is why the test above still fails on a
        // plain builder. Registering via META-INF/services covers consumers that build their own
        // mapper without going through JsonMapperUtils.
        final var discovered = JsonMapper.builder().findAndAddModules().build();

        assertSame(RED, discovered.readValue("\"red\"", Color.class));
        assertEquals(new Color.Unknown("cyan"), discovered.readValue("\"cyan\"", Color.class));
    }
}
