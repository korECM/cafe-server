package zip.cafe.api.auth

import org.springframework.http.HttpStatus.CREATED
import org.springframework.web.bind.annotation.*
import zip.cafe.api.dto.ApiResponse
import zip.cafe.api.dto.ApiResponse.Companion.success
import zip.cafe.api.dto.LocalSignUpResponse
import zip.cafe.service.auth.AuthService
import zip.cafe.service.auth.KakaoAuthService

@RequestMapping("/auth/kakao")
@RestController
class KakaoAuthController(
    private val kakaoAuthService: KakaoAuthService,
    private val authService: AuthService
) {

    @ResponseStatus(CREATED)
    @PostMapping("/signIn")
    fun signUp(@RequestParam accessToken: String): ApiResponse<LocalSignUpResponse> {
        kakaoAuthService.getUserInfo(accessToken)
        return success(null)
    }
}
