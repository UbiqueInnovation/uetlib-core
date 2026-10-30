package ch.ubique.uetlib.core.hibernate.converter;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import org.junit.jupiter.api.Test;

class Base64ImageConverterTest {

    private final Base64ImageConverter converter = new Base64ImageConverter();

    @Test
    void convertToDatabaseColumn_ValidBase64_ShouldDecodeToBytes() {
        final var expected = "hello world".getBytes(StandardCharsets.UTF_8);
        final var encoded = Base64.getEncoder().encodeToString(expected);

        assertArrayEquals(expected, converter.convertToDatabaseColumn(encoded));
    }

    @Test
    void convertToEntityAttribute_Bytes_ShouldEncodeToBase64() {
        final var input = "hello world".getBytes(StandardCharsets.UTF_8);
        final var expected = Base64.getEncoder().encodeToString(input);

        assertEquals(expected, converter.convertToEntityAttribute(input));
    }

    @Test
    void roundTrip_FromEntityAttribute_ShouldPreserveBytes() {
        final byte[] original = {0, 1, 2, 3, 4, (byte) 255, (byte) 128, 64};

        final var encoded = converter.convertToEntityAttribute(original);
        final var decoded = converter.convertToDatabaseColumn(encoded);

        assertArrayEquals(original, decoded);
    }

    @Test
    void roundTrip_FromDatabaseColumn_ShouldPreserveString() {
        final var original = "some binary content".getBytes(StandardCharsets.UTF_8);
        final var encoded = Base64.getEncoder().encodeToString(original);

        final var decoded = converter.convertToDatabaseColumn(encoded);
        final var reEncoded = converter.convertToEntityAttribute(decoded);

        assertEquals(encoded, reEncoded);
    }

    @Test
    void convertToDatabaseColumn_EmptyString_ShouldReturnEmptyByteArray() {
        assertArrayEquals(new byte[0], converter.convertToDatabaseColumn(""));
    }

    @Test
    void convertToEntityAttribute_EmptyByteArray_ShouldReturnEmptyString() {
        assertEquals("", converter.convertToEntityAttribute(new byte[0]));
    }

    @Test
    void convertToDatabaseColumn_InvalidBase64_ShouldThrow() {
        assertThrows(
                IllegalArgumentException.class,
                () -> converter.convertToDatabaseColumn("not valid base64 !!!"));
    }
}
