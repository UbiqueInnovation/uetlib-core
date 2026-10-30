package ch.ubique.uetlib.core.hibernate.converter;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class SetOfEnumToStringConverterTest {

    private enum Colour {
        RED,
        GREEN,
        BLUE
    }

    /** An actual instantiation of the abstract converter for {@link Colour}. */
    private static final class TwoFunctionConstructorColourConverter
            extends SetOfEnumToStringConverter<Colour> {
        private TwoFunctionConstructorColourConverter() {
            super(Colour::name, Colour::valueOf);
        }
    }

    /** An actual instantiation using the {@code Class<E>} convenience constructor. */
    private static final class ClassConstructorColourConverter
            extends SetOfEnumToStringConverter<Colour> {
        private ClassConstructorColourConverter() {
            super(Colour.class);
        }
    }

    /** Both instantiations must behave identically. */
    static Stream<SetOfEnumToStringConverter<Colour>> converters() {
        return Stream.of(
                new TwoFunctionConstructorColourConverter(), new ClassConstructorColourConverter());
    }

    @ParameterizedTest
    @MethodSource("converters")
    void convertToDatabaseColumn_SingleValue_ShouldReturnName(
            final SetOfEnumToStringConverter<Colour> converter) {
        assertEquals("RED", converter.convertToDatabaseColumn(Set.of(Colour.RED)));
    }

    @ParameterizedTest
    @MethodSource("converters")
    void convertToDatabaseColumn_MultipleValues_ShouldJoinWithComma(
            final SetOfEnumToStringConverter<Colour> converter) {
        final var joined = converter.convertToDatabaseColumn(Set.of(Colour.RED, Colour.BLUE));

        assertNotNull(joined);
        // Set iteration order is unspecified, so assert on the parsed content.
        assertEquals(Set.of("RED", "BLUE"), Set.of(joined.split(",")));
    }

    @ParameterizedTest
    @MethodSource("converters")
    void convertToDatabaseColumn_EmptySet_ShouldReturnEmptyString(
            final SetOfEnumToStringConverter<Colour> converter) {
        assertEquals("", converter.convertToDatabaseColumn(Set.of()));
    }

    @ParameterizedTest
    @MethodSource("converters")
    void convertToDatabaseColumn_Null_ShouldReturnNull(
            final SetOfEnumToStringConverter<Colour> converter) {
        assertNull(converter.convertToDatabaseColumn(null));
    }

    @ParameterizedTest
    @MethodSource("converters")
    void convertToEntityAttribute_Joined_ShouldParseToEnumSet(
            final SetOfEnumToStringConverter<Colour> converter) {
        assertEquals(
                Set.of(Colour.RED, Colour.GREEN), converter.convertToEntityAttribute("RED,GREEN"));
    }

    @ParameterizedTest
    @MethodSource("converters")
    void convertToEntityAttribute_SingleValue_ShouldParse(
            final SetOfEnumToStringConverter<Colour> converter) {
        assertEquals(Set.of(Colour.BLUE), converter.convertToEntityAttribute("BLUE"));
    }

    @ParameterizedTest
    @MethodSource("converters")
    void convertToEntityAttribute_Null_ShouldReturnNull(
            final SetOfEnumToStringConverter<Colour> converter) {
        assertNull(converter.convertToEntityAttribute(null));
    }

    @ParameterizedTest
    @MethodSource("converters")
    void convertToEntityAttribute_UnknownValue_ShouldThrow(
            final SetOfEnumToStringConverter<Colour> converter) {
        assertThrows(
                IllegalArgumentException.class, () -> converter.convertToEntityAttribute("PURPLE"));
    }

    @ParameterizedTest
    @MethodSource("converters")
    void convertToEntityAttribute_ShouldReturnUnmodifiableSet(
            final SetOfEnumToStringConverter<Colour> converter) {
        final var result = converter.convertToEntityAttribute("RED");

        assertNotNull(result);
        assertThrows(UnsupportedOperationException.class, () -> result.add(Colour.GREEN));
    }

    @ParameterizedTest
    @MethodSource("converters")
    void roundTrip_ShouldPreserveSet(final SetOfEnumToStringConverter<Colour> converter) {
        final var original = Set.of(Colour.RED, Colour.GREEN, Colour.BLUE);

        final var joined = converter.convertToDatabaseColumn(original);
        final var parsed = converter.convertToEntityAttribute(joined);

        assertEquals(original, parsed);
    }
}
