package zip.cafe.api.auth.dto

import javax.validation.constraints.NotBlank


data class KakaoSignInRequest(
    @field:NotBlank(message = "accessToken이 필요합니다")
    val accessToken: String
)

data class KakaoSignInResponse(
    val token: String
)
