package zip.cafe.api

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

    @PostMapping("/nickname/duplicate")
    fun nicknameDuplicationCheck(
        @RequestParam nickname: String
    ): ResponseEntity<ApiResponse<CheckNicknameDuplicationResponse>> {
        val isDuplicated = memberService.checkNicknameDuplication(nickname)
        val response = CheckNicknameDuplicationResponse(isDuplicated)
        return ok(success(response))
    }
}
