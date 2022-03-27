package study.cafe.api

import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import study.cafe.api.dto.ApiResponse
import study.cafe.api.dto.ApiResponse.Companion.success
import study.cafe.api.dto.member.CheckNicknameDuplicationResponse
import study.cafe.service.MemberNicknameService

@RequestMapping("/members")
@RestController
class MemberController(
    private val memberNicknameService: MemberNicknameService
) {

    @PostMapping("/nickname/duplicate")
    fun nicknameDuplicationCheck(@RequestParam nickname: String): ResponseEntity<ApiResponse<CheckNicknameDuplicationResponse>> {
        val isDuplicated = memberNicknameService.checkNicknameDuplication(nickname)
        val response = CheckNicknameDuplicationResponse(isDuplicated)
        return ok(success(response))
    }
}
