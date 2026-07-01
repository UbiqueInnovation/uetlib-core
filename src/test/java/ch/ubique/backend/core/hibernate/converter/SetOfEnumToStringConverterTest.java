package ch.ubique.backend.core.hibernate.converter;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.util.Set;

class SetOfEnumToStringConverterTest {

    private enum Colour {
        RED,
        GREEN,
        BLUE
    }

    /** An actual instantiation of the abstract converter for {@link Colour}. */
    private static final class ColourConverter extends SetOfEnumToStringConverter<Colour> {
        private ColourConverter() {
            super(Colour::name, Colour::valueOf);
        }
    }

    private final ColourConverter converter = new ColourConverter();

    @Test
    void convertToDatabaseColumn_SingleValue_ShouldReturnName() {
        assertEquals("RED", converter.convertToDatabaseColumn(Set.of(Colour.RED)));
    }

    @Test
    void convertToDatabaseColumn_MultipleValues_ShouldJoinWithComma() {
        final var joined = converter.convertToDatabaseColumn(Set.of(Colour.RED, Colour.BLUE));

        assertNotNull(joined);
        // Set iteration order is unspecified, so assert on the parsed content.
        assertEquals(Set.of("RED", "BLUE"), Set.of(joined.split(",")));
    }

    @Test
    void convertToDatabaseColumn_EmptySet_ShouldReturnEmptyString() {
        assertEquals("", converter.convertToDatabaseColumn(Set.of()));
    }

    @Test
    void convertToDatabaseColumn_Null_ShouldReturnNull() {
        assertNull(converter.convertToDatabaseColumn(null));
    }

    @Test
    void convertToEntityAttribute_Joined_ShouldParseToEnumSet() {
        assertEquals(
                Set.of(Colour.RED, Colour.GREEN), converter.convertToEntityAttribute("RED,GREEN"));
    }

    @Test
    void convertToEntityAttribute_SingleValue_ShouldParse() {
        assertEquals(Set.of(Colour.BLUE), converter.convertToEntityAttribute("BLUE"));
    }

    @Test
    void convertToEntityAttribute_Null_ShouldReturnNull() {
        assertNull(converter.convertToEntityAttribute(null));
    }

    @Test
    void convertToEntityAttribute_UnknownValue_ShouldThrow() {
        assertThrows(
                IllegalArgumentException.class, () -> converter.convertToEntityAttribute("PURPLE"));
    }

    @Test
    void convertToEntityAttribute_ShouldReturnUnmodifiableSet() {
        final var result = converter.convertToEntityAttribute("RED");

        assertNotNull(result);
        assertThrows(UnsupportedOperationException.class, () -> result.add(Colour.GREEN));
    }

    @Test
    void roundTrip_ShouldPreserveSet() {
        final var original = Set.of(Colour.RED, Colour.GREEN, Colour.BLUE);

        final var joined = converter.convertToDatabaseColumn(original);
        final var parsed = converter.convertToEntityAttribute(joined);

        assertEquals(original, parsed);
    }
}
