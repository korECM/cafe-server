package study.cafe.api

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import study.cafe.api.dto.local.LocalSignUpRequest
import study.cafe.service.LocalAuthService
import javax.validation.Valid

@RestController
class LocalAuthController(
    private val localAuthService: LocalAuthService
) {

    @Operation(summary = "ID/PW 회원가입", description = "ID와 Password로 하는 회원가입")
    @PostMapping("/signUp")
    fun signUp(@Parameter @Valid @RequestBody dto: LocalSignUpRequest): Long {
        return localAuthService.signUp(dto)
    }
}
