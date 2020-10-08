package ch.ubique.backend.core.security

import org.slf4j.LoggerFactory
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.servlet.ModelAndView
import java.io.IOException
import java.util.*
import javax.servlet.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/** LogRequestInterceptor for logging user activity.  */
class LogRequestInterceptor : HandlerInterceptor, Filter {
    @Throws(Exception::class)
    override fun postHandle(
            request: HttpServletRequest,
            response: HttpServletResponse,
            handler: Any,
            modelAndView: ModelAndView?) {
        log(request, response)
    }

    private fun log(request: HttpServletRequest, response: HttpServletResponse) {
        val ipAddress = Optional.ofNullable(request.getHeader(X_FORWARDED_FOR_HEADER))
                .orElse(request.remoteAddr)
        logger.info(String.format(
                "Request: %s | Method: %s | HTTP Status: %d | IP: %s",
                request.requestURI,
                request.method,
                response.status,
                ipAddress))
    }

    override fun destroy() {}

    @Throws(IOException::class, ServletException::class)
    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        if (request is HttpServletRequest && response is HttpServletResponse) {
            log(request, response)
        }
        chain.doFilter(request, response)
    }

    @Throws(ServletException::class)
    override fun init(arg0: FilterConfig) {
    }

    companion object {
        private const val X_FORWARDED_FOR_HEADER = "X-FORWARDED-FOR"
        private val logger = LoggerFactory.getLogger(LogRequestInterceptor::class.java)
    }
}