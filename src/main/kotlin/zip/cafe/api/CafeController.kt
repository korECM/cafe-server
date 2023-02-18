package zip.cafe.api

import org.springframework.web.bind.annotation.*
import zip.cafe.api.dto.*
import zip.cafe.api.dto.ApiResponse.Companion.success
import zip.cafe.api.dto.FollowersWhoLikeCafe.InnerFollowersWhoLikeCafe.Companion.from
import zip.cafe.api.dto.FollowersWhoWriteReview.InnerFollowersWhoWriteReview.Companion.from
import zip.cafe.api.dto.SingleCafeInfo.Image.Companion.from
import zip.cafe.api.dto.SingleCafeInfo.InnerMenu.Companion.from
import zip.cafe.api.dto.SingleCafeInfo.Keyword.Companion.from
import zip.cafe.security.LoginUserId
import zip.cafe.service.CafeService

@RequestMapping("/cafes")
@RestController
class CafeController(private val cafeService: CafeService) {

    @GetMapping("/{cafeId}")
    fun findCafeByIdForDetail(@LoginUserId(optional = true) userId: Long?, @PathVariable("cafeId") cafeId: Long): ApiResponse<SingleCafeInfo> {
        val cafe = requireNotNull(cafeService.findByIdForDetailPage(cafeId)) { "$cafeId 카페를 찾을 수 없습니다" }
        val reviewSummary = cafeService.getReviewSummaryById(cafeId)
        val reviewImageSummary = cafeService.getReviewImageSummaryById(cafeId)
        val countOfReviewByFollowee = userId?.let { cafeService.getFriendReviewCountByCafeId(cafeId, userId) } ?: 0L
        val keywordSummaryOrderByRank = cafeService.getKeywordSummaryById(cafeId).sortedBy { it.rank }

        return success(
            SingleCafeInfo(
                id = cafe.id,
                name = cafe.name,
                address = cafe.address,
                location = cafe.location,
                openingHours = cafe.openingHours.replace(",", "\n").replace("|", "\n"),
                reviewScoreStat = reviewSummary.associate { it.score.score to it.count },
                countOfReviewByFollowee = countOfReviewByFollowee,
                keywords = keywordSummaryOrderByRank.map(::from),
                cafeImages = listOf(
                    SingleCafeInfo.Image(1L, "https://picsum.photos/200"),
                    SingleCafeInfo.Image(2L, "https://picsum.photos/200"),
                    SingleCafeInfo.Image(3L, "https://picsum.photos/200")
                ),
                reviewImages = reviewImageSummary.map(::from),
                menus = cafe.menus.map(::from),
            )
        )
    }

    @GetMapping("/{cafeId}/reviews")
    fun findReviewByCafeIdForCafeDetail(
        @LoginUserId(optional = true) userId: Long?,
        @PathVariable("cafeId") cafeId: Long,
        @RequestParam(required = false) minReviewId: Long?,
        @RequestParam(required = false, defaultValue = "10") limit: Long
    ): ApiResponse<ReviewWithPagination> {
        val reviews = cafeService.getDetailReviewsByCafeIdAndUserId(cafeId, userId, minReviewId, limit)
        return success(reviews)
    }

    @GetMapping("/{cafeId}/reviews/follower")
    fun findFollowerReviewByCafeIdForCafeDetail(
        @LoginUserId(optional = true) userId: Long?,
        @PathVariable("cafeId") cafeId: Long,
    ): ApiResponse<ReviewWithoutPagination> {
        val reviews = cafeService.getFollowerReviewByCafeIdAndUserId(cafeId, userId)
        return success(reviews)
    }

    @GetMapping("/{cafeId}/followers/write/review")
    fun findFollowersWhoWriteReview(@LoginUserId(optional = true) userId: Long?, @PathVariable("cafeId") cafeId: Long): ApiResponse<FollowersWhoWriteReview> {
        if (userId == null) {
            return success(null)
        }
        val followersWhoWriteReview = userId.let { cafeService.findFollowerWhoWriteReview(it, cafeId) }
        return success(FollowersWhoWriteReview(followersWhoWriteReview.map(::from)))
    }

    @GetMapping("/{cafeId}/followers/like/cafe")
    fun findFollowersWhoLikeCafe(@LoginUserId(optional = true) userId: Long?, @PathVariable("cafeId") cafeId: Long): ApiResponse<FollowersWhoLikeCafe> {
        if (userId == null) {
            return success(null)
        }
        val followersWhoLikeCafe = userId.let { cafeService.findFollowerWhoLikeCafe(it, cafeId) }
        return success(FollowersWhoLikeCafe(followersWhoLikeCafe.map(::from)))
    }
}
