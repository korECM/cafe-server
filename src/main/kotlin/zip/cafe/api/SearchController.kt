package zip.cafe.api

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import zip.cafe.api.dto.ApiResponse
import zip.cafe.api.dto.ApiResponse.Companion.success
import zip.cafe.api.dto.MemberInfo
import zip.cafe.api.dto.MemberInfo.Companion.from
import zip.cafe.service.SearchService

@RequestMapping("/search")
@RestController
class SearchController(
    private val searchService: SearchService
) {

    @GetMapping
    fun search(@RequestParam query: String): ApiResponse<List<MemberInfo>> {
        val searchResult = searchService.search(query)
        return success(searchResult.map(::from))
    }
}
