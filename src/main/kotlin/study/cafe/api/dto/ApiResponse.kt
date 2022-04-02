package study.cafe.api.dto

import io.swagger.v3.oas.annotations.media.Schema

data class ApiResponse<T>(
    @Schema(title = "에러 메시지")
    val message: String? = "",
    val body: T? = null
) {
    companion object {
        fun error(message: String?): ApiResponse<Unit> = ApiResponse(message = message)

        fun <T> success(body: T?): ApiResponse<T> = ApiResponse(body = body)
    }
}
