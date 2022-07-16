package zip.cafe.api

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import zip.cafe.api.dto.ApiResponse
import zip.cafe.api.dto.ApiResponse.Companion.success
import zip.cafe.api.dto.FollowersWhoLikeCafe
import zip.cafe.api.dto.FollowersWhoLikeCafe.InnerFollowersWhoLikeCafe.Companion.from
import zip.cafe.api.dto.FollowersWhoWriteReview
import zip.cafe.api.dto.FollowersWhoWriteReview.InnerFollowersWhoWriteReview.Companion.from
import zip.cafe.api.dto.SingleCafeInfo
import zip.cafe.api.dto.SingleCafeInfo.Image.Companion.from
import zip.cafe.api.dto.SingleCafeInfo.InnerMenu.Companion.from
import zip.cafe.api.dto.SingleCafeInfo.Keyword.Companion.from
import zip.cafe.security.LoginUserId
import zip.cafe.service.CafeService

@RequestMapping("/cafes")
@RestController
class CafeController(
    private val cafeService: CafeService
) {

    @GetMapping("/{cafeId}")
    fun findCafeByIdForDetail(@LoginUserId(optional = true) userId: Long?, @PathVariable("cafeId") cafeId: Long): ApiResponse<SingleCafeInfo> {
        val cafe = requireNotNull(cafeService.findByIdForDetailPage(cafeId)) { "$cafeId 카페를 찾을 수 없습니다" }
        val reviewSummary = cafeService.getReviewSummaryById(cafeId)
        val imageSummary = cafeService.getImageSummaryById(cafeId)
        val keywordSummary = cafeService.getKeywordSummaryById(cafeId)

        return success(
            SingleCafeInfo(
                id = cafe.id,
                name = cafe.name,
                address = cafe.address,
                openingHours = cafe.openingHours,
                averageOfFinalScores = reviewSummary.averageOfFinalScores,
                reviewCount = reviewSummary.numberOfReviews,
                keywords = keywordSummary.map(::from),
                cafeImages = imageSummary.map(::from),
                menus = cafe.menus.map(::from),
            )
        )
    }

    @GetMapping("/{cafeId}/followers/write/review")
    fun findFollowersWhoWriteReview(@LoginUserId(optional = true) userId: Long?, @PathVariable("cafeId") cafeId: Long): ApiResponse<FollowersWhoWriteReview> {
        if (userId == null) {
            return success(null)
        }
        val followersWhoWriteReview = userId.let { cafeService.findFollowerWhoWriteReview(it, cafeId) }

        return success(
            FollowersWhoWriteReview(
                followersWhoWriteReview.map(::from)
            )
        )
    }

    @GetMapping("/{cafeId}/followers/like/cafe")
    fun findFollowersWhoLikeCafe(@LoginUserId(optional = true) userId: Long?, @PathVariable("cafeId") cafeId: Long): ApiResponse<FollowersWhoLikeCafe> {
        if (userId == null) {
            return success(null)
        }
        val followersWhoLikeCafe = userId.let { cafeService.findFollowerWhoLikeCafe(it, cafeId) }

        return success(
            FollowersWhoLikeCafe(
                followersWhoLikeCafe.map(::from)
            )
        )

    }
}
