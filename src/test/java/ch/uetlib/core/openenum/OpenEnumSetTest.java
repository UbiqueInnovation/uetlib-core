package ch.uetlib.core.openenum;

import static ch.uetlib.core.openenum.OpenEnumSetTest.Color.KnownColor.*;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.annotation.JsonCreator;

import org.junit.jupiter.api.Test;

import tools.jackson.databind.json.JsonMapper;

import java.util.Collection;
import java.util.List;

@SuppressWarnings({"ConstantValue", "DataFlowIssue"})
class OpenEnumSetTest {

    sealed interface Color extends OpenEnumValue<String> {

        OpenEnumSet<Color, String> KNOWN_VALUES =
                OpenEnumSet.of(KnownColor.class, UnknownColor::new);

        static Collection<Color> values() {
            return KNOWN_VALUES.values();
        }

        @JsonCreator
        static Color valueOfOrUnknown(final String value) {
            return KNOWN_VALUES.valueOfOrUnknown(value);
        }

        enum KnownColor implements Color {
            RED("red"),
            GREEN("green"),
            BLUE("blue");

            private final String value;

            KnownColor(final String value) {
                this.value = value;
            }

            @Override
            public String value() {
                return value;
            }
        }

        record UnknownColor(String value) implements Color, UnknownOpenEnumValue<String> {}
    }

    /**
     * {@code describe} has no {@code default} branch: {@link Color} is sealed over {@link
     * Color.KnownColor} and {@link Color.UnknownColor}, so the compiler rejects this switch
     * expression unless every permitted subtype is covered — add a new implementation of {@code
     * Color} and this stops compiling until a matching case is added here.
     */
    private static String describe(final Color color) {
        return switch (color) {
            case RED, GREEN, BLUE -> "known:" + color.value();
            case UnknownColor(final var raw) -> "unknown:" + raw;
        };
    }

    @Test
    void switchOverColorIsExhaustiveWithoutADefaultBranch() {
        assertEquals("known:red", describe(RED));
        assertEquals("known:green", describe(GREEN));
        assertEquals("known:blue", describe(BLUE));
        assertEquals("unknown:cyan", describe(Color.valueOfOrUnknown("cyan")));
    }

    @Test
    void resolvesKnownToTheSameConstant() {
        assertSame(RED, Color.valueOfOrUnknown("red"));
        assertTrue(Color.valueOfOrUnknown("red").isKnown());
        assertTrue(Color.valueOfOrUnknown("green").isKnown());
    }

    @Test
    void resolvesUnknownPreservingTheRawValue() {
        final var cyan = Color.valueOfOrUnknown("cyan");

        assertEquals(new UnknownColor("cyan"), cyan);
        assertFalse(cyan.isKnown());
        assertInstanceOf(UnknownOpenEnumValue.class, cyan);
    }

    @Test
    void valuesReturnsAllConstantsInDeclarationOrder() {
        assertEquals(List.copyOf(Color.values()), List.of(RED, GREEN, BLUE));
    }

    @Test
    void nullIsRejectedByValueOfOrUnknown() {
        assertThrows(NullPointerException.class, () -> Color.valueOfOrUnknown(null));
    }

    /**
     * No Jackson module needed: {@code @JsonValue} on {@link OpenEnumValue#value()} covers
     * serialization for every open-enum type generically, and {@code @JsonCreator} on {@link
     * Color#valueOfOrUnknown} covers deserialization for this specific domain type.
     */
    @Test
    void roundTripsThroughPlainJacksonWithoutAnyModule() {
        final var mapper = JsonMapper.builder().build();

        assertEquals("\"red\"", mapper.writeValueAsString(RED));
        assertEquals("\"cyan\"", mapper.writeValueAsString(new UnknownColor("cyan")));

        assertEquals(RED, mapper.readValue("\"red\"", Color.class));
        assertEquals(new UnknownColor("cyan"), mapper.readValue("\"cyan\"", Color.class));
    }

    enum DuplicateColor implements OpenEnumValue<String> {
        X("x"),
        Y("x");

        private final String value;

        DuplicateColor(final String value) {
            this.value = value;
        }

        @Override
        public String value() {
            return value;
        }
    }

    @Test
    @SuppressWarnings("java:S5778")
    void rejectsDuplicateKnownValues() {
        assertThrows(
                DuplicateOpenEnumValueException.class,
                () ->
                        OpenEnumSet.of(
                                DuplicateColor.class,
                                v -> {
                                    throw new UnsupportedOperationException();
                                }));
    }
}
