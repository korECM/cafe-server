package zip.cafe.api

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.*
import org.springframework.test.web.servlet.get
import zip.cafe.api.utils.spec.WebMvcTestSpec
import zip.cafe.entity.review.CafeKeyword
import zip.cafe.service.KeywordService
import zip.cafe.utils.documentRequest
import zip.cafe.utils.documentResponse

@WebMvcTest(KeywordController::class)
class KeywordControllerTest : WebMvcTestSpec() {
    @MockkBean
    private lateinit var keywordService: KeywordService

    init {
        "test" {
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
                            documentRequest,
                            documentResponse,
                            responseFields(
                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                fieldWithPath("body").type(JsonFieldType.ARRAY).description("데이터"),
                                fieldWithPath("body[].id").type(JsonFieldType.NUMBER).description("키워드 id"),
                                fieldWithPath("body[].keyword").type(JsonFieldType.STRING).description("키워드 이름"),
                                fieldWithPath("body[].emoji").type(JsonFieldType.STRING).description("키워드 이모지"),
                            )
                        )
                    )
                }
        }
    }
}
