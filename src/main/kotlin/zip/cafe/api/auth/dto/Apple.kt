package zip.cafe.api.auth.dto


data class AppleSignInRequest(
    val identityToken: String
)

data class AppleSignInResponse(
    val token: String
)
