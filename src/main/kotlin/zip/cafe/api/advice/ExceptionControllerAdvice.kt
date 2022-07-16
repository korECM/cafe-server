package zip.cafe.api.advice

import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.*
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.status
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import zip.cafe.api.dto.ApiResponse
import zip.cafe.api.dto.ApiResponse.Companion.error
import zip.cafe.security.LoginFailedException

@RestControllerAdvice
class ExceptionControllerAdvice : ResponseEntityExceptionHandler() {

    @ResponseStatus(BAD_REQUEST)
    override fun handleMethodArgumentNotValid(
        ex: MethodArgumentNotValidException,
        headers: HttpHeaders,
        status: HttpStatus,
        request: WebRequest
    ): ResponseEntity<Any> {
        logger.error("handleMethodArgumentNotValid", ex)
        return status(BAD_REQUEST).body(error(ex.messages()))
    }

    private fun MethodArgumentNotValidException.messages(): String {
        return bindingResult.fieldErrors.joinToString(", ") { "${it.field}: ${it.defaultMessage.orEmpty()}" }
    }

    @ResponseStatus(UNAUTHORIZED)
    @ExceptionHandler(LoginFailedException::class)
    fun handleUnauthorizedException(exception: LoginFailedException): ApiResponse<Unit> {
        logger.error("handleUnauthorizedException", exception)
        return error(exception.message)
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException::class, IllegalStateException::class)
    fun handleBadRequestException(exception: RuntimeException): ApiResponse<Unit> {
        logger.error("handleBadRequestException", exception)
        return error(exception.message)
    }

    @ResponseStatus(INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception::class)
    fun handleGlobalException(exception: Exception): ApiResponse<Unit> {
        logger.error("handleGlobalException", exception)
        return error(exception.message)
    }

    override fun handleExceptionInternal(
        ex: java.lang.Exception,
        body: Any?,
        headers: HttpHeaders,
        status: HttpStatus,
        request: WebRequest
    ): ResponseEntity<Any> {
        logger.error("handleExceptionInternal", ex)
        return status(status).body(error(ex.message))
    }
}
