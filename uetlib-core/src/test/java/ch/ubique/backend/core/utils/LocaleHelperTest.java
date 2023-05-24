package ch.ubique.backend.core.utils;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Locale;

@SuppressWarnings("DataFlowIssue")
class LocaleHelperTest {

    @Test
    void findBestMatchLocale() {
        final var deGermanyXX = new Locale("de", "DE", "XX");

        assertEquals(deGermanyXX, LocaleHelper.findBestMatchLocale(deGermanyXX, List.of(deGermanyXX, Locale.GERMANY, Locale.GERMAN)));
        assertEquals(deGermanyXX, LocaleHelper.findBestMatchLocale(deGermanyXX, List.of(Locale.GERMAN, Locale.GERMANY, deGermanyXX)));
        assertEquals(Locale.GERMANY, LocaleHelper.findBestMatchLocale(deGermanyXX, List.of(Locale.ENGLISH, Locale.GERMANY)));

        assertEquals(Locale.GERMANY, LocaleHelper.findBestMatchLocale(Locale.GERMANY, List.of(deGermanyXX, Locale.GERMANY, Locale.GERMAN)));
        assertEquals(Locale.GERMANY, LocaleHelper.findBestMatchLocale(Locale.GERMANY, List.of(Locale.GERMAN, Locale.GERMANY, deGermanyXX)));

        assertEquals(Locale.GERMAN, LocaleHelper.findBestMatchLocale(Locale.GERMAN, List.of(deGermanyXX, Locale.GERMANY, Locale.GERMAN)));
        assertEquals(Locale.GERMAN, LocaleHelper.findBestMatchLocale(Locale.GERMAN, List.of(Locale.GERMAN, Locale.GERMANY, deGermanyXX)));

        assertNull(LocaleHelper.findBestMatchLocale(deGermanyXX, List.of()));
        assertNull(LocaleHelper.findBestMatchLocale(deGermanyXX, null));
    }

    @Test
    void findBestMatchLocaleString() {
        final var deGermanyXX = new Locale("de", "DE", "XX");
        final var deGermanyXXString = deGermanyXX.toString();
        final var deGermanyString = Locale.GERMANY.toString();
        final var deString = Locale.GERMAN.toString();

        assertEquals(deGermanyXXString, LocaleHelper.findBestMatchLocaleString(deGermanyXX, List.of(deGermanyXXString, deGermanyString, deString)));
        assertEquals(deGermanyXXString, LocaleHelper.findBestMatchLocaleString(deGermanyXX, List.of(deString, deGermanyString, deGermanyXXString)));
        assertEquals(deGermanyString, LocaleHelper.findBestMatchLocaleString(deGermanyXX, List.of(Locale.ENGLISH.toString(), deGermanyString)));

        assertEquals(deGermanyString, LocaleHelper.findBestMatchLocaleString(Locale.GERMANY, List.of(deGermanyXXString, deGermanyString, deString)));
        assertEquals(deGermanyString, LocaleHelper.findBestMatchLocaleString(Locale.GERMANY, List.of(deString, deGermanyString, deGermanyXXString)));

        assertEquals(deString, LocaleHelper.findBestMatchLocaleString(Locale.GERMAN, List.of(deGermanyXXString, deGermanyString, deString)));
        assertEquals(deString, LocaleHelper.findBestMatchLocaleString(Locale.GERMAN, List.of(deString, deGermanyString, deGermanyXXString)));

        assertNull(LocaleHelper.findBestMatchLocaleString(deGermanyXX, List.of()));
        assertThrows(NullPointerException.class,() -> LocaleHelper.findBestMatchLocaleString(deGermanyXX, null));
    }
}
