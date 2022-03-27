package study.cafe.api

import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import study.cafe.api.dto.ApiResponse
import study.cafe.api.dto.ApiResponse.Companion.success
import study.cafe.api.dto.member.CheckNickNameDuplicationResponse
import study.cafe.service.MemberNicknameService

@RequestMapping("/members")
@RestController
class MemberController(
    private val memberNicknameService: MemberNicknameService
) {

    @PostMapping("/nickname/duplicate")
    fun nickNameDuplicationCheck(@RequestParam nickName: String): ResponseEntity<ApiResponse<CheckNickNameDuplicationResponse>> {
        val isDuplicated = memberNicknameService.checkNickNameDuplication(nickName)
        val response = CheckNickNameDuplicationResponse(isDuplicated)
        return ok(success(response))
    }
}
