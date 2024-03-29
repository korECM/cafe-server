package zip.cafe.api.auth

import org.springframework.http.HttpStatus.CREATED
import org.springframework.web.bind.annotation.*
import zip.cafe.api.auth.dto.KakaoSignInRequest
import zip.cafe.api.auth.dto.KakaoSignInResponse
import zip.cafe.api.dto.ApiResponse
import zip.cafe.api.dto.ApiResponse.Companion.success
import zip.cafe.service.auth.AuthService
import zip.cafe.service.auth.KakaoAuthService
import zip.cafe.service.profile.ProfileService
import java.util.*
import javax.validation.Valid

@RequestMapping("/auth/kakao")
@RestController
class KakaoAuthController(
    private val kakaoAuthService: KakaoAuthService,
    private val authService: AuthService,
    private val profileService: ProfileService,
) {

    @ResponseStatus(CREATED)
    @PostMapping("/signIn")
    fun signUp(@Valid @RequestBody request: KakaoSignInRequest): ApiResponse<KakaoSignInResponse> {
        val memberId = kakaoAuthService.findMemberIdByKakaoAccessToken(request.accessToken)
        val generatedToken = authService.generateToken(memberId, Date())
        val profileInitInfo = profileService.checkProfileInit(memberId)
        return success(
            KakaoSignInResponse(
                token = generatedToken,
                isProfileInit = profileInitInfo.isInit,
                nickname = profileInitInfo.nickname,
                profileImageURL = profileInitInfo.profileImageURL,
            )
        )
    }
}
