package zip.cafe.api

import org.springframework.http.HttpStatus.CREATED
import org.springframework.web.bind.annotation.*
import zip.cafe.api.dto.ApiResponse
import zip.cafe.api.dto.ApiResponse.Companion.success
import zip.cafe.api.dto.FootPrintRegisterRequest
import zip.cafe.security.LoginUserId
import javax.validation.Valid

@RequestMapping("/footprints")
@RestController
class FootPrintController(
) {
    @ResponseStatus(CREATED)
    @PostMapping("")
    fun register(
        @LoginUserId userId: Long,
        @Valid @RequestBody request: FootPrintRegisterRequest
    ): ApiResponse<Nothing> {
        return success(null)
    }
}
