package ch.ubique.backend.core.http.cache;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Locale;

public final class CacheHeaderUtil {

    private static final DateTimeFormatter HTTP_DATE_FORMATTER =
            DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss z", Locale.US)
                    .withZone(ZoneId.of("UTC"));

    private CacheHeaderUtil() {}

    public static String formatHttpHeaderTimestamp(final TemporalAccessor temporalAccessor) {
        return HTTP_DATE_FORMATTER.format(temporalAccessor);
    }
}
