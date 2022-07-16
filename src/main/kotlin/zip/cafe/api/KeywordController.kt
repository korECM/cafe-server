package zip.cafe.api

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import zip.cafe.api.dto.ApiResponse
import zip.cafe.api.dto.ApiResponse.Companion.success
import zip.cafe.api.dto.KeywordListElement
import zip.cafe.service.KeywordService

@RestController
class KeywordController(
    private val keywordService: KeywordService
) {

    @GetMapping("/keywords")
    fun keywords(): ApiResponse<List<KeywordListElement>> {
        val keywordList = keywordService.getKeywords()
            .map { KeywordListElement.from(it) }
        return success(keywordList)
    }
}
