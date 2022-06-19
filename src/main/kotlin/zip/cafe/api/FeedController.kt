package zip.cafe.api

import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import zip.cafe.api.dto.ApiResponse
import zip.cafe.api.dto.ApiResponse.Companion.success
import zip.cafe.api.dto.FeedInfo
import zip.cafe.security.LoginUserId
import zip.cafe.service.FeedService

@RestController
class FeedController(
    private val feedService: FeedService
) {

    @Operation(summary = "리뷰 피드 API", description = "메인 홈 화면 친구들 리뷰 내려주는 API")
    @GetMapping("/feeds/review")
    fun reviewFeeds(@LoginUserId memberId: Long, @RequestParam(required = false) minReviewId: Long?): ResponseEntity<ApiResponse<List<FeedInfo>>> {
        val feeds = feedService.getReviewFeeds(memberId, null)
        return ok(success(feeds))
    }
}
