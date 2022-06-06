package zip.cafe.api

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import zip.cafe.api.dto.ApiResponse
import zip.cafe.api.dto.ApiResponse.Companion.success
import zip.cafe.api.dto.CheckNicknameDuplicationResponse
import zip.cafe.service.MemberService

@RequestMapping("/members")
@RestController
class MemberController(
    private val memberService: MemberService
) {

    @Operation(summary = "닉네임 중복 확인", description = "닉네임이 이미 존재하는지 확인하는")
    @PostMapping("/nickname/duplicate")
    fun nicknameDuplicationCheck(
        @Parameter(
            description = "닉네임",
            example = "홍길동"
        ) @RequestParam nickname: String
    ): ResponseEntity<ApiResponse<CheckNicknameDuplicationResponse>> {
        val isDuplicated = memberService.checkNicknameDuplication(nickname)
        val response = CheckNicknameDuplicationResponse(isDuplicated)
        return ok(success(response))
    }
}
