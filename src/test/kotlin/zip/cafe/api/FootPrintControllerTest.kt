package zip.cafe.api

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.web.servlet.post
import zip.cafe.api.dto.FootPrintRegisterRequest
import zip.cafe.api.utils.mockmvc.documentWithHandle
import zip.cafe.api.utils.restdocs.*
import zip.cafe.api.utils.spec.WebMvcTestSpec
import zip.cafe.config.formatAsDefault
import zip.cafe.seeds.MOCK_MVC_USER_ID
import zip.cafe.service.ReviewService
import java.time.LocalDate

@WebMvcTest(FootPrintController::class)
class FootPrintControllerTest : WebMvcTestSpec() {
    @MockkBean
    private lateinit var reviewService: ReviewService

    init {
        "방문 기록 등록" {
            val cafeId = 5L
            val userId = MOCK_MVC_USER_ID
            val visitDate = LocalDate.now()

            val footprintId = 5L
            val request = FootPrintRegisterRequest(cafeId, visitDate)

            every { reviewService.createFootprint(cafeId, userId, visitDate) } returns footprintId

            val response = mockMvc.post("/footprints") {
                this.contentType = APPLICATION_JSON
                this.content = objectMapper.writeValueAsString(request)
            }

            response.andExpect {
                status { isCreated() }
            }.andDo {
                documentWithHandle(
                    "write-footprint",
                    requestFields(
                        "cafeId" type NUMBER means "카페 Id" example cafeId.toString(),
                        "visitDate" type DATE means "방문 날짜" example visitDate.formatAsDefault()
                    ),
                    responseBody(
                        "body" beneathPathWithSubsectionId "body",
                        "footprintId" type NUMBER means "생성된 발자국 Id 토큰" example footprintId
                    )
                )
            }
        }
    }
}
