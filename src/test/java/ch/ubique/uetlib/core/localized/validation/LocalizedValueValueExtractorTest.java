package ch.ubique.uetlib.core.localized.validation;

import static org.mockito.Mockito.*;

import ch.ubique.uetlib.core.localized.LocalizedValue;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.valueextraction.ValueExtractor;
import java.io.Serializable;
import java.util.List;
import java.util.Locale;
import org.junit.jupiter.api.Test;

class LocalizedValueValueExtractorTest {

    @Test
    void testExtractValues_withMultipleLocales() {
        // Arrange
        final var localizedValue = LocalizedValue.stringBuilder().en("Hello").fr("Bonjour").build();
        final var extractor = new LocalizedValueValueExtractor();
        final var receiver = mock(ValueExtractor.ValueReceiver.class);

        // Act
        extractor.extractValues(localizedValue, receiver);

        // Assert
        verify(receiver).keyedValue(null, Locale.ENGLISH, "Hello");
        verify(receiver).keyedValue(null, Locale.FRENCH, "Bonjour");
        verifyNoMoreInteractions(receiver);
    }

    @Test
    void testExtractValues_withNullValue() {
        // Arrange
        final var extractor = new LocalizedValueValueExtractor();
        final var receiver = mock(ValueExtractor.ValueReceiver.class);

        // Act
        extractor.extractValues(null, receiver);

        // Assert
        verify(receiver).value(null, null);
        verifyNoMoreInteractions(receiver);
    }

    @Test
    void testExtractValues_withNullEntry() {
        // Arrange
        final var localizedValue = LocalizedValue.de(new LocalizedEntry(null));
        final var extractor = new LocalizedValueValueExtractor();
        final var receiver = mock(ValueExtractor.ValueReceiver.class);

        // Act
        extractor.extractValues(localizedValue, receiver);

        // Assert
        verify(receiver).keyedValue(null, Locale.GERMAN, new LocalizedEntry(null));
        verifyNoMoreInteractions(receiver);
    }

    @Test
    void testExtractValues_withEmptyEntry() {
        // Arrange
        final var localizedValue = LocalizedValue.de(new LocalizedEntry(List.of()));
        final var extractor = new LocalizedValueValueExtractor();
        final var receiver = mock(ValueExtractor.ValueReceiver.class);

        // Act
        extractor.extractValues(localizedValue, receiver);

        // Assert
        verify(receiver).keyedValue(null, Locale.GERMAN, new LocalizedEntry(List.of()));
        verifyNoMoreInteractions(receiver);
    }

    record LocalizedEntry(@Valid @NotEmpty List<String> entries) implements Serializable {}
}
