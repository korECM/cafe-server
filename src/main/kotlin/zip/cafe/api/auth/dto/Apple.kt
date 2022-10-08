package zip.cafe.api.auth.dto

import javax.validation.constraints.NotBlank


data class AppleSignInRequest(
    @field:NotBlank(message = "accessToken이 필요합니다")
    val identityToken: String,
    val firstName: String?,
    val lastName: String?,
)

data class AppleSignInResponse(
    val token: String
)
