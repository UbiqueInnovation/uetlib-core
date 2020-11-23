package ch.ubique.backend.core.http.header

import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import java.text.SimpleDateFormat
import java.util.*
import javax.servlet.http.HttpServletResponse

/** Helper class to generate [ResponseEntity] responses and/or request header.  */
class HeaderUtility private constructor() {
    companion object {
        const val CACHE_CONTROL = "Cache-Control"
        const val NEXT_REFRESH = "X-Next-Refresh"
        const val MAX_AGE = "max-age"

        /** format Date as String or parse to Date (package-private to enable tests)  */
        val headerDateFormatter = ThreadLocal<SimpleDateFormat>()

        /**
         * Create a [ResponseEntity] with HTTP-Status 404, not found. Does not contain any body
         * data.
         *
         * @return [ResponseEntity]
         */
        @JvmStatic
        fun <T> createNotFoundResponseEntity(): ResponseEntity<T> {
            return ResponseEntity(HttpStatus.NOT_FOUND)
        }

        /**
         * Creates a [ResponseEntity] with the given body, HTTP-Status 200, and the max age and
         * next refresh headers.
         *
         * @param body [T] The body of the response
         * @param maxAgeSeconds max-age value in seconds
         * @param nextRefreshSeconds X-Next-Refresh value in seconds
         * @return [ResponseEntity] with the given body, headers and status OK
         */
        @JvmStatic
        fun <T> createOKResponseEntity(
                body: T, maxAgeSeconds: Int, nextRefreshSeconds: Int): ResponseEntity<T> {
            return ResponseEntity(
                    body, createHeaders(maxAgeSeconds, nextRefreshSeconds), HttpStatus.OK)
        }

        /**
         * Creates a [ResponseEntity] with the given body and HTTP-Status 200.
         *
         * @param body [T] The body of the response
         * @return [ResponseEntity] with the given body, headers and status OK
         */
        @JvmStatic
        fun <T> createOKResponseEntity(body: T): ResponseEntity<T> {
            return ResponseEntity(body, HttpStatus.OK)
        }

        /**
         * Creates a [ResponseEntity] with the given body and HTTP-Status 500.
         *
         * @param body [T] The body of the response
         * @return [ResponseEntity] with the given body, headers and status 500
         */
        @JvmStatic
        fun <T> createInternalServerErrorResponseEntity(body: T): ResponseEntity<T> {
            return ResponseEntity(body, HttpStatus.INTERNAL_SERVER_ERROR)
        }

        /**
         * Creates an empty [ResponseEntity] with HTTP-Status 500.
         *
         * @return [ResponseEntity] with status 503
         */
        @JvmStatic
        fun createInternalServerErrorResponseEntity(): ResponseEntity<Any> {
            return ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)
        }

        /**
         * Creates a [ResponseEntity] with the given body and HTTP-Status 400.
         *
         * @param body [T] The body of the response
         * @return [ResponseEntity] with the given body, headers and status 400
         */
        @JvmStatic
        fun <T> createBadRequestResponseEntity(body: T): ResponseEntity<T> {
            return ResponseEntity(body, HttpStatus.BAD_REQUEST)
        }

        /**
         * Returns an empty [ResponseEntity] with HTTP-Status 503.
         *
         * @return
         */
        @JvmStatic
        fun <T> createServiceUnavailableResponseEntity(): ResponseEntity<T> {
            return ResponseEntity<T>(HttpStatus.SERVICE_UNAVAILABLE)
        }

        /**
         * Returns an empty [ResponseEntity] with HTTP-Status 204 (no content)
         *
         * @param nextRefreshSeconds
         * @param maxAgeSeconds
         * @return
         */
        @JvmStatic
        fun <T> createNoContentResponseEntity(nextRefreshSeconds: Int, maxAgeSeconds: Int): ResponseEntity<T> {
            val headers = createHeaders(maxAgeSeconds, nextRefreshSeconds)
            return ResponseEntity<T>(headers, HttpStatus.NO_CONTENT)
        }

        /**
         * Sets the following two headers:
         *
         *
         *  * Cache-Control: max-age=maxAgeSeconds
         *  * X-Next-Refresh: now + nextRefreshSeconds
         *
         *
         * @param response to set header values on
         * @param nextRefreshSeconds X-Next-Refresh value in seconds
         * @param maxAgeSeconds max-age value in seconds
         */
        @JvmStatic
        fun setHeaders(
                response: HttpServletResponse, maxAgeSeconds: Int, nextRefreshSeconds: Int) {
            response.setHeader(CACHE_CONTROL, createMaxAgeHeaderValue(maxAgeSeconds))
            response.setHeader(NEXT_REFRESH, createNextRefreshHeaderValue(nextRefreshSeconds))
        }

        /**
         * Creates headers with the following two entries:
         *
         *
         *  * Cache-Control: max-age=maxAgeSeconds
         *  * X-Next-Refresh: now + nextRefreshSeconds
         *
         *
         * @param maxAgeSeconds max-age value in seconds
         * @param nextRefreshSeconds X-Next-Refresh value in seconds
         * @return [HttpHeaders] with the headers set
         */
        @JvmStatic
        fun createHeaders(maxAgeSeconds: Int, nextRefreshSeconds: Int): HttpHeaders {
            val headers = HttpHeaders()
            headers.add(CACHE_CONTROL, createMaxAgeHeaderValue(maxAgeSeconds))
            headers.add(NEXT_REFRESH, createNextRefreshHeaderValue(nextRefreshSeconds))
            return headers
        }

        /**
         * Creates the max-age header value with the given max-age in seconds (package-private to enable
         * tests)
         *
         * @param maxAgeSeconds max-age value in seconds
         * @return max-age string
         */
        @JvmStatic
        fun createMaxAgeHeaderValue(maxAgeSeconds: Int): String {
            return "$MAX_AGE=$maxAgeSeconds"
        }

        /**
         * return the header value for X-Next-Refresh (package-private to enable tests)
         *
         * @param nextRefreshSeconds X-Next-Refresh value in seconds
         * @return X-Next-Refresh string
         */
        @JvmStatic
        fun createNextRefreshHeaderValue(nextRefreshSeconds: Int): String {
            return formatHeaderDate(getDate(nextRefreshSeconds * 1000L))
        }

        /**
         * Formats the date to a http timestamp. (package-private to enable tests)
         *
         * @param date to format
         * @return formatted date string
         */
        @JvmStatic
        private fun formatHeaderDate(date: Date?): String {
            var sdf = headerDateFormatter.get()
            if (sdf == null) {
                sdf = SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US)
                sdf.timeZone = TimeZone.getTimeZone("GMT")
                headerDateFormatter.set(sdf)
            }
            return sdf.format(date)
        }

        /**
         * Returns the date from now plus the given number of milliseconds. (package-private to enable
         * tests)
         *
         * @param millis Millisecond offset from now
         * @return [Date] with offset millis from now
         */
        @JvmStatic
        fun getDate(millis: Long): Date {
            return Date(System.currentTimeMillis() + millis)
        }
    }
}