package study.cafe.api

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import study.cafe.api.dto.ApiResponse
import study.cafe.api.dto.ApiResponse.Companion.success
import study.cafe.api.dto.local.LocalSignUpRequest
import study.cafe.api.dto.local.LocalSignUpResponse
import study.cafe.service.AuthService
import study.cafe.service.LocalAuthService
import javax.validation.Valid

@RestController
class LocalAuthController(
    private val localAuthService: LocalAuthService,
    private val authService: AuthService
) {

    @Operation(summary = "ID/PW 회원가입", description = "ID와 Password로 하는 회원가입")
    @PostMapping("/signUp")
    fun signUp(@Parameter @Valid @RequestBody dto: LocalSignUpRequest): ResponseEntity<ApiResponse<LocalSignUpResponse>> {
        val registeredMemberId = localAuthService.signUp(dto)
        val token = authService.generateToken(registeredMemberId)
        return ok().body(success(LocalSignUpResponse(token)))
    }
}
