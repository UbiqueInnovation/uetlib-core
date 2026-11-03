package ch.ubique.uetlib.core.utils;

import static ch.ubique.uetlib.core.utils.HttpHeaderUtils.formatHttpHeaderDate;
import static ch.ubique.uetlib.core.utils.HttpHeaderUtils.parseHttpDateOrOffsetHeader;
import static ch.ubique.uetlib.core.utils.TimeUtils.ZONE_ID_EUROPE_ZURICH;
import static ch.ubique.uetlib.core.utils.TimeUtils.ZONE_ID_GMT;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;

class HttpHeaderUtilsTest {

    @Test
    void testFormatHttpHeaderDate() {
        final var lastSecond1999 = ZonedDateTime.of(1999, 12, 31, 23, 59, 59, 0, ZONE_ID_GMT);
        assertEquals("Fri, 31 Dec 1999 23:59:59 GMT", formatHttpHeaderDate(lastSecond1999));

        // make sure day of month is 0 padded
        final var fifthOfMarch2025 = ZonedDateTime.of(2025, 3, 5, 15, 3, 7, 0, ZONE_ID_GMT);
        assertEquals("Wed, 05 Mar 2025 15:03:07 GMT", formatHttpHeaderDate(fifthOfMarch2025));

        // test zoned date time with time zone other than GMT
        final var notUtc = ZonedDateTime.of(2025, 3, 5, 16, 3, 7, 0, ZONE_ID_EUROPE_ZURICH);
        assertEquals("Wed, 05 Mar 2025 15:03:07 GMT", formatHttpHeaderDate(notUtc));
    }

    @Test
    void testParseHttpDateOrOffsetHeader() {
        final var lastSecond1999 = ZonedDateTime.of(1999, 12, 31, 23, 59, 59, 0, ZONE_ID_GMT);
        assertEquals(
                lastSecond1999,
                parseHttpDateOrOffsetHeader("Fri, 31 Dec 1999 23:59:59 GMT", ZonedDateTime.now()));
        assertEquals(
                lastSecond1999.plusSeconds(120),
                parseHttpDateOrOffsetHeader("120", lastSecond1999));
    }
}
