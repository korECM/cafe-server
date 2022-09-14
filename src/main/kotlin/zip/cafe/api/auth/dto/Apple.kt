package zip.cafe.api.auth.dto

import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull


data class AppleSignInRequest(
    @field:NotBlank(message = "accessToken이 필요합니다")
    val identityToken: String,
    @field:NotNull(message = "firstName이 필요합니다")
    val firstName: String,
    @field:NotNull(message = "lastName이 필요합니다")
    val lastName: String,
)

data class AppleSignInResponse(
    val token: String
)
