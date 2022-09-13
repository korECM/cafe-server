package zip.cafe.api.auth

import org.springframework.http.HttpStatus.CREATED
import org.springframework.web.bind.annotation.*
import zip.cafe.api.auth.dto.AppleSignInRequest
import zip.cafe.api.auth.dto.AppleSignInResponse
import zip.cafe.api.dto.ApiResponse
import zip.cafe.api.dto.ApiResponse.Companion.success
import zip.cafe.service.auth.AppleAuthService
import zip.cafe.service.auth.AuthService
import java.util.*
import javax.validation.Valid

@RequestMapping("/auth/apple")
@RestController
class AppleAuthController(
    private val appleAuthService: AppleAuthService,
    private val authService: AuthService,
) {

    @ResponseStatus(CREATED)
    @PostMapping("/signIn")
    fun signUp(@Valid @RequestBody request: AppleSignInRequest): ApiResponse<AppleSignInResponse> {
        println(request)
        val memberId = appleAuthService.findMemberIdByAppleIdentityToken(request.identityToken)
        val generatedToken = authService.generateToken(memberId, Date())
        return success(AppleSignInResponse(generatedToken))
    }
}
