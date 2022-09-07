package zip.cafe.api.auth

import org.springframework.http.HttpStatus.CREATED
import org.springframework.web.bind.annotation.*
import zip.cafe.api.auth.dto.AppleSignInRequest
import zip.cafe.api.auth.dto.AppleSignInResponse
import zip.cafe.api.dto.ApiResponse
import zip.cafe.api.dto.ApiResponse.Companion.success

@RequestMapping("/auth/apple")
@RestController
class AppleAuthController(
) {

    @ResponseStatus(CREATED)
    @PostMapping("/signIn")
    fun signUp(@RequestBody request: AppleSignInRequest): ApiResponse<AppleSignInResponse> {
        return success(null)
    }
}
