package study.cafe.api

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

    @PostMapping("/signUp")
    fun signUp(@Valid @RequestBody dto: LocalSignUpRequest): Long {
        return localAuthService.signUp(dto)
    }
}
