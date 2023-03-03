package zip.cafe.api

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import zip.cafe.api.dto.*
import zip.cafe.api.dto.ApiResponse.Companion.success
import zip.cafe.api.dto.CafeInfo.Companion.from
import zip.cafe.api.dto.KeywordInfo.Companion.from
import zip.cafe.api.dto.MemberInfo.Companion.from
import zip.cafe.service.SearchService
import zip.cafe.util.Point
import zip.cafe.util.Rectangle

@RequestMapping("/search")
@RestController
class SearchController(
    private val searchService: SearchService
) {

    @PostMapping("/member")
    fun searchMember(@RequestBody request: MemberSearchRequest): ApiResponse<List<MemberInfo>> {
        val result = searchService.searchMember(request.name)
        return success(result.map(::from))
    }

    @PostMapping("/cafe-boundary")
    fun searchCafeInBoundary(@RequestBody request: CafeSearchRequestWithBoundary): ApiResponse<List<CafeInfo>> {
        val boundary = Rectangle(
            leftTop = Point(latitude = request.leftTopLatitude, longitude = request.leftTopLongitude),
            rightBottom = Point(latitude = request.rightBottomLatitude, longitude = request.rightBottomLongitude)
        )
        val result = searchService.searchCafeInBoundary(
            request.name,
            request.visitPurposeList,
            request.foodList,
            request.keywordIdList,
            boundary,
            request.minCafeId,
            request.limit
        )
        return success(result.map(::from))
    }

    @PostMapping("/cafe")
    fun searchCafe(@RequestBody request: CafeSearchRequest): ApiResponse<List<CafeInfo>> {
        val result = searchService.searchCafe(
            request.name,
            request.visitPurposeList,
            request.foodList,
            request.keywordIdList,
            request.minCafeId,
            request.limit
        )
        return success(result.map(::from))
    }


    @PostMapping("/keyword")
    fun searchKeyword(@RequestBody request: KeywordSearchRequest): ApiResponse<List<KeywordInfo>> {
        val result = searchService.searchKeyword(request.name)
        return success(from(result))
    }
}
