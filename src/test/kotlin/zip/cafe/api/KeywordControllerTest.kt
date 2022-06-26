package zip.cafe.api

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.servlet.get
import zip.cafe.api.utils.restdocs.*
import zip.cafe.api.utils.spec.WebMvcTestSpec
import zip.cafe.entity.review.CafeKeyword
import zip.cafe.service.KeywordService

@WebMvcTest(KeywordController::class)
class KeywordControllerTest : WebMvcTestSpec() {
    @MockkBean
    private lateinit var keywordService: KeywordService

    init {
        "GET 키워드 목록" {
            every { keywordService.getKeywords() } returns listOf(
                CafeKeyword(keyword = "키워드 이름1", emoji = "🚰"),
                CafeKeyword(keyword = "키워드 이름2", emoji = "🎁")
            )

            val response = mockMvc.get("/keywords")

            response.andExpect {
                status { isOk() }
            }
                .andDo {
                    handle(
                        document(
                            "get-keyword-list",
                            responseBody(
                                "body" beneathPathWithSubsectionId "body",
                                "id" type NUMBER means "키워드 id",
                                "keyword" type STRING means "키워드 이름" example "아늑한",
                                "emoji" type STRING means "키워드 이모지" example "🤷‍♂️",
                            )
                        )
                    )
                }
        }
    }
}
