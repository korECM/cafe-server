package zip.cafe.api.auth.dto


data class KakaoSignInRequest(
    val accessToken: String
)

data class KakaoSignInResponse(
    val token: String
)
