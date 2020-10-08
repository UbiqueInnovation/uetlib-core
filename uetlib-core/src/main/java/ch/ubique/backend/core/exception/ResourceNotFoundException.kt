package ch.ubique.backend.core.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus
import java.text.MessageFormat
import java.util.function.Supplier
import javax.validation.constraints.NotNull

/**
 * Whenever a ResourceNotFoundException is thrown in a spring controller method that is not handled
 * elsewhere, spring will return a 404 as the defined bey the @ResponseStatus annotation here.
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND) // 404
class ResourceNotFoundException : RuntimeException {
    constructor() : super()
    constructor(message: String?) : super(message)
    constructor(message: String?, cause: Throwable?) : super(message, cause)

    companion object {
        private const val serialVersionUID = -3763002992193794029L
        fun withMessage(
                pattern: @NotNull String?, vararg arguments: Any?): ResourceNotFoundException {
            return ResourceNotFoundException(MessageFormat.format(pattern ?: "", *arguments))
        }

        fun throwWithMessage(
                pattern: @NotNull String?, vararg arguments: Any?): Supplier<ResourceNotFoundException> {
            return Supplier {
                ResourceNotFoundException(MessageFormat.format(pattern ?: "", *arguments))
            }
        }
    }
}