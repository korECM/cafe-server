package zip.cafe.api.profile

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import zip.cafe.api.dto.ApiResponse
import zip.cafe.api.dto.ApiResponse.Companion.success
import zip.cafe.api.profile.dto.ProfileInfo
import zip.cafe.security.LoginUserId
import zip.cafe.service.profile.ProfileService

@RequestMapping("/profiles")
@RestController
class ProfileController(
    private val profileService: ProfileService
) {

    @GetMapping("/members/{memberId}")
    fun getProfile(@LoginUserId loginMemberId: Long, @PathVariable memberId: Long): ApiResponse<ProfileInfo> {
        val profile = profileService.getProfile(memberId)
        return success(profile)
    }

    @GetMapping("/members/{memberId}/reviews")
    fun getReview(@LoginUserId loginMemberId: Long, @PathVariable memberId: Long): ApiResponse<ProfileInfo> {
        val profile = profileService.getProfile(memberId)
        return success(profile)
    }
}
