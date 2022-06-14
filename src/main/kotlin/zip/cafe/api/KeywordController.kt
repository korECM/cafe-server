package zip.cafe.api

import io.swagger.v3.oas.annotations.Operation
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

    @Operation(summary = "키워드 리스트 반환", description = "리뷰 작성을 위해 키워드 리스트를 반환")
    @GetMapping("/keywords")
    fun keywords(): ApiResponse<List<KeywordListElement>> {
        val keywordList = keywordService.getKeywords()
            .map { KeywordListElement.from(it) }
        return success(keywordList)
    }
}
