package zip.cafe.api

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

    @GetMapping("/feeds/review")
    fun reviewFeeds(@LoginUserId memberId: Long, @RequestParam(required = false) minReviewId: Long?): ResponseEntity<ApiResponse<List<FeedInfo>>> {
        val feeds = feedService.getReviewFeeds(memberId, null)
        return ok(success(feeds))
    }
}
