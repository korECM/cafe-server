package zip.cafe.api.auth

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import zip.cafe.api.dto.*
import zip.cafe.api.dto.ApiResponse.Companion.success
import zip.cafe.service.auth.AuthService
import zip.cafe.service.auth.LocalAuthService
import java.util.*
import javax.validation.Valid

@RequestMapping("/auth/local")
@RestController
class LocalAuthController(
    private val localAuthService: LocalAuthService,
    private val authService: AuthService
) {

    @PostMapping("/signUp")
    fun signUp(@Valid @RequestBody request: LocalSignUpRequest): ApiResponse<LocalSignUpResponse> {
        val registeredMemberId = localAuthService.signUp(request.toDto())
        val token = authService.generateToken(registeredMemberId, Date())
        return success(LocalSignUpResponse(token))
    }

    @PostMapping("/signIn")
    fun signIn(@Valid @RequestBody request: LocalSignInRequest): ApiResponse<LocalSignInResponse> {
        val memberId = localAuthService.signIn(request.toDto())
        val token = authService.generateToken(memberId, Date())
        return success(LocalSignInResponse(token))
    }
}
