package study.cafe.api.advice

import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import study.cafe.api.dto.ApiResponse
import study.cafe.security.LoginFailedException
import io.swagger.v3.oas.annotations.responses.ApiResponse as SwaggerApiResponse

@RestControllerAdvice
class ExceptionControllerAdvice : ResponseEntityExceptionHandler() {

    @SwaggerApiResponse(responseCode = "400", description = "사용자의 요청 형식이 잘못된 경우")
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    override fun handleMethodArgumentNotValid(
        ex: MethodArgumentNotValidException,
        headers: HttpHeaders,
        status: HttpStatus,
        request: WebRequest
    ): ResponseEntity<Any> {
        logger.error("message", ex)
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ApiResponse.error(ex.messages()))
    }

    private fun MethodArgumentNotValidException.messages(): String {
        return bindingResult.fieldErrors.joinToString(", ") { "${it.field}: ${it.defaultMessage.orEmpty()}" }
    }

    @ExceptionHandler(LoginFailedException::class)
    fun handleUnauthorizedException(exception: LoginFailedException): ResponseEntity<ApiResponse<Unit>> {
        logger.error("message", exception)
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(ApiResponse.error(exception.message))
    }

    @SwaggerApiResponse(responseCode = "400", description = "사용자의 요청이 유효하지 않은 경우")
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException::class, IllegalStateException::class)
    fun handleBadRequestException(exception: RuntimeException): ResponseEntity<ApiResponse<Unit>> {
        logger.error("message", exception)
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ApiResponse.error(exception.message))
    }

    @SwaggerApiResponse(responseCode = "500", description = "서버 내부 오류")
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception::class)
    fun handleGlobalException(exception: Exception): ResponseEntity<ApiResponse<Unit>> {
        logger.error("message", exception)
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ApiResponse.error(exception.message))
    }
}
