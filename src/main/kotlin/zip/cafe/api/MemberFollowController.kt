package zip.cafe.api

import org.springframework.http.HttpStatus.ACCEPTED
import org.springframework.http.HttpStatus.CREATED
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import zip.cafe.api.dto.ApiResponse
import zip.cafe.api.dto.ApiResponse.Companion.success
import zip.cafe.security.LoginUserId
import zip.cafe.service.MemberFollowService
import zip.cafe.util.logger

@RestController
class MemberFollowController(
    private val memberFollowService: MemberFollowService
) {

    @ResponseStatus(CREATED)
    @PostMapping("/members/{targetMemberId}/follow")
    fun follow(@LoginUserId loginMemberId: Long, @PathVariable targetMemberId: Long): ApiResponse<Nothing> {
        logger().info("$loginMemberId follow $targetMemberId")
        memberFollowService.follow(loginMemberId, targetMemberId)
        return success(null)
    }

    @ResponseStatus(ACCEPTED)
    @PostMapping("/members/{targetMemberId}/unfollow")
    fun unfollow(@LoginUserId loginMemberId: Long, @PathVariable targetMemberId: Long): ApiResponse<Nothing> {
        logger().info("$loginMemberId unfollow $targetMemberId")
        memberFollowService.unfollow(loginMemberId, targetMemberId)
        return success(null)
    }
}
