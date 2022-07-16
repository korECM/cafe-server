package zip.cafe.api

import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import zip.cafe.api.dto.*
import zip.cafe.api.dto.ApiResponse.Companion.success
import zip.cafe.service.AuthService
import zip.cafe.service.LocalAuthService
import java.util.*
import javax.validation.Valid

@RequestMapping("/auth/local")
@RestController
class LocalAuthController(
    private val localAuthService: LocalAuthService,
    private val authService: AuthService
) {

    @PostMapping("/signUp")
    fun signUp(@Valid @RequestBody request: LocalSignUpRequest): ResponseEntity<ApiResponse<LocalSignUpResponse>> {
        val registeredMemberId = localAuthService.signUp(request.toDto())
        val token = authService.generateToken(registeredMemberId, Date())
        return ok().body(success(LocalSignUpResponse(token)))
    }

    @PostMapping("/signIn")
    fun signIn(@Valid @RequestBody request: LocalSignInRequest): ResponseEntity<ApiResponse<LocalSignInResponse>> {
        val memberId = localAuthService.signIn(request.toDto())
        val token = authService.generateToken(memberId, Date())
        return ok().body(success(LocalSignInResponse(token)))
    }
}
