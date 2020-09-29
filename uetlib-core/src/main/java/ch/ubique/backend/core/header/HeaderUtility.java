package ch.ubique.backend.core.header;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/** Helper class for request header. */
public class HeaderUtility {

    public static final String CACHE_CONTROL = "Cache-Control";
    public static final String NEXT_REFRESH = "X-Next-Refresh";
    public static final String MAX_AGE = "max-age";
    private static final ThreadLocal<SimpleDateFormat> headerDateFormatter =
            new ThreadLocal<SimpleDateFormat>();

    /**
     * Create a {@link ResponseEntity<T>} with HTTP-Status 404, not found. Does not contain any body
     * data.
     *
     * @return {@link ResponseEntity<T>}
     */
    public static <T> ResponseEntity<T> createNotFoundResponseEntity() {
        return new ResponseEntity<T>(HttpStatus.NOT_FOUND);
    }

    /**
     * Creates a {@link ResponseEntity<T>} with the given body, HTTP-Status 200, and the max age and
     * next refresh headers.
     *
     * @param body {@link T} The body of the response
     * @param nextRefreshSeconds X-Next-Refresh value in seconds
     * @param maxAgeSeconds max-age value in seconds
     * @return {@link ResponseEntity<T>} with the given body, headers and status OK
     */
    public static <T> ResponseEntity<T> createOKResponseEntity(
            T body, int nextRefreshSeconds, int maxAgeSeconds) {
        return new ResponseEntity<T>(
                body, createHeaders(maxAgeSeconds, nextRefreshSeconds), HttpStatus.OK);
    }

    /**
     * Sets the following two headers:
     *
     * <ul>
     *   <li>Cache-Control: max-age=maxAgeSeconds
     *   <li>X-Next-Refresh: now + nextRefreshSeconds
     * </ul>
     *
     * @param response to set header values on
     * @param nextRefreshSeconds X-Next-Refresh value in seconds
     * @param maxAgeSeconds max-age value in seconds
     */
    public static void setHeaders(
            HttpServletResponse response, int maxAgeSeconds, int nextRefreshSeconds) {
        response.setHeader(CACHE_CONTROL, createMaxAgeHeaderValue(maxAgeSeconds));
        response.setHeader(NEXT_REFRESH, createNextRefreshHeaderValue(nextRefreshSeconds));
    }

    /**
     * Creates headers with the following two entries:
     *
     * <ul>
     *   <li>Cache-Control: max-age=maxAgeSeconds
     *   <li>X-Next-Refresh: now + nextRefreshSeconds
     * </ul>
     *
     * @param maxAgeSeconds max-age value in seconds
     * @param nextRefreshSeconds X-Next-Refresh value in seconds
     * @return {@link HttpHeaders} with the headers set
     */
    public static HttpHeaders createHeaders(int maxAgeSeconds, int nextRefreshSeconds) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(CACHE_CONTROL, createMaxAgeHeaderValue(maxAgeSeconds));
        headers.add(NEXT_REFRESH, createNextRefreshHeaderValue(nextRefreshSeconds));
        return headers;
    }

    /**
     * Creates the max-age header value with the given max-age in seconds
     *
     * @param maxAgeSeconds max-age value in seconds
     * @return max-age string
     */
    public static String createMaxAgeHeaderValue(int maxAgeSeconds) {
        return MAX_AGE + "=" + maxAgeSeconds;
    }

    /**
     * return the header value for X-Next-Refresh
     *
     * @param nextRefreshSeconds X-Next-Refresh value in seconds
     * @return X-Next-Refresh string
     */
    public static String createNextRefreshHeaderValue(int nextRefreshSeconds) {
        return formatHeaderDate(getDate(nextRefreshSeconds * 1000L));
    }

    /**
     * Formats the date to a http timestamp.
     *
     * @param date to format
     * @return formatted date string
     */
    private static String formatHeaderDate(Date date) {
        SimpleDateFormat sdf = headerDateFormatter.get();
        if (sdf == null) {
            sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
            sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
            headerDateFormatter.set(sdf);
        }
        return sdf.format(date);
    }

    /**
     * Returns the date from now plus the given number of milliseconds.
     *
     * @param millis Millisecond offset from now
     * @return {@link Date} with offset millis from now
     */
    private static Date getDate(long millis) {
        return new Date(System.currentTimeMillis() + millis);
    }
}
