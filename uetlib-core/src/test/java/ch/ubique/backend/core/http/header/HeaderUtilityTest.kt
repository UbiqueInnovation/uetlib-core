package ch.ubique.backend.core.http.header

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import org.springframework.mock.web.MockHttpServletResponse
import java.text.ParseException
import java.util.*

class HeaderUtilityTest {

    private fun getSeconds(date: Date): Int {
        val calendar = Calendar.getInstance()
        calendar.time = date
        return calendar[Calendar.SECOND]
    }

    @Throws(ParseException::class)
    private fun parseDate(date: String): Date {
        return HeaderUtility.headerDateFormatter.get().parse(date)
    }

    private fun formatDate(date: Date): String {
        return HeaderUtility.headerDateFormatter.get().format(date)
    }

    @Test
    fun testCreateNotFoundResponseEntity() {
        val responseEntity = HeaderUtility.createNotFoundResponseEntity<String>()
        Assertions.assertEquals(HttpStatus.NOT_FOUND, responseEntity.statusCode)
        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), responseEntity.statusCodeValue)
        Assertions.assertNull(responseEntity.body)
    }

    @Test
    fun testCreateOKResponseEntity() {
        var body = "BODY"
        var responseEntity = HeaderUtility.createOKResponseEntity(body)
        Assertions.assertEquals(HttpStatus.OK, responseEntity.statusCode)
        Assertions.assertEquals(HttpStatus.OK.value(), responseEntity.statusCodeValue)
        Assertions.assertEquals(body, responseEntity.body)


        body = "BODY AND HEADERS"
        val maxAgeSeconds = 5 * 60 * 60
        val nextRefreshSeconds = 3 * 60 * 60
        responseEntity = HeaderUtility.createOKResponseEntity(body, maxAgeSeconds, nextRefreshSeconds)
        Assertions.assertEquals(HttpStatus.OK, responseEntity.statusCode)
        Assertions.assertEquals(HttpStatus.OK.value(), responseEntity.statusCodeValue)
        Assertions.assertEquals(body, responseEntity.body)
    }

    @Test
    fun testCreateInternalServerErrorResponseEntity() {
        val body = "BODY"
        val responseEntity = HeaderUtility.createInternalServerErrorResponseEntity(body)
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.statusCode)
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), responseEntity.statusCodeValue)
        Assertions.assertEquals(body, responseEntity.body)
    }

    @Test
    fun testCreateServiceUnavailableResponse() {
        val responseEntity = HeaderUtility.createServiceUnavailableResponseEntity<Any>()
        Assertions.assertEquals(HttpStatus.SERVICE_UNAVAILABLE, responseEntity.statusCode)
        Assertions.assertEquals(HttpStatus.SERVICE_UNAVAILABLE.value(), responseEntity.statusCodeValue)
        Assertions.assertEquals(null, responseEntity.body)
    }

    @Test
    fun testCreateNoContentResponseEntity() {
        val responseEntity = HeaderUtility.createNoContentResponseEntity<Any>(3600, 10800)
        Assertions.assertEquals(HttpStatus.NO_CONTENT, responseEntity.statusCode)
        Assertions.assertEquals(HttpStatus.NO_CONTENT.value(), responseEntity.statusCodeValue)
        Assertions.assertEquals(null, responseEntity.body)
    }

    @Test
    fun testSetHeaders() {
        val response = MockHttpServletResponse()
        val maxAgeSeconds = 60 * 60
        val nextRefreshSeconds = 30 * 60
        HeaderUtility.setHeaders(response, maxAgeSeconds, nextRefreshSeconds)
        val expectedCacheControl = HeaderUtility.createMaxAgeHeaderValue(maxAgeSeconds)
        val actualCacheControl = response.getHeader(HeaderUtility.CACHE_CONTROL)
        Assertions.assertEquals(expectedCacheControl, actualCacheControl)
    }

    @Test
    @Throws(ParseException::class)
    fun testCreateHeaders() {
        val maxAgeSeconds = 60 * 60
        val nextRefreshSeconds = 30 * 60
        val headers = HeaderUtility.createHeaders(maxAgeSeconds, nextRefreshSeconds)
        val expectedCacheControl = HeaderUtility.createMaxAgeHeaderValue(maxAgeSeconds)
        val actualCacheControl = headers.cacheControl
        Assertions.assertEquals(expectedCacheControl, actualCacheControl)

        val nextRefreshHeaders = headers[HeaderUtility.NEXT_REFRESH]
        Assertions.assertNotEquals(null, nextRefreshHeaders)
        Assertions.assertEquals(nextRefreshHeaders!!.size, 1)

        val actualDate = parseDate(nextRefreshHeaders[0])
        val expectedDate = parseDate(HeaderUtility.createNextRefreshHeaderValue(nextRefreshSeconds))

        // either both dates have equal millis and thus have the same formatted string ...
        val equalMillisAndEqualString = (actualDate.time == expectedDate.time
                && formatDate(actualDate) == formatDate(expectedDate))
        // ... or the actual date is before the expected date but at most ten milliseconds earlier
        val actualIsBeforeAndAtMostTenMillis = (actualDate.before(expectedDate)
                && expectedDate.time - actualDate.time <= 10L)
        Assertions.assertTrue(equalMillisAndEqualString || actualIsBeforeAndAtMostTenMillis)
    }

    @Test
    fun testCreateMaxAgeHeaderValue() {
        val maxAge = "max-age"
        Assertions.assertEquals(maxAge, HeaderUtility.MAX_AGE)

        var maxAgeSeconds = 0
        var expectedHeaderValue = "$maxAge=$maxAgeSeconds"
        var actualHeaderValue = HeaderUtility.createMaxAgeHeaderValue(maxAgeSeconds)
        Assertions.assertEquals(expectedHeaderValue, actualHeaderValue)

        maxAgeSeconds = -1
        expectedHeaderValue = "$maxAge=$maxAgeSeconds"
        actualHeaderValue = HeaderUtility.createMaxAgeHeaderValue(maxAgeSeconds)
        Assertions.assertEquals(expectedHeaderValue, actualHeaderValue)

        maxAgeSeconds = 60 * 60
        expectedHeaderValue = "$maxAge=$maxAgeSeconds"
        actualHeaderValue = HeaderUtility.createMaxAgeHeaderValue(maxAgeSeconds)
        Assertions.assertEquals(expectedHeaderValue, actualHeaderValue)

        maxAgeSeconds = Int.MAX_VALUE
        expectedHeaderValue = "$maxAge=$maxAgeSeconds"
        actualHeaderValue = HeaderUtility.createMaxAgeHeaderValue(maxAgeSeconds)
        Assertions.assertEquals(expectedHeaderValue, actualHeaderValue)

        maxAgeSeconds = Int.MIN_VALUE
        expectedHeaderValue = "$maxAge=$maxAgeSeconds"
        actualHeaderValue = HeaderUtility.createMaxAgeHeaderValue(maxAgeSeconds)
        Assertions.assertEquals(expectedHeaderValue, actualHeaderValue)
    }

    @Test
    fun testCreateNextRefreshHeaderValue() {
    }

    @Test
    fun testGetDate() {
        var millis = 0L
        var expectedDate = Date(System.currentTimeMillis() + millis)
        var actualDate = HeaderUtility.getDate(millis)
        Assertions.assertEquals(getSeconds(expectedDate), getSeconds(actualDate))

        millis = -1L
        expectedDate = Date(System.currentTimeMillis() + millis)
        actualDate = HeaderUtility.getDate(millis)
        Assertions.assertEquals(getSeconds(expectedDate), getSeconds(actualDate))

        millis = Long.MAX_VALUE
        expectedDate = Date(System.currentTimeMillis() + millis)
        actualDate = HeaderUtility.getDate(millis)
        Assertions.assertEquals(getSeconds(expectedDate), getSeconds(actualDate))

        millis = Long.MIN_VALUE
        expectedDate = Date(System.currentTimeMillis() + millis)
        actualDate = HeaderUtility.getDate(millis)
        Assertions.assertEquals(getSeconds(expectedDate), getSeconds(actualDate))

        millis = 60 * 60 * 1000L
        expectedDate = Date(System.currentTimeMillis() + millis)
        actualDate = HeaderUtility.getDate(millis)
        Assertions.assertEquals(getSeconds(expectedDate), getSeconds(actualDate))
    }
}