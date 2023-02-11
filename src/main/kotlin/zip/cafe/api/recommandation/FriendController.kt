package zip.cafe.api.recommandation

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import zip.cafe.api.dto.ApiResponse
import zip.cafe.api.dto.ApiResponse.Companion.success
import zip.cafe.api.recommandation.dto.FriendRecommendationResponse
import zip.cafe.security.LoginUserId
import zip.cafe.service.recommendation.RecommendationService

@RequestMapping("/recommendation")
@RestController
class FriendController(
    private val recommendationService: RecommendationService
) {

    @GetMapping("/friends")
    fun getRecommendationFriends(@LoginUserId loginMemberId: Long): ApiResponse<FriendRecommendationResponse> {
        val result = recommendationService.getFriendRecommendation(loginMemberId)
        return success(FriendRecommendationResponse(result))
    }}
