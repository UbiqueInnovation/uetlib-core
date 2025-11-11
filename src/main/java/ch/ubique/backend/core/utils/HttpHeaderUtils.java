package ch.ubique.backend.core.utils;

import static ch.ubique.backend.core.utils.TimeUtils.TIME_UTILS_GMT;
import static ch.ubique.backend.core.utils.TimeUtils.ZONE_ID_GMT;

import org.apache.commons.lang3.math.NumberUtils;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;
import java.util.Locale;

public final class HttpHeaderUtils {

    private static final ZoneId ZONE_ID_GMT = ZoneId.of("GMT").normalized();
    private static final DateTimeFormatter HTTP_HEADER_DATE_FORMATTER =
            DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss z", Locale.US)
                    .withZone(ZONE_ID_GMT);

    private HttpHeaderUtils() {}

    /** return the header value for X-Next-Refresh (package-private to enable tests) */
    public static String createNextRefreshHeaderValue(final Duration nextRefresh) {
        return formatHttpHeaderDate(TIME_UTILS_GMT.now().plus(nextRefresh));
    }

    /**
     * return the given date format for HTTP headers
     *
     * @param temporal date to format
     * @return formatted date
     */
    public static String formatHttpHeaderDate(final Temporal temporal) {
        return HTTP_HEADER_DATE_FORMATTER.format(
                temporal instanceof final Instant instant ? instant.atZone(ZONE_ID_GMT) : temporal);
    }

    /**
     * Parse Retry-After HTTP Header which can either be a date in format of {@link
     * #HTTP_HEADER_DATE_FORMATTER} or a deltaSeconds offset from now
     *
     * @param dateOrOffsetHeaderValue date to parse as date or seconds offset from now
     * @param offsetStart offset start to add offset seconds in the case of the header representing
     *     a seconds offset
     * @return parse date
     */
    public static ZonedDateTime parseHttpDateOrOffsetHeader(
            final String dateOrOffsetHeaderValue, final ZonedDateTime offsetStart) {
        if (NumberUtils.isParsable(dateOrOffsetHeaderValue)) {
            final var deltaSeconds = Long.parseLong(dateOrOffsetHeaderValue);
            return offsetStart.plusSeconds(deltaSeconds);
        }
        return ZonedDateTime.parse(dateOrOffsetHeaderValue, HTTP_HEADER_DATE_FORMATTER)
                .withZoneSameInstant(ZONE_ID_GMT);
    }
}
