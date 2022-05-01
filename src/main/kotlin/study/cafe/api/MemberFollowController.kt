package study.cafe.api

import org.springframework.http.HttpStatus.ACCEPTED
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.status
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import study.cafe.api.dto.ApiResponse
import study.cafe.api.dto.ApiResponse.Companion.success
import study.cafe.security.LoginUserId
import study.cafe.service.MemberFollowService

@RestController
class MemberFollowController(
    private val memberFollowService: MemberFollowService
) {

    @PostMapping("/members/{targetMemberId}/follow")
    fun follow(@LoginUserId loginMemberId: Long, @PathVariable targetMemberId: Long): ResponseEntity<ApiResponse<Nothing>> {
        memberFollowService.follow(loginMemberId, targetMemberId)
        return status(CREATED).body(success(null))
    }

    @PostMapping("/members/{targetMemberId}/unfollow")
    fun unfollow(@LoginUserId loginMemberId: Long, @PathVariable targetMemberId: Long): ResponseEntity<ApiResponse<Nothing>> {
        memberFollowService.unfollow(loginMemberId, targetMemberId)
        return status(ACCEPTED).body(success(null))
    }
}
