package study.cafe.api.advice

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
import study.cafe.api.dto.ApiResponse
import study.cafe.api.dto.ApiResponse.Companion.error
import study.cafe.security.LoginFailedException
import io.swagger.v3.oas.annotations.responses.ApiResponse as SwaggerApiResponse

@RestControllerAdvice
class ExceptionControllerAdvice : ResponseEntityExceptionHandler() {

    @SwaggerApiResponse(responseCode = "400", description = "사용자의 요청 형식이 잘못된 경우")
    @ResponseStatus(BAD_REQUEST)
    override fun handleMethodArgumentNotValid(
        ex: MethodArgumentNotValidException,
        headers: HttpHeaders,
        status: HttpStatus,
        request: WebRequest
    ): ResponseEntity<Any> {
        logger.error("message", ex)
        return status(BAD_REQUEST).body(error(ex.messages()))
    }

    private fun MethodArgumentNotValidException.messages(): String {
        return bindingResult.fieldErrors.joinToString(", ") { "${it.field}: ${it.defaultMessage.orEmpty()}" }
    }

    @SwaggerApiResponse(responseCode = "401", description = "사용자의 인증 정보가 유효하지 않은 경우")
    @ResponseStatus(UNAUTHORIZED)
    @ExceptionHandler(LoginFailedException::class)
    fun handleUnauthorizedException(exception: LoginFailedException): ApiResponse<Unit> {
        logger.error("message", exception)
        return error(exception.message)
    }

    @SwaggerApiResponse(responseCode = "400", description = "사용자의 요청이 유효하지 않은 경우")
    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException::class, IllegalStateException::class)
    fun handleBadRequestException(exception: RuntimeException): ApiResponse<Unit> {
        logger.error("message", exception)
        return error(exception.message)
    }

    @SwaggerApiResponse(responseCode = "500", description = "서버 내부 오류")
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception::class)
    fun handleGlobalException(exception: Exception): ApiResponse<Unit> {
        logger.error("message", exception)
        return error(exception.message)
    }

    override fun handleExceptionInternal(
        ex: java.lang.Exception,
        body: Any?,
        headers: HttpHeaders,
        status: HttpStatus,
        request: WebRequest
    ): ResponseEntity<Any> {
        logger.error("message", ex)
        return status(status).body(error(ex.message))
    }
}
