package zip.cafe.api

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import zip.cafe.api.dto.ApiResponse
import zip.cafe.api.dto.ApiResponse.Companion.success
import zip.cafe.api.dto.SingleCafeInfo
import zip.cafe.api.dto.SingleCafeInfo.Image.Companion.from
import zip.cafe.api.dto.SingleCafeInfo.Keyword.Companion.from
import zip.cafe.service.CafeService

@RequestMapping("/cafes")
@RestController
class CafeController(
    private val cafeService: CafeService
) {

    @GetMapping("/{cafeId}")
    fun findCafeByIdForDetail(@PathVariable("cafeId") cafeId: Long): ApiResponse<SingleCafeInfo> {
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
                menus = listOf()
            )
        )
    }
}
