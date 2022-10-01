package zip.cafe.api

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import zip.cafe.api.dto.ApiResponse
import zip.cafe.api.dto.ApiResponse.Companion.success
import zip.cafe.api.dto.CafeInfo
import zip.cafe.api.dto.CafeInfo.Companion.from
import zip.cafe.api.dto.KeywordInfo
import zip.cafe.api.dto.KeywordInfo.Companion.from
import zip.cafe.api.dto.MemberInfo
import zip.cafe.api.dto.MemberInfo.Companion.from
import zip.cafe.service.SearchService

@RequestMapping("/search")
@RestController
class SearchController(
    private val searchService: SearchService
) {

    @GetMapping("/member")
    fun searchMember(@RequestParam name: String): ApiResponse<List<MemberInfo>> {
        val result = searchService.searchMember(name)
        return success(result.map(::from))
    }

    @GetMapping("/cafe")
    fun searchCafe(@RequestParam name: String): ApiResponse<List<CafeInfo>> {
        val result = searchService.searchCafe(name)
        return success(result.map(::from))
    }

    @GetMapping("/keyword")
    fun searchKeyword(@RequestParam keyword: String): ApiResponse<List<KeywordInfo>> {
        val result = searchService.searchKeyword(keyword)
        println(result)
        return success(from(result))
    }
}
